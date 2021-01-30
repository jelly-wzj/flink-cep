package com.jelly.flink.stream;


import com.jelly.flink.entity.JobDetail;
import com.jelly.flink.functions.AviatorRegexFunction;
import com.jelly.flink.util.TypeInformationUtils;
import io.siddhi.core.executor.function.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.siddhi.SiddhiCEP;
import org.apache.flink.streaming.siddhi.SiddhiStream;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SiddhiStreamConverter
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description siddhi stream 转换器
 */
public class SiddhiStreamConverter implements StreamFactory {

    @Override
    public <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env) {
        // 获取数据源流
        List<TransformStream> transformStreams = buildSourceStreams(jobDetail.getSources(), env);
        List<String> streamIds = transformStreams.stream().map(TransformStream::getStreamId).collect(Collectors.toList());
        // 数据源流转化siddhi流
        SiddhiStream.ExecutableStream executableStream = buildSiddhiStream(transformStreams, env);
        // sql stream id映射
        String cql = cqlFormat(jobDetail.getCql(), streamIds);

        return (DataStream<T>) executableStream.cql(cql).returnAsMap("outputStream");
    }

    /**
     * 将flink stream　转化成 siddhi　stream
     *
     * @param convertionStreams
     * @param env
     * @return
     */
    private SiddhiStream.ExecutableStream buildSiddhiStream(List<TransformStream> convertionStreams, StreamExecutionEnvironment env) {
        // 设置siddhi
        SiddhiCEP siddhiCEP = SiddhiCEP.getSiddhiEnvironment(env);
        // 注册函数
        registryFunction(siddhiCEP);

        TransformStream convertionStream = convertionStreams.remove(0);
        // 单流
        SiddhiStream.SingleSiddhiStream<LinkedHashMap<String, Object>> singleSiddhiStream = siddhiCEP.from(convertionStream.getStreamId(), convertionStream.getDataStream(), TypeInformationUtils.getTypeInformation(convertionStream.getTypes()), convertionStream.getFields());
        if (convertionStreams.isEmpty()) {
            return singleSiddhiStream;
        }

        // 多流
        SiddhiStream.UnionSiddhiStream<LinkedHashMap<String, Object>> unionSiddhiStream = null;
        for (TransformStream cs : convertionStreams) {
            if (null == unionSiddhiStream) {
                unionSiddhiStream = singleSiddhiStream.union(cs.getStreamId(), cs.getDataStream(), TypeInformationUtils.getTypeInformation(cs.getTypes()), cs.getFields());
            } else {
                unionSiddhiStream = unionSiddhiStream.union(cs.getStreamId(), cs.getDataStream(), cs.getFields());
            }
        }
        return unionSiddhiStream;
    }

    /**
     * @param siddhiCEP
     */
    private void registryFunction(SiddhiCEP siddhiCEP) {
        // 注册AVIATOR 函数库
        siddhiCEP.registerExtension("aviator", AviatorRegexFunction.class);
        siddhiCEP.registerExtension("cast", CastFunctionExecutor.class);
        siddhiCEP.registerExtension("coalesce", CoalesceFunctionExecutor.class);
        siddhiCEP.registerExtension("convert", ConvertFunctionExecutor.class);
        siddhiCEP.registerExtension("set", CreateSetFunctionExecutor.class);
        siddhiCEP.registerExtension("currentTimeMillis", CurrentTimeMillisFunctionExecutor.class);
        siddhiCEP.registerExtension("default", DefaultFunctionExecutor.class);
        siddhiCEP.registerExtension("eventTimestamp", EventTimestampFunctionExecutor.class);
        siddhiCEP.registerExtension("ifThenElse", IfThenElseFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfBoolean", InstanceOfBooleanFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfDouble", InstanceOfDoubleFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfFloatFunction", InstanceOfFloatFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfInteger", InstanceOfIntegerFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfLong", InstanceOfLongFunctionExecutor.class);
        siddhiCEP.registerExtension("instanceOfString", InstanceOfStringFunctionExecutor.class);
        siddhiCEP.registerExtension("max", MaximumFunctionExecutor.class);
        siddhiCEP.registerExtension("min", MinimumFunctionExecutor.class);
        siddhiCEP.registerExtension("script", ScriptFunctionExecutor.class);
        siddhiCEP.registerExtension("sizeOfSet", SizeOfSetFunctionExecutor.class);
        siddhiCEP.registerExtension("uuid", UUIDFunctionExecutor.class);
    }

    /**
     * cql 格式化
     *
     * @param cql
     * @param streamIds
     * @return
     */
    private String cqlFormat(String cql, List<String> streamIds) {
        for (int i = 0; i < streamIds.size(); i++) {
            cql = StringUtils.replace(cql,"${" + i + "}", streamIds.get(i));
        }
        return cql;
    }
}
