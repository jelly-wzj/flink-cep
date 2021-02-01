package com.roc.stream.groovy;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class FlinkKafkaSimpleSchema {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         * 设置检查点
         */
        env.enableCheckpointing(5000);
        try {

            /**
             * 加载外部规则
             */
            File file = new File("./src/main/java/com/jelly/flink/stream/groovy/Rule.groovy");
//            Pattern<Event, Event> pattern = GroovyEngine.INSTANCE.runClass(FileUtils.readFileToString(file), "run", null);

            Pattern<Event, Event> pattern1 = Pattern.<Event>begin("begin").where(new SimpleCondition<Event>() {
                @Override
                public boolean filter(Event event) throws Exception {
                    return event.getValue().contains("失败");
                }
            });

            Pattern<Event, Event> pattern2 = Pattern.<Event>begin("begin").where(new SimpleCondition<Event>() {
                @Override
                public boolean filter(Event event) throws Exception {
                    return event.getValue().contains("成功");
                }
            });

//            Pattern<Event, Event> pattern = GroupPattern.begin(GroupPattern.begin(pattern1)).within(Time.seconds(5));

            Pattern<Event, Event> pattern = pattern2.next(pattern1).within(Time.seconds(5));

            /** 初始化 Consumer 配置 */
            Properties consumerConfig = new Properties();
            consumerConfig.setProperty("bootstrap.servers", "localhost:9092");
            consumerConfig.setProperty("group.id", "risk_control");


            /** 初始化 Kafka Consumer */
            FlinkKafkaConsumer<Event> flinkKafkaConsumer =
                    new FlinkKafkaConsumer<>(
                            "flink_kafka_poc_input",
                            new InputEventSchema(),
                            consumerConfig
                    );
            /** 配置offset */
            flinkKafkaConsumer.setStartFromEarliest();

            /** 将 Kafka Consumer 加入到流处理 */
            DataStream<Event> stream = env.addSource(flinkKafkaConsumer);

            /**
             * 匹配规则
             */
            PatternStream<Event> patternStream = CEP.pattern(stream, pattern);
            DataStream<Event> outstream = patternStream.select((PatternSelectFunction<Event, Event>) map -> {
                List<Event> next = map.get("next");
                return new Event(null == next.get(0).getKey() ? UUID.randomUUID().toString() : next.get(0).getKey(), next.get(0).getValue(), next.get(0).getTopic(), next.get(0).getPartition(), next.get(0).getOffset());
            });
            outstream.print("next");

            /** 初始化 Producer 配置 */
            Properties producerConfig = new Properties();
            producerConfig.setProperty("bootstrap.servers", "localhost:9092");
            producerConfig.setProperty("max.request.size", "102428800");

            /** 初始化 Kafka Producer */
            FlinkKafkaProducer<Event> myProducer = new FlinkKafkaProducer<Event>(
                    "flink_kafka_poc_output",
                    new OutputEventSchema(),
                    producerConfig
            );

            /** 将 Kafka Producer 加入到流处理 */
            outstream.addSink(myProducer);
            /** 执行 */
            env.execute();
        } catch (Exception e) {
        }
    }
}