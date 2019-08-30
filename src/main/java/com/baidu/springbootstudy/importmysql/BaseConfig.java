package com.baidu.springbootstudy.importmysql;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BaseConfig {

    public static Map<String, String> init() {
        // jar包的同级目录
        String configPath = System.getProperty("user.dir") + File.separator + "config.properties";
        // 类路径下的配置文件
        // InputStream in = BaseConfig.class.getClassLoader().getResourceAsStream("config.properties");
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(configPath));
        } catch (FileNotFoundException e) {
            System.out.println(configPath + "配置文件路径不存在");
            e.printStackTrace();
            System.exit(1);
        }
        Properties prop = new Properties();
        Map<String, String> map = null;
        try {
            map = new HashMap<>();
            prop.load(inputStream);
            String user = prop.getProperty("user");
            String url = prop.getProperty("url");
            String password = prop.getProperty("password");
            String path = prop.getProperty("path");
            String datapath = prop.getProperty("datapath");
            map.put("user", user);
            map.put("url", url);
            map.put("password", password);
            map.put("path", path);
            map.put("datapath", datapath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    // 获取表，字段映射的元数据
    public static Map<String, List<String>> getMeta(String mappingFilePath) {
        System.out.println("映射文件路径：" + mappingFilePath);
        System.out.println();
        Map<String, List<String>> map = new HashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(mappingFilePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                JSONObject lineObj = JSONObject.parseObject(line);
                if (lineObj != null) {
                    List<String> list = new LinkedList<>();
                    String tableName = lineObj.getString("tableName");
                    list.add(tableName);
                    String columns = lineObj.getString("columns");
                    String[] fields = columns.split(",");
                    for (int i = 0; i < fields.length; i++) {
                        list.add(fields[i]);
                    }
                    String fileName = lineObj.getString("fileName");
                    map.put(fileName, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
