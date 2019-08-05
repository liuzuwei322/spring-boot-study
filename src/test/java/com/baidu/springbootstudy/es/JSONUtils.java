package com.baidu.springbootstudy.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;

public class JSONUtils {

    public static void main(String[] args) throws  Exception{
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
}
