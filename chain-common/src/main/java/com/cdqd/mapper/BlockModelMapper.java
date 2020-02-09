package com.cdqd.mapper;

import com.cdqd.model.BlockModel;
import com.cdqd.model.BlockModelExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BlockModelMapper {
    int countByExample(BlockModelExample example);

    int deleteByExample(BlockModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlockModel record);

    int insertSelective(BlockModel record);

    List<BlockModel> selectByExample(BlockModelExample example);

    BlockModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlockModel record, @Param("example") BlockModelExample example);

    int updateByExample(@Param("record") BlockModel record, @Param("example") BlockModelExample example);

    int updateByPrimaryKeySelective(BlockModel record);

    int updateByPrimaryKey(BlockModel record);
}