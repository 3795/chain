package com.cdqd.config;

import com.cdqd.data.OrderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description: 加载Order节点数据
 * Created At 2020/2/7
 */
@Component
public class InitData {

    private static Logger logger = LoggerFactory.getLogger(InitData.class);

    @Autowired
    private ServerInfo serverInfo;

    @Autowired
    private OrderConfig orderConfig;

    private static volatile OrderData orderData;

    @PostConstruct
    public void init() {
        // 1. 初始化Order节点本身的数据
        if (orderConfig.isLeader()) {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId());
        } else {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId(), orderConfig.getLeaderIp());
        }

        // TODO 2. 向CA节点认证，暂时跳过

        // 3. 对于非Leader节点，需要从Leader节点处获取所有的Order节点列表，并向其他节点广播自身的网络地址
        if (orderData.isLeader()) {
            orderData.addAddress(orderData.getId(), orderData.getAddress());
        } else {

        }
    }
}
