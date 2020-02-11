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
     * @param startIndex 起始区块索引
     * @param endIndex   终止区块索引
     */
    void syncBlock(Integer startIndex, Integer endIndex);

    /**
     * 打包区块，
     * @param messageList
     * @return
     */
    boolean packAndBroadcast(List<String> messageList);
}
