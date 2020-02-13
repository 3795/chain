package com.cdqd.service.impl;

import com.alibaba.fastjson.JSON;
import com.cdqd.service.HTTPService;
import com.cdqd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:
 * Created At 2020/2/13
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
}
