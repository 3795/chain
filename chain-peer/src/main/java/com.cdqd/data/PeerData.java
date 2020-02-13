package com.cdqd.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Peer节点需要保存的数据
 * Created At 2020/2/13
 */
public class PeerData {

    // 记录Order节点的文职
    private volatile Map<Integer, String> orderAddressMap = new ConcurrentHashMap<>();

    // 记录死活不明的Order节点，map<orderId, count>. OrderId: 节点ID, count: 重试次数
    private volatile Map<Integer, Integer> doubtOrderMap = new ConcurrentHashMap<>();

    public PeerData() {
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
}
