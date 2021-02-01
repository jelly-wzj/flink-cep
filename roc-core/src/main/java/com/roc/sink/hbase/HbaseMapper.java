package com.roc.sink.hbase;

import org.apache.flink.api.common.functions.Function;
import org.apache.hadoop.hbase.client.Put;

import java.io.Serializable;

/**
 * HbaseMapper
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 */
public interface HbaseMapper<T> extends Function, Serializable {

    HbaseConnectorDescription getConnectorDescription();

    Put getValue(T data);
}