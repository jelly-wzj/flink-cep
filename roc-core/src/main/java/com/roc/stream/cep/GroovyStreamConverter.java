package com.roc.stream.cep;

import com.roc.entity.JobDetail;
import com.roc.stream.StreamFactory;
import com.roc.stream.TransformStream;
import com.roc.util.GroovyEngine;
import org.apache.commons.io.FileUtils;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SiddhiStreamConverter
 * <p>
 *
 * @author jelly.wang
 * @create 2021/01/30
 * @description flink默认转换器
 */
public class GroovyStreamConverter implements StreamFactory {
    private static final Logger LOGGER = Logger.getLogger(GroovyStreamConverter.class);

    @Override
    public <T> DataStream<T> convert(JobDetail jobDetail, StreamExecutionEnvironment env) {
        List<TransformStream> transformStreams = buildSourceStreams(jobDetail.getSources(), env);
        try {
            Pattern<T, ?> pattern = GroovyEngine.INSTANCE.runClass(FileUtils.readFileToString(new File("/media/jelly/_dde_data/project/git/roc/roc-core/src/test/java/com/jelly/test/groovy/RuleRoc.groovy")), "run", null);
            PatternStream<T> patternStream = CEP.pattern(union(transformStreams), pattern);
            return patternStream.select((PatternSelectFunction<T, T>) map -> (T) map.get("result").get(0));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

}
