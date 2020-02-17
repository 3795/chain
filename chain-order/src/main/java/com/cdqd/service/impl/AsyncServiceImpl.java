package com.cdqd.service.impl;

import com.cdqd.core.Block;
import com.cdqd.service.AsyncService;
import com.cdqd.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.cdqd.data.MetaData.orderData;

@Service
public class AsyncServiceImpl implements AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private NetworkService networkService;


    @Async(value = "asyncServiceExecutor")
    @Override
    public void broadcastBlock(String address, Block block) {
        boolean result = networkService.broadcastBlock(address, block);
        if (result) {
            BlockChainServiceImpl.addSuccessCount();
        } else {
            // 发送失败，则将该节点信息加入怀疑节点，确认节点是否存活
            orderData.addDoubtOrder(address);
        }
        BlockChainServiceImpl.addAddressCount();
    }


}
