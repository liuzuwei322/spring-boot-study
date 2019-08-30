package com.baidu.springbootstudy.utils;

import com.baidu.springbootstudy.controller.RunShellController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    private static String pathJar = System.getProperty("java.class.path");
    private static final Logger logger = LoggerFactory.getLogger(RunShellController.class);

    /**
     * 获取服务器Jar包所在绝对路径
     * @return
     */
    public static String getJarDir() {
        if (pathJar.indexOf(":") >= 0) {
            String[] tmpArr = pathJar.split(":");
            pathJar = tmpArr[tmpArr.length - 1];
        }
        int firstIndex = pathJar.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = pathJar.lastIndexOf(File.separator) + 1;

        return pathJar.substring(firstIndex, lastIndex);
    }

    public static Map<String, Long> randomRead (String path, long position) {
        Map<String, Long> map = new HashMap<>();
        long readByte = 0;
        String content = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
            randomAccessFile.seek(position);
            byte[] bytes = new byte[1024];
            int read = randomAccessFile.read(bytes);
            if (read == -1) {
                logger.info("文件读到头了");
                map.put("", 0L);
                return map;
            }
            content = new String(bytes, 0, read, "utf-8");
            readByte = randomAccessFile.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(content, readByte);
        return map;
    }

    public static String randomReadByArr(String path, long[] position) {
        long readByte = 0;
        String content = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
            randomAccessFile.seek(position[0]);
            byte[] bytes = new byte[1024];
            int read = randomAccessFile.read(bytes);
            if (read == -1) {
                logger.info("文件读到头了");
                position[0] = 0;
                return "";
            }
            content = new String(bytes, 0, read, "utf-8");
            readByte = randomAccessFile.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        position[0] = readByte;
        return content;
    }

    public static String writeFile (String path) {
        if (path == null || path.length() < 1) {
            return "hdfs路径不能为空";
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            String[] linux = {"/bin/sh", "run.sh", path};
            ProcessBuilder processBuilder = new ProcessBuilder(linux);
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();
            // 把字节流转化为缓冲字符流，window默认编码格式为GBK,如果不写输出会乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                outputStream.write(line.getBytes());
                outputStream.write("\r\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }
}
