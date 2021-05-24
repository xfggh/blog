package com.xfggh.blog.mapper;

import com.xfggh.blog.entity.ImoocContent;
import com.xfggh.blog.entity.ImoocContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ImoocContentMapper {
    long countByExample(ImoocContentExample example);

    int deleteByExample(ImoocContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ImoocContent record);

    int insertSelective(ImoocContent record);

    List<ImoocContent> selectByExampleWithBLOBs(ImoocContentExample example);

    List<ImoocContent> selectByExample(ImoocContentExample example);

    ImoocContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ImoocContent record, @Param("example") ImoocContentExample example);

    int updateByExampleWithBLOBs(@Param("record") ImoocContent record, @Param("example") ImoocContentExample example);

    int updateByExample(@Param("record") ImoocContent record, @Param("example") ImoocContentExample example);

    int updateByPrimaryKeySelective(ImoocContent record);

    int updateByPrimaryKeyWithBLOBs(ImoocContent record);
}