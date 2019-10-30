package com.baidu.springbootstudy.test;

public class OutClass {
    private static class InnerClass {
        private static String name = "我是内部";
        private int age = 24;
        public void say() {
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        InnerClass innerClass = new OutClass.InnerClass();
        System.out.println(innerClass.age);
        System.out.println(InnerClass.name);
        innerClass.say();
    }
}
