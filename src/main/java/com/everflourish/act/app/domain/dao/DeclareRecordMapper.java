package com.everflourish.act.app.domain.dao;

import org.apache.ibatis.annotations.Mapper;

import com.everflourish.act.app.domain.bean.DeclareRecord;

@Mapper
public interface DeclareRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DeclareRecord record);

    int insertSelective(DeclareRecord record);

    DeclareRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DeclareRecord record);

    int updateByPrimaryKeyWithBLOBs(DeclareRecord record);

    int updateByPrimaryKey(DeclareRecord record);
}