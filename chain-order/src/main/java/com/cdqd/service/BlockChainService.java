package com.cdqd.service;

import com.cdqd.core.Block;

import java.util.List;

/**
 * Description: 节点使用的区块链服务
 * Created At 2020/2/10
 */
public interface BlockChainService {

    /**
     * 判断该节点中是否有区块链
     *
     * @return
     */
    boolean chainExist();

    /**
     * 添加一个区块到节点中
     *
     * @param block
     */
    void insertBlock(Block block);

    /**
     * 查询该节点最新的区块
     *
     * @return
     */
    Block queryPrevBlock();

    /**
     * 拉取本节点保存的区块
     *
     * @param blockIndex 起始区块索引
     * @param size       区块个数
     * @return
     */
    List<Block> pullBlock(Integer blockIndex, Integer size);

    /**
     * 以Leader节点为准，同步区块
     *
     * @param targetAddress 目标节点地址
     */
    void syncBlock(String targetAddress);

    /**
     * 打包区块，写入区块，并向其他Order节点广播该区块
     *
     * @param messageList
     * @return
     */
    boolean packAndBroadcast(List<String> messageList);

    /**
     * 向其他节点广播区块
     *
     * @param block
     * @return
     */
    boolean broadcast(Block block);
}
