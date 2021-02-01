package com.roc.stream.groovy

import org.apache.flink.cep.pattern.GroupPattern;
import org.apache.flink.cep.pattern.Pattern
import org.apache.flink.cep.pattern.conditions.SimpleCondition
import org.apache.flink.streaming.api.windowing.time.Time

class Rule {
/*    def runPattern() {
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
    }*/


    def runTest(p) {
        return "hello groovy"
    }

    def runGroup() {
        Pattern<Event, ?> pattern = Pattern.<String> begin("begin")
                .where(new SimpleCondition<Event>() {
                    @Override
                    boolean filter(Event event) throws Exception {
                        return event.getValue().contains("失败")
                    }
                })
//                .within(Time.seconds(5))

        Pattern<Event, ?> pattern2 = Pattern.<String> begin("begin")
                .where(new SimpleCondition<Event>() {
                    @Override
                    boolean filter(Event event) throws Exception {
                        return event.getValue().contains("成功")
                    }
                })
//                .within(Time.seconds(5))

        return GroupPattern.begin(pattern).next(pattern2).within(Time.seconds(5))
    }



}