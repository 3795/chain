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
     * @param blockIndex
     * @param size
     * @return
     */
    List<Block> pullBlockFromLeader(Integer blockIndex, Integer size);
}
