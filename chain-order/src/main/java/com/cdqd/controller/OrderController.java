package com.cdqd.controller;

import com.cdqd.client.CaClient;
import com.cdqd.component.KafkaProducer;
import com.cdqd.core.Block;
import com.cdqd.data.MetaData;
import com.cdqd.dto.BlockDTO;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.service.BlockChainService;
import com.cdqd.util.BlockUtil;
import com.cdqd.util.EncryptUtil;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.cdqd.data.MetaData.chainData;
import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 对外提供服务
 * Created At 2020/2/7
 */
@RestController
@RequestMapping
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private BlockChainService blockChainService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Resource
    private CaClient caClient;

    /**
     * 拉取其他Order节点的网络地址
     *
     * @return
     */
    @GetMapping("/fetch-address")
    public ServerResponseVO orderAddress() {
        Map<Integer, String> orderAddressMap = orderData.getOrderAddressMap();
        return ServerResponseVO.success(orderAddressMap);
    }

    /**
     * 接受其他Order节点发送的注册请求
     *
     * @param orderId
     * @param orderAddress
     * @return
     */
    @PostMapping("/register")
    public ServerResponseVO register(@RequestParam("orderId") Integer orderId,
                                     @RequestParam("orderAddress") String orderAddress) {
        orderData.addOrderAddress(orderId, orderAddress);
        logger.info("新节点加入，OrderId:{}, Address: {}", orderId, orderAddress);
        return ServerResponseVO.success(ResponseCodeEnum.SUCCESS);
    }

    /**
     * 查询本节点保存的区块高度
     *
     * @return
     */
    @GetMapping("/block-index")
    public ServerResponseVO blockIndex() {
        return ServerResponseVO.success(MetaData.chainData.getIndex());
    }

    /**
     * 拉取该节点保存的区块
     *
     * @param index 起始区块索引
     * @param size  每次拉取的区块个数
     * @return
     */
    @GetMapping("/pull-block")
    public ServerResponseVO pullBlock(@RequestParam("index") Integer index,
                                      @RequestParam(value = "size") Integer size) {
        List<Block> blockList = blockChainService.pullBlock(index, size);
        return ServerResponseVO.success(blockList);
    }

    /**
     * Peer节点向Order节点提交内容
     *
     * @param data
     * @return
     */
    @PostMapping("/commit-data")
    public ServerResponseVO kafka(@RequestParam("peerId") Integer peerId,
                                  @RequestParam("data") String data,
                                  @RequestParam("sign") String sign) {
        // 查询公钥
        ServerResponseVO response = caClient.getPublicKey(peerId);
        if (response.getCode() != ResponseCodeEnum.SUCCESS.getCode()) {
            return ServerResponseVO.error(response.getMessage());
        }
        try {
            // 数据验签
            String publicKey = response.getData().toString();
            boolean result = EncryptUtil.verify(publicKey, data, sign);
            if (!result) {
                return ServerResponseVO.error("数据非法");
            }
            kafkaProducer.send(data);
            return ServerResponseVO.success("提交内容成功");
        } catch (Exception e) {
            logger.error("验签过程出现错误，Message：{}", e.getMessage());
            return ServerResponseVO.error("验签错误");
        }
    }

    /**
     * 接收网络中广播出来的区块
     *
     * @param blockDTO
     * @return
     */
    @PostMapping("/broad-block")
    public ServerResponseVO broadBlock(@RequestBody BlockDTO blockDTO) {
        Block block = Block.blockDTO2Block(blockDTO);
        chainData.setTmpBlock(block);
        logger.info("收到一个区块, Index: {}", block.getIndex());
        if (BlockUtil.verify(chainData.getIndex() + 1, chainData.getPrevHashValue(), block)) {
            return ServerResponseVO.success("接收区块成功");
        } else {
            String address = orderData.getHighestOrder();
            logger.info("将从节点 {} 处同步区块", address);
            // 这个地方，同步区块时是可能会抛出异常的，但是在Controller层，就不再处理了
            // 此处不再处理，对整个流程没有什么影响，下一次收到Ack消息时，再从其他节点同步区块
            blockChainService.syncBlock(address);
            return ServerResponseVO.error("忽略本次收到的区块，BlockIndex: " + block.getIndex());
        }
    }

    /**
     * 接收Leader发出的Ack信息
     *
     * @return
     */
    @PostMapping("/ack-block")
    public ServerResponseVO ackBlock() {
        Block block = chainData.getTmpBlock();
        if (block != null) {
            try {
                /**
                 * 可能情况：
                 * 1. LeaderA广播了一个区块，Order节点收到了区块，但并未写入，放入tmpBlock中
                 * 2. LeaderA广播了一个ack消息，因为网络阻塞或其他原因，Order节点并未收到该ack消息，未将tmpBlock写入系统
                 * 3. LeaderA广播了一个新的区块，Order节点收到了该区块，并覆盖了放在tmpBlock中的上一个未写入区块
                 * 4. Order节点收到了ack消息，写入系统时，区块校验失败，此时应以Leader节点为准，抛弃广播收到的区块，直接同步区块
                 */
                blockChainService.insertBlock(block);
                chainData.setTmpBlock(null);        // 清空临时保存的区块
            } catch (Exception e) {
                logger.error("Ack区块失败，Message: {}", e.getMessage());
            }
            return ServerResponseVO.success("OK");
        }
        return ServerResponseVO.success("OK");
    }
}
