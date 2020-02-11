package com.cdqd.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Order节点需要保存的数据
 * Created At 2020/2/7
 */
public class OrderData {

    private String ip;      // 本节点的IP

    private Integer port;   // 本节点的端口

    private String leaderAddress;        // leader节点的IP地址

    private volatile boolean leader;     // 该Order节点是否是Leader节点

    private String name;        // Order节点名称

    private int id;     // Order节点Id1

    private String address; // 该Order节点的网络地址

    private volatile Map<Integer, String> orderAddressMap = new ConcurrentHashMap<>();        // 其他Order节点的地址

    public OrderData() {
    }

    public OrderData(String ip, Integer port, boolean leader, String name, int id) {
        this.ip = ip;
        this.port = port;
        this.leader = leader;
        this.name = name;
        this.id = id;
        this.address = ip + ":" + port;
    }

    public OrderData(String ip, Integer port, boolean leader, String name, int id, String leaderAddress) {
        this.ip = ip;
        this.port = port;
        this.leaderAddress = leaderAddress;
        this.leader = leader;
        this.name = name;
        this.id = id;
        this.address = ip + ":" + port;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public void setLeaderAddress(String leaderAddress) {
        this.leaderAddress = leaderAddress;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getLeaderAddress() {
        return leaderAddress;
    }

    public boolean isLeader() {
        return leader;
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
    }

}
