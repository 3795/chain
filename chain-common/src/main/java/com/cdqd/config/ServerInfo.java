package com.cdqd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description: 获取节点的IP地址和端口号
 * Created At 2020/2/7
 */
@Component
public class ServerInfo {

    private Logger logger = LoggerFactory.getLogger(ServerInfo.class);

    @Value("${server.port}")
    private int serverPort;

    @Value("${spring.cloud.client.ip-address}")
    private String serverHost;

    /**
     * 获取服务的IP地址
     * @return
     */
    public String getHost() {
        return serverHost;
    }

    /**
     * 获取服务的端口
     * @return
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * 获取服务的网络地址
     * @return
     */
    public String getUrl() {
        return "http://" + getHost() + ":" + getServerPort();
    }


}
