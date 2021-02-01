package com.roc.stream;

import com.roc.entity.JobDetail;
import com.roc.util.GroovyEngine;
import org.apache.commons.io.FileUtils;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.log4j.Logger;

import javax.script.ScriptException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SiddhiStreamConverter
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description flink默认转换器
 */
public class DefaultStreamConverter implements StreamFactory {
    private static final Logger LOGGER = Logger.getLogger(DefaultStreamConverter.class);

    @Override
    public <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env) {
        List<TransformStream> transformStreams = buildSourceStreams(jobDetail.getSources(), env);
        try {
            Pattern<T, ?> pattern = GroovyEngine.INSTANCE.runClass(FileUtils.readFileToString(new File("/media/jelly/_dde_data/project/git/roc/roc-core/src/main/java/com/roc/stream/groovy/RuleRoc.groovy")), "run", null);
            PatternStream<T> patternStream = CEP.pattern(union(transformStreams), pattern);
            return patternStream.select(new PatternSelectFunction<T, T>() {
                @Override
                public T select(Map<String, List<T>> map) throws Exception {
                    return null;
                }
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 多流合并
     *
     * @param transformStreams
     * @return
     */
    private <T> DataStream<T> union(List<TransformStream> transformStreams) {
        TransformStream transformStream = transformStreams.remove(0);
        DataStream<T> dataStream = (DataStream<T>) transformStream.getDataStream();
        //　单流
        if (transformStreams.isEmpty()) {
            return dataStream;
        }
        // 多流
        DataStream[] dataStreams = transformStreams.stream().map(ts -> ts.getDataStream()).collect(Collectors.toList()).toArray(new DataStream[]{});
        return dataStream.union(dataStreams);
    }
}
