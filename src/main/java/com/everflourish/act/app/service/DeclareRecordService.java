package com.everflourish.act.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.DeclareReadRecord;
import com.everflourish.act.app.domain.bean.DeclareRecord;
import com.everflourish.act.app.domain.dao.DeclareReadRecordMapper;
import com.everflourish.act.app.domain.dao.DeclareRecordMapper;
import com.everflourish.act.db.DbManager;

@Service
public class DeclareRecordService {
	@Autowired
	DeclareRecordMapper declareRecordMapper;
	@Autowired
	DeclareReadRecordMapper declareReadRecordMapper;
	/**
	 * 根据网点编号分页查询申报信息
	 * @param wdNo
	 * @param page
	 * @return
	 */
	public List<Map<String, Object>> getDeclareRecord(String wdNo,Integer page) {
		String sql = "SELECT * FROM(SELECT * FROM declare_record WHERE create_by = ?) dr LEFT JOIN " + 
				"(SELECT drr.us_read_status,drr.ce_read_status,drr.create_time ddr_create_time,drr.dec_id,drr.content ddr_content FROM  " + 
				"declare_read_record drr RIGHT JOIN (SELECT dec_id,MAX(create_time) ctime FROM declare_read_record GROUP BY dec_id ) s ON " + 
				"drr.create_time = s.ctime AND drr.dec_id = s.dec_id " + 
				" WHERE drr.wd_no = ?) ddr ON dr.id = ddr.dec_id ORDER BY dr.create_time DESC LIMIT ?,10";
		List<Map<String, Object>> drList = DbManager.wxApp.queryForList(sql, new Object[] { wdNo,wdNo,(page - 1) * 10});
		return drList;
	}
	/**
	 * 查询总数
	 * @param wdNo
	 * @return
	 */
	public Integer getTotalCount(String wdNo) {
		String sql = "SELECT count(1) FROM declare_record WHERE create_by = ?";
		List<Object[]> obs = DbManager.wxApp.queryForListObjects(sql, new Object[] {wdNo});
		return Integer.parseInt(obs.get(0)[0].toString());
	}
	/**
	 * 更新申报记录状态
	 * @param decId
	 */
	public void uodateDecStatus(String decId,String wdNo) {
		String sql = "UPDATE declare_record SET `status` = 1,modify_time = ? WHERE id = ? and create_by = ?";
		DbManager.wxApp.update(sql, new Object[]{new Timestamp(System.currentTimeMillis()),decId,wdNo});
	}
	/**
	 * 添加申报记录
	 */
	@Transactional
	public void addDec(DeclareRecord record,DeclareReadRecord drr) {
		declareRecordMapper.insert(record);
		declareReadRecordMapper.insert(drr);
	}
	/**
	 * 管理员加载申报消息列表
	 * @param wdNo
	 * @param title
	 * @param mcId
	 * @return
	 */
	public List<Map<String, Object>> adminGetDeclareRecord(String key,String mcId,Integer page,Integer pageSize){
		String sql = "SELECT dr.*,tw.mc_id FROM declare_record dr LEFT JOIN t_wd tw ON dr.create_by = tw.store_code where";
		if(key != null && key.length() > 0) {
			key = "%" + key + "%";
			sql = sql + " dr.create_by like ? or dr.content like ?";
		}else {
			key = "1";
			sql = sql + " 1 = ? and 1 = ?";
		}
		
		if(mcId != null && mcId.length() > 0) {
			sql = sql + " and tw.mc_id = ? ";
		}else {
			mcId = "1";
			sql = sql + " and 1 = ? ";
		}
		sql = sql + " limit ?,? ";
		List<Map<String, Object>> decList = DbManager.wxApp.queryForList(sql, new Object[] { key,key,mcId,(page - 1)*10,10 });
		return decList;
	}
	/**
	 * 获取总条数
	 * @param key
	 * @param mcId
	 * @return
	 */
	public Integer getCount(String key,String mcId) {
		String sql = "SELECT count(1) FROM declare_record dr LEFT JOIN t_wd tw ON dr.create_by = tw.store_code where";
		if(key != null && key.length() > 0) {
			key = "%" + key + "%";
			sql = sql + " dr.create_by like ? or dr.title like ?";
		}else {
			key = "1";
			sql = sql + " 1 = ? and 1 = ?";
		}
		
		if(mcId != null && mcId.length() > 0) {
			sql = sql + " and tw.mc_id = ? ";
		}else {
			mcId = "1";
			sql = sql + " and 1 = ? ";
		}
		List<Object[]> obs = DbManager.wxApp.queryForListObjects(sql, new Object[] { key,key,mcId });
		return Integer.parseInt(obs.get(0)[0].toString());
	}
}
