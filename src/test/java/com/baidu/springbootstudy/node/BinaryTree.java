package com.baidu.springbootstudy.node;

public class BinaryTree {

    public static void main(String[] args) {
        BinaryTreeNode root = new BinaryTreeNode();
        for (int i = 0; i < 10; i++) {
            creatBinaryTree(root, i);
        }
        System.out.println(root);
    }

    // 创建一个二叉搜索树
    public static void creatBinaryTree (BinaryTreeNode treeNode, Integer number) {
        if (treeNode.getVal() == null) {
            treeNode.setVal(number);
            return;
        }

        if (number < treeNode.getVal()) {
            if (treeNode.getLeftNode() == null) {
                treeNode.setLeftNode(new BinaryTreeNode(number));
            } else {
                creatBinaryTree(treeNode.getLeftNode(), number);
            }
        } else {
            if (treeNode.getRightNode() == null) {
                treeNode.setRightNode(new BinaryTreeNode(number));
            } else {
                creatBinaryTree(treeNode.getRightNode(), number);
            }
        }
    }

    //定义根节点
    private static class BinaryTreeNode {
        private Integer val;
        private BinaryTreeNode  leftNode;//左子树
        private BinaryTreeNode  rightNode;//右子树

        public BinaryTreeNode(){}
        public BinaryTreeNode(Integer val){
            this.val=val;
        }

        public Integer getVal() {
            return val;
        }
        public void setVal(Integer val) {
            this.val = val;
        }
        public BinaryTreeNode getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(BinaryTreeNode leftNode) {
            this.leftNode = leftNode;
        }

        public BinaryTreeNode getRightNode() {
            return rightNode;
        }

        public void setRightNode(BinaryTreeNode rightNode) {
            this.rightNode = rightNode;
        }

        @Override
        public String toString() {
            return "BinaryTreeNode{" +
                    "val=" + val +
                    ", leftNode=" + leftNode +
                    ", rightNode=" + rightNode +
                    '}';
        }
    }
}


