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

    private boolean seed;

    private String name;

    private int id;

    private String seedAddress;

    public boolean isSeed() {
        return seed;
    }

    public void setSeed(boolean seed) {
        this.seed = seed;
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

    public String getSeedAddress() {
        return seedAddress;
    }

    public void setSeedAddress(String seedAddress) {
        this.seedAddress = seedAddress;
    }
}
