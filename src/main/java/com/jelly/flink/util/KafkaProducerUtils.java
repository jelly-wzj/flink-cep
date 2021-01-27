package com.jelly.flink.util;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * kafka数据生产工具类
 *
 * @author jelly.wang @date 2019-12-03 10:02
 * @ClassName: KafkaProducerUtils
 * @Description: TODO
 */
public class KafkaProducerUtils {
    private static final Logger logger = Logger.getLogger(KafkaProducerUtils.class);
    private KafkaProducer producer;

    public KafkaProducerUtils(String kafkaBrokers) {
        final Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        prop.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.dptech.kafka.KafkaPartition");
        producer = new KafkaProducer<>(prop);
    }

    public KafkaProducerUtils(String aclKafkaBrokers, String userName, String passWord) {
        final Properties prop = new Properties();

        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, aclKafkaBrokers);
        prop.put("serializer.class", "kafka.serializer.StringSerializer");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        final String jaasConfig = new StringBuffer("org.apache.kafka.common.security.plain.PlainLoginModule required username=").append(userName).append(" password=").append(passWord).append(";").toString();
        prop.put("sasl.jaas.config", jaasConfig);

        prop.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        prop.setProperty(SaslConfigs.SASL_MECHANISM, "PLAIN");

        producer = new KafkaProducer<>(prop);
    }

    public boolean sendMsg(String topicName, String msgData) {
        final boolean[] sendResult = {true};

        producer.send(new ProducerRecord<>(topicName, msgData), (metadata, e) -> {
            if (null != e) {
                sendResult[0] = false;
                logger.error("kafka producer found error:" + e.getMessage(), e);
            }
        });

        return sendResult[0];
    }

    public boolean sendMsg(String topicName, String key, String msgData) {
        final boolean[] sendResult = {true};

        producer.send(new ProducerRecord<>(topicName, key, msgData), (metadata, e) -> {
            if (null != e) {
                sendResult[0] = false;
                logger.error("kafka producer found error:" + e.getMessage(), e);
            }
        });

        return sendResult[0];
    }

    public void close() {
        if (null != producer) producer.close();
    }

}