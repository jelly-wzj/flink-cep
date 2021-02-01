package com.roc.stream.groovy

import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.streaming.api.windowing.time.Time

class RuleRoc implements Serializable {
    def run() {
        return Pattern.<String> begin("begin").where(new SimpleCondition<LinkedHashMap<String, Object>>() {
            @Override
            boolean filter(LinkedHashMap<String, Object> dataMap) throws Exception {
                return dataMap.get("name").toString().contains("失败")
            }
        }).next("next").where(new SimpleCondition<LinkedHashMap<String, Object>>() {
            @Override
            boolean filter(LinkedHashMap<String, Object> dataMap) throws Exception {
                return dataMap.get("name").toString().contains("失败")
            }
        }).within(Time.seconds(5))
    }
}
