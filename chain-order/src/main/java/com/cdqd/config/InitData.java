package com.cdqd.config;

import com.cdqd.data.OrderData;
import com.cdqd.exception.ServerException;
import com.cdqd.service.OrderFunction;
import com.cdqd.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFunction orderFunction;

    private static volatile OrderData orderData;

    @PostConstruct
    public void init() {
        // 1. 初始化Order节点本身的数据
        if (orderConfig.isLeader()) {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId());
        } else {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId(), orderConfig.getLeaderAddress());
        }

        logger.info("初始化节点数据成功");

        // TODO 2. 向CA节点认证，暂时跳过
        logger.info("CA节点认证成功");

        orderData.addAddress(orderData.getId(), orderData.getAddress());        // 将自身的地址加入map
        // 3. 对于非Leader节点，需要从Leader节点处获取所有的Order节点列表，并向其他节点广播自身的网络地址
        if (!orderData.isLeader()) {
            try {
                Map<String, String> orderAddressMap = orderService.fetchAddress(orderData.getLeaderAddress());
                for (Map.Entry<String, String> entry : orderAddressMap.entrySet()) {
                    this.addOrderAddress(Integer.parseInt(entry.getKey()), entry.getValue());
                    boolean success = orderFunction.register(entry.getValue(), orderData.getId(), orderData.getAddress());
                    if (!success) {
                        // TODO 将该目标地址加入CheckAliveMap中，后续判断是否存活
                    }
                }
            } catch (Exception e) {
                logger.error("接入现有节点网络失败，Message: {}", e.getMessage());
                return;
            }

        }
        logger.info("接入现有节点网络成功");
        logger.info("节点启动成功，Id:{}, Name:{}, IP:{}, Port:{}", orderData.getId(), orderData.getName(),
                orderData.getIp(), orderData.getPort());
    }

    /**
     * 添加Order节点网络地址
     * @param orderId
     * @param orderAddress
     */
    public void addOrderAddress(Integer orderId, String orderAddress) {
        orderData.addAddress(orderId, orderAddress);
    }

    /**
     * 获取本节点保存的Order节点Id和网络地址
     * @return
     */
    public Map<Integer, String> getOrderAddressMap() {
        return orderData.getOrderAddressMap();
    }
}
