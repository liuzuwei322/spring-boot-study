package com.baidu.springbootstudy.jdbc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class CreateTable extends BaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(CreateTable.class);

    @Test
    public void create () {
        Map<String, List<String>> tableMeta = getTableMeta();
        Set<Map.Entry<String, List<String>>> entries = tableMeta.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            createTable(entry.getKey(), entry.getValue());
        }
    }

    public void createTable (String tableName, List<String> columns) {
        System.out.println();
        String deleteSql = "DROP TABLE if EXISTS " + tableName;
        logger.info("删除表的SQL：" + deleteSql);
        try {
            pstm = conn.prepareStatement(deleteSql);
            pstm.execute();
            logger.info(tableName + "删除成功");
        } catch (SQLException e) {
            logger.error("删除" + tableName + "时发生了异常");
            logger.error("异常信息：" + e.getMessage());
            return;
        }

        String sql = "create table " + tableName + "(";
        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size() -1) {
                sql += columns.get(i) + " varchar(255))";
                break;
            }
            sql += columns.get(i) + " varchar(255), ";
        }
        logger.info(sql);
        try {
            pstm = conn.prepareStatement(sql);
            pstm.execute();
            logger.info(tableName + "创建成功");
        } catch (SQLException e) {
            logger.error("创建表" + tableName + "时发生了异常");
            logger.error("异常信息：" + e.getMessage());
        }
    }

    // 获取表，字段映射的元数据
    public Map<String, List<String>> getTableMeta () {
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
                    String columns = lineObj.getString("columns");
                    String[] fields = columns.split(",");
                    for (int i = 0; i < fields.length; i++) {
                        list.add(fields[i]);
                    }
                    map.put(tableName, list);
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
