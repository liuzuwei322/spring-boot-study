package com.baidu.springbootstudy.thread;

public class JoinApp {
    public static void main(String[] args) {
        Thread a = new Thread(() -> System.out.println("a"));
        Thread b = new Thread(() -> System.out.println("b"));
        Thread c = new Thread(() -> System.out.println("c"));
        try {
            a.start();
            a.join();
            b.start();
            b.join();
            c.start();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main");
    }
}
