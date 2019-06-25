package com.jelly.flink.sink;

import com.jelly.flink.util.schema.SimpleMapSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author jenkin
 */
public class FlinkSimpleKafkaSink implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kafkaBrokers;
    private String topic;

    public FlinkSimpleKafkaSink(String kafkaBrokers, String topic) {
        this.kafkaBrokers = kafkaBrokers;
        this.topic = topic;
    }

    public FlinkKafkaProducer build() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaBrokers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
//        properties.put("partitioner.class", "com.ecarx.bigdata.common.kafka.clients.KafkaPartition");
        return new FlinkKafkaProducer<>(topic, new SimpleMapSchema(), properties);
    }

}
