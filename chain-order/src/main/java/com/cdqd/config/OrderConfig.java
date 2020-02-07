package com.cdqd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description: 读取Order节点的配置参数
 * Created At 2020/2/7
 */
@Component
@ConfigurationProperties(prefix = "order")
public class OrderConfig {

    private boolean leader;

    private String name;

    private int id;

    private String leaderIp;

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeaderIp() {
        return leaderIp;
    }

    public void setLeaderIp(String leaderIp) {
        this.leaderIp = leaderIp;
    }
}
