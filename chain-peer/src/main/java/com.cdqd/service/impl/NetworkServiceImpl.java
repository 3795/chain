package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.exception.ServerException;
import com.cdqd.service.NetworkService;
import com.cdqd.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cdqd.data.MetaData.peerData;

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
        try {
            return orderService.getBlockIndex(address);
        } catch (Exception e) {
            peerData.addDoubtOrder(address);
            throw new ServerException("获取节点 " + address + " 区块高度失败");
        }
    }

    @Override
    public List<Block> pullBlock(String address, int startIndex, int size) {
        try {
            return orderService.pullBlock(address, startIndex, size);
        } catch (Exception e) {
            peerData.addDoubtOrder(address);
            logger.warn("Message: {}", e.getMessage());
            throw new ServerException("从节点 " + address + " {} 拉取区块失败");
        }
    }

    @Override
    public int heartConn(String address) {
        try {
            return this.getBlockIndex(address);
        } catch (Exception e) {
            logger.warn("节点 {} 无心跳响应", address);
            return -1;
        }
    }

    @Override
    public boolean commitData(String data) {
        for (Map.Entry<Integer, String> entry : peerData.getAvailableOrder().entrySet()) {
            try {
                orderService.commitData(entry.getValue(), peerData.getId(), data);
                return true;
            } catch (Exception e) {
                /**
                 * 此处要对异常做出详细的判断：
                 * 1. 网络异常：直接向其他的Order节点提交，并将该Order节点加入可疑节点列表
                 * 2. 数据错误，如加解密错误等，直接返回信息提示用户
                 */
                logger.error("向Order {} 节点提交数据失败，Message: {}", entry.getValue(), e.getMessage());
            }
        }
        return false;
    }
}
