package com.cdqd.service;

/**
 * Description: Order节点具有的功能
 * Created At 2020/2/8
 */
public interface OrderFunction {

    /**
     * 向其他的Order节点注册自己的网络地址
     *
     * @param targetAddress 目标Order节点
     * @param orderAddress  节点自身的网络地址
     * @param orderId       节点自身的ID
     * @return
     */
    boolean register(String targetAddress, Integer orderId, String orderAddress);
}
