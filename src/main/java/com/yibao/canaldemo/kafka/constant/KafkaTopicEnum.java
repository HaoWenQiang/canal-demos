package com.yibao.canaldemo.kafka.constant;


import java.util.Objects;

/**
 * @author D
 * @date
 */
public enum KafkaTopicEnum {
    /**
     * kafka topic
     */
    DEFAULT_TOPIC("yjmap", "默认topic"),
    YJMAP_TOPIC("yjmap1", "topic-test1");

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


    public String getDesc() {
        return desc;
    }

}
