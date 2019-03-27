package com.everflourish.act.app.service;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.DeclareReadRecord;
import com.everflourish.act.app.domain.bean.Users;
import com.everflourish.act.db.DbManager;

@Service
public class UsersService {

	/**
	 * 根据微信ID查询用户信息
	 */
	public Users getUsersByFromUserName(String fromUserName) {
		String sql = "select * FROM users WHERE from_user_name = ?";
		Users user = DbManager.wxApp.queryForBean(sql, Users.class, new Object[] { fromUserName });
		return user;
	}
	/**
	 * 添加用户
	 */
	public void addUsers(Users user) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replace("-", "");
		String sql = "INSERT INTO users (id,from_user_name,nick_name,wd_no,phone,create_time,modify_time) VALUES(?,?,?,?,?,?,?)";
		DbManager.wxApp.insert(sql, new Object[] { id,user.getFromUserName(),user.getNickName(),
				user.getWdNo(),user.getPhone(),new Timestamp(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()) });
	}
	/**
	 * 更新用户信息
	 */
	public void updateUsers(Users user) {
		String sql = "UPDATE users SET wd_no = ?,phone = ?,modify_time = ?,nick_name = ?,head_image = ? WHERE id = ?";
		DbManager.wxApp.update(sql, new Object[] {user.getWdNo(),user.getPhone(),
				new Timestamp(System.currentTimeMillis()),user.getNickName(),user.getHeadImage(),user.getId()});
	}
	
	public void get() {
		String sql = "SELECT * FROM declare_read_record";
		DeclareReadRecord s = DbManager.wxApp.queryForBean(sql, DeclareReadRecord.class, new Object[] {});
		System.out.println(s.getContent());
	}
}
