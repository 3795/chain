package com.cdqd.cron;

import com.cdqd.service.BlockChainService;
import com.cdqd.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cdqd.data.MetaData.peerData;

/**
 * Description: 定时任务
 * Created At 2020/2/14
 */
@Component
public class CronTask {

    private static final Logger logger = LoggerFactory.getLogger(CronTask.class);

    @Autowired
    private NetworkService networkService;

    @Autowired
    private BlockChainService blockChainService;

    /**
     * 从Order节点同步区块
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void syncBlock() {
        blockChainService.syncBlock();
    }

    /**
     * 检查存储的Order节点的活性
     * 每2分钟执行一次
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void checkActivity() {
        Map<Integer, Integer> map = peerData.getDoubtOrderMap();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            String address = peerData.getOrderAddressMap().get(entry.getKey());
            int blockIndex = networkService.heartConn(address);
            if (blockIndex == -1) {
                int count = entry.getValue() + 1;
                if (count > 2) {
                    // 重试了3次仍失败，则判定该节点死亡，移除该节点信息
                    peerData.removeDiedOrder(entry.getKey());
                } else {
                    peerData.getDoubtOrderMap().put(entry.getKey(), count);
                }
            } else {
                // 将该节点从可疑节点中移除，恢复正常
                peerData.getDoubtOrderMap().remove(entry.getKey());
            }
        }
    }

    /**
     * 增量更新保存的Order节点信息
     * 每10分钟执行一次
     */
    @Scheduled(cron = "0 0/10 * * * *")
    public void updateOrderAddress() {
        List<String> addressList = new ArrayList<>();
        Map<Integer, String> map = peerData.getAvailableOrder();
        if (map.size() == 0) {
            logger.error("所有Order节点已下线，无可用Order节点");
            return;
        }
        int size = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (size > 1) {
                break;
            }
            addressList.add(entry.getValue());
            size++;
        }
        Map<String, String> result = networkService.fetchOrderAddress(addressList);
        if (result.size() == 0) {
            logger.error("无法连接到任一Order节点，本次更新结束");
            return;
        }
        // 进行增量更新
        for (Map.Entry<String, String> entry : result.entrySet()) {
            peerData.addOrderAddress(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        logger.info("更新OrderAddressMap成功");
    }

}
