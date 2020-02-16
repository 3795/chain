package com.cdqd.client;

import com.cdqd.vo.ServerResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 调用ca节点接口
 * Created At 2020/2/16
 */
@FeignClient(value = "chain-ca", fallback = CaClientFallback.class)
public interface CaClient {

    @PostMapping("/ca/auth")
    ServerResponseVO auth(@RequestParam("nodeId") Integer nodeId,
                          @RequestParam("nodeName") String nodeName,
                          @RequestParam("publicKey") String publicKey,
                          @RequestParam("address") String address,
                          @RequestParam("nodeType") Integer nodeType);

    @GetMapping("/ca/public-key")
    ServerResponseVO getPublicKey(@RequestParam("nodeId") Integer nodeId);


}


