package com.baidu.springbootstudy.node;

public class LinkedNode2 {
    private static class Node {
        Node next;
        String name;
        public Node(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "Node{" + "next=" + next + ", name='" + name + '\'' + '}';
        }
    }

    static Node head;
    public static void addHead(String name) {
        Node node = new Node(name);
        if (head == null) {
            head = node;
            return;
        }
        node.next = head;
        head = node;
    }

    public static void addTail(String name) {
        Node node = new Node(name);
        Node tempNode = head;
        if (head == null) {
            head = node;
            return;
        }
        while (tempNode.next != null) {
            tempNode = tempNode.next;
        }
        tempNode.next = node;
    }

    public static void main(String[] args) {

        addTail("d");
        addTail("c");
        addTail("b");
        addTail("a");

        while (head.next != null) {
            System.out.println(head.name);
            head = head.next;
        }
        System.out.println(head.name);
    }
}
