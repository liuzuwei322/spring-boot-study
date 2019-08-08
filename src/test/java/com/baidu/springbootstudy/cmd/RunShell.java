package com.baidu.springbootstudy.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class RunShell {

    private static final Logger logger = LoggerFactory.getLogger(RunShell.class.getName());

    public static void main(String[] args) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("D:\\files\\cmd.log"), true);
            Process  p = new ProcessBuilder("ipconfig", "/all").start();

            // 把字节流转化为缓冲字符流，window默认编码格式为GBK,如果不写输出会乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
                outputStream.write(line.getBytes());
                outputStream.write("\n".getBytes());
                outputStream.flush();
            }
            // 在关闭读写流之前先flush(),缓冲区的数据强行写入到文件
            // FileOutputStream 本身没有重写flush方法，所有在FileOutputStream里调用flush是没有用的
            if (1 == 2) {
                // 默认缓存是8kb
                InputStream inputStream = p.getInputStream();
                inputStream.read();
                new BufferedOutputStream(new FileOutputStream(new File("")));
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
