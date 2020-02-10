package com.cdqd.util;

import com.alibaba.fastjson.JSON;
import com.cdqd.core.Block;
import com.cdqd.core.BlockContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 区块链工具
 * Created At 2020/2/10
 */
public class BlockUtil {

    private final static Logger logger = LoggerFactory.getLogger(BlockUtil.class);

    /**
     * 计算区块的Hash值
     *
     * @param block
     * @return
     */
    public static String sumHashValue(Block block) {
        StringBuilder buffer = new StringBuilder();
        for (BlockContent content : block.getContentList()) {
            buffer.append(JSON.toJSONString(content)).append(",");
        }
        String content = String.format("Index:%d, ContentList:%s, PrevHashValue:%s, CreateTime:%s",
                block.getIndex(), buffer.toString(), block.getPrevHashValue(), block.getCreateTime());
        return EncryptUtil.md5(content);
    }

    /**
     * 验证区块的合法性
     *
     * @param exceptIndex       期望的区块索引
     * @param exceptPrevHashValue       期望的区块前置Hash
     * @param block     待校验的区块
     * @return
     */
    public static boolean verify(int exceptIndex, String exceptPrevHashValue, Block block) {
        if (exceptIndex == 1) {
            return true;
        }
        if (exceptIndex != block.getIndex()) {
            logger.error("区块校验失败，索引值不符合预期，Except: {}, Actual: {}", exceptIndex, block.getIndex());
            return false;
        }

        if (!exceptPrevHashValue.equals(block.getPrevHashValue())) {
            logger.error("区块校验失败，前置Hash值不符合预期，Except: {}, Actual: {}", exceptPrevHashValue, block.getPrevHashValue());
            return false;
        }

        String actualHashValue = sumHashValue(block);
        if (!actualHashValue.equals(block.getHashValue())) {
            logger.error("区块校验失败，Hash值不符合预期，Except: {}, Actual: {}", actualHashValue, block.getHashValue());
            return false;
        }
        return true;
    }
}
