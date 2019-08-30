package com.baidu.springbootstudy.importmysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ImportData {
    public static void main(String[] args) {
        // 初始化配置
        Map<String, String> config = BaseConfig.init();
        if (config == null) {
            System.out.println("初始化失败");
            return;
        }
        String user = config.get("user");
        String url = config.get("url");
        String password = config.get("password");
        String path = config.get("path");
        String datapath = config.get("datapath");
        Connection conn = null;
        PreparedStatement pstm =null;

        // 加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("驱动加载成功");
        System.out.println();
        Map<String, List<String>> map = BaseConfig.getMeta(path);

        // 导入数据
        importData(conn, pstm, map, datapath);
    }

    public static void importData(Connection conn, PreparedStatement pstm, Map<String, List<String>> map, String dataFilePath) {
        try {
            FileReader fr = null;
            BufferedReader br = null;
            File file = new File(dataFilePath);
            String[] fileList = file.list();
            if (fileList == null) {
                System.out.println(dataFilePath + " 数据资源路径不存在");
                return;
            }
            for (int i = 0; i < fileList.length; i++) {
                String dir = dataFilePath + File.separator + fileList[i];
                if (new File(dir).isDirectory()) {
                    continue;
                }
                System.out.println("新插入数据文件：" + fileList[i]);
                fr = new FileReader(dir);

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
                sb.append("INSERT INTO " + tableName + "(");
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
                sb.append(filds + " VALUES(" + values);
                System.out.println("插入Sql：" + sb.toString());
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
