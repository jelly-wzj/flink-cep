package com.jelly.flink.util;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * AbstractStreamEnv
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 * @description flinkstream 环境变量
 */
public abstract class AbstractStreamEnv {
    private StreamExecutionEnvironment env;

    public StreamExecutionEnvironment getEnv() {
        return env;
    }

    public void setEnv(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public abstract void init();

    public void inited() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 快照周期
        env.enableCheckpointing(1000);
        // 快照间的最小间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 快照超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同一时间的快照个数
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION: cancel后，会保留Checkpoint数据
        //ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: cancel后，会删除Checkpoint数据，但job执行失败的时会保存
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // 开启 EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    }
}
