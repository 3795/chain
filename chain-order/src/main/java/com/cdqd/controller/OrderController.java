package com.cdqd.controller;

import com.cdqd.component.KafkaProducer;
import com.cdqd.core.Block;
import com.cdqd.data.MetaData;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.service.BlockChainService;
import com.cdqd.vo.ServerResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 添加内容，并生成区块
     *
     * @param contents
     * @return
     */
    @PostMapping("/add-block")
    public ServerResponseVO addBlock(@RequestParam("contents") List<String> contents) {
        Block block = Block.generateBlock(chainData, contents);
        blockChainService.insertBlock(block);
        return ServerResponseVO.success("添加成功");
    }

    /**
     * Peer节点向Order节点提交内容
     *
     * @param data
     * @return
     */
    @PostMapping("/data")
    public ServerResponseVO kafka(@RequestParam("data") String data) {
        kafkaProducer.send(data);
        return ServerResponseVO.success("添加成功");
    }
}
