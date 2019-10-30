package com.baidu.springbootstudy.node;

public class LinkedNode {
    private class Node {
        String name;
        Node next;

        public Node(String name, Node next) {
            this.name = name;
            this.next = next;
        }
    }

    Node head;

    public LinkedNode() {
        this.head = new Node(null, null);
    }

    // 头插法
    public void add(String name) {
        this.head = new Node(name, this.head);
    }

    public static void main(String[] args) {
        LinkedNode linkedNode = new LinkedNode();
        String[] names = {"朱元璋", "朱允炆", "朱棣", "朱高炽", "朱瞻基", "朱祁镇", "朱祁钰", "朱祁镇", "朱见深", "朱祐樘", "朱厚照", "朱厚熜", "朱载垕", "朱翊钧", "朱常洛", "朱由校", "朱由检"};
        for (int i = 0; i < names.length; i++) {
            linkedNode.add(names[i]);
        }
        System.out.println(linkedNode.head.name);
    }
}
