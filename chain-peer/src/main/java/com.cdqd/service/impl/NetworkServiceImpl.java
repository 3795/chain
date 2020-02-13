package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.service.NetworkService;
import com.cdqd.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Created At 2020/2/13
 */
@Service
public class NetworkServiceImpl implements NetworkService {

    private static final Logger logger = LoggerFactory.getLogger(NetworkServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Override
    public Map<String, String> fetchOrderAddress(List<String> orderAddress) {
        Map<String, String> map = new HashMap<>();
        if (orderAddress.size() == 0) {
            logger.error("未填写种子节点位置");
            return map;
        }
        for (String address : orderAddress) {
            try {
                map = orderService.fetchAddress(address);
                if (map.size() > 0) {
                    return map;
                }
            } catch (Exception e) {
                logger.warn("从节点 {} 获取Order列表失败, Message: {}", address, e.getMessage());
            }
        }
        return map;
    }

    @Override
    public int getBlockIndex(String address) {
        return 0;
    }

    @Override
    public List<Block> pullBlock(String address, int startIndex, int size) {
        return null;
    }
}
