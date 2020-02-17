package com.cdqd.service;

import com.cdqd.core.Block;

/**
 * 异步服务
 */
public interface AsyncService {

    /**
     * 异步的向其他Order节点广播区块
     * @param address
     * @param block
     */
    void broadcastBlock(String address, Block block);
}
