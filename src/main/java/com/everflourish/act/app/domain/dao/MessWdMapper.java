package com.everflourish.act.app.domain.dao;

import org.apache.ibatis.annotations.Mapper;

import com.everflourish.act.app.domain.bean.MessWd;

@Mapper
public interface MessWdMapper {
    int deleteByPrimaryKey(String id);

    int insert(MessWd record);

    int insertSelective(MessWd record);

    MessWd selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MessWd record);

    int updateByPrimaryKey(MessWd record);
}