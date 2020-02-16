package com.cdqd.controller;

import com.cdqd.client.CaClient;
import com.cdqd.component.KafkaProducer;
import com.cdqd.core.Block;
import com.cdqd.data.MetaData;
import com.cdqd.dto.BlockDTO;
import com.cdqd.enums.NodeTypeEnum;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.service.BlockChainService;
import com.cdqd.util.BlockUtil;
import com.cdqd.util.EncryptUtil;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.KeyPair;
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
     * 临时接口，方便测试功能，后续删除
     * 添加内容，并生成区块
     *
     * @param contents
     * @return
     */
    @PostMapping("/add-block")
    public ServerResponseVO addBlock(@RequestParam("contents") List<String> contents) {
        Block block = Block.generateBlock(chainData, contents);
        blockChainService.insertBlock(block);
        return ServerResponseVO.success("添加区块成功");
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
            System.out.println("公钥：" + publicKey);
            System.out.println("数据" + data);
            System.out.println("签名" + sign);
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

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = EncryptUtil.getKeyPair();
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7BQQKtgtbyumRuR9bwro+Lrf5Yo3OTSFZjGnNUd3xI9PmT4fbfx+JG5ZJmbUHA/tE6IDXTWz8efjQZWlebPsMJC4Br0NB4VhpXIAFOCXagwRQSMP7cXDRzfe6YMD2bdUCJi9Mp6vVN6UGoBV5+3b+2aNsZ15pXAKPRrDTEdywyQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALsFBAq2C1vK6ZG5H1vCuj4ut/lijc5NIVmMac1R3fEj0+ZPh9t/H4kblkmZtQcD+0TogNdNbPx5+NBlaV5s+wwkLgGvQ0HhWGlcgAU4JdqDBFBIw/txcNHN97pgwPZt1QImL0ynq9U3pQagFXn7dv7Zo2xnXmlcAo9GsNMR3LDJAgMBAAECgYBCanOXAjNic9si3yVdxvexIZJARFmrzARt8smYGtzAyEJ2ZgQPVUPzwxJKLQX26dkQDanDzEFKIF7WU01qNTWp6x8JDa4rW/eS6ge9M8L7L1h+YK35M2DNQviY1xEdXtF4d2AOahGRwiScTwqW/7um5h3zR/WTDhO2s1wZyA1DSQJBAPzEuRiw67m0CxMGcpac7T9VoNzTHcCgcGoUvHzzJN6FVQO85D6d6f4apcHXFVg2Rqw5SPsveA1CPRjcJQfwE5cCQQC9aRsK9Cz+XK/AvsR2uSNEkNugVpFXCXAyp6m/h0kfvv+CwHvgwABCP31RAGZqgsC8yx2dq9cy6hDw11+F/GqfAkA1BEYWibVHpB3WhbmvIYcZi6pL5vQMnRo3BdZrFsya57hnKk1tXM3hgFFYEPbnI6s7IGDQXqp9jXEnL7WMrqxNAkEAvAAMGHY+BobG56Ax7slaSR8i72WdQu+aTpz+Lp3AJeN1Rzl4e573YsoTv4ePIB8B4SIWFj7PDbkn8XVEWgAtbwJBAL1ZHkaeg0+pm5R91EemViU3w2ZT7ohJW9MgSQAVgzyWeJmM4OeXtVP9JqSzSFObCF9KJ836Otmt0sLX5i4KkfE=";
        String data = "block1";
        String s = "fCo0Q9+fxD2aHVkbk0FNcWaq64U2R0/vZ0PCIX3GN4ZDvXIh8FcSgt4W3ZhFzZExVQY22ndm8K78ZN0SbwmFH9wnv6EoZ53c0IRk2PMnxwfRgn09PGTh+JFxITCz0ZSY5EOqrWH7VpC8TxuREPV0xH8z9qxiKk6R+AJrAkwC0ms=";
        String sign = EncryptUtil.sign(privateKey, data);
        System.out.println(EncryptUtil.verify(publicKey, data, s));
        System.out.println(s);
        System.out.println(sign);
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
            } catch (Exception e) {
                logger.error("Ack区块失败，Message: {}", e.getMessage());
            }
            return ServerResponseVO.success("OK");
        }
        return ServerResponseVO.success("OK");
    }
}
