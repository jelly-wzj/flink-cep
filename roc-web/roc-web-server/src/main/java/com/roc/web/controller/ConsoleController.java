package com.roc.web.controller;

import com.roc.common.time.ClockUtil;
import com.roc.web.bean.JobDetail;
import com.roc.web.bean.RespBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ConsoleController {
    private final Logger logger = LoggerFactory.getLogger(ConsoleController.class);

    /**
     * @param jobDetail
     * @return RespBean
     * @author jelly.wang
     * @Description 生成并提交flink作业任务
     */
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RespBean submitTask(@RequestBody JobDetail jobDetail) {
        try {
            // 补全类变量参数ID
            jobDetail.setId("taskId_" + ClockUtil.currentTimeMillis());
            jobDetail.setTimeType("processing");
            switch (jobDetail.getStreamEngine().toLowerCase()) {
                case "cql":
                case "sql":
                    jobDetail.setStreamEngine("com.roc.stream.cep.SiddhiStreamConverter");
                    break;
                case "java":
                case "groovy":
                    jobDetail.setStreamEngine("com.roc.stream.cep.GroovyStreamConverter");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported parsing engine");
            }

            // source id
            List<JobDetail.SourceDetail> sources = jobDetail.getSources();
            for (int i = 0; i < sources.size(); i++) {
                sources.get(i).setId("sourceId_" + i);
            }

            // sink id
            List<JobDetail.SinkDetail> sinks = jobDetail.getSinks();
            for (int j = 0; j < sources.size(); j++) {
                sinks.get(j).setId("sinkId_" + j);
            }

//            ShellUtils.runShell("java -jar roc-core-*.jar -d " + JSONUtils.toJSONString(jobDetail));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RespBean("error", "任务提交失败 -): " + e.getMessage());
        }
        return new RespBean("success", "任务提交成功");
    }
}
