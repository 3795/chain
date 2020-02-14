package com.cdqd.service;

import com.cdqd.core.Block;

/**
 * Description: 对节点中的区块链进行操作
 * Created At 2020/2/13
 */
public interface BlockChainService {
    /**
     * 判断本地是否存在区块链
     * @return
     */
    boolean chainExist();

    /**
     * 查询节点中保存的上一个区块
     * @return
     */
    Block queryPrevBlock();

    /**
     * 从Order节点处同步区块
     */
    boolean syncBlock();

    /**
     * 向本地存储系统中写入区块链
     * @param block
     */
    void insertBlock(Block block);
}
