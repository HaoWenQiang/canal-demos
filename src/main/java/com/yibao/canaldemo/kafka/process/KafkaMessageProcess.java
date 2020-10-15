package com.yibao.canaldemo.kafka.process;

import com.yibao.canaldemo.canal.TableBean;
import com.yibao.canaldemo.kafka.constant.KafkaTopicEnum;

/**
 * @author D
 * @date 2019-07-27
 */
public interface KafkaMessageProcess {

    /**
     * 获取消费对应的topic
     *
     * @return topic
     */
    KafkaTopicEnum getTopic();

    /**
     * 消费对应业务逻辑
     *
     * @param message 消息体
     */
    void process(TableBean message);
}
