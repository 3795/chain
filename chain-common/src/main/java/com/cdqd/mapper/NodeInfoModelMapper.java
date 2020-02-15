package com.cdqd.mapper;

import com.cdqd.model.NodeInfoModel;
import com.cdqd.model.NodeInfoModelExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NodeInfoModelMapper {
    int countByExample(NodeInfoModelExample example);

    int deleteByExample(NodeInfoModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(NodeInfoModel record);

    int insertSelective(NodeInfoModel record);

    List<NodeInfoModel> selectByExample(NodeInfoModelExample example);

    NodeInfoModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NodeInfoModel record, @Param("example") NodeInfoModelExample example);

    int updateByExample(@Param("record") NodeInfoModel record, @Param("example") NodeInfoModelExample example);

    int updateByPrimaryKeySelective(NodeInfoModel record);

    int updateByPrimaryKey(NodeInfoModel record);
}