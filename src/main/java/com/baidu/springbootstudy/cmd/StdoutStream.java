package com.baidu.springbootstudy.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StdoutStream implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StdoutStream.class);

    private String logPath;
    private Process process;
    private OutputStream outputStream;
    private Integer status;

    public StdoutStream(Process process, String logPath) throws FileNotFoundException {
        this.process = process;
        this.logPath = logPath;
        if (this.logPath != null) {
            this.outputStream = new FileOutputStream(new File(logPath));
        }
        this.status = 0;
        logger.info("shell输出流对象构建成功，状态为0");
    }

    @Override
    public void run() {
        this.status = 1;
        logger.info("shell输出流对象运行中，状态为1");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (outputStream != null) {
                    outputStream.write(line.getBytes());
                    outputStream.write("\r\n".getBytes());
                }
            }
            if (process.exitValue() == 0) {
                this.status = 3;
                logger.info("shell输出流对象运行完毕，exitValue为0，状态为3");
            } else {
                this.status = 2;
                logger.info("shell未正常结束，状态置为2，exitValue为：" + process.exitValue());
            }
            if (outputStream != null) {
                outputStream.close();
            }

        } catch (IOException e) {
            this.status = 2;
            logger.info("shell输出流对象运行出错，状态为2");
            e.printStackTrace();
            try {
                process.destroy();
            } catch (Error error) {
                process.destroyForcibly();
            }
        }
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
