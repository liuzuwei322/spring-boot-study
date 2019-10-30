package com.baidu.springbootstudy.quene;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class QueneApp {
    private final Map<String, String> map = new HashMap<>();
    /**
     * 队列，先进先出
     * offer如果队列满了，返回false
     *
     */
    @Test
    public void quene () {
        // offer poll peek push  这四个方法都是继承自Deque
        LinkedList<String> queue = new LinkedList<>();
        // offer 方法 添加一个元素到队列中，底层调用的add方法 队列： 先进先出
        queue.offer("what ");
        queue.offer("the ");
        queue.offer("fuck");
        // push 方法在栈顶添加，相当于addFirst
        queue.push("oh ");

        // 查看但不删除队列的头元素，如果队列是空，则返回null
        System.out.println(queue.peek());

        // 删除头元素，如果队列是空，就返回null
        // System.out.println(queue.poll());

        // add 方法 继承自List集合的方法
        queue.add("list");

        queue.stream().forEach(System.out::println);
    }

    @Test
    public void linkedListAsStack () {
        LinkedList<Object> stack = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            stack.push(i);
        }
        while (stack.peek() != null) {
            System.out.println(stack.poll());
        }
    }

    @Test
    public void linkedListAsQuene () {
        Queue<Object> quene = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            quene.offer(i);
        }
        while (quene.peek() != null) {
            System.out.println(quene.poll());
        }
    }
}
