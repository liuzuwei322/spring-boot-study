package com.baidu.springbootstudy.cmd;

import com.baidu.springbootstudy.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public abstract class ShellBase {

    private static final Logger logger = LoggerFactory.getLogger(ShellBase.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    // log的文件夹路径
    protected String folderPath;
    // 进程对象
    protected ProcessBuilder processBuilder;
    // 进程对象
    protected Process process;
    // 输入流对象
    protected StdoutStream stdoutStream;
    // 日志文件路径
    protected String logPath;
    // shell命令
    protected String[] cmd;

    public Result runShell() {
        Result res = new Result();
        try {
            this.processBuilder = new ProcessBuilder(cmd);
            logger.info("运行shell命令: " + Arrays.stream(cmd).collect(Collectors.toList()));
            this.processBuilder.redirectErrorStream(true);
            this.process = this.processBuilder.start();
            this.stdoutStream = new StdoutStream(this.process, this.logPath);
            // 启动一个线程
            new Thread(stdoutStream).start();
            logger.info("运行shell命令线程已启动");
            res.setSuccessed();
        } catch (IOException e) {
            e.printStackTrace();
            res.setFailed();
            return res;
        }
        return res;
    }

    public Result rangeRead(long offset, int size) {
        Result res = new Result();
        if (size > 10240 || size < 0) {
            res.setFailed();
            res.setData("单次读取字节数不合法，请输入大于0并且小于10240的数");
            return res;
        }

        String path = stdoutStream.getLogPath();
        // 文件指针位置初始化
        String content;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
            randomAccessFile.seek(offset);
            byte[] bytes = new byte[size];
            int read = randomAccessFile.read(bytes);
            if (read == -1) {
                content = "";
            } else {
                content = new String(bytes, 0, read, "utf-8");
            }
            long pointer = randomAccessFile.getFilePointer();
            // 假装为偏移量
            res.setTaskId(String.valueOf(pointer));
            res.setSuccessed();
            res.setData(content);
        } catch (Exception e) {
            e.printStackTrace();
            res.setFailed();
            res.setData(e.getMessage());
            return res;
        }
        return res;
    }

    public String getLogPath(String folderPath, String param) {
        File file = new File(folderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName = param + "_" + sdf.format(new Date());
        String logPath = folderPath + File.separator + fileName + ".log";
        return logPath;
    }

    public boolean checkParam(String param) {
        if (param.contains("./")) {
            return false;
        }
        return true;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }

    public void setProcessBuilder(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public StdoutStream getStdoutStream() {
        return stdoutStream;
    }

    public void setStdoutStream(StdoutStream stdoutStream) {
        this.stdoutStream = stdoutStream;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}
