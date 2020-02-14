package com.cdqd.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Peer节点需要保存的数据
 * Created At 2020/2/13
 */
public class PeerData {

    private static final Logger logger = LoggerFactory.getLogger(PeerData.class);

    private Integer id;

    private String name;

    private String ip;

    private Integer port;

    // 记录Order节点的文职
    private volatile Map<Integer, String> orderAddressMap = new ConcurrentHashMap<>();

    // 记录死活不明的Order节点，map<orderId, count>. OrderId: 节点ID, count: 重试次数
    private volatile Map<Integer, Integer> doubtOrderMap = new ConcurrentHashMap<>();

    public PeerData(Integer id, String name, String ip, Integer port) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public void addOrderAddress(Integer orderId, String address) {
        orderAddressMap.put(orderId, address);
    }

    /**
     * 返回可用的Order节点Map
     *
     * @return
     */
    public Map<Integer, String> getAvailableOrder() {
        Map<Integer, String> map = new HashMap<>();
        for (Map.Entry<Integer, String> entry : orderAddressMap.entrySet()) {
            if (this.doubtOrderMap.containsKey(entry.getKey())) {
                continue;
            }
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * 添加存活不明的Order节点
     *
     * @param address
     */
    public void addDoubtOrder(String address) {
        for (Map.Entry<Integer, String> entry : orderAddressMap.entrySet()) {
            if (entry.getValue().equals(address)) {
                addDoubtOrder(entry.getKey());
            }
        }
    }

    public void addDoubtOrder(Integer orderId) {
        if (!this.doubtOrderMap.containsKey(orderId)) {
            this.doubtOrderMap.put(orderId, 0);
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public Map<Integer, Integer> getDoubtOrderMap() {
        return this.doubtOrderMap;
    }

    public Map<Integer, String> getOrderAddressMap() {
        return orderAddressMap;
    }

    /**
     * 移除已经死亡的Order节点
     *
     * @param orderId
     */
    public void removeDiedOrder(Integer orderId) {
        // 判定一次，防止误删
        if (this.doubtOrderMap.containsKey(orderId) &&
                this.doubtOrderMap.get(orderId) == 2) {
            String info = this.orderAddressMap.get(orderId);
            this.orderAddressMap.remove(orderId);
            this.doubtOrderMap.remove(orderId);
            logger.info("已移除无响应Order节点，OrderId: {}, Address: {}", orderId, info);
        }
    }
}
