package com.cdqd.service;

import com.cdqd.core.Block;

import java.util.List;
import java.util.Map;

/**
 * Description: Peer节点的网络服务
 * Created At 2020/2/13
 */
public interface NetworkService {

    /**
     * 从种子节点处获取其他Order节点的网络地址
     *
     * @param orderAddress
     * @return
     */
    Map<String, String> fetchOrderAddress(List<String> orderAddress);

    /**
     * 获取Order节点的区块存储高度
     *
     * @param address
     * @return
     */
    int getBlockIndex(String address);

    /**
     * 从Order节点拉取区块链
     *
     * @param address    Order地址
     * @param startIndex 起始区块索引
     * @param size       区块个数
     * @return
     */
    List<Block> pullBlock(String address, int startIndex, int size);
}
