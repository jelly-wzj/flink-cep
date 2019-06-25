package com.jelly.flink.util;

import com.jelly.flink.entity.JobDetail;
import com.jelly.flink.sink.FlinkSimpleElasticsearchSink;
import com.jelly.flink.sink.FlinkSimpleHbaseSink;
import com.jelly.flink.sink.FlinkSimpleKafkaSink;
import com.jelly.flink.sink.FlinkSimpleMysqlSink;
import com.jelly.flink.source.FlinkSimpleHBaseSource;
import com.jelly.flink.source.FlinkSimpleKafkaSource;
import com.jelly.flink.source.FlinkSimpleMysqlSource;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.joor.Reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jelly
 */
public final class SourceSinkConstructor {
    // source
    private final static Map<String, String> SOURCES = new HashMap<String, String>() {{
        put("HBASE", "createHbaseSource");
        put("KAFKA", "createKafkaSource");
        put("MYSQL", "createMysqlSource");
    }};
    // sink
    private final static Map<String, String> SINKS = new HashMap<String, String>() {{
        put("HBASE", "createHbaseSink");
        put("KAFKA", "createKafkaSink");
        put("ELASTICSEARCH", "createElasticsearchSink");
        put("MYSQL", "createMysqlSink");
    }};

    private SourceSinkConstructor() {
    }

    private static JobDetail.SourceDetail sourceDetail;
    private static JobDetail.SinkDetail sinkDetail;


    public static SourceFunction newSourceFunction(JobDetail.SourceDetail sd) {
        sourceDetail = sd;
        return Reflect.on(SourceSinkConstructor.class).call(SOURCES.get(sd.getType().toUpperCase())).get();
    }

    public static SinkFunction newSinkFunction(JobDetail.SinkDetail sd) {
        sinkDetail = sd;
        return Reflect.on(SourceSinkConstructor.class).call(SINKS.get(sd.getType().toUpperCase())).get();
    }

    /**
     * source instance
     *
     * @return
     */
    private static SourceFunction createHbaseSource() {
        return new FlinkSimpleHBaseSource(sourceDetail.getHost(), sourceDetail.getResource());
    }

    private static SourceFunction createKafkaSource() {
        return new FlinkSimpleKafkaSource(sourceDetail.getHost(), sourceDetail.getId(), sourceDetail.getResource()).build();
    }

    private static SourceFunction createMysqlSource() {
        String[] userAndPass = sourceDetail.getAuth().split(":");
        return new FlinkSimpleMysqlSource(sourceDetail.getHost(), userAndPass[0], userAndPass[1], sourceDetail.getResource());
    }

    /**
     * sink instance
     *
     * @return
     */
    private static SinkFunction createElasticsearchSink() {
        return new FlinkSimpleElasticsearchSink(sinkDetail.getId(), sinkDetail.getAuth(), sinkDetail.getHost(), sinkDetail.getStore()).build();
    }

    private static SinkFunction createHbaseSink() {
        return new FlinkSimpleHbaseSink(sinkDetail.getHost(), sinkDetail.getStore()).build();
    }

    private static SinkFunction createKafkaSink() {
        return new FlinkSimpleKafkaSink(sinkDetail.getHost(), sinkDetail.getStore()).build();
    }

    private static SinkFunction createMysqlSink() {
        String[] userAndPass = sinkDetail.getAuth().split(":");
        return new FlinkSimpleMysqlSink(sinkDetail.getHost(), userAndPass[0], userAndPass[1], sinkDetail.getStore());
    }
}
