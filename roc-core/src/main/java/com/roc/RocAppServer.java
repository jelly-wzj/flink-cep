package com.roc;

import com.alibaba.fastjson.JSON;
import com.roc.common.base.ObjectUtil;
import com.roc.entity.JobDetail;
import com.roc.util.AbstractStreamEnv;
import com.roc.util.SourceSinkConstructor;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
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
public class RocAppServer extends AbstractStreamEnv {
    private final static String JOB_NAME = RocAppServer.class.getSimpleName();
    private final static Logger LOG = LoggerFactory.getLogger(RocAppServer.class.getName());
    private final static String separator = "#=>";

    {
        init();
    }

    public static void main(String[] args) {
        JobDetail jobDetail = fromArgs(args);

        final RocAppServer nm = new RocAppServer();
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

    /**
     * JobDetail from Args
     *
     * @param args
     * @return
     */
    private static JobDetail fromArgs(String[] args) {
        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();
        options.addOption("jd", "jobDetail", true, "Set the rules for running the flink job");

        JobDetail jobDetail = null;
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("jd")) {
                jobDetail = JSON.parseObject(commandLine.getOptionValue("jd"), JobDetail.class);
            } else {
                new HelpFormatter().printHelp(" ", options);
                exit("Parameter types not supported by the command line");
            }
        } catch (ParseException e) {
            exit(e.getMessage());
        }

        // job构建参数校验
        if (!jobParamsVerify(jobDetail)) {
            exit("Parameter verification abnormal");
        }

        return jobDetail;
    }

    /**
     * params verify
     *
     * @param jobDetail
     * @return
     */
    private static boolean jobParamsVerify(JobDetail jobDetail) {
        try {
            // 1.空值校验
            if (ObjectUtil.hasNullValue(jobDetail, "timeType", "streamEngine")) {
                LOG.error("IllegalArgumentException: parameter is not assigned");
                return false;
            }

            // 2.表达式格式校验
            String exp = jobDetail.getExp();
            if (!exp.contains(separator)) {
                LOG.error("IllegalArgumentException: exp`s delimiter error [" + separator + "]");
                return false;
            }
            String[] expContentArray = exp.split(separator);
            ExpType expType = ExpType.get(expContentArray[0]);
            if (!expType.verify(expContentArray[1])) {
                LOG.error("IllegalArgumentException: exp`s grammar error [" + ExpType.get(expContentArray[0]) + "]");
                return false;
            } else {
                jobDetail.setExp(expContentArray[1]);
                if (StringUtils.isBlank(jobDetail.getStreamEngine())) {
                    jobDetail.setStreamEngine(expType.engine);
                }
            }
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
        }
        return true;
    }

    // 退出进程
    private static void exit(String cause) {
        if (StringUtils.isNotBlank(cause)) LOG.error("Process finished with exit cause: " + cause);
        System.exit(0);
    }


    private enum ExpType {
        NONE("none", null) {
            @Override
            public boolean verify(String content) {
                // @NOTHING TODO
                return false;
            }
        }, CQL("cql", "com.roc.stream.cep.SiddhiStreamConverter") {
            // siddhi sql 语法校验
            @Override
            public boolean verify(String content) {
                return true;
            }
        }, SCRIPT("script", "com.roc.stream.cep.GroovyStreamConverter") {
            // groovy script 语法校验
            @Override
            public boolean verify(String content) {
                return true;
            }
        }, STELLAR("stellar", "com.roc.stream.stellar.StellarStreamConverter") {
            // stellar 语法校验
            @Override
            public boolean verify(String content) {
                return true;
            }
        }, FILE("file", "com.roc.stream.cep.GroovyStreamConverter") {
            // 文件类型校验
            @Override
            public boolean verify(String content) {
                if (content.endsWith("java") || content.endsWith("groovy"))
                    return true;
                else return false;
            }
        };
        private String name;
        private String engine;

        ExpType(String name, String engine) {
            this.name = name;
            this.engine = engine;
        }

        public static ExpType get(String type) {
            try {
                return ExpType.valueOf(type.toUpperCase());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return NONE;
            }
        }

        public abstract boolean verify(String content);
    }
}
