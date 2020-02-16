package com.cdqd.client;

import com.cdqd.vo.ServerResponseVO;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Created At 2020/2/16
 */
@Component
public class CaClientFallback implements CaClient {

    @Override
    public ServerResponseVO auth(Integer nodeId,
                                 String nodeName,
                                 String publicKey,
                                 String address,
                                 Integer nodeType) {
        return ServerResponseVO.error("无法连接到CA节点");
    }
}
