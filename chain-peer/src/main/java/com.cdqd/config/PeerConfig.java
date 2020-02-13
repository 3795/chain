package com.cdqd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: Peer节点的配置
 * Created At 2020/2/13
 */
@Component
@ConfigurationProperties(prefix = "peer")
public class PeerConfig {

    private Integer id;

    private String name;

    private List<String> orderAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(List<String> orderAddress) {
        this.orderAddress = orderAddress;
    }
}
