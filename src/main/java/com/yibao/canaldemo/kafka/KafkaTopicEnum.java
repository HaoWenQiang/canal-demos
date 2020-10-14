package com.yibao.canaldemo.kafka;


import java.util.Objects;

/**
 * @author D
 * @date
 */
public enum KafkaTopicEnum {
    /**
     * kafka topic
     * 数据库名+"_"+表名
     */
    DEFAULT_TOPIC("yjmap", "默认topic")
    ;

    private String topic;

    private String desc;

    KafkaTopicEnum(String topic, String desc) {
        this.topic = topic;
        this.desc = desc;
    }

    public static KafkaTopicEnum valueOfTopic(String topic) {
        for (KafkaTopicEnum topicEnum : KafkaTopicEnum.values()) {
            if (Objects.equals(topicEnum.topic, topic)) {
                return topicEnum;
            }
        }
        return null;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
