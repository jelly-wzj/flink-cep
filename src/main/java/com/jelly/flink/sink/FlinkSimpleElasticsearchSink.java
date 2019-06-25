package com.jelly.flink.sink;

import com.alibaba.fastjson.JSON;
import com.jelly.flink.common.FlinkRealtimeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink;
import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FlinkSimpleElasticsearchSink
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 * <p>
 */
public class FlinkSimpleElasticsearchSink implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static Logger LOG = LoggerFactory.getLogger(FlinkSimpleElasticsearchSink.class.getName());

    private String clusterName;
    private String xpackAuth;
    private String esServers;
    private String indexName;

    public FlinkSimpleElasticsearchSink(String clusterName, String xpackAuth, String esServers, String indexName) {
        this.clusterName = clusterName;
        this.xpackAuth = xpackAuth;
        this.esServers = esServers;
        this.indexName = indexName;
    }

    public ElasticsearchSink build() {
        return new ElasticsearchSink<>(new HashMap<String, String>() {{
            put(FlinkRealtimeConstants.ES_CLUSTER_NAME_KEY, clusterName);
            // 是否嗅探整个集群状态
            put(FlinkRealtimeConstants.ES_SNIFF_KEY, "true");
            if (StringUtils.isNotEmpty(xpackAuth)) {
                put(FlinkRealtimeConstants.ES_XPACK_SECURITY_USER_KEY, xpackAuth);
            }
        }}, new ArrayList<InetSocketAddress>() {{
            for (String esHost : esServers.split(",")) {
                try {
                    add(new InetSocketAddress(InetAddress.getByName(esHost.split(":")[0]), FlinkRealtimeConstants.ES_TANS_PORT));
                } catch (UnknownHostException e) {
                    LOG.error(e.getMessage());
                }
            }
        }}, (ElasticsearchSinkFunction<Map<String, Object>>) (data, runtimeContext, requestIndexer) -> {
            final IndexRequest flinkIndexRequest = new IndexRequest(indexName, indexName);
            flinkIndexRequest.source(data);
            requestIndexer.add(flinkIndexRequest);
        });
    }
}
