package com.yibao.canaldemo.config;

import com.google.common.collect.Sets;
import com.yibao.canaldemo.kafka.constant.KafkaTopicEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * @author D
 * @date
 */
@Configuration
public class KafkaTopicConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        Set<String> topicSet = Sets.newHashSet();
        for (KafkaTopicEnum topicEnum : KafkaTopicEnum.values()) {
            topicSet.add(topicEnum.getTopic());
        }
        System.setProperty("topics", StringUtils.join(topicSet, ","));
    }
}