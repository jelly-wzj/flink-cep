package com.jelly.test.groovy;

import com.jelly.flink.util.KafkaProducerUtils;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

public class KafkaSendMessageTest {


    @Test
    public void send() {
        KafkaProducerUtils kafkaProducerUtils = new KafkaProducerUtils("localhost:9092");
        String[] datas = new String[]{"失败", "成功", "失败", "失败", "成功", "成功"};
        Random random = new Random();
        while (true) {
            kafkaProducerUtils.sendMsg("flink_kafka_poc_input", UUID.randomUUID().toString(), datas[random.nextInt(5)]);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
