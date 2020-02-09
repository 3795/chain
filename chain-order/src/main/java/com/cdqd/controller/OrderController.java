package com.cdqd.controller;

import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 对外提供服务
 * Created At 2020/2/7
 */
@RestController
@RequestMapping
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * 拉取其他Order节点的网络地址
     *
     * @return
     */
    @GetMapping("/fetch-address")
    public ServerResponseVO orderAddress() {
        Map<Integer, String> orderAddressMap = orderData.getOrderAddressMap();
        return ServerResponseVO.success(orderAddressMap);
    }

    /**
     * 接受其他Order节点发送的注册请求
     *
     * @param orderId
     * @param orderAddress
     * @return
     */
    @PostMapping("/register")
    public ServerResponseVO register(@RequestParam("orderId") Integer orderId,
                                     @RequestParam("orderAddress") String orderAddress) {
        orderData.addOrderAddress(orderId, orderAddress);
        logger.info("新节点加入，OrderId:{}, Address: {}", orderId, orderAddress);
        return ServerResponseVO.success(ResponseCodeEnum.SUCCESS);
    }
}
