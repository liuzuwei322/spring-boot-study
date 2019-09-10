package com.baidu.springbootstudy.jdbc;

import com.alibaba.fastjson.JSONObject;
import org.junit.After;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class BaseConfig {

    public String url = "jdbc:mysql://localhost:3306/datatrans?rewriteBatchedStatements=true";
    public String user = "root";
    public String password = "root";

    Connection conn = null;
    PreparedStatement pstm =null;
    Long startTime = 0L;
    Map<String, List<String>> map = null;

    // 唯一需要改动的地方
    String basePath = "D:\\files";
    // mock的数据文件路径
    String dataFilePath = basePath + File.separator + "MockDatas\\record";
    // 表的映射文件路径
    String mappingFilePath = basePath + File.separator + "mapping.txt";

    @Before
    public void before () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            map = getMeta();
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after () {
        Long endTime = System.currentTimeMillis();
        System.out.println("OK, 插入完成, 耗时：" + (endTime - startTime) + "毫秒");
    }

    // 获取表，字段映射的元数据
    public Map<String, List<String>> getMeta () {
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

