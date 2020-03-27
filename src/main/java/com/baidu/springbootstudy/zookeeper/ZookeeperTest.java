package com.baidu.springbootstudy.zookeeper;

import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest implements Watcher {

    public static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperTest.class);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final String ADDRESS = "10.164.27.139:8181";

    private static final String PREFIX_SYNC = "/test2020032503";

    private static ZooKeeper zooKeeper ;

//    static {
//        PropertyConfigurator.configure("D:\\code\\IDEA\\myself\\spring-boot-study\\src\\main\\resources\\log4j.properties");
//    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println("事件通知：" + event.getState());
        if(Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
            } else if (Event.EventType.NodeChildrenChanged == event.getType()) {
                try {
                    System.out.println("get Child:" + zooKeeper.getChildren(event.getPath(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        //设置默认日志级别

        LOGGER.info("123");
        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTest());

        countDownLatch.await();

//        zooKeeper.create(PREFIX_SYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));
//
//
//        zooKeeper.create(PREFIX_SYNC + "/c121", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//
//        zooKeeper.create(PREFIX_SYNC + "/c221", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//
//        zooKeeper.create(PREFIX_SYNC + "/c331", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//
//        Thread.sleep(1000 * 10);
    }


    public static void init() {
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream("D:\\code\\IDEA\\myself\\spring-boot-study\\src\\main\\resources\\log4j.properties");
            properties.load(in);
            in.close();
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
