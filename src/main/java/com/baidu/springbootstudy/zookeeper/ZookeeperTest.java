package com.baidu.springbootstudy.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static final String ADDRESS = "10.164.27.139:8181";

    private static final String PREFIX_SYNC = "/test20200323";

    private static ZooKeeper zooKeeper ;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("事件通知：" + event.getState());
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }else if(Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    System.out.println("get Child:" + zooKeeper.getChildren(event.getPath(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTest());
        countDownLatch.await();

        System.out.println("create before");
        zooKeeper.create(PREFIX_SYNC, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create after");

        zooKeeper.create(PREFIX_SYNC + "/c11", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));

        zooKeeper.create(PREFIX_SYNC + "/c22", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));

        Thread.sleep(1000);

        zooKeeper.create(PREFIX_SYNC + "/c33", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(zooKeeper.getChildren(PREFIX_SYNC, true));
    }
}
