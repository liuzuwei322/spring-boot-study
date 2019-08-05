package com.baidu.springbootstudy.jdbc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import java.io.*;
import java.sql.*;
import java.util.List;

public class ImportData extends BaseConfig {

    @Test
    public void test () {
        String separator = File.separator;
        if ("\\".equals(separator)) {
            System.out.println("Windows");
        } else if ("/".equals(separator)) {
            System.out.println("Linux");
        }
    }

    @Test
    public void importData () {
        try {
            FileReader fr = null;
            BufferedReader br = null;
            File file = new File(dataFilePath);
            String[] fileList = file.list();
            if (fileList == null) {
                System.out.println(dataFilePath + " 路径不存在");
                return;
            }
            for (int i = 0; i < fileList.length; i++) {
                System.out.println("新插入数据文件：" + fileList[i]);
                fr = new FileReader(dataFilePath + File.separator + fileList[i]);
                br = new BufferedReader(fr);
                List<String> list = map.get(fileList[i]);
                String tableName = list.get(0);

                // 先清空表数据
                String sql = "delete from " + tableName;
                pstm = conn.prepareStatement(sql);
                pstm.executeUpdate();
                System.out.println("删除表：" + tableName);

                // 拼接sql
                StringBuffer sb = new StringBuffer();
                sb.append("INSERT INTO "+tableName+"(");
                String line = "";
                String filds = "";
                String values = "";
                for (int j = 1; j < list.size(); j++) {
                    if (j == list.size() -1) {
                        filds += list.get(j) + ")";
                        values += "?)";
                        break;
                    }
                    filds += list.get(j) + ",";
                    values += "?,";
                }
                sb.append(filds+" VALUES("+values);
                System.out.println("插入Sql："+sb.toString());
                pstm = conn.prepareStatement(sb.toString());
                int count = 0;
                while((line = br.readLine()) != null) {
                    JSONObject jsonObject = JSON.parseObject(line);
                    for (int k = 1; k < list.size(); k++) {
                        pstm.setString(k, jsonObject.getString(list.get(k)));
                        count++;
                    }
                    pstm.addBatch();
                }
                pstm.executeBatch();
                System.out.println("成功插入" + count + "条记录");
                System.out.println();
            }
            if (fr != null) {
                fr.close();
                br.close();
            } else {
                System.out.println(dataFilePath + "文件夹下面没有数据相关文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
