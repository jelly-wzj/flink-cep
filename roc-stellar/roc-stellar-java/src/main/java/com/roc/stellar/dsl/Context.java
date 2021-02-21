package com.roc.stellar.dsl;

import org.apache.flink.streaming.api.datastream.DataStream;

import java.io.Serializable;

/**
 * Context
 * <p>
 *
 * @author jelly.wang
 * @create 2021/02/21
 */
public class Context implements Serializable {
    private DataStream upstream;

    public DataStream getUpstream() {
        return upstream;
    }

    public void setUpstream(DataStream upstream) {
        this.upstream = upstream;
    }
}
