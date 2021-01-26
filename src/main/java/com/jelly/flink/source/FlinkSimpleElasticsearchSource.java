package com.jelly.flink.source;

import com.alibaba.fastjson.JSON;
import com.jelly.flink.common.FlinkRealtimeConstants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.jelly.flink.common.FlinkRealtimeConstants.*;

/**
 * @author : jelly.wang
 * @date : Created in 2021-01-26 下午10:15
 * @description: elasticsearch source
 */
public class FlinkSimpleElasticsearchSource extends RichSourceFunction<String> {
    private static final long serialVersionUID = 1L;
    private final static Logger LOG = LoggerFactory.getLogger(FlinkSimpleElasticsearchSource.class.getName());
    private final Integer pageSize = 1000;

    private String clusterName;
    private String xpackAuth;
    private String esServers;
    private String indexName;

    private TransportClient transportClient;

    public FlinkSimpleElasticsearchSource(String clusterName, String xpackAuth, String esServers, String indexName) {
        this.clusterName = clusterName;
        this.xpackAuth = xpackAuth;
        this.esServers = esServers;
        this.indexName = indexName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        final Settings.Builder builder = Settings.builder()
                .put(ES_CLUSTER_NAME_KEY, clusterName)
                .put(ES_SNIFF_KEY, true);

        if (StringUtils.isNotEmpty(xpackAuth)) {
            builder.put(ES_XPACK_SECURITY_USER_KEY, xpackAuth);
        }

        transportClient = new PreBuiltTransportClient(builder.build());
        try {
            for (String esHost : esServers.split(",")) {
                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost.split(":")[0]), FlinkRealtimeConstants.ES_TANS_PORT));
            }
        } catch (UnknownHostException e) {
            LOG.error("create elstaticsearch client exception ", e);
        }
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        String scrollId = StringUtils.EMPTY;
        for (; ; ) {
            SearchResponse searchResponse;
            if (StringUtils.isNotEmpty(scrollId)) {
                searchResponse = transportClient.prepareSearchScroll(scrollId)
                        .setScroll(TimeValue.timeValueMinutes(30)).execute().actionGet();
            } else {
                searchResponse = transportClient
                        .prepareSearch(this.indexName)
                        .setTypes(this.indexName)
                        .setQuery(new MatchAllQueryBuilder())
                        .setSize(pageSize)
                        .setScroll(TimeValue.timeValueMinutes(30))
                        .execute()
                        .actionGet();
            }
            // 设置游标
            scrollId = searchResponse.getScrollId();

            SearchHit[] searchHits = searchResponse.getHits().getHits();
            if (ArrayUtils.isNotEmpty(searchHits)) {
                for (SearchHit hit : searchHits) {
                    ctx.collect(JSON.toJSONString(hit.getSource()));
                }
            }
        }
    }

    @Override
    public void cancel() {
        if (null != transportClient) {
            transportClient.close();
        }
    }
}
