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
        // 3. 在2步的基础上，查询本地的区块链，与Order节点进行同步

        // 4. 节点启动成功

        // 5. 每隔一定时间就跟Order节点同步区块
        for (String string : peerConfig.getOrderAddress()) {
            System.out.println(string);
        }
    }

    private boolean syncBlock() {
        try {
            if (blockChainService.chainExist()) {
                Block block = blockChainService.queryPrevBlock();
                chainData.set(block.getIndex(), block.getHashValue());
            }
            blockChainService.syncBlock();
        } catch (Exception e) {
            logger.warn("区块同步失败");
            return false;
        }
        return true;
    }

    /**
     * 初始化节点数据
     */
    private void initPeerNode() {
        peerData = new PeerData();
        chainData = new ChainData();
        logger.info("节点初始化成功");
    }

    /**
     * 向CA节点注册
     * @return
     */
    private boolean nodeAuthentication() {
        logger.info("CA节点身份认证成功");
        return true;
    }

    /**
     * 与Order节点建立通信，并获取更多的Order节点位置
     * @return
     */
    private boolean connToOrder() {
        Map<String, String> map = networkService.fetchOrderAddress(peerConfig.getOrderAddress());
        if (map.size() == 0) {
            logger.error("无法连接到Order任一节点，节点启动失败");
            return false;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            peerData.addOrderAddress(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return true;
    }


}
