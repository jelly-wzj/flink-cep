package com.roc.stream.stellar;

import com.roc.entity.JobDetail;
import com.roc.stellar.dsl.Context;
import com.roc.stellar.dsl.Stellar;
import com.roc.stream.StreamFactory;
import com.roc.stream.TransformStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.joor.Reflect;
import org.reflections.Reflections;

import java.util.List;

/**
 * StellarStreamConverter
 * <p>
 *
 * @author jelly.wang
 * @create 2021/02/20
 * @description stellar stream 转换器
 */
public class StellarStreamConverter implements StreamFactory {
    private final String STELLAR_FUNCTIONS_PACKAGE = "com.roc.stellar";

    @Override
    public <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env) {
        List<TransformStream> transformStreams = buildSourceStreams(jobDetail.getSources(), env);
        // WORD_COUNT_FUN()
        String exp = jobDetail.getExp();
        return buildStellarStream(StringUtils.substringBefore(exp, "("), union(transformStreams));
    }


    /**
     * 通过 stellar function获取业务 DataStream
     *
     * @param name
     * @param upStream
     * @return
     */
    private <T> DataStream<T> buildStellarStream(String name, DataStream<T> upStream) {
        Reflections reflections = new Reflections(STELLAR_FUNCTIONS_PACKAGE);
        Class<?> stellarFunctionClass = reflections.getTypesAnnotatedWith(Stellar.class).stream().filter(stellarAnnotatedClass ->
                stellarAnnotatedClass.getAnnotation(Stellar.class).name().equals(name)).findFirst().get();
        Context context = new Context();
        context.setUpstream(upStream);
        return Reflect.onClass(stellarFunctionClass).create().call("apply", context).get();
    }
}
