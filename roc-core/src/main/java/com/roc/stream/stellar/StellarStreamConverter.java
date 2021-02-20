package com.roc.stream.stellar;

import com.roc.entity.JobDetail;
import com.roc.stellar.annotation.Stellar;
import com.roc.stream.StreamFactory;
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
        return null;
    }


    /**
     * 通过 stellar function获取业务 DataStream
     *
     * @param name
     * @param args
     * @return
     */
    private DataStream buildStellarStream(String name, List<Object> args) {
        Reflections reflections = new Reflections(STELLAR_FUNCTIONS_PACKAGE);
        Class<?> stellarFunctionClass = reflections.getTypesAnnotatedWith(Stellar.class).stream().filter(stellarAnnotatedClass ->
                stellarAnnotatedClass.getAnnotation(Stellar.class).name().equals(name)).findFirst().get();
        return Reflect.onClass(stellarFunctionClass).create().call("apply", args).get();
    }
}
