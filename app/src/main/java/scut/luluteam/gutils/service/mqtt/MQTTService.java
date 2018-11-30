package scut.luluteam.gutils.service.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

import scut.luluteam.gutils.utils.ShowUtil;

import static scut.luluteam.gutils.service.mqtt.MQTTConfig.MQTTHOST;
import static scut.luluteam.gutils.service.mqtt.MQTTConfig.MQTTPASSWORD;
import static scut.luluteam.gutils.service.mqtt.MQTTConfig.MQTTUSERNAME;
import static scut.luluteam.gutils.service.mqtt.MQTTConfig.Qoses;
import static scut.luluteam.gutils.service.mqtt.MQTTConfig.Topics;


public class MQTTService extends Service {

    private String TAG = "MQTTService";
    Timer timer;


    public MQTTService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * 返回一个整型值，用来描述系统在杀掉服务后是否要继续启动服务，返回值有三种：

         START_NOT_STICKY
         系统不重新创建服务，除非有将要传递来的intent。这是最安全的选项，可以避免在不必要的时候运行服务。

         START_STICKY
         系统重新创建服务并且调用onStartCommand()方法，但并不会传递最后一次传递的intent，只是传递一个空的intent。
         除非存在将要传递来的intent，那么就会传递这些intent。
         这个适合播放器一类的服务，不需要执行命令，只需要独自运行，等待任务。

         START_REDELIVER_INTENT
         系统重新创建服务并且调用onStartCommand()方法，传递最后一次传递的intent。其余存在的需要传递的intent会按顺序传递进来。
         这适合像下载一样的服务，立即恢复，积极执行。
         */

        Log.e(TAG, "Start MQTT Service");

        MQTTManager.getInstance().startMQTT();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        MQTTManager.getInstance().stopMQTT();
    }

    /**
     * @param topic
     * @param json  由MQTTMessage封装来的消息，转化为json
     */
    public static void sendMessage(String topic, String json) {
        MQTTManager.getInstance().sendMessage(topic, json);
    }


    //=============================================================================================

    /**
     * 内部类：控制Mqtt的开启、关闭和发送消息的功能
     */
    private static class MQTTManager {


        /**
         * 本类的一个单例
         */
        private volatile static MQTTManager mqttManager;

        /**
         * Mqtt
         */
        private MqttClient mqttClient;
        /**
         * 建立连接用的参数：如用户名、密码、超时时间等
         */
        private MqttConnectOptions options;

        /**
         * 发送的Mqtt消息
         */
        private MqttMessage message;
        /**
         * 用于处理接收Mqtt消息的类
         */
        private MessageHandler messageHandler;

        /**
         * 计时器：连接丢失后，间隔一段时间重连
         */
        private Timer timer = new Timer();

        /**
         * 用于处理发送和接收消息的线程
         */
        private HandlerThread handlerThread;
        private Handler handler;

        /**
         * MQTT Service 的状态信息
         */
        private enum MQTTState {
            RUNNING, STOPPED, RECONNECTING
        }

        /**
         * 当前状态的描述
         */
        private MQTTState currentState = MQTTState.STOPPED;

        private String TAG = "MqttManager";


        /**
         * 私有的构造函数
         */
        private MQTTManager() {
            try {
                mqttClient = new MqttClient(MQTTHOST, MqttClient.generateClientId(), new MemoryPersistence());

                options = new MqttConnectOptions();
                //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
                options.setCleanSession(true);
                //设置连接的用户名
                options.setUserName(MQTTUSERNAME);
                //设置连接的密码
                options.setPassword(MQTTPASSWORD.toCharArray());
                // 设置超时时间 单位为秒
                options.setConnectionTimeout(10);
                // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
                options.setKeepAliveInterval(20);

//                options.setWill();
                message = new MqttMessage();
                messageHandler = MessageHandler.getInstance();


            } catch (MqttException e) {
                e.printStackTrace();
                currentState = MQTTState.STOPPED;
            }
        }

        /**
         * 单例模式
         *
         * @return
         */
        private static MQTTManager getInstance() {
            if (mqttManager == null) {
                synchronized (MQTTManager.class) {
                    if (mqttManager == null) {
                        mqttManager = new MQTTManager();
                    }
                }
            }
            return mqttManager;
        }


        /**
         * 开启Mqtt
         */
        private void startMQTT() {
            //检查状态
            if (currentState != MQTTState.STOPPED) {
                ShowUtil.Toast("MQTT Service is running. Do not start again.");
                return;
            }

            //设置线程
            handlerThread = new HandlerThread("MQTT Handler Thread");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());


            //设置消息
            message.setQos(1);
            message.setRetained(false);


            //设置客户端回调
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    currentState = MQTTState.RECONNECTING;
                    reconnect();
                }

                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    Log.e(TAG, "信消息到达\tmessageArrived:\t" +
                            "topic -- " + topic + "\t" +
                            "message -- " + message);
                    //ShowUtil.Toast(context, message.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            messageHandler.onMessage(topic, message);
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.v(TAG, "发送完成\tisComplete:" + token.isComplete());
                }
            });

            //开启客户端连接
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Start to connect……");
                        mqttClient.connect(options);
                        if (mqttClient.isConnected()) {
                            Log.v(TAG, "MQTT 连接成功");
                            currentState = MQTTState.RUNNING;
                            mqttClient.subscribe(Topics, Qoses);
                        } else {
                            Log.e(TAG, "MQTT 连接失败");
                            currentState = MQTTState.RECONNECTING;
                            reconnect();
                        }
                    } catch (MqttException e) {
                        e.printStackTrace();
                        currentState = MQTTState.RECONNECTING;
                        reconnect();
                    }
                }
            });

        }


        /**
         * 当连接MQTT失败时，重新发起连接
         */
        private void reconnect() {
            if (currentState != MQTTState.RECONNECTING) {
                return;
            }
            ShowUtil.Toast("Mqtt连接已断开，正在重连……");
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        mqttClient.connect(options);
                        if (!mqttClient.isConnected()) {
                            return;
                        }
                        ShowUtil.Toast("Mqtt 连接成功");
                        mqttClient.subscribe(Topics, Qoses);
                        currentState = MQTTState.RUNNING;
                        //EventBus.getDefault().post(new EventBusMessage(EventBusMessage.EventBusMessageType.NETWORKONLINE));
                        timer.cancel();
                    } catch (MqttException e) {
                        //e.printStackTrace();
                        Log.e(TAG, "重连失败～继续尝试");
                    }
                }
            };
            /**
             * 下一次的执行时间点=上一次程序执行完成的时间点+间隔时间
             */
            timer.schedule(timerTask, 200, 2000);
        }

        /**
         * 停止Mqtt
         */
        private void stopMQTT() {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            } finally {
                handler.getLooper().quit();
                currentState = MQTTState.STOPPED;
            }
            Log.e(TAG, "MQTT Service has Stopped");
        }


        /**
         * 使用Mqtt发送数据
         *
         * @param topic 指定话题
         * @param json  所有数据都封装成json，然后指定话题后发送
         */
        private void sendMessage(final String topic, final String json) {

            if (currentState != MQTTState.RUNNING) {
                ShowUtil.Toast("Mqtt Server is not running.");
                return;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        message.setPayload(json.getBytes());
                        mqttClient.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
            });


        }


    }


}
