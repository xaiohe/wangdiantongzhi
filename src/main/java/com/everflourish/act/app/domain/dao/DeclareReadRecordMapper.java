package com.everflourish.act.app.domain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.everflourish.act.app.domain.bean.DeclareReadRecord;

@Mapper
public interface DeclareReadRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(DeclareReadRecord record);

    int insertSelective(DeclareReadRecord record);

    DeclareReadRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DeclareReadRecord record);

    int updateByPrimaryKeyWithBLOBs(DeclareReadRecord record);

    int updateByPrimaryKey(DeclareReadRecord record);
}