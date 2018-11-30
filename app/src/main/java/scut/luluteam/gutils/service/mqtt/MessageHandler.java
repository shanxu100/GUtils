package scut.luluteam.gutils.service.mqtt;


import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by guan on 5/20/17.
 * <p>
 * 处理 mqtt 接收的消息的类
 */

public class MessageHandler {

    private String TAG = "MessageHandler";

    //===============================================================================

    /**
     * 单例模式：懒汉式、静态内部类
     */
    private MessageHandler() {
    }

    private static class MessageHandlerBuilder {
        public final static MessageHandler messageHandler = new MessageHandler();
    }

    public static MessageHandler getInstance() {
        return MessageHandlerBuilder.messageHandler;
    }

    //===============================================================================

    /**
     * 重要：
     * 对mqtt消息的处理，在这里进行
     * <p>
     * 注意：运行此方法的进程，不是主线程，而是MQTTService中的Handler中的进程
     *
     * @param topic
     * @param mqttMessage
     */
    public void onMessage(String topic, MqttMessage mqttMessage) {

        if (topic.equals("TOPIC_TEST")) {
            System.out.println(topic+"\t"+mqttMessage.toString());

//            //首先解析成父类，获取Type字段
//            MQTTConfig.MQTTMessage tmpMessage = GsonUtil.fromJson(mqttMessage.toString(),
//                    MQTTConfig.MQTTMessage.class);
//            //再根据type字段，分类讨论
//            if (MQTTConfig.MessageType.STRING == tmpMessage.type) {
//
//                MQTTConfig.StringMessage message = GsonUtil.fromJson(mqttMessage.toString(),
//                        MQTTConfig.StringMessage.class);
//                ShowUtil.Toast("String");
//
//            } else if (MQTTConfig.MessageType.ACTION_LOCK_SCREEN == tmpMessage.type) {
//
//                MQTTConfig.LockScreenMessage message = GsonUtil.fromJson(mqttMessage.toString(),
//                        MQTTConfig.LockScreenMessage.class);
//                DeviceManager.getInstance().keepScreenLocked(message.keepLocked);
//
//            } else if (MQTTConfig.MessageType.ACTION_CLICK_SCREEN == tmpMessage.type) {
//
//                MQTTConfig.ClickScreenMessage message = GsonUtil.fromJson(mqttMessage.toString(),
//                        MQTTConfig.ClickScreenMessage.class);
//                int x = message.xInScreen;
//                int y = message.yInScreen;
//
//            }

        } else if (false) {

        }
    }
}
