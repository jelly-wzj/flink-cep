package com.jelly.flink.stream;

import com.jelly.flink.entity.JobDetail;
import com.jelly.flink.stream.groovy.Event;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;
import java.util.UUID;

/**
 * SiddhiStreamConverter
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description flink默认转换器
 */
public class DefaultStreamConverter implements StreamFactory{
    @Override
    public <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env) {
        List<TransformStream> transformStreams = buildSourceStreams(jobDetail.getSources(), env);

      /*
        PatternStream<Event> patternStream = CEP.pattern(stream, pattern);
        DataStream<Event> outstream = patternStream.select((PatternSelectFunction<Event, Event>) map -> {
            List<Event> next = map.get("next");
            return new Event(null == next.get(0).getKey() ? UUID.randomUUID().toString() : next.get(0).getKey(), next.get(0).getValue(), next.get(0).getTopic(), next.get(0).getPartition(), next.get(0).getOffset());
        });*/

        return null;
    }
}
