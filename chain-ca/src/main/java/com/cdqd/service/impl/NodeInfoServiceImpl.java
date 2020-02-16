package com.cdqd.service.impl;

import com.cdqd.enums.NodeTypeEnum;
import com.cdqd.enums.ResponseCodeEnum;
import com.cdqd.enums.StatusEnum;
import com.cdqd.exception.ServerException;
import com.cdqd.mapper.NodeInfoModelMapper;
import com.cdqd.model.NodeInfoModel;
import com.cdqd.model.NodeInfoModelExample;
import com.cdqd.service.NodeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NodeInfoServiceImpl implements NodeInfoService {

    private static final Logger logger = LoggerFactory.getLogger(NodeInfoServiceImpl.class);

    @Autowired
    private NodeInfoModelMapper nodeInfoMapper;


    @Override
    public void auth(Integer nodeId, String nodeName, String publicKey, String address, Integer nodeType) {
        NodeInfoModel model = queryById(nodeId);
        if (model == null) {
            model = new NodeInfoModel(nodeId, nodeName, publicKey, address, nodeType);
            insert(model);
            return;
        }
        // Id, name, type 三者一致才能证明为同一节点
        if (model.getName().equals(nodeName) &&
                model.getType().equals(nodeType)) {
            // 对数据进行更新
            model.setAddress(address);
            model.setStatus(StatusEnum.ON.getCode());
            model.setRegisterTime(new Date());
            model.setPublicKey(publicKey);
            update(model);
        } else {
            throw new ServerException(ResponseCodeEnum.NODE_ID_REPEAT);
        }
    }

    private void update(NodeInfoModel model) {
        int result = nodeInfoMapper.updateByPrimaryKeySelective(model);
        if (result != 1) {
            throw new ServerException(ResponseCodeEnum.UPDATE_FAILED);
        }
    }

    private void insert(NodeInfoModel model) {
        int result = nodeInfoMapper.insertSelective(model);
        if (result != 1) {
            throw new ServerException(ResponseCodeEnum.INSERT_FAILED);
        }
    }

    @Override
    public String queryPublicKey(Integer nodeId) {
        NodeInfoModel model = queryById(nodeId);
        if (model == null || !model.getType().equals(NodeTypeEnum.PEER.getType())) {
            throw new ServerException(ResponseCodeEnum.PUBLIC_KEY_NOT_EXIST);
        }
        return model.getPublicKey();
    }

    /**
     * 根据节点ID查找节点信息
     *
     * @param nodeId
     * @return
     */
    private NodeInfoModel queryById(Integer nodeId) {
        NodeInfoModelExample example = new NodeInfoModelExample();
        NodeInfoModelExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        List<NodeInfoModel> list = nodeInfoMapper.selectByExample(example);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
}
