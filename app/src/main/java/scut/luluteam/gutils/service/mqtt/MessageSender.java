package scut.luluteam.gutils.service.mqtt;


import scut.luluteam.gutils.utils.GsonUtil;

/**
 * Created by guan on 5/20/17.
 */

public class MessageSender {


    //==================发送具体的MQTT消息=========================================

    /**
     * 使用mqtt发送文件：每次读取1024 byte的数据，编码后发送出去
     * 需要在新线程中运行
     * <p>
     * 注意：此方法仅用做测试，并不能用在实际生产环境中
     *
     * @param topic
     * @param filePath
     */
    //region
    /*
    public static void sendFile(final String topic, final String filePath, final Context mContext) {
        if (currentState != MQTTService.MQTTManager.MQTTState.RUNNING) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                final EventBusMessage.Builder builder = new EventBusMessage.Builder(EventBusMessage.MessageType.Loading);
                builder.isDone(false);
                EventBus.getDefault().post(builder.build());

                final MQTTConfig.MQTTMessage mqttMessage = new MQTTConfig.MQTTMessage(MQTTConfig.MQTTMessage.MessageType.FILE);
                mqttMessage.fileName = FileUtil.getFileNameFromPath(filePath);
                ReadFromFile.readFileByBytes(filePath, new ReadFromFile.FileListener() {
                    @Override
                    public void onReadByBytesCallback(byte[] bytes, boolean isDone) {
                        mqttMessage.content = Base64.encodeToString(bytes, Base64.DEFAULT);
                        mqttMessage.isDone = isDone;
                        sendMessage(topic, GsonUtil.getGsonInstance().toJson(mqttMessage));
                        if (isDone) {
                            ShowUtil.LogAndToast(mContext, "文件发送完毕");
                            EventBus.getDefault().post(builder.isDone(true).build());
                        }
                    }
                });
            }
        });

    }
    */

    //endregion

    /**
     * 使用MQTT发送String类型数据
     *
     * @param topic
     * @param content String的具体内容，建议为json格式
     */
    public static void sendString(final String topic, final String content) {
        //创建承载String类型数据的message
        MQTTConfig.StringMessage message = new MQTTConfig.StringMessage("This is s String message:" + content);
        MQTTService.sendMessage(topic, GsonUtil.toJson(message));

    }

    /**
     * 推送下载的命令
     *
     * @param topic
     * @param simpleFileName
     * @param downloadUrl
     */
    public static void sendActionDownload(String topic, String simpleFileName, String downloadUrl) {

        MQTTConfig.DownloadMessage message = new MQTTConfig.DownloadMessage(downloadUrl, simpleFileName);
        MQTTService.sendMessage(topic, GsonUtil.toJson(message));
    }

    /**
     * 推送锁屏的命令
     *
     * @param topic
     * @param keepLocked
     */
    public static void sendActionLockScreen(final String topic, boolean keepLocked) {

        MQTTConfig.LockScreenMessage message = new MQTTConfig.LockScreenMessage(keepLocked);
        MQTTService.sendMessage(topic, GsonUtil.toJson(message));
    }

}
