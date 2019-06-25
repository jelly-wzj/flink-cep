package com.jelly.flink.sink.hbase;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * HbaseSink
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 */
public class HbaseSink<IN> extends RichSinkFunction<IN> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(HbaseSink.class);

    private Connection connection;
    private HbaseMapper<IN> hbaseSinkMapper;
    private HbaseConnectorDescription hbaseConnectorDescription;

    public HbaseSink(HbaseMapper<IN> hbaseSinkMapper) {
        this.hbaseSinkMapper = hbaseSinkMapper;
    }

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) {
        hbaseConnectorDescription = hbaseSinkMapper.getConnectorDescription();

        Configuration conf = HBaseConfiguration.create();
        String host = StringUtils.EMPTY, port = StringUtils.EMPTY;

        final String[] split = hbaseConnectorDescription.getZkQuorum().split(",");
        for (String hp : split) {
            final String[] h = hp.split(":");
            host += h[0] + ",";
            port = h[1];
        }
        conf.set(HConstants.ZOOKEEPER_QUORUM, host);
        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, port);
        Integer threadpoolSize = hbaseConnectorDescription.getThreadpoolSize();
        ExecutorService pool = null;
        if (threadpoolSize != null && threadpoolSize > 0) {
            pool = Executors.newScheduledThreadPool(threadpoolSize);
        }

        try {
            connection = ConnectionFactory.createConnection(conf, pool);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void invoke(IN input, Context context) {
        try (Table table = connection.getTable(TableName.valueOf(hbaseConnectorDescription.getTableName()))) {
            table.put(hbaseSinkMapper.getValue(input));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}