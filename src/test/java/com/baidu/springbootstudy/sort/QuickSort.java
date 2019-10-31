package com.baidu.springbootstudy.sort;

public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {10, 3, 4, 4, 1, 2, 9, 5, 1, 1};
        split(arr, 0, arr.length-1);
        for (int i = 0; i < arr.length; i++) {
            System.out.printf(arr[i] + " ");
        }
    }

    public static void split(int[] arr, int start, int end) {
        if (start > end) {
            return;
        } else {
            int middle = sort(arr, start, end);
            split(arr, start, middle-1);
            split(arr, middle+1, end);
        }

    }

    public static int sort(int[] arr, int start, int end) {
        int base = arr[end];
        while (start < end) {
            while (start < end && arr[start] <= base) {
                start++;
            }
            if (start < end) {
                // start和end交换位置
                int temp = arr[start];
                arr[start] = arr[end];
                arr[end] = temp;
            }
            while (start < end && arr[end] >= base) {
                end --;
            }
            if (start < end) {
                // end和start交换位置
                int temp = arr[end];
                arr[end] = arr[start];
                arr[start] = temp;
            }
        }
        return start;
    }
}
