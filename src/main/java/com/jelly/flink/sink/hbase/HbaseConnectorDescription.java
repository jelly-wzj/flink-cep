package com.jelly.flink.sink.hbase;


/**
 * HbaseConnectorDescription
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 */
public class HbaseConnectorDescription {
    private String zkQuorum;
    private String tableName;
    private Integer threadpoolSize;

    public HbaseConnectorDescription(String zkQuorum, String tableName) {
        this(zkQuorum, tableName, null);
    }

    public HbaseConnectorDescription(String zkQuorum, String tableName, Integer threadpoolSize) {
        this.zkQuorum = zkQuorum;
        this.tableName = tableName;
        this.threadpoolSize = threadpoolSize;
    }

    public String getZkQuorum() {
        return zkQuorum;
    }

    public void setZkQuorum(String zkQuorum) {
        this.zkQuorum = zkQuorum;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getThreadpoolSize() {
        return threadpoolSize;
    }

    public void setThreadpoolSize(Integer threadpoolSize) {
        this.threadpoolSize = threadpoolSize;
    }
}
