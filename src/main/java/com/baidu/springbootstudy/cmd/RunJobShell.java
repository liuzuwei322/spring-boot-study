package com.baidu.springbootstudy.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class RunJobShell extends ShellBase {

    private static final Logger logger = LoggerFactory.getLogger(RunJobShell.class);
    // 运行类型
    private String runType;
    // 任务名称
    private String jobName;
    // hdfs地址
    private String hdfsPath;
    // 基准时间
    private String time;


    public RunJobShell(String runType, String jobName, String hdfsPath, String time, String folderPath) {
        this.runType = runType;
        this.jobName = jobName;
        this.hdfsPath = hdfsPath;
        this.time = time;
        this.folderPath = folderPath;
        if ("\\".equals(File.separator)) {
            this.cmd = new String[]{"cmd.exe", "/C", "D:\\files\\ip.bat", runType, jobName, hdfsPath, time};
        } else {
            this.cmd = new String[]{"/bin/sh", "run.sh", runType, jobName, hdfsPath, time};
        }
        this.logPath = getLogPath(folderPath, jobName);
        logger.info("ReadShellUtil构造成功,日志路径：" + logPath);
    }

    public String getUid(String hdfsPath) {
        String[] strings = hdfsPath.split("/");
        int size = strings.length - 1;
        String name = strings[size];
        String uid = name.substring(0, name.length() - 4);
        return uid;
    }

    public RunJobShell() {}
}
