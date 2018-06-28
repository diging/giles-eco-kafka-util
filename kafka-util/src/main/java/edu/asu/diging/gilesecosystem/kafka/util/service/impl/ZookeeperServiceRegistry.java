package edu.asu.diging.gilesecosystem.kafka.util.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.gilesecosystem.kafka.util.service.IZookeeperServiceRegistry;
import edu.asu.diging.gilesecosystem.kafka.util.service.Properties;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

/**
 * This class registers Nepomuk with Zookeeper for discovery by Giles (or 
 * potentially other services).
 * 
 * @author jdamerow
 *
 */
@Service
public class ZookeeperServiceRegistry implements IZookeeperServiceRegistry {
    
    private String znode;
    private String appUrl;

    private CuratorFramework curatorFramework;
    private ConcurrentHashMap<String, String> uriToZnodePath;
    
    @Autowired
    private IPropertiesManager propertiesManager;

    @PostConstruct
    public void init() throws Exception {
        curatorFramework = CuratorFrameworkFactory
                .newClient(propertiesManager.getProperty(Properties.ZOOKEEPER_HOST) 
                        + ":" 
                        + propertiesManager.getProperty(Properties.ZOOKEEPER_PORT) , new RetryNTimes(5, 1000));
        curatorFramework.start();
        uriToZnodePath = new ConcurrentHashMap<>();
        
        znode = propertiesManager.getProperty(Properties.ZOOKEEPER_SERVICE_ROOT) + propertiesManager.getProperty(Properties.ZOOKEEPER_SERVICE_NAME);
        appUrl = propertiesManager.getProperty(Properties.APP_BASE_URL);
        registerApp();
    }
    
    private void registerApp() throws Exception {
        // forPath actually throws Exception
        if (curatorFramework.checkExists().forPath(znode) == null) {
            curatorFramework.create().creatingParentsIfNeeded().forPath(znode);
        }
        String znodePath = curatorFramework
                .create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(znode+"/_", appUrl.getBytes());
        uriToZnodePath.put(appUrl, znodePath);
    }
}
