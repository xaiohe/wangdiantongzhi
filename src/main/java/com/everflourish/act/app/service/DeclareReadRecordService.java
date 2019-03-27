package com.everflourish.act.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.DeclareReadRecord;
import com.everflourish.act.app.domain.dao.DeclareReadRecordMapper;
import com.everflourish.act.db.DbManager;

@Service
public class DeclareReadRecordService {
	@Autowired
	DeclareReadRecordMapper declareReadRecordMapper;
	/**
	 * 查询聊天记录
	 * @param decId
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getDeclareReadRecord(String decId,Integer page) {
		String sql = "SELECT u.head_image headImage,drr.id id,drr.user_id userId,drr.dec_id decId,"
				+ "drr.create_time createTime,drr.content content,drr.modify_time modifyTime,"
				+ "drr.wd_no wdNo,drr.phone phone,drr.create_by createBy,drr.us_read_status usReadStatus,"
				+ "drr.ce_read_status ceReadStatus FROM (SELECT * FROM declare_read_record WHERE dec_id = ?) "
				+ "drr LEFT JOIN users u ON drr.user_id = u.id ORDER BY drr.create_time DESC LIMIT ?,10";
		List<Map<String, Object>> drrList = DbManager.wxApp.queryForList(sql, new Object[] { decId,(page - 1) * 10});
		return drrList;
	}
	public Integer getTotalCount(String decId) {
		String sql = "SELECT count(1) FROM declare_read_record WHERE dec_id = ?";
		List<Object[]> obs = DbManager.wxApp.queryForListObjects(sql, new Object[] {decId});
		return Integer.parseInt(obs.get(0)[0].toString());
	}
	/**
	 * 更新用户阅读状态
	 * @param decId
	 */
	public void updateDeclareReadRecordUReadStatus(String decId,String wdNo) {
		String sql = "UPDATE declare_read_record SET us_read_status = 1,modify_time = ? WHERE dec_id = ? and wd_no = ?";
		DbManager.wxApp.update(sql, new Object[] {new Timestamp(System.currentTimeMillis()),decId,wdNo});
	}
	/**
	 * 添加聊天记录
	 * @param record
	 */
	public void addDeclareReadRecord(DeclareReadRecord record) {
		declareReadRecordMapper.insert(record);
	}
}
