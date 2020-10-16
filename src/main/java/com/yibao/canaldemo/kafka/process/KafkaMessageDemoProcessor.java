package com.yibao.canaldemo.kafka.process;

import com.alibaba.fastjson.JSON;
import com.yibao.canaldemo.canal.TableBean;
import com.yibao.canaldemo.kafka.constant.KafkaTopicEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author D
 * @date 2019/07/05
 * 该实现用于处理 mjmap 的topic
 */
@Slf4j
@Service
public class KafkaMessageDemoProcessor implements KafkaMessageProcess {

    @Override
    public KafkaTopicEnum getTopic() {
        return KafkaTopicEnum.DEFAULT_TOPIC;
    }

    @Override
    public void process(TableBean message) {
        System.err.println("模拟消费:" + JSON.toJSONString(message));

    }
//    暂时不用
//    @Autowired
    BulkProcessor bulkProcessor;

    private void sync2Es(TableBean message) {
        bulkProcessor
//          删除使用的 api       DeleteRequest request = new DeleteRequest(index, type, id);
                .add(new IndexRequest(message.getDatabase(),
                        message.getTable(), message.getPkNames().get(0)).source(message));
        bulkProcessor.close();
    }

}
