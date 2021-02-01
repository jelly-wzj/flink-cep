package com.roc.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author : jelly.wang
 * @date : Created in 2021-01-26 下午10:15
 * @description: kafka source
 */
public class FlinkSimpleKafkaSource implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bootstrapServers;
    private String groupId;
    private String topic;

    public FlinkSimpleKafkaSource(String bootstrapServers, String groupId, String topic) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.topic = topic;
    }

    public FlinkKafkaConsumer build() {
        final Properties properties = new Properties() {{
            setProperty("bootstrap.servers", bootstrapServers);
            setProperty("group.id", groupId);
            setProperty("auto.offset.reset", "earliest");
            setProperty("enable.auto.commit", "true");
            setProperty("auto.commit.interval.ms", "1000");
            setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        }};
        return new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), properties);
    }

}
