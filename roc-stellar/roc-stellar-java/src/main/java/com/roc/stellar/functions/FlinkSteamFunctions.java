package com.roc.stellar.functions;

import com.alibaba.fastjson.JSON;
import com.roc.stellar.dsl.Context;
import com.roc.stellar.dsl.Stellar;
import com.roc.stellar.exceptions.ParseException;
import com.roc.stellar.dsl.StellarFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class FlinkSteamFunctions {

    @Stellar(name = "WORD_COUNT_FUN")
    public static class WordCountFunction implements StellarFunction {

        @Override
        public <T> DataStream<T> apply(Context context) throws ParseException {
            DataStream<LinkedHashMap<String, Object>> upstream = context.getUpstream();
            SingleOutputStreamOperator<WordWithCount> outputStream = upstream.flatMap(new FlatMapFunction<LinkedHashMap<String, Object>, WordWithCount>() {
                @Override
                public void flatMap(LinkedHashMap<String, Object> value, Collector<WordWithCount> out) throws Exception {
                    Object nameObj = value.get("name");
                    if (null != nameObj) {
                        out.collect(new WordWithCount(nameObj.toString(), 1L));
                    }
                }
            }).keyBy("word")
                    .timeWindow(Time.seconds(2), Time.seconds(1))
                    .sum("count");

            outputStream.print();

            return (DataStream<T>) outputStream.flatMap(new FlatMapFunction<WordWithCount, Map>() {
                @Override
                public void flatMap(WordWithCount value, Collector<Map> out) throws Exception {
                    Map map = new LinkedHashMap(JSON.parseObject(JSON.toJSONString(value), Map.class));
                    map.put("id", UUID.randomUUID());
                    out.collect(map);
                }
            });
        }

        public static class WordWithCount {
            public String word;
            public long count;

            public WordWithCount() {
            }

            public WordWithCount(String word, long count) {
                this.word = word;
                this.count = count;
            }

            @Override
            public String toString() {
                return "WordWithCount{" +
                        "word='" + word + '\'' +
                        ", count=" + count +
                        '}';
            }
        }
    }

}
