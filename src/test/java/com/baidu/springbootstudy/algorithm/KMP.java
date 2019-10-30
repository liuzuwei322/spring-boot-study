package com.baidu.springbootstudy.algorithm;

public class KMP {
    public static int stringSearch(String big, String small) {
        int bigIndex = 0;
        int smallIndex = 0;
        int bigSize = big.length();
        int smallSize = small.length();

        while (smallIndex < smallSize && bigIndex < bigSize) {
            if (big.charAt(bigIndex) == small.charAt(smallIndex)) {
                bigIndex++;
                smallIndex++;
            } else {
                bigIndex = bigIndex - smallIndex + 1;
                smallIndex = 0;
            }
        }

        if (smallIndex == smallSize) {
            return bigIndex - smallIndex;
        }
        return -1;
    }

    public static void main(String[] args) {
        int index = stringSearch("mvababc", "abc");
        System.out.println(index);

        System.out.println("mvababc".indexOf("abc"));
    }
}
