package com.baidu.springbootstudy.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class NioApp {
    @Test
    public void test () {
        // 非直接缓存区，存在JVM的内存中
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer.isDirect());

        // 直接缓存区，存在操作系统的物理内存
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(32);
        System.out.println(allocateDirect.isDirect());
        getBaseData(byteBuffer);

        byteBuffer.put("liuzuwei".getBytes());
        System.out.println();
        getBaseData(byteBuffer);

        byteBuffer.flip();
//        char c = (char) byteBuffer.get();
//        System.out.println(c);

        byte[] b = new byte[byteBuffer.limit()];
        byteBuffer.get(b);
        System.out.println(new String(b, 0, b.length));
    }

    public void getBaseData (ByteBuffer byteBuffer) {
        System.out.println("容量：" + byteBuffer.capacity());
        System.out.println("界限：" + byteBuffer.limit());
        System.out.println("位置：" + byteBuffer.position());
    }
}
