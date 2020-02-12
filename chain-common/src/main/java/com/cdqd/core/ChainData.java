package com.cdqd.core;

/**
 * Description: 本地存储区块链的信息
 * Created At 2020/2/9
 */
public class ChainData {

    private volatile int index;      // 存储的区块高度

    private volatile String prevHashValue;       // 上一个区块的Hash值

    private volatile Block tmpBlock;        // 接收广播后，写入系统前，暂存的区块

    public ChainData() {
        this.index = 0;
        this.prevHashValue = "0";
        this.tmpBlock = null;
    }

    public synchronized void set(int index, String prevHashValue) {
        this.index = index;
        this.prevHashValue = prevHashValue;
    }

    public synchronized void update(String prevHashValue) {
        this.addIndex();
        this.prevHashValue = prevHashValue;
    }

    public synchronized int getIndex() {
        return index;
    }

    public synchronized String getPrevHashValue() {
        return prevHashValue;
    }

    private synchronized void addIndex() {
        index = index + 1;
    }

    public Block getTmpBlock() {
        return tmpBlock;
    }

    public void setTmpBlock(Block tmpBlock) {
        this.tmpBlock = tmpBlock;
    }
}
