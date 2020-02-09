package com.cdqd.mapper;

import com.cdqd.model.BlockContentModel;
import com.cdqd.model.BlockContentModelExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BlockContentModelMapper {
    int countByExample(BlockContentModelExample example);

    int deleteByExample(BlockContentModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlockContentModel record);

    int insertSelective(BlockContentModel record);

    List<BlockContentModel> selectByExample(BlockContentModelExample example);

    BlockContentModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlockContentModel record, @Param("example") BlockContentModelExample example);

    int updateByExample(@Param("record") BlockContentModel record, @Param("example") BlockContentModelExample example);

    int updateByPrimaryKeySelective(BlockContentModel record);

    int updateByPrimaryKey(BlockContentModel record);
}