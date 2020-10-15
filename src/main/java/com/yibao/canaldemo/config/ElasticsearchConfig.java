package com.yibao.canaldemo.config;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

/**
 * @author D
 * @date
 */
//@Component
public class ElasticsearchConfig implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    private PreBuiltXPackTransportClient transportClient;

    @Value("${elasticsearch.cluster.name}")
    private String clusterName;
    @Value("${elasticsearch.cluster-nodes}")
    private String clusterNodes;
    @Value("${spring.data.elasticsearch.xpack-security-user}")
    private String xpackSecurityUser;

    @Bean
    public TransportClient transportClient() throws Exception {
        transportClient = new PreBuiltXPackTransportClient(
                Settings.builder()
                        .put("cluster.name", clusterName)
                        .put("xpack.security.user", xpackSecurityUser)
                        .put("client.transport.sniff", false)
                        .build());
        if (StringUtils.hasText(clusterNodes)) {
            String[] clusterNodeArr = clusterNodes.split(",");
            for (String clusterNode : clusterNodeArr) {
                String[] ipAndPortSocketArr = clusterNode.split(":");
                String ip = ipAndPortSocketArr[0];
                int port = Integer.valueOf(ipAndPortSocketArr[1]);
                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
            }
        }
        logger.info("elasticsearch transportClient 连接成功");
        return transportClient;
    }

    @Bean
    public BulkProcessor bulkProcessor(TransportClient transportClient) {
        return BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                logger.info("{} : Push bulk data to es, size is {}", executionId, request.requests().size());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                logger.info("{} : {} data has been saved in elasticsearch. It tooks {} mills, {}", executionId, request.numberOfActions(), response.getTook().getMillis(), response.hasFailures());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                logger.info("{} : Failed...", executionId);
            }
        }).setBulkActions(2000)
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5)).build();
    }


    @Override
    public void destroy() {
        if (transportClient != null) {
            transportClient.close();
        }
    }
}
