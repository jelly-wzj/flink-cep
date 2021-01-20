package com.jelly.flink.sink;

import com.jelly.flink.common.FlinkRealtimeConstants;
import com.jelly.flink.sink.hbase.HbaseConnectorDescription;
import com.jelly.flink.sink.hbase.HbaseMapper;
import com.jelly.flink.sink.hbase.HbaseSink;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.Serializable;
import java.util.Map;

/**
 * FlinkSimpleHbaseSink
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 * <p>
 * 默认以数据中ID为ROWKEY
 */
public class FlinkSimpleHbaseSink implements Serializable {
    private static final long serialVersionUID = 1L;

    private String zkServers;
    private String tableName;

    public FlinkSimpleHbaseSink(String zkServers, String tableName) {
        this.zkServers = zkServers;
        this.tableName = tableName;
    }

    public HbaseSink build() {

        return new HbaseSink(new HbaseMapper<Map>() {
            @Override
            public HbaseConnectorDescription getConnectorDescription() {
                return new HbaseConnectorDescription(zkServers, tableName);
            }

            @Override
            public Put getValue(Map dataMap) {
                final Put put = new Put(Bytes.toBytes(dataMap.remove("id").toString()));
                put.setDurability(Durability.ASYNC_WAL);
                dataMap.forEach((k, v) -> put.addColumn(Bytes.toBytes(FlinkRealtimeConstants.HBASE_COMMON_CF), Bytes.toBytes(String.valueOf(k)), Bytes.toBytes(String.valueOf(v))));
                return put;
            }
        });
    }
}
