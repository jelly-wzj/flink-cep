package com.roc.stellar.functions;

import com.roc.stellar.dsl.Context;
import com.roc.stellar.dsl.Stellar;
import com.roc.stellar.exceptions.ParseException;
import com.roc.stellar.dsl.StellarFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.LinkedHashMap;

public class FlinkSteamFunctions {

    @Stellar(name = "WORD_COUNT_FUN")
    public static class WordCountFunction implements StellarFunction {

        @Override
        public <T> DataStream<T> apply(Context context) throws ParseException {
            DataStream<LinkedHashMap<String, Object>> upstream = context.getUpstream();

            return (DataStream<T>) upstream.flatMap((FlatMapFunction<LinkedHashMap<String, Object>, Tuple2>) (stringObjectLinkedHashMap, collector) -> {
                Object nameObj = stringObjectLinkedHashMap.get("name");
                if (null != nameObj) {
                    collector.collect(new Tuple2<>(nameObj.toString(), 1));
                }
            }).keyBy(value -> value).sum(1);
        }
    }

}
