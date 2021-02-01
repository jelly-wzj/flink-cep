package com.jelly.test.groovy;

import com.alibaba.fastjson.JSON;
import com.roc.util.KafkaProducerUtils;
import org.junit.Test;

import java.util.Date;
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

    @Test
    public void send2() {
        KafkaProducerUtils kafkaProducerUtils = new KafkaProducerUtils("localhost:9092");
        String[] datas = new String[]{"失败", "成功", "失败", "失败", "成功", "成功"};
        Random random = new Random();
        int i = 1;
        while (true) {
            kafkaProducerUtils.sendMsg("flink_kafka_poc_input", UUID.randomUUID().toString(), JSON.toJSONString(new Data(i + "", datas[random.nextInt(5)], 1000.0D, new Date().getTime())));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    static class Data {
        private String id;
        private String name;
        private Double price;
        private Long timestamp;

        public Data() {
        }

        public Data(String id, String name, Double price, Long timestamp) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.timestamp = timestamp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
