package com.everflourish.act.app.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.ReadRecord;
import com.everflourish.act.app.domain.bean.Users;
import com.everflourish.act.db.DbManager;

@Service
public class ReadRecordService {

	/**
	 * 添加阅读记录
	 */
	public void addReadRecord(Users user,String messId) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replace("-", "");
		String sql = "INSERT INTO read_record (id,user_id,mess_id,create_time,content,image_url,modify_time,wd_no,phone) VALUES(?,?,?,?,?,?,?,?,?)";
		DbManager.wxApp.insert(sql, new Object[] {id,user.getId(),messId,new Timestamp(System.currentTimeMillis()),
				null,null,new Timestamp(System.currentTimeMillis()),user.getWdNo(),user.getPhone()});
	}
	/**
	 * 更新阅读记录
	 * 
	 */
	public void updateReadRecord(ReadRecord rr) {
		String sql = "UPDATE read_record SET content = ?,image_url = ?,modify_time = ?,user_id = ? WHERE wd_no = ? AND mess_id = ?";
		DbManager.wxApp.update(sql, new Object[] { rr.getContent(),rr.getImageUrl(),
				new Timestamp(System.currentTimeMillis()),rr.getUserId(),rr.getWdNo(),rr.getMessId()});
	}
	/**
	 * 获取阅读记录
	 */
	public ReadRecord getReadRecordListByMessId(String messId,String wdNo){
		String sql = "SELECT rr.user_id,rr.wd_no,rr.modify_time,rr.image_url,rr.content FROM read_record rr RIGHT JOIN "
				+ "( SELECT MAX(modify_time) mt,wd_no FROM read_record WHERE mess_id = ? and wd_no = ? GROUP BY wd_no) s "
				+ "ON s.wd_no = rr.wd_no AND s.mt = rr.modify_time ";
		ReadRecord rr = DbManager.wxApp.queryForBean(sql, ReadRecord.class, new Object[]{ messId,wdNo });
		return rr;
	}
}
