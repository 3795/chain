package com.cdqd.service.impl;

import com.alibaba.fastjson.JSON;
import com.cdqd.core.Block;
import com.cdqd.dto.BlockDTO;
import com.cdqd.service.HTTPService;
import com.cdqd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public int getBlockIndex(String targetAddress) {
        String responseStr = httpService.get(HTTP_PREFIX + targetAddress + "/order/block-index");
        return Integer.parseInt(responseStr);
    }

    @Override
    public List<Block> pullBlock(String targetAddress, Integer blockIndex, Integer size) {
        Map<String, String> params = new HashMap<>();
        params.put("index", blockIndex.toString());
        params.put("size", size.toString());
        String responseStr = httpService.get(HTTP_PREFIX + targetAddress + "/order/pull-block", params);
        List list = JSON.parseObject(responseStr, List.class);
        List<Block> blockList = new ArrayList<>();
        for (Object object : list) {
            String str = JSON.toJSONString(object);
            BlockDTO blockDTO = JSON.parseObject(str, BlockDTO.class);
            blockList.add(Block.blockDTO2Block(blockDTO));
        }
        return blockList;
    }

    @Override
    public void broadcastBlock(String address, Block block) {
        httpService.postForObject(HTTP_PREFIX + address + "/order/broad-block", block);
    }

    @Override
    public void broadcastAckBlock(String address) {
        httpService.postForObject(HTTP_PREFIX + address + "/order/ack-block", null);
    }

}
