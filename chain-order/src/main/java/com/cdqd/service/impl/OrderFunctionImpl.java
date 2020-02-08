package com.cdqd.service.impl;

import com.cdqd.service.OrderFunction;
import com.cdqd.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: Order节点具有的功能
 * Created At 2020/2/8
 */
@Service
public class OrderFunctionImpl implements OrderFunction {

    private static Logger logger = LoggerFactory.getLogger(OrderFunctionImpl.class);

    @Autowired
    private OrderService orderService;

    @Override
    public boolean register(String targetAddress, Integer orderId, String orderAddress) {
        try {
            orderService.register(targetAddress, orderId, orderAddress);
        } catch (Exception e) {
            logger.error("向节点注册{}失败, Message: {}", e.getMessage());
            return false;
        }
        return true;
    }
}
