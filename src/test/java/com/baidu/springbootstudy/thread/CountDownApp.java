package com.baidu.springbootstudy.thread;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class CountDownApp {
    public static void main(String[] args) {
        int[] data = queryData();
        CountDownLatch countDownLatch = new CountDownLatch(data.length);
        IntStream.range(0, data.length).forEach((i) -> {
            new Thread(() -> {
                System.out.println(Thread.currentThread() + "处理第" + (i + 1) + "条数据");
                int value = data[i];
                data[i] = (value % 2 == 0) ? value * 2 : value * 10;
                countDownLatch.countDown();
            }).start();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据都处理完了");
        save(data);
    }

    public static int[] queryData () {
        return new int[]{1,2,3,4,5,6,7,8,9};
    }

    public static void save(int[] data) {
        System.out.println(Arrays.toString(data));
    }
}
