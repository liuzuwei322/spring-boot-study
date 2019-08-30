package com.baidu.springbootstudy.sort;

import java.util.Arrays;
import java.util.stream.IntStream;

public class HeapSort {

    public static void main(String[] args) {
        int[] arr = new int[]{1,4,5,2,6,2,8,9};
        createHeap(arr, arr.length - 1);
        IntStream stream = Arrays.stream(arr);
        stream.forEach(System.out::println);
    }


    public static void createHeap (int[] arr, int lastIndex) {
        for (int i = (arr.length / 2) -1; i >= 0; i--) {
            int top = i;
            while ((top * 2 + 1) <= lastIndex) {
                int left = top * 2 + 1;
                if (left < lastIndex) {
                    int right = left + 1;
                    if (arr[left] < arr[right]) {
                        left++;
                    }
                }
                if (arr[top] < arr[left]) {
                    // 上面和左边交换位置，左边换到上面去
                    int temp = arr[left];
                    arr[left] = arr[top];
                    arr[top] = temp;
                    top = left;
                } else {
                    break;
                }
            }
        }
    }
}
