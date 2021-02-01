package com.roc.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.roc.common.FlinkRealtimeConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @author : jelly.wang
 * @date : Created in 2021-01-26 下午10:15
 * @description: hbase source
 */
public class FlinkSimpleHBaseSource extends RichSourceFunction<String> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(FlinkSimpleHBaseSource.class);

    private String zkServers;
    private String tableName;

    private Connection connection;
    private Table table;

    private String timeRange;

    public FlinkSimpleHBaseSource(String zkServers, String tableName) {
        this(zkServers, tableName, null);
    }

    public FlinkSimpleHBaseSource(String zkServers, String tableName, String timeRange) {
        this.zkServers = zkServers;
        this.tableName = tableName;
        this.timeRange = timeRange;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        String[] sps = zkServers.split(",");
        String host = StringUtils.EMPTY, port = StringUtils.EMPTY;
        for (String sp : sps) {
            final String[] split = sp.split(":");
            host += split[0] + ",";
            port = split[1];
        }
        conf.set("hbase.zookeeper.quorum", host);
        conf.set("hbase.zookeeper.property.clientPort", port);
        connection = ConnectionFactory.createConnection(conf);
        table = connection.getTable(TableName.valueOf(tableName));
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        final Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes(FlinkRealtimeConstants.HBASE_COMMON_CF));
        scan.setCaching(1000);
        scan.setMaxVersions();

        // 设置时间范围
        if (StringUtils.isNotEmpty(timeRange)) {
            String[] times = timeRange.split(",");
            scan.setTimeRange(Long.valueOf(times[0]), Long.valueOf(times[1]));
        }

        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> iterator = rs.iterator();
        while (iterator.hasNext()) {
            Map<String, String> resMap = new HashMap<>();
            Result result = iterator.next();
            for (Cell cell : result.listCells()) {
                String cellName = new String(CellUtil.cloneQualifier(cell));
                String cellValue = new String(CellUtil.cloneValue(cell), "UTF-8").trim();
                resMap.put(cellName, cellValue);
            }
            ctx.collect(JSON.toJSONString(resMap, SerializerFeature.WriteNullStringAsEmpty));
        }
    }

    @Override
    public void cancel() {
        try {
            if (table != null) {
                table.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
