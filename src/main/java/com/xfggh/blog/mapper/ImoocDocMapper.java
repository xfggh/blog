package com.xfggh.blog.mapper;

import com.xfggh.blog.entity.ImoocDoc;
import com.xfggh.blog.entity.ImoocDocExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ImoocDocMapper {
    long countByExample(ImoocDocExample example);

    int deleteByExample(ImoocDocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ImoocDoc record);

    int insertSelective(ImoocDoc record);

    List<ImoocDoc> selectByExample(ImoocDocExample example);

    ImoocDoc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ImoocDoc record, @Param("example") ImoocDocExample example);

    int updateByExample(@Param("record") ImoocDoc record, @Param("example") ImoocDocExample example);

    int updateByPrimaryKeySelective(ImoocDoc record);

    int updateByPrimaryKey(ImoocDoc record);
}