package com.cdqd.service;

import java.util.Map;

/**
 * Description: Peer节点功能
 * Created At 2020/2/13
 */
public interface OrderService {

    /**
     * 拉取其他Order节点网络地址
     *
     * @param address
     * @return
     */
    Map<String, String> fetchAddress(String address);
}
