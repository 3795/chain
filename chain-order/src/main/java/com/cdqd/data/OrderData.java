package com.cdqd.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Order节点需要保存的数据
 * Created At 2020/2/7
 */
public class OrderData {

    private String ip;      // 本节点的IP

    private Integer port;   // 本节点的端口

    private String leaderIp;        // leader节点的IP地址

    private boolean leader;

    private String name;

    private int id;

    private String address;

    private volatile Map<Integer, String> orderAddressMap = new ConcurrentHashMap<>();        // 其他Order节点的地址

    public OrderData() {
    }

    public OrderData(String ip, Integer port, boolean leader, String name, int id) {
        this.ip = ip;
        this.port = port;
        this.leader = leader;
        this.name = name;
        this.id = id;
        this.address = "http://" + ip + ":" + port;
    }

    public OrderData(String ip, Integer port, boolean leader, String name, int id, String leaderIp) {
        this.ip = ip;
        this.port = port;
        this.leaderIp = leaderIp;
        this.leader = leader;
        this.name = name;
        this.id = id;
        this.address = "http://" + ip + ":" + port;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public void setLeaderIp(String leaderIp) {
        this.leaderIp = leaderIp;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getLeaderIp() {
        return leaderIp;
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

    /**
     * 添加其他Order节点的网络地址
     * @param orderId       节点ID
     * @param orderAddress  节点网络地址
     */
    public void addAddress(int orderId, String orderAddress) {
        orderAddressMap.put(orderId, orderAddress);
    }

}
