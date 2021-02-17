package com.roc.web.controller;

import com.roc.web.bean.JobDetail;
import com.roc.web.bean.RespBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class ConsoleController {

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RespBean submitTask(@RequestBody JobDetail jobDetail){
        System.out.println(jobDetail.toString());
        return new RespBean("success", "任务提交成功");
    }
}
