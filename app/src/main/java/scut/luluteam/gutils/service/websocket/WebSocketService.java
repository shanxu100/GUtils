package scut.luluteam.gutils.service.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URISyntaxException;

@Deprecated
public class WebSocketService extends Service {
    public WebSocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WebSocketManager.startWebSocket();

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        WebSocketManager.stopWebSocket();
        super.onDestroy();
    }

    /**
     * 发送消息
     * @param msg
     */
    public static void sendMsg(String msg) {
        WebSocketManager.sendMsg(msg);
    }


    //静态内部类
    private static class WebSocketManager {

        private static WebSocketManager manager = new WebSocketManager();
        private String TAG = "WebSocketManager";

        private WebSocketClient client;
        //private String tse = "";

        private ConnectState currentState = ConnectState.CLOSED;

        enum ConnectState {
            OPEN, CLOSED, ERROR
        }


        /**
         * 构造函数
         */
        private WebSocketManager() {
            try {
                client = new WebSocketClient(WSConfig.getWSHOST_URL()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        currentState = ConnectState.OPEN;
                        Log.e(TAG, "WS on OPEN");

                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e(TAG, "WS on Msg:" + message);
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        currentState = ConnectState.CLOSED;
                        Log.e(TAG, "WS on close");

                    }

                    @Override
                    public void onError(Exception ex) {
                        currentState = ConnectState.ERROR;
                        Log.e(TAG, "WS on ERROR");


                    }
                };
            } catch (URISyntaxException e) {
                currentState = ConnectState.ERROR;
                e.printStackTrace();
            }catch (Exception e)
            {
                currentState = ConnectState.ERROR;
                e.printStackTrace();
            }

        }

        public static void startWebSocket() {
            manager.start();
        }

        public static void stopWebSocket() {
            manager.stop();
        }

        public static void sendMsg(String msg) {
            manager.send(msg);
        }

        //=====================================================
        private void start() {
            client.connect();

        }

        private void stop() {
            client.close();
        }

        private void send(String msg) {
            if (client.isOpen()) {
                client.send(msg);
            }
        }


    }
}
