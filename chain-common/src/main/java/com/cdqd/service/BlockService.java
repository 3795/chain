package com.cdqd.service;

import com.cdqd.core.Block;

import java.util.List;

/**
 * Description: 存储区块链的接口
 * Created At 2020/2/9
 */
public interface BlockService {

    /**
     * 判断数据库中是否存储过区块
     *
     * @return
     */
    boolean exist();

    /**
     * 追加区块
     *
     * @param block
     */
    void insertBlock(Block block);

    /**
     * 查询最新追加的区块
     *
     * @return
     */
    Block queryPrevBlock();

    /**
     * 拉取本节点保存的区块
     *
     * @param blockIndex 起始区块索引
     * @param size  区块个数
     * @return
     */
    List<Block> pullBlock(Integer blockIndex, Integer size);
}
