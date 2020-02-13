package com.cdqd.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Order节点需要保存的数据
 * Created At 2020/2/7
 */
public class OrderData {

    private String ip;      // 本节点的IP

    private Integer port;   // 本节点的端口

    private String seedAddress;        // leader节点的IP地址

    private boolean seed;     // 该Order节点是否是Leader节点

    private String name;        // Order节点名称

    private int id;     // Order节点Id

    private String address; // 该Order节点的网络地址

    // 记录其他Order节点的地址
    private volatile Map<Integer, String> orderAddressMap = new ConcurrentHashMap<>();

    // 记录死活不明的Order节点，map<orderId, count>. OrderId: 节点ID, count: 重试次数
    private volatile Map<Integer, Integer> doubtOrderMap = new ConcurrentHashMap<>();

    // 记录每个Order节点的区块高度，map<orderId, blockIndex>. OrderId: 节点ID, blockIndex: 该节点内部的区块高度
    private volatile Map<Integer, Integer> orderBlockIndexMap = new TreeMap<>();

    public OrderData(String ip, Integer port, boolean seed, String name, int id) {
        this.ip = ip;
        this.port = port;
        this.seed = seed;
        this.name = name;
        this.id = id;
        this.address = ip + ":" + port;
    }

    public OrderData(String ip, Integer port, boolean seed, String name, int id, String seedAddress) {
        this.ip = ip;
        this.port = port;
        this.seedAddress = seedAddress;
        this.seed = seed;
        this.name = name;
        this.id = id;
        this.address = ip + ":" + port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getSeedAddress() {
        return seedAddress;
    }

    public boolean isSeed() {
        return seed;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Map<Integer, String> getOrderAddressMap() {
        return orderAddressMap;
    }

    /**
     * 添加其他Order节点的网络地址
     *
     * @param orderId      节点ID
     * @param orderAddress 节点网络地址
     */
    public void addOrderAddress(int orderId, String orderAddress) {
        orderAddressMap.put(orderId, orderAddress);
        putOrderBlockIndex(orderId, 0);     // 默认的区块高度都为0
    }

    /**
     * 添加疑似节点
     *
     * @param orderId       节点ID
     */
    public void addDoubtOrder(int orderId) {
        if (!doubtOrderMap.containsKey(orderId)) {
            this.doubtOrderMap.put(orderId, 0);
        }
    }

    /**
     * 添加疑似节点
     * @param address       节点地址
     */
    public void addDoubtOrder(String address) {
        for (Map.Entry<Integer, String> entry : orderAddressMap.entrySet()) {
            if (entry.getValue().equals(address)) {
                addDoubtOrder(entry.getKey());
            }
        }
    }

    /**
     * 添加或更新节点区块高度记录
     *
     * @param orderId
     * @param blockIndex
     */
    public void putOrderBlockIndex(int orderId, int blockIndex) {
        orderBlockIndexMap.put(orderId, blockIndex);
    }

    public Map<Integer, Integer> getDoubtOrderMap() {
        return doubtOrderMap;
    }

    /**
     * 查找区块高度最高的节点网络地址
     *
     * @return
     */
    public String getHighestOrder() {
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(orderBlockIndexMap.entrySet());
        // 按照blockIndex降序排列
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<Integer, Integer> entry : list) {
            Integer orderId = entry.getKey();
            // 防止出现找出的节点生死不明，浪费时间，跳过，找可用的节点
            if (this.doubtOrderMap.containsKey(orderId)) {
                continue;
            }
            return this.orderAddressMap.get(orderId);
        }
        return this.seedAddress;
    }

    /**
     * 删除节点
     * @param orderId
     */
    public void removeOrder(Integer orderId) {
        this.orderAddressMap.remove(orderId);      // 删除地址信息
        this.doubtOrderMap.remove(orderId);        // 删除重试次数信息
        this.orderBlockIndexMap.remove(orderId);   // 删除节点区块高度信息
    }

    /**
     * 返回可用的Order节点Map
     *
     * @return
     */
    public Map<Integer, String> getAvailableOrder() {
        Map<Integer, String> map = new HashMap<>();
        for (Map.Entry<Integer, String> entry : orderAddressMap.entrySet()) {
            if (entry.getValue().equals(this.address)) {
                continue;
            }
            if (this.doubtOrderMap.containsKey(entry.getKey())) {
                continue;       // 跳过生死不明的节点
            }
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
