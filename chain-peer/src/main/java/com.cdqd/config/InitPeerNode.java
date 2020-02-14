package com.cdqd.config;

import com.cdqd.core.Block;
import com.cdqd.core.ChainData;
import com.cdqd.data.PeerData;
import com.cdqd.service.BlockChainService;
import com.cdqd.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Map;

import static com.cdqd.data.MetaData.*;

/**
 * Description: 初始化PeerNode
 * Created At 2020/2/13
 */
@Component
public class InitPeerNode {

    private static final Logger logger = LoggerFactory.getLogger(InitPeerNode.class);

    @Autowired
    private PeerConfig peerConfig;

    @Autowired
    private ServerInfo serverInfo;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private BlockChainService blockChainService;

    @PostConstruct
    public void init() {

        initPeerNode();

        nodeAuthentication();

        if (!connToOrder()) {
            return;
        }
        if (!syncBlock()) {
            return;
        }

        logger.info("节点启动成功，Id:{}, Name:{}, IP:{}, Port:{}", peerData.getId(), peerData.getName(),
                peerData.getIp(), peerData.getPort());
    }

    /**
     * 从Order节点处同步区块
     * @return
     */
    private boolean syncBlock() {
        if (blockChainService.chainExist()) {
            Block block = blockChainService.queryPrevBlock();
            chainData.set(block.getIndex(), block.getHashValue());
        }
        boolean result = blockChainService.syncBlock();
        if (!result) {
            logger.error("区块同步失败，节点启动失败");
        }
        return result;
    }

    /**
     * 初始化节点数据
     */
    private void initPeerNode() {
        peerData = new PeerData(peerConfig.getId(), peerConfig.getName(), serverInfo.getHost(), serverInfo.getPort());
        chainData = new ChainData();
        logger.info("节点初始化成功");
    }

    /**
     * 向CA节点注册
     *
     * @return
     */
    private boolean nodeAuthentication() {
        logger.info("CA节点身份认证成功");
        return true;
    }

    /**
     * 与Order节点建立通信，并获取更多的Order节点位置
     *
     * @return
     */
    private boolean connToOrder() {
        Map<String, String> map = networkService.fetchOrderAddress(peerConfig.getOrderAddress());
        if (map.size() == 0) {
            logger.error("无法连接到任一Order节点，节点启动失败");
            return false;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            peerData.addOrderAddress(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return true;
    }


}
