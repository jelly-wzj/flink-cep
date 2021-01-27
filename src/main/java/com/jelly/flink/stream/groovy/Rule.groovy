package com.jelly.flink.stream.groovy;

import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.streaming.api.windowing.time.Time

class Rule implements Serializable {
    def run() {
        Pattern<Event, ?> pattern = Pattern.<String> begin("begin")
                .where(new SimpleCondition<Event>() {
                    @Override
                    boolean filter(Event event) throws Exception {
                        return event.getValue().contains("失败")
                    }
                })

                .next("next")
                .where(new SimpleCondition<Event>() {
                    @Override
                    boolean filter(Event event) throws Exception {
                        return event.getValue().contains("失败")
                    }
                })
//                .next("next2")
//                .where(new SimpleCondition<LoginEvent>() {
//                    @Override
//                    boolean filter(LoginEvent loginEvent) throws Exception {
//                        return loginEvent.getType().equals("success")
//                    }
//                })
                .within(Time.seconds(5))
        return pattern
    }
}