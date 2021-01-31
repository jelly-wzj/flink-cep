package com.jelly.flink.stream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.jelly.flink.entity.JobDetail;
import com.jelly.flink.util.SourceSinkConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * StreamFactory
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description
 */
public interface StreamFactory {

    <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env);

    /**
     * 构建原始数据源流
     *
     * @param sourceDetail
     * @param env
     * @return
     */
    default TransformStream buildSourceStream(JobDetail.SourceDetail sourceDetail, StreamExecutionEnvironment env) {
        DataStream<String> sourcedDataStream = env.addSource(SourceSinkConstructor.newSourceFunction(sourceDetail));
        // 获取数据源中字段名和类型
        final String inputFields = sourceDetail.getFields();
        String[] fieldList = null, typeList = null;
        if (StringUtils.isNoneBlank(inputFields)) {
            final String[] splitFields = inputFields.split(",");
            int len = splitFields.length;
            fieldList = new String[len];
            typeList = new String[len];
            for (int i = 0; i < len; i++) {
                final String[] ft = splitFields[i].split(" ");
                fieldList[i] = ft[0];
                typeList[i] = ft[1];
            }
        }

        return new TransformStream(sourceDetail.getId(), sourcedDataStream.rebalance().map(new MapFunction<String, LinkedHashMap<String, Object>>() {
            @Override
            public LinkedHashMap<String, Object> map(String value) throws Exception {
                return JSON.parseObject(value, LinkedHashMap.class, Feature.OrderedField);
            }
        }), typeList, fieldList);
    }

    /**
     * @param sourceDetailList
     * @param env
     * @return
     */
    default List<TransformStream> buildSourceStreams(List<JobDetail.SourceDetail> sourceDetailList, StreamExecutionEnvironment env) {
        return new ArrayList<>(sourceDetailList.size()) {{
            sourceDetailList.forEach(sourceDetail -> add(buildSourceStream(sourceDetail, env)));
        }};
    }
}
