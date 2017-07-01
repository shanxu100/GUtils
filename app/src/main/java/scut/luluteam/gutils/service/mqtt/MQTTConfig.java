package scut.luluteam.gutils.service.mqtt;


/**
 * Created by guan on 5/18/17.
 */

public class MQTTConfig {

    /**
     * MQTT 配置信息
     */
    //public static String MQTTHOST = "tcp://125.216.242.147:1883";
    public static String MQTTHOST = "tcp://222.201.145.167:61613";
    public static String MQTTUSERNAME = "admin";
    public static String MQTTPASSWORD = "password";

    /**
     * 订阅的话题列表，新的topic直接在这里添加即可,不可为空
     */
    public static String[] Topics = {"TOPIC_TEST"};
    public static int[] Qoses = {1};


    enum MessageType {STRING, ACTION_DOWNLOAD, ACTION_LOCK_SCREEN, ACTION_CLICK_SCREEN}

    //==============================================================================================

    /**
     * 父类：用于封装 MQTT 消息，配合Gson使用
     */
    public static class MQTTMessage {

        public MessageType type;


        public MQTTMessage(MessageType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("class name : " + this.getClass().getSimpleName() + "\t");
            builder.append("type : " + this.type);
            System.out.println(builder.toString());
            return "";
        }
    }

    /**
     * 封装发送简单字符串的消息
     */
    public static class StringMessage extends MQTTMessage {
        public String content;

        public StringMessage(String content) {
            /**
             * 在没有super的时候默认会调用父类的 “无参构造函数”
             * super();  手动调用父类的“无参构造函数”，其实这样写是多余的
             * super(content);   手动调用父类的“有参构造函数”
             *
             * 情景一：父类中只有一个“有参构造函数”，没有“无参构造函数”。
             * 子类继承父类，子类的构造函数必须使用super(参数……)，手动的调用父类的有参构造函数
             */
            super(MessageType.STRING);
            this.content = content;
        }

    }

    /**
     * 封装锁屏的消息
     */
    public static class LockScreenMessage extends MQTTMessage {
        public boolean keepLocked;

        public LockScreenMessage(boolean keepLocked) {
            super(MessageType.ACTION_LOCK_SCREEN);
            this.keepLocked = keepLocked;
        }
    }

    /**
     * 封装点击屏幕位置的消息
     */
    public static class ClickScreenMessage extends MQTTMessage {
        public int xInScreen;
        public int yInScreen;

        public ClickScreenMessage(int xInScreen, int yInScreen) {
            super(MessageType.ACTION_CLICK_SCREEN);
            this.xInScreen = xInScreen;
            this.yInScreen = yInScreen;
        }
    }

    /**
     * 封装下载文件的消息
     */
    public static class DownloadMessage extends MQTTMessage {
        public String url;
        public String simpleFileName = null;

        public DownloadMessage(String url, String simpleFileName) {
            super(MessageType.ACTION_DOWNLOAD);
            this.simpleFileName = simpleFileName;
            this.url = url;
        }

    }


}
