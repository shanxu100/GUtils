package scut.luluteam.gutils.utils;


import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * @author Guan
 * @date Created on 2018/1/6
 */
public class MqttClientManager {

    private final static int qos = 2;
    //    private final static String broker = "tcp://127.0.0.1:1883";
    private final static String broker = "tcp://222.201.145.132:1883";

    private final static String clientId = "claudyApp" + System.currentTimeMillis();
    private final static String TAG = "MqttClientManager";
    /**
     * 订阅的 topic
     */
    private final static String[] topics = new String[]{"claudyApp"};
    private final static int[] Qos = new int[]{2};

    private MqttAndroidClient client;
    private ScheduledExecutorService reconnectExecutor;
    private static volatile MqttClientManager manager;


    private MqttClientManager() {

    }

    public static MqttClientManager getInstance() {
        if (manager == null) {
            synchronized (MqttClientManager.class) {
                if (manager == null) {
                    manager = new MqttClientManager();
                }
            }
        }
        return manager;
    }


    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(2);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        //自动重连：1-2-4-8-16……最多两分钟
        options.setAutomaticReconnect(true);
        return options;
    }


    /**
     * 主要：发送消息
     *
     * @param topic
     * @param message msg
     * @return
     */
    public boolean sendMessage(String topic, MqttMessage message) {
        try {
            if (client.isConnected()) {
                client.publish(topic, message);
                Log.i(TAG, "mqtt publish: msg=" + message.toString());
                return true;
            } else {
                Log.e(TAG, "未连接mqtt服务器，发送失败...msg=" + message.toString());
                return false;
            }
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重连
     */
    public void reconnect() {

        reconnectExecutor = new ScheduledThreadPoolExecutor(1);
        reconnectExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "try to reconnect...");
                if (!client.isConnected()) {
                    try {
                        client.connect(getOptions());
                        client.subscribe(topics, Qos);
                        Log.e(TAG, "mqtt连接成功...");
                    } catch (MqttException e) {
//                            e.printStackTrace();
                        Log.e(TAG, "连接失败……请检查网络");
                    }
                } else {
                    reconnectExecutor.shutdown();
                    Log.d(TAG, "mqtt关闭重连。。。");
//                        reconnectExecutor = null;
                }
            }
        }, 1, 3, TimeUnit.SECONDS);

    }

    /**
     * 开启
     */
    public void start(Context context) {
        try {
            client = null;
//            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client = new MqttAndroidClient(context, broker, clientId);
            client.setCallback(new MyMqttCallback());

            if (client != null && !client.isConnected()) {
                client.connect(getOptions(), null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        try {
                            Log.e(TAG, "connect 成功");
                            client.subscribe(topics, Qos);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });

                Log.i(TAG, "connect to MQTT service");
            } else {
                Log.i(TAG, "already connected to MQTT service");
            }
        } catch (MqttException e) {
            e.printStackTrace();
//            manager.reconnect();
        }
    }

    /**
     * 结束
     */
    public void stop() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
                client = null;
            }
//                reconnectTimer.cancel();
            Log.e(TAG, "stop MQTT service");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private class MyMqttCallback implements MqttCallbackExtended {

        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "失去连接------> 正在重连");
            //MqttClientManager.getInstance().reconnect();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "messageArrived: " + message.toString());
            message.getId();
            message.getQos();

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
//            logger.info("deliveryComplete---------" + token.isComplete());
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.e(TAG, "Mqtt连接成功");
            try {
                //因为是  options.setCleanSession(true); ，所以得重新订阅消息
                client.subscribe(topics, Qos);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * SendMessage()中client.publish()是不是线程安全的
     *
     * @param args
     */
    public static void main(String[] args) {
        MqttClientManager manager = MqttClientManager.getInstance();

    }

}
