package com.cdqd.service;

/**
 * 节点信息
 */
public interface NodeInfoService {

    /**
     * 验证节点合法性
     * @param nodeId
     * @param nodeName
     * @param publicKey
     */
    void auth(Integer nodeId, String nodeName, String publicKey, String address, Integer nodeType);

    /**
     * 查询节点的公钥
     * @param nodeId
     * @return
     */
    String queryPublicKey(Integer nodeId);
}
