package com.baidu.springbootstudy.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.*;

public class InsertToEs {
    // 常口
    private static List<String> czrkFields = Arrays.asList("sfzh","rklx","sex","address","jggjdq","name","byzk","zjxy","whcd","udwid","hyzk","age");
    // 暂口
    private static List<String> zzrkFields = Arrays.asList("sfzh","rklx","sex","address","name","rybh","rfid","whcd","udwid","hyzk","plate_number","age","face_id","imsi");
    // 漏控
    private static List<String> lkrkFields = Arrays.asList("sfzh","rfid","udwid","face_id","rybh","plate_number","imsi");

    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("10.133.143.27", 8089, "http")));

    static {
        PropertyConfigurator.configure("D:\\code\\IDEA\\myself\\spring-boot-study\\src\\main\\resources\\log4j.properties");
    }

    public static void main(String[] args) {
        insertPersonInfo();
    }

    public static void insertSfzh () {
        String path = "D:\\files\\drug_241-241";
        // 探头名称
        List<String> placeSet = ESUtil.getFileContert("D:\\files\\tan_tou_info", "name");
        // 探头ID
        List<String> tantouId = ESUtil.getFileContert("D:\\files\\tantou", null);

        List<String> sfzhList = ESUtil.getLine(path);
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();

        String zzrk = ESUtil.readJsonFile("D:\\files\\zzrk.json");
        JSONObject jsonObject = JSONObject.parseObject(zzrk);
        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");
        Random random = new Random();
        for (int i = 0; i < sfzhList.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            int temp = random.nextInt(51);
            Map<String, Object> map = new HashMap<String, Object>();
            //设备Id
            String name = placeSet.get(random.nextInt(placeSet.size()));
            String sfzh = sfzhList.get(i);
            map.put("object_id", sfzh);
            map.put("id", i + 5950);
            map.put("address", object.get("address"));
            map.put("name", object.get("name"));
            map.put("sex", "男");
            map.put("cameraId", tantouId.get(temp));
            map.put("device_address", name);
            map.put("device_name", name);
            map.put("id_type", "SFZH");
            map.put("join_uniq_id", "SFZH$" + sfzh);
            map.put("lng", "121.361593175");
            map.put("lat", "31.1177078647");
            list.add(map);
        }
        String[] rangeDays = ESUtil.getRangeDays(12, 20);
        for (int i = 0; i < rangeDays.length; i++) {
            batchInsert("person_track_" + rangeDays[i], list);
        }
        //batchInsert("test_lzw_20190801", list);

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertPersonInfo () {
        List<String> stringList = Arrays.asList("personal_info_zzrk", "personal_info_czrk", "personal_info_lkrk");
        for (String indexName : stringList) {
            if (!checkIndexIsExit(indexName)) {
                createIndex(indexName);
            } else {
                deleteIndex(indexName);
            }
        }
        if (1 == 11) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 暂口
        List<Map<String, Object>> zzrkList = data("D:\\files\\zzrk.json", zzrkFields);
        // 常口
        List<Map<String, Object>> czrkList = data("D:\\files\\czrk.json", czrkFields);
        // 漏控
        List<Map<String, Object>> lkrkList = data("D:\\files\\lkrk.json", lkrkFields);
        if (1 == 1) {
            batchInsert("personal_info_zzrk", zzrkList);
            batchInsert("personal_info_czrk", czrkList);
            batchInsert("personal_info_lkrk", lkrkList);
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> data (String path, List<String> typeList) {
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        String lkrk = ESUtil.readJsonFile(path);
        JSONObject jsonObject = JSONObject.parseObject(lkrk);
        JSONArray jsonArray = jsonObject.getJSONArray("RECORDS");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Map<String, Object> map = new HashMap<String, Object>();
            for (int j = 0; j < typeList.size(); j++) {
                String key = typeList.get(j);
                map.put(key, object.get("_source." + key));
            }
            //map.put("id", i + 1);
            map.put("id", object.get("_source.udwid"));
            list.add(map);
        }
        System.out.println(list);
        return list;
    }

    public static void batchInsert (String indexName, List<Map<String, Object>> lists) {
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> map : lists) {
            IndexRequest indexRequest = new IndexRequest(indexName, "document", map.get("id").toString()).source(map);
            bulkRequest.add(indexRequest);
        }
        try {
            client.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIndexIsExit (String indexName) {
        OpenIndexRequest indexRequest = new OpenIndexRequest(indexName);
        boolean res = true;
        try {
            client.indices().open(indexRequest).isAcknowledged();
        } catch (ElasticsearchStatusException e) {
            String m = "Elasticsearch exception [type=index_not_found_exception, reason=no such index]";
            if (m.equals(e.getMessage())) {
                System.out.println(indexName + "索引不存在");
                res = false;
            }
        } catch (IOException e) {
            System.out.println("检查索引是否存在发生IOException异常");
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static void createIndex (String indexName) {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        try {
            client.indices().create(indexRequest);
            System.out.println(indexName + "索引创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteIndex (String indexName) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            client.indices().delete(deleteIndexRequest);
            System.out.println(indexName + "索引删除成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
