package com.baidu.springbootstudy.test;

import com.baidu.springbootstudy.entity.Person;
import com.baidu.springbootstudy.es.ESUtil;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TestApp {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return super.initialValue();
        }
    };
    @Test
    public void test () {
        ThreadLocal<String> local = new ThreadLocal<>();
        local.set("abc");
        System.out.println(local.get());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        System.out.println(time);
    }

    @Test
    public void date () {
        String[] day = ESUtil.getLastNDaysArr(66);
        for (int i = 1; i< day.length; i++) {
            System.out.println(day[i]);
        }
    }

    @Test
    public void rangeDate () {
        String[] day = ESUtil.getRangeDays(54, 69);
        for (int i = 0; i< day.length; i++) {
            System.out.println(day[i]);
        }
    }

    @Test
    public void list () {
        List<String> list = Arrays.asList("1 2 3 4");
        String[] strings = list.get(0).split(" ");
        System.out.println(strings);
        System.out.println(Arrays.asList(strings));

        String[] nums = {"1", "2", "3", "4"};
        System.out.println(Arrays.asList(nums));

    }

    @Test
    public void flatMap () {
        List<String> strings = Arrays.asList("hello hi tom jerry", "hello word tom hello", "hi hi tom hello");
        Stream<String> stream = strings.stream().flatMap(x -> Arrays.asList(x.split(" ")).stream());
        stream.forEach(System.out::println);
    }

    @Test
    public void array () {
        long[] pointer = {0};
        for (int i = 0; i < 10; i++) {
            pointer[0] = i;
            System.out.println(pointer[0]);
        }
    }

    @Test
    public void regex () {
        Pattern pattern = Pattern.compile("[0-9A-Za-z_/.]+");
        Matcher matcher = pattern.matcher("/label_engine/workspace/job/xml//fc73a8d8961c230a3342465dd399bb63_20190812173609.xml");
        boolean b = matcher.matches();
        System.out.println(b);
    }

    @Test
    public void space (){
        Pattern pattern = Pattern.compile("\\s+");
        String path = "123213123123123321 ";
        String[] strings = path.split("\\s+");
        System.out.println(strings.length);

        System.out.println("abc.xml".endsWith(".xml"));

        System.out.println("asdf");
        System.out.println("asfs../".contains("./"));

    }

    @Test
    public void file() {
        File file = new File("D:\\software\\IntelliJ IDEA 2019.1.3\\lib\\abc");
        file.mkdir();
    }

    @Test
    public void candy() {
        int[] ratings = {1, 1, 4, 5};
        int[] dp = new int[ratings.length];
        dp[0] = 1;
        int sum = 0;
        for(int i = 1; i < ratings.length; i++){

            if(ratings[i] > ratings[i-1]){
                dp[i] = dp[i-1]+1;
            }else {
                dp[i]=1;
            }
        }
        for(int i =ratings.length-2;i>=0;i--){
            if(ratings[i] > ratings[i+1]&&dp[i]<=dp[i+1]){
                dp[i]=dp[i+1]+1;
            }
        }
        for(int i : dp){
            sum+=i;
        }
        System.out.println(sum);
    }

    @Test
    public void split() {
        String str = "/label_engine/workspace/job/xml//fc73a8d8961c230a3342465dd399bb63_20190812173609.xml";
        String[] strings = str.split("/");
        String name = strings[strings.length-1];
        System.out.println(name);
        System.out.println(name.substring(0, name.length() - 4));
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 4};
        removeDuplicates(nums);
    }

    @Test
    public void obj() {
        Person person = new Person();
        person.say("abc");
        System.out.println(person.getUserName());
    }
}
