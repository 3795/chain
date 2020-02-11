package com.cdqd.config;

import com.cdqd.core.Block;
import com.cdqd.core.ChainData;
import com.cdqd.data.OrderData;
import com.cdqd.service.BlockChainService;
import com.cdqd.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.cdqd.data.MetaData.chainData;
import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 加载Order节点数据
 * Created At 2020/2/7
 */
@Component
public class InitSystem {

    private static Logger logger = LoggerFactory.getLogger(InitSystem.class);

    @Autowired
    private ServerInfo serverInfo;

    @Autowired
    private OrderConfig orderConfig;

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private NetworkService networkService;

    @PostConstruct
    public void init() {
        // 1. 初始化Order节点本身的数据
        initOrderData();

        // 2. 认证节点身份
        nodeAuthentication();

        // 3. 接入网络
        if (!accessNodeNetwork()) {
            return;
        }

        // 4. 同步区块或者生成初始区块
        if (orderData.isLeader()) {
            initBlockChainSystem();
        } else {
            if (!syncBlockChain()) {
                return;
            }
        }

        logger.info("接入现有节点网络成功");
        logger.info("节点启动成功，Id:{}, Name:{}, IP:{}, Port:{}", orderData.getId(), orderData.getName(),
                orderData.getIp(), orderData.getPort());
    }

    /**
     * 初始化节点数据
     */
    private void initOrderData() {
        if (orderConfig.isLeader()) {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId());
        } else {
            orderData = new OrderData(serverInfo.getHost(), serverInfo.getServerPort(),
                    orderConfig.isLeader(), orderConfig.getName(), orderConfig.getId(), orderConfig.getLeaderAddress());
        }
        chainData = new ChainData();
        logger.info("初始化节点数据成功");
    }

    /**
     * 向CA节点认证身份
     */
    private void nodeAuthentication() {
        // TODO 2. 向CA节点认证
        logger.info("CA节点认证成功");
    }

    /**
     * 加入现有的节点网络
     */
    private boolean accessNodeNetwork() {
        orderData.addOrderAddress(orderData.getId(), orderData.getAddress());        // 将自身的地址加入map
        // 3. 对于非Leader节点，需要从Leader节点处获取所有的Order节点列表，并向其他节点广播自身的网络地址
        if (!orderData.isLeader()) {
            try {
                Map<String, String> orderAddressMap = networkService.fetchAddressFromLeader();
                for (Map.Entry<String, String> entry : orderAddressMap.entrySet()) {
                    if (entry.getValue().equals(orderData.getAddress())) {
                        continue;
                    }
                    orderData.addOrderAddress(Integer.parseInt(entry.getKey()), entry.getValue());
                    boolean success = networkService.register(entry.getValue());
                    if (!success) {
                        // TODO 将该目标地址加入CheckAliveMap中，后续判断是否存活
                    }
                }
            } catch (Exception e) {
                logger.error("接入现有节点网络失败，Message: {}，节点启动失败!", e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * 从Leader节点处同步区块链
     *
     * @return
     */
    private boolean syncBlockChain() {
        // 查询Leader节点的区块高度
        int leaderIndex = networkService.getLeaderIndex();
        int initIndex = 0;
        if (blockChainService.chainExist()) {
            Block block = blockChainService.queryPrevBlock();  // 查询节点自身区块的高度
            chainData.set(block.getIndex(), block.getHashValue());
            initIndex = block.getIndex();
            if (leaderIndex <= initIndex) {
                return true;
            }
        }
        try {
            blockChainService.syncBlock(initIndex + 1, leaderIndex);
        } catch (Exception e) {
            logger.warn("同步区块失败");
            return false;
        }
        return true;
    }

    /**
     * 初始化区块链系统
     *
     * @return
     */
    private void initBlockChainSystem() {
        // 判断是否有区块存在（即节点重启情况）
        if (!blockChainService.chainExist()) {
            Block metadataBlock = Block.generateInitialBlock();
            blockChainService.insertBlock(metadataBlock);   // 将元区块放入存储系统
            logger.info("区块链网络初始化完成");
        } else {
            Block prevBlock = blockChainService.queryPrevBlock();
            chainData.set(prevBlock.getIndex(), prevBlock.getHashValue());
        }
    }
}
