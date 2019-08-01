package com.baidu.springbootstudy.quene;

import org.junit.Test;
import java.util.LinkedList;
import java.util.Queue;

public class QueneApp {
    @Test
    public void quene () {
        Queue<String> queue = new LinkedList<>();
        queue.offer("what ");
        queue.offer("the ");
        queue.offer("fuck");
        queue.stream().forEach(System.out::print);
    }
}
