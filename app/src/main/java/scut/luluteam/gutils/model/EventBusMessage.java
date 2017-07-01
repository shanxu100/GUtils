package scut.luluteam.gutils.model;


import scut.luluteam.gutils.service.mqtt.MQTTConfig;

/**
 * Created by guan on 5/19/17.
 */

public class EventBusMessage {
    /**
     * EventBus的消息类型
     */
    public enum MessageType {
        Loading, Login
    }

    public MessageType type;
    public String Content;
    public boolean isDone;
    public MQTTConfig.MQTTMessage mqttMessage;

    private EventBusMessage(Builder builder) {
        this.type = builder.type;
        this.Content = builder.Content;
        this.isDone = builder.isDone;
        this.mqttMessage = builder.mqttMessage;
    }


    /**
     * 创建EventBus的Builder
     */
    public static class Builder {
        /**
         * 必填
         */
        public MessageType type;

        /**
         * 选填:要设置默认值
         */
        private String Content = "";
        private boolean isDone = false;
        private MQTTConfig.MQTTMessage mqttMessage = null;

        public Builder(MessageType type) {
            this.type = type;
        }

        public EventBusMessage build() {
            return new EventBusMessage(this);
        }

        public Builder setContent(String Content) {
            this.Content = Content;
            return this;
        }

        public Builder isDone(boolean isDone) {
            this.isDone = isDone;
            return this;
        }

        public Builder setMQTTMessage(MQTTConfig.MQTTMessage mqttMessage) {
            this.mqttMessage = mqttMessage;
            return this;
        }


    }


}
