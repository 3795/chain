package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.exception.ServerException;
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
            logger.error("向节点注册 {} 失败, Message: {}", targetAddress, e.getMessage());
            orderData.addDoubtOrder(targetAddress);
            return false;
        }
        return true;
    }

    @Override
    public Map<String, String> fetchAddressFromSeed() {
        try {
            return orderService.fetchAddress(orderData.getSeedAddress());
        } catch (Exception e) {
            throw new ServerException(String.format("从种子节点 %s 拉取其他Order节点网络地址失败, Message: %s",
                    orderData.getSeedAddress(), e.getMessage()));
        }
    }

    @Override
    public int getBlockIndex(String targetAddress) {
        try {
            return orderService.getBlockIndex(targetAddress);
        } catch (Exception e) {
            orderData.addDoubtOrder(targetAddress);
            throw new ServerException("获取节点 " + targetAddress + " 区块高度失败");
        }
    }

    @Override
    public List<Block> pullBlock(String targetAddress, Integer blockIndex, Integer size) {
        try {
            return orderService.pullBlock(targetAddress, blockIndex, size);
        } catch (Exception e) {
            orderData.addDoubtOrder(targetAddress);
            throw new ServerException("从节点 " + targetAddress + " {} 拉取区块失败");
        }
    }

    @Override
    public boolean broadcastBlock(String address, Block block) {
        try {
            orderService.broadcastBlock(address, block);
            return true;
        } catch (Exception e) {
            if (!ResponseCodeEnum.IGNORE_BROAD_BLOCK.getMessage().equals(e.getMessage())) {
                orderData.addDoubtOrder(address);
            }
            logger.warn("BroadcastBlock failed, Address: {}, Message: {}", address, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean broadcastAckBlock(String address) {
        try {
            orderService.broadcastAckBlock(address);
            return true;
        } catch (Exception e) {
            logger.warn("BroadcastAckBlock failed, Address{}, Message: {}", address, e.getMessage());
            orderData.addDoubtOrder(address);
            return false;
        }
    }

    @Override
    public int heartConn(String address) {
        try {
            return orderService.getBlockIndex(address);
        } catch (Exception e) {
            logger.warn("Heart connection no response, address: {}", address);
            return -1;
        }
    }
}
