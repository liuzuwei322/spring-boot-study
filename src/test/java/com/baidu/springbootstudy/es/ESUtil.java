package com.baidu.springbootstudy.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ESUtil {

    /**
     * 读取json文件，返回json串
     * @param filePath
     * @return
     */
    public static String readJsonFile(String filePath) {
        String json = "";
        FileReader fileReader = null;
        Reader reader = null;
        try {
            File jsonFile = new File(filePath);
            fileReader = new FileReader(jsonFile);
            reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            json = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

    public static List<String> getLine (String path) {
        List<String> list = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String line = "";
            fis = new FileInputStream(path);
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(fis);
            // 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            // 当读取的一行不为空时,把读到的str的值赋给str1
            return list;
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static List<String> getFileContert (String path, String filed) {
        List<String> list = new ArrayList<>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            String line = "";
            fis = new FileInputStream(path);
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(fis);
            // 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (filed == null) {
                    list.add(line);
                } else {
                    JSONObject object = JSONObject.parseObject(line);
                    String code = object.getString(filed);
                    list.add(code);
                }
            }
            // 当读取的一行不为空时,把读到的str的值赋给str1
            return list;
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 获取日期 格式yyyyMMdd
    public static String[] getLastNDaysArr(int n) {

        long oneDayDelta = 60L * 60L * 24L * 1000L;
        long curTime = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String[] res = new String[n];
        for(int i =0; i < n; i++) {
            res[i] = simpleDateFormat.format(new Date(curTime - i * oneDayDelta));
        }
        return res;
    }

    public static String[] getRangeDays (int after, int before) {
        long oneDayDelta = 60L * 60L * 24L * 1000L;
        long curTime = System.currentTimeMillis();
        int count = after + before;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String[] res = new String[count -1];

        for(int i = 0; i < before; i++) {
            res[before - i - 1] = simpleDateFormat.format(new Date(curTime - i * oneDayDelta));
        }

        for(int i = 1; i < after; i++) {
            res[before + i -1] = simpleDateFormat.format(new Date(curTime + i * oneDayDelta));
        }

        return res;
    }

    public static void main(String[] args) throws  Exception {
        String[] daysArr = getRangeDays(58, 3);
        System.out.println(Arrays.asList(daysArr));
        if (1 == 1) {
            return;
        }
        String path = "D:\\files\\zankou.json";
        String s = readJsonFile(path);
        JSONObject jobj = JSONObject.parseObject(s);
        JSONArray links = jobj.getJSONArray("RECORDS");

        for (int i = 0 ; i < links.size();i++){
            JSONObject key1 = (JSONObject)links.get(i);
            String name = (String)key1.get("_source.name");
            String url = (String)key1.get("_source.rybh");
            System.out.println(name);
            System.out.println(url);
        }
    }
}
