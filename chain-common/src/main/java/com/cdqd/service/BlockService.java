package com.cdqd.service;

import com.cdqd.core.Block;

/**
 * Description: 存储区块链的接口
 * Created At 2020/2/9
 */
public interface BlockService {

    /**
     * 判断数据库中是否存储过区块
     * @return
     */
    boolean exist();

    /**
     * 追加区块
     * @param block
     */
    void insertBlock(Block block);


}
