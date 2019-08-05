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
import java.io.IOException;
import java.util.*;

public class TestElasticSearch4J {
    private static List<String> certificateCode = new ArrayList<>();
    private static List<String> faceIdList = new ArrayList<>();
    private static List<String> imsiList = new ArrayList<>();
    private static List<String> plateNumberList = new ArrayList<>();
    private static List<String> rfidList = new ArrayList<>();

    private static List<String> placeSet = new ArrayList<>();
    private static List<String> tantouId = new ArrayList<>();
    private static List<String> types = Arrays.asList("sfzh", "face_id", "imsi", "plate_number", "rfid");
    private static Map<String, List<String>> typeMap = new HashMap<>();
    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("10.133.143.27", 8089, "http")));

    public static void main(String[] args) {
        // 暂口
        String zankou = JSONUtils.readJsonFile("D:\\files\\zankou.json");
        JSONObject zankouJson = JSONObject.parseObject(zankou);
        JSONArray zankouJsonArray = zankouJson.getJSONArray("RECORDS");

        for (int i = 0 ; i < zankouJsonArray.size();i++) {
            JSONObject object = zankouJsonArray.getJSONObject(i);
            // sfzh
            String sfzh = object.getString("_source.sfzh");
            certificateCode.add(sfzh);
            // face_id
            String face_id = object.getString("_source.face_id");
            faceIdList.add(face_id);
            // rfid
            String rfid = object.getString("_source.rfid");
            rfidList.add(rfid);
            // imsi
            String imsi = object.getString("_source.imsi");
            imsiList.add(imsi);
            // 车牌号码
            String plate_number = object.getString("_source.plate_number");
            plateNumberList.add(plate_number);
        }

        // 常口
        String czrk = JSONUtils.readJsonFile("D:\\files\\czrk.json");
        JSONObject czrkJson = JSONObject.parseObject(czrk);
        JSONArray czrkJsonArray = czrkJson.getJSONArray("RECORDS");

        for (int i = 0; i < czrkJsonArray.size(); i++) {
            JSONObject object = czrkJsonArray.getJSONObject(i);
            // sfzh
            String sfzh = object.getString("_source.sfzh");
            certificateCode.add(sfzh);
            // face_id
            String face_id = object.getString("_source.face_id");
            faceIdList.add(face_id);
            // rfid
            String rfid = object.getString("_source.rfid");
            rfidList.add(rfid);
            // imsi
            String imsi = object.getString("_source.imsi");
            imsiList.add(imsi);
            // 车牌号码
            String plate_number = object.getString("_source.plate_number");
            plateNumberList.add(plate_number);
        }

        // 漏控
        String lkrk = JSONUtils.readJsonFile("D:\\files\\lkrk.json");
        JSONObject lkrkJson = JSONObject.parseObject(lkrk);
        JSONArray lkrkJsonArray = lkrkJson.getJSONArray("RECORDS");

        for (int i = 0; i < lkrkJsonArray.size(); i++) {
            JSONObject object = lkrkJsonArray.getJSONObject(i);
            String sfzh = object.getString("_source.sfzh");
            certificateCode.add(sfzh);
        }

        // 身份证号
        //certificateCode = ImportToEsUtil.getFileContert("D:\\files\\shang_wang_ji_lu", "Certificate_code");

        // 探头名称
        placeSet = ImportToEsUtil.getFileContert("D:\\files\\tan_tou_info", "name");
        // 探头ID
        tantouId = ImportToEsUtil.getFileContert("D:\\files\\tantou", null);

        typeMap.put("sfzh", certificateCode);
        typeMap.put("face_id", faceIdList);
        typeMap.put("imsi", imsiList);
        typeMap.put("plate_number", plateNumberList);
        typeMap.put("rfid", rfidList);


        String[] day = ImportToEsUtil.getLastNDaysArr(66);
        long startTime = System.currentTimeMillis();
        for (int i = 1; i< day.length; i++) {
            //System.out.println(day[i]);
            //deleteIndex("person_track_" + day[i]);
            mockData("person_track_" + day[i]);
        }
        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime) / 1000;
        System.out.println("批量插入完成，耗时：" + time + "秒");
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mockData (String indexName){
        if (!checkIndexIsExit(indexName)) {
            createIndex(indexName);
        } else {
            deleteIndex(indexName);
        }
        if (1 == 11) {
            return;
        }
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        Random random = new Random();
        for (int i = 0; i< certificateCode.size() * 5; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            int temp = random.nextInt(51);
            int typeId = random.nextInt(5);
            // 类型
            String type = types.get(typeId);
            //设备Id
            String name = placeSet.get(random.nextInt(placeSet.size()));
            map.put("device_address", name);
            map.put("device_name", name);
            map.put("cameraId", tantouId.get(temp));
            map.put("id_type", type.toUpperCase());
            map.put("device_type", "NETBAR_INFO");
            List<String> typeList = typeMap.get(type);
            if ( i / 5 >= typeList.size()) {
                String object_id = typeList.get(random.nextInt(typeList.size()));
                map.put("object_id", object_id);
                map.put("join_uniq_id", type.toUpperCase() + "$" + object_id);
            } else {
                String object_id = typeList.get(i / 5);
                map.put("object_id", object_id);
                map.put("join_uniq_id", type.toUpperCase() + "$" + object_id);
            }
            map.put("lng", "121.361593175");
            map.put("lat", "31.1177078647");
            map.put("id", i + 1);
            list.add(map);
        }
        batchInsert(indexName, list);
    }

    public static void insert (String indexName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "刘祖伟");
        IndexRequest indexRequest = new IndexRequest(indexName, "document", "1001").source(map);
        try {
            client.index(indexRequest);
            System.out.println("添加成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
