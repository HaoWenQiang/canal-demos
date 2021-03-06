package com.yibao.canaldemo.kafka.listenter;

import com.alibaba.fastjson.JSON;
import com.yibao.canaldemo.canal.TableBean;
import com.yibao.canaldemo.kafka.process.KafkaMessageProcessFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author D
 * @date 2019-07-27
 */
@Slf4j
@Component
public class KafkaConsumerListener {

    @Autowired
    private KafkaMessageProcessFactory kafkaMessageProcessFactory;

    @KafkaListener(topics = {"#{'${topics}'.split(',')}"})
    public void listener(ConsumerRecord<?, ?> record) {
        log.info("消费数据:" + record.value().toString());
        //工厂模式处理不同topic类型的消息
        TableBean tableBean = JSON.parseObject(record.value().toString(), TableBean.class);
        kafkaMessageProcessFactory.process(tableBean);
//        log.info("消费结束[kafka topic:{},partition:{}]", tableBean.getDatabase() + "_" + tableBean.getTable(), record.partition());
        log.info("消费结束[kafka topic:{},partition:{}]", tableBean.getDatabase(), record.partition());
    }

//  这种方式 消息被切碎，还需要自己手动拼装
//    public void listener(List<String> list) {
//        // 测试 list
//        for (String s : list) {
//            System.out.println("s = " + s);
//        }
//    }
}
