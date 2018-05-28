package scut.luluteam.gutils.service.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.utils.ShowUtil;

public class SocketService extends Service {
    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SocketManager.startSocket();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketManager.stopSocket();
    }

    //静态内部类
    public static class SocketManager {
        //单例模式
        private static SocketManager manager = new SocketManager();
        private Socket mSocket;
        private Handler handler;
        private Boolean isConnected = false;
        private String TAG = "SocketManager";

        private String mUsername = "GuanGuan";

        /**
         * 构造函数
         */
        private SocketManager() {
            handler = new Handler(Looper.getMainLooper());
            try {
                mSocket = IO.socket(SocketConfig.SOCKETHOST);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        /**
         * 因为SocketManager本身是静态内部类。内部类是延时加载的，也就是说只会在第一次使用时加载。
         * 所以该类的单例模式直接在定义变量的时候初始化就好
         *
         * @return
         */
        @Deprecated
        private static SocketManager getInstance() {
            if (manager == null) {
                synchronized (SocketManager.class) {
                    if (manager == null) {
                        manager = new SocketManager();
                    }
                }
            }
            return manager;
        }

        public static void startSocket() {
            manager.start();
        }

        public static void stopSocket() {
            manager.stop();
        }

        public static void send(String msg) {
            manager.attemptSend(msg);
        }

        //=============================定义监听器====================================
        private Emitter.Listener onConnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnected) {
                            if (null != mUsername) {
                                mSocket.emit("add user", mUsername);
                            }
                            ShowUtil.UIToast(App.getAppContext(), "connected");
                            isConnected = true;
                        }
                    }
                });
            }
        };

        private Emitter.Listener onDisconnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ShowUtil.UIToast(App.getAppContext(), "diconnected");
                        isConnected = false;
                    }
                });
            }
        };

        private Emitter.Listener onConnectError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e(TAG, "Error connecting");
                        ShowUtil.UIToast(App.getAppContext(), "Error connecting");
                        isConnected = false;
                    }
                });
            }
        };
        private Emitter.Listener onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                System.out.println(args[0]);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        String message;
                        try {
                            username = data.getString("username");
                            message = data.getString("message");
                            ShowUtil.UIToast("receive new message:" + data.toString());
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            return;
                        }

                        //removeTyping(username);
                        //addMessage(username, message);
                    }
                });
            }
        };

        private Emitter.Listener onUserJoined = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        int numUsers;
                        try {
                            username = data.getString("username");
                            numUsers = data.getInt("numUsers");
                            ShowUtil.UIToast("receive new message:" + data.toString());
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            return;
                        }

                        //addLog(getResources().getString(R.string.message_user_joined, username));
                        //addParticipantsLog(numUsers);
                    }
                });
            }
        };

        //==============================监听器定义完毕============================================

        /**
         * 开始连接
         */
        private void start() {

            if (isConnected) {
                ShowUtil.UIToast("Socket is connected");
                return;
            }

            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("new message", onNewMessage);
            mSocket.on("user joined", onUserJoined);
//            mSocket.on("user left", onUserLeft);
//            mSocket.on("typing", onTyping);
//            mSocket.on("stop typing", onStopTyping);
            mSocket.connect();

        }

        /**
         * 断开连接
         */
        private void stop() {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off("new message", onNewMessage);
            mSocket.off("user joined", onUserJoined);
//            mSocket.off("user left", onUserLeft);
//            mSocket.off("typing", onTyping);
//            mSocket.off("stop typing", onStopTyping);

        }

        /**
         * 发送
         */
        private void attemptSend(String message) {
            if (null == mUsername) return;
            if (!mSocket.connected()) return;

            // perform the sending message attempt.
            mSocket.emit("new message", message);
        }


    }


}
