package com.baidu.springbootstudy.es;

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
    private static Map<String, String> mapType = new HashMap<String, String>();
    private static List<String> certificateCode = new ArrayList<String>();
    private static List<String> placeSet = new ArrayList<String>();
    private static List<String> tantouId = new ArrayList<String>();
    private static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("10.133.143.27", 8089, "http")));

    public static void main(String[] args) {

        // 身份证号
        certificateCode = ImportToEsUtil.getFileContert("D:\\files\\shang_wang_ji_lu", "Certificate_code");
        // 探头名称
        placeSet = ImportToEsUtil.getFileContert("D:\\files\\tan_tou_info", "name");
        // 探头ID
        tantouId = ImportToEsUtil.getFileContert("D:\\files\\tantou", null);

        String[] day = ImportToEsUtil.getLastNDaysArr(2);
        long startTime = System.currentTimeMillis();
        for (int i = 1; i< day.length; i++) {
            System.out.println(day[i]);
            //deleteIndex("person_track_" + day[i]);
            //insert("person_track_20190731");
            mockData("test_lzw_" + day[i]);
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
        for (int i = 0; i< certificateCode.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            int temp = random.nextInt(20);
            //设备Id
            String name = placeSet.get(i);
            String sfzh = certificateCode.get(i);
            map.put("device_address", name);
            map.put("device_name", name);
            map.put("cameraId", tantouId.get(temp));
            map.put("id_type", "SFZH");
            map.put("device_type", "NETBAR_INFO");
            map.put("object_id", sfzh);
            map.put("join_uniq_id", "SFZH$" + sfzh);
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
