package com.cdqd.service.impl;

import com.alibaba.fastjson.JSON;
import com.cdqd.service.HTTPService;
import com.cdqd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 调用其他Order节点提供的服务
 * Created At 2020/2/8
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private HTTPService<String> httpService;

    private static final String HTTP_PREFIX = "http://";

    @Override
    public Map<String, String> fetchAddress(String address) {
        String responseStr = httpService.get(HTTP_PREFIX + address + "/order/fetch-address");
        return JSON.parseObject(responseStr, Map.class);
    }

    @Override
    public void register(String targetAddress, Integer orderId, String orderAddress) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        paramMap.put("orderAddress", orderAddress);
        httpService.post(HTTP_PREFIX + targetAddress + "/order/register", paramMap);
    }

}
