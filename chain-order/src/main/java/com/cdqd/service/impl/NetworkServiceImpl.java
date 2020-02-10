package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.service.NetworkService;
import com.cdqd.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 节点网络服务
 * Created At 2020/2/10
 */
@Service
public class NetworkServiceImpl implements NetworkService {

    private final static Logger logger = LoggerFactory.getLogger(NetworkServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Override
    public boolean register(String targetAddress) {
        try {
            orderService.register(targetAddress, orderData.getId(), orderData.getAddress());
        } catch (Exception e) {
            logger.error("向节点注册{}失败, Message: {}", targetAddress, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Map<String, String> fetchAddressFromLeader() {
        return orderService.fetchAddress(orderData.getLeaderAddress());
    }

    @Override
    public int getLeaderIndex() {
        return orderService.getLeaderIndex(orderData.getLeaderAddress());
    }

    @Override
    public List<Block> pullBlockFromLeader(Integer blockIndex, Integer size) {
        return orderService.pullBlock(orderData.getLeaderAddress(), blockIndex, size);
    }
}
