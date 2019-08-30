package com.baidu.springbootstudy.cmd;

import java.io.*;
public class LiunxShell {
    public static void main(String[] args) {
        String[] linux = {"/bin/sh", "run.sh"};
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("/home/work/liuzuwei/shell.log"), true);
            ProcessBuilder processBuilder = new ProcessBuilder(linux);
            processBuilder.redirectErrorStream(true);
            Process p = processBuilder.start();

            // 把字节流转化为缓冲字符流，window默认编码格式为GBK,如果不写输出会乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                outputStream.write(line.getBytes());
                outputStream.write("\r\n".getBytes());
            }
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
