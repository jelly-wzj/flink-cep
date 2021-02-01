package com.roc.common;


/**
 * FlinkRealtimeConstants
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 */
public interface FlinkRealtimeConstants {
    // hbase family
    String HBASE_COMMON_CF = "f";

    // EsTransportClient端口
    Integer ES_TANS_PORT = 9300;
    String ES_CLUSTER_NAME_KEY = "cluster.name";
    String ES_XPACK_SECURITY_USER_KEY = "xpack.security.user";
    String ES_SNIFF_KEY = "client.transport.sniff";
}
