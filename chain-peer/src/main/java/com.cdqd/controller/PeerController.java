package com.cdqd.controller;

import com.cdqd.data.MetaData;
import com.cdqd.service.NetworkService;
import com.cdqd.util.EncryptUtil;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 控制器
 * Created At 2020/2/14
 */
@RestController
@RequestMapping
public class PeerController {

    private static final Logger logger = LoggerFactory.getLogger(PeerController.class);

    @Autowired
    private NetworkService networkService;

    @PostMapping("/commit-data")
    public ServerResponseVO commitData(@RequestParam("data") String data) {
        try {
            // 生成签名
            String sign = EncryptUtil.sign(MetaData.peerData.getPrivateKey(), data);
            boolean result = networkService.commitData(data, sign);
            if (result) {
                return ServerResponseVO.success("数据提交成功");
            } else {
                return ServerResponseVO.error("数据提交失败");
            }
        } catch (Exception e) {
            logger.error("RSA签名失败，Message：{}", e.getMessage());
            return ServerResponseVO.error("服务器内部错误，数据提交失败");
        }
    }
}
