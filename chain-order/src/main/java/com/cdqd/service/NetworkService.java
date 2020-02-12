package com.cdqd.service;

import com.cdqd.core.Block;

import java.util.List;
import java.util.Map;

/**
 * Description: 节点的网络服务
 * Created At 2020/2/10
 */
public interface NetworkService {

    /**
     * 向其他的Order节点注册自己的网络地址
     *
     * @param targetAddress 目标Order节点
     * @return
     */
    boolean register(String targetAddress);

    /**
     * 拉取其他Order节点网络地址
     *
     * @return
     */
    Map<String, String> fetchAddressFromLeader();

    /**
     * 查询Leader节点的区块高度
     *
     * @return
     */
    int getLeaderIndex();

    /**
     * 从Leader节点拉取区块
     *
     * @param targetAddress
     * @param blockIndex
     * @param size
     * @return
     */
    List<Block> pullBlock(String targetAddress, Integer blockIndex, Integer size);

    /**
     *  向其他Order节点广播区块
     * @param address
     * @param block
     * @return
     */
    boolean broadcastBlock(String address, Block block);

    /**
     * 向其他Order节点广播区块写入的信息
     * @param address
     */
    boolean broadcastAckBlock(String address);

    /**
     * 对目标节点发送一个心跳请求
     * @param address       目标地址
     * @return  返回目标节点的区块高度
     */
    int heartConn(String address);
}
