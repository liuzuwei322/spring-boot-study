package com.baidu.springbootstudy.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCBatch {

    private static final Logger logger = LoggerFactory.getLogger(JDBCBatch.class);

    public String url = "jdbc:mysql://localhost:3306/query?rewriteBatchedStatements=true";
    public String user = "root";
    public String password = "root";

    Connection conn = null;
    PreparedStatement pstm =null;
    Long startTime = 0L;
    @Before
    public void before () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after () {
        Long endTime = System.currentTimeMillis();
        logger.info("OK, 插入完成, 耗时：" + (endTime - startTime) + "毫秒");
    }

    @Test
    public void batchInsert () {
        BufferedReader br = null;
        String tableName = "search";
        int success = 0;
        int failed = 0;
        try {
            br = new BufferedReader(new FileReader("D:\\files\\output-上海.txt"));
            String line = "";

            int count = 0;
            // 拼接sql
            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO " + tableName + "(baiduid, city, search_content, time, ip, phone)  VALUES(?,?,?,?,?,?)");
            try {
                pstm = conn.prepareStatement(sb.toString());
            } catch (SQLException e) {
                logger.error("sql语法错误");
                e.printStackTrace();
                return;
            }
            while ((line = br.readLine()) != null) {
                try {
                    String[] fileds = line.split("\t");
                    for (int i = 0; i < fileds.length; i++) {
                        if (i == 5) {
                            pstm.setString(i+1, fileds[i+1]);
                            break;
                        }
                        pstm.setString(i+1, fileds[i]);
                    }
                    pstm.addBatch();
                    count++;
                    if (count >= 5000) {
                        count = 0;
                        pstm.executeBatch();
                        pstm.clearBatch();
                        success++;
                        logger.info("成功插入一批");
                    }
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                    logger.info("失败了一批");
                    failed++;
                }
            }
            try {
                pstm.executeBatch();
                success++;
                logger.info("最后一批成功插入");
            } catch (SQLException e) {
                logger.info("最后一批插入失败");
                failed++;
            }
            logger.info("成功了：" + success + "批， 失败了：" + failed + "批");
        } catch (IOException e) {
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
    }

}
