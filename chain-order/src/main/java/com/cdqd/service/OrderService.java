package com.cdqd.service;

import java.util.Map;

/**
 * Description: 调用其他Order节点提供的服务
 * Created At 2020/2/8
 */
public interface OrderService {

    /**
     * 拉取其他Order节点网络地址
     * @param address
     * @return
     */
    Map<String, String> fetchAddress(String address);

    /**
     * 接受其他节点的注册
     * @param targetAddress
     * @param orderId
     * @param orderAddress
     */
    void register(String targetAddress, Integer orderId, String orderAddress);
}
