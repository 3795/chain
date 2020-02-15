package com.cdqd.controller;

import com.cdqd.service.NodeInfoService;
import com.cdqd.vo.ServerResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CaController {

    @Autowired
    private NodeInfoService nodeInfoService;

    /**
     * 验证节点是否合法
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     * @param publicKey 节点的公钥
     * @return
     */
    @PostMapping("/auth")
    public ServerResponseVO auth(@RequestParam("nodeId") Integer nodeId,
                                 @RequestParam("nodeName") String nodeName,
                                 @RequestParam("publicKey") String publicKey,
                                 @RequestParam("address") String address,
                                 @RequestParam("nodeType") Integer nodeType) {
        nodeInfoService.auth(nodeId, nodeName, publicKey, address, nodeType);
        return ServerResponseVO.success("OK");
    }

    /**
     * 查询各节点的公钥
     * @param nodeId
     * @return
     */
    @GetMapping("/public-key")
    public ServerResponseVO getPublicKey(@RequestParam("nodeId") Integer nodeId) {
        String publicKey = nodeInfoService.queryPublicKey(nodeId);
        return ServerResponseVO.success(publicKey);
    }
}
