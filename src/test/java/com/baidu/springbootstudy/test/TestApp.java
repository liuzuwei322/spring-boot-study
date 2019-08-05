package com.baidu.springbootstudy.test;

import com.baidu.springbootstudy.es.ImportToEsUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
        String line = "FEB3D032F680C57F801F73F03D882239\t长春\t孩子病了没钱看怎么办\t20190514182602\t124.234.135.138\t" +
                "http://wap.baidu.com/s?cip=124.234.135.138&baiduid=FEB3D032F680C57F801F73F03D882239" +
                "&tn=zbios&pu=sz@1320_480,cuid@5999ABC2798C2025EF0F3D722A8A58B421B3463A3FRQNHIQMRE,cua" +
                "@1242_2208_iphone_10.5.5.10_0,cut@iPhone7%2C1_10.3.3,osname@baiduboxapp,ctv@1,cfrom@1099a,csrc" +
                "@bdbox_tserch_txt,cud@RUEwMTI4OTEtNDk5QS00QUY4LTg5NzYtRjIxOEI5NUE1NTE5&bd_page_type=1" +
                "&word=孩子病了没钱看怎么办&sa=tks_1&ss=1000&network=1_0" +
                "&from=1099a&ant_ct=FICvrzSREOJhp8EizwLO5CY1aehGoa3tu4o7I9TO3EgnzC5tA8aG5e4PtO6Zibt1&rsv_sug4=26091" +
                "&ts=9320539&isid=FEB3D0915778223951668" +
                "&mod=0&async=1&idc_info=hz&host=m.baidu.com\t18686371155\t-\t-\t18686371155";
        String[] split = line.split("\t");
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
        System.out.println(local.get());
    }

    @Test
    public void date () {
        String[] day = ImportToEsUtil.getLastNDaysArr(66);
        for (int i = 1; i< day.length; i++) {
            System.out.println(day[i]);
        }
    }

    @Test
    public void list () {
        List<String> list = Arrays.asList("1", "2");
        System.out.println(list.get(8));
    }
}
