package com.cdqd.cron;

import com.cdqd.service.BlockChainService;
import com.cdqd.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.cdqd.data.MetaData.chainData;
import static com.cdqd.data.MetaData.orderData;

/**
 * Description: 系统定时任务
 * 1. 与其他节点之间的心跳连接
 * 2. 对于失联节点的确认与移出
 * Created At 2020/2/12
 */
@Component
public class TimedTask {

    private static final Logger logger = LoggerFactory.getLogger(TimedTask.class);

    @Autowired
    private NetworkService networkService;

    @Autowired
    private BlockChainService blockChainService;

    /**
     * 与其他节点的心跳连
     * 每两分钟执行一次
     */
    @Scheduled(cron = "0 0/2 * * * *")
    public void heartConn() {
        for (Map.Entry<Integer, String> entry : orderData.getAvailableOrder().entrySet()) {
            int blockIndex = networkService.heartConn(entry.getValue());
            if (blockIndex == -1) {     // 心跳没有回应
                orderData.addDoubtOrder(entry.getKey());
            } else {
                orderData.putOrderBlockIndex(entry.getKey(), blockIndex);
                if ((blockIndex - chainData.getIndex()) > 2) {
                    // 区块高度差大于2，则说明广播过程或接收过程出现了问题
                    // 直接以该节点为基准，进行区块同步
                    logger.warn("本地区块出现滞后性，开始以节点 {} 为准进行同步，本地高度: {}, 目标高度: {}");
                    blockChainService.syncBlock(entry.getValue(), chainData.getIndex() + 1, blockIndex);
                }
            }
        }
    }

    /**
     * 检查Order节点的活性
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkActivity() {
        Map<Integer, Integer> map = orderData.getDoubtOrderMap();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            String address = orderData.getOrderAddressMap().get(entry.getKey());
            int blockIndex = networkService.heartConn(address);
            if (blockIndex == -1) {
                int count = entry.getValue() + 1;
                if (count > 2) {
                    // 重试了3次仍失败，则判定该节点死亡，移除该节点信息
                    orderData.getOrderAddressMap().remove(entry.getKey());
                    orderData.getDoubtOrderMap().remove(entry.getKey());
                } else {
                    orderData.getDoubtOrderMap().put(entry.getKey(), count);
                }
            } else {
                // 将该节点从可疑节点中移除，恢复正常
                orderData.getDoubtOrderMap().remove(entry.getKey());
            }
        }
    }
}
