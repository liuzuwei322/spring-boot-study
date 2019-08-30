package com.baidu.springbootstudy.io;

import java.io.RandomAccessFile;

public class RandomAccessFileApp {

    public static void main(String[] args) {
        long count = 0;
        while (1 == 1) {
            long randomRead = randomRead("D:\\files\\shell.log", count);
            if (randomRead == 0) {
                break;
            }
            count = randomRead;
        }
    }

    public static long randomRead (String path, long position) {
        long readByte = 0;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");
            System.out.println("跳过字节数：" + position);
            randomAccessFile.seek(position);
            byte[] bytes = new byte[1024];
            int read = randomAccessFile.read(bytes);
            if (read == -1) {
                System.out.println("文件读到头了");
                return 0;
            }
            System.out.print(new String(bytes, 0, read, "utf-8"));

            readByte = randomAccessFile.getFilePointer();
            System.out.println("读完后的指针所在的偏移量：" + readByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readByte;
    }
}
