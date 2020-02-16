package com.cdqd.config;

import com.cdqd.client.CaClient;
import com.cdqd.core.Block;
import com.cdqd.core.ChainData;
import com.cdqd.data.PeerData;
import com.cdqd.enums.NodeTypeEnum;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.service.BlockChainService;
import com.cdqd.service.NetworkService;
import com.cdqd.util.EncryptUtil;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.security.KeyPair;
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

    @Resource
    private CaClient caClient;

    @PostConstruct
    public void init() {

        initPeerNode();

        if(!nodeAuthentication()) {
            return;
        }

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
     *
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
        try {
            KeyPair keyPair = EncryptUtil.getKeyPair();
            String publicKey = EncryptUtil.getPublicKey(keyPair);
            String privateKey = EncryptUtil.getPrivateKey(keyPair);
            peerData.setPrivateKey(privateKey);
            ServerResponseVO response = caClient.auth(peerData.getId(), peerData.getName(), publicKey,
                    peerData.getIp() + ":" + peerData.getPort(), NodeTypeEnum.PEER.getType());
            if (response.getCode() == ResponseCodeEnum.SUCCESS.getCode()) {
                logger.info("CA节点认证成功");
                return true;
            } else {
                logger.error("CA节点认证失败，节点启动失败! Message: {}", response.getMessage());
                return false;
            }
        } catch (Exception e) {
            logger.error("生成秘钥对失败，Message：{}", e.getMessage());
            return false;
        }
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
