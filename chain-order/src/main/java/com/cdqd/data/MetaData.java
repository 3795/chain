package com.cdqd.data;

import com.cdqd.core.ChainData;

/**
 * Description: 系统元数据
 * Created At 2020/2/9
 */
public class MetaData {

    public static volatile OrderData orderData;     // 节点本身的数据

    public static volatile ChainData chainData;     // 节点存储的区块信息
}
