package com.baidu.springbootstudy.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.springbootstudy.cmd.GlobalVar;
import com.baidu.springbootstudy.cmd.RunJobShell;
import com.baidu.springbootstudy.cmd.ShellBase;
import com.baidu.springbootstudy.entity.Result;
import com.baidu.springbootstudy.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/shell")
public class RunShellController {

    private static final Logger logger = LoggerFactory.getLogger(RunShellController.class);
    private static String LOG = FileUtils.getJarDir() + "shellLog";
    private static String PACKGE = FileUtils.getJarDir() + "shellPackge";

    @RequestMapping(value =  "/run", method = RequestMethod.POST)
    public Result run(@RequestBody JSONObject jsonObject) {
        String hdfsPath = jsonObject.getString("hdfsPath");
        String date = jsonObject.getString("date");
        String time = jsonObject.getString("time");
        String name = jsonObject.getString("name");
        String runType = jsonObject.getString("runType");

        String baseTime = date + " " + time;
        RunJobShell runJobShell = new RunJobShell(runType, name, hdfsPath, baseTime, LOG);
        if(!runJobShell.checkParam(runType) || !runJobShell.checkParam(name) || !runJobShell.checkParam(baseTime)
                || !runJobShell.checkParam(baseTime)){
            Result result = new Result();
            result.setFailed();
            result.setData("不合法命令");
            return result;
        }
        // 设置唯一id
        String uid = runJobShell.getUid(hdfsPath);
        GlobalVar.map.put(uid, runJobShell);
        logger.info("运行shell成功，全局uid：" + uid);
        Result result = runJobShell.runShell();
        result.setData(uid);
        return result;
    }

    @RequestMapping(value = "/getData")
    public Result getData(@RequestParam long position, @RequestParam String uid, @RequestParam String size) {
        ShellBase shellBase = GlobalVar.map.get(uid);
        if (shellBase == null) {
            Result result = new Result();
            result.setData("数据文件不存在，请先运行xml，再来获取数据");
            result.setFailed();
            return result;
        }
        Result result = shellBase.rangeRead(position, Integer.parseInt(size));
        return result;
    }

    @RequestMapping(value =  "/status", method = RequestMethod.POST)
    public Result status(@RequestBody JSONObject jsonObject) {
        Result result = new Result();
        String uid = jsonObject.getString("uid");
        ShellBase shellBase = GlobalVar.map.get(uid);
        if (shellBase == null) {
            logger.info("uid不存在，请先运行xml，再来获取状态");
            result.setFailed();
            result.setData(null);
            return result;
        }
        int status = shellBase.getStdoutStream().getStatus();
        result.setSuccessed();
        result.setData(status);
        return result;
    }
}
