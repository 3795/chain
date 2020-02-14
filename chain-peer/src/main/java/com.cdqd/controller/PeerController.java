package com.cdqd.controller;

import com.cdqd.service.NetworkService;
import com.cdqd.vo.ServerResponseVO;
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

    @Autowired
    private NetworkService networkService;

    @PostMapping("/commit-data")
    public ServerResponseVO commitData(@RequestParam("data") String data) {
        boolean result = networkService.commitData(data);
        if (result) {
            return ServerResponseVO.success("数据提交成功");
        } else {
            return ServerResponseVO.error("数据提交失败");
        }
    }
}
