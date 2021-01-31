package com.jelly.flink;

import com.alibaba.fastjson.JSON;
import com.jelly.flink.entity.JobDetail;
import com.jelly.flink.util.AbstractStreamEnv;
import com.jelly.flink.util.ObjectUtils;
import com.jelly.flink.util.SourceSinkConstructor;
import org.apache.commons.cli.*;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * CepMain
 * <p>
 *
 * @author jelly.wang
 * @create 2019/03/26
 */
public class CepMain extends AbstractStreamEnv {
    private final static String JOB_NAME = CepMain.class.getSimpleName();
    private final static Logger LOG = LoggerFactory.getLogger(CepMain.class.getName());

    {
        init();
    }

    public static void main(String[] args) {
        // 解析参数
        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        options.addOption("jd", "jobDetail", true, "Set the rules for running the flink job.");

        JobDetail jobDetail;
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("jd")) {
                jobDetail = JSON.parseObject(commandLine.getOptionValue("jd"), JobDetail.class);
                // 空值校验
                if (ObjectUtils.hasNullValue(jobDetail, "timeType", "streamEngine")) {
                    LOG.error("IllegalArgumentException: parameter is not assigned.");
                    return;
                }
            } else {
                new HelpFormatter().printHelp(" ", options);
                return;
            }
        } catch (ParseException | IllegalAccessException e) {
            LOG.error(e.getMessage());
            return;
        }

        final CepMain nm = new CepMain();
        // 获取执行环境
        StreamExecutionEnvironment env = nm.getEnv();
        // 设置数据自定义时间
        switch (jobDetail.getTimeType()) {
            case "event":
                env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
                break;
            case "ingestion":
                env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
                break;
            default:
                env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
                break;
        }

        DataStream<Map<String, Object>> output = Reflect.onClass(jobDetail.getStreamEngine()).create().call("convert", jobDetail, env).get();

        jobDetail.getSinks().forEach((sink) -> output.rebalance().addSink(SourceSinkConstructor.newSinkFunction(sink)));

        // 执行任务
        try {
            env.execute(JOB_NAME);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


    @Override
    public void init() {
        super.inited();
    }
}
