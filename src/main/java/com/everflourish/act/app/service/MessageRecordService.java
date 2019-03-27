package com.everflourish.act.app.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.everflourish.act.app.controller.WxAdminController;
import com.everflourish.act.app.domain.bean.MessageRecord;
import com.everflourish.act.app.domain.bean.ReadRecord;
import com.everflourish.act.app.util.ExcelUtil;
import com.everflourish.act.app.util.FileToZip;
import com.everflourish.act.app.util.FileUtil;
import com.everflourish.act.db.DbManager;

@Service
public class MessageRecordService {
	private static final Logger logger = LoggerFactory.getLogger(MessageRecordService.class);
	@Value("${image.address}")
	private String imageAddress;
	@Value("${image.url}")
	private String imgUrl;
	/**
	 * 用户获取消息列表
	 */
	public List<Map<String, Object>> getMessageRecordList(String wdNO,Integer page){
		String sql = "SELECT mr.id id,mr.content content,mr.title title, rr.id rrId, mr.create_time createTime, mr.`status` `status` " + 
				"FROM (SELECT * FROM message_record WHERE `status` != 1 AND NOW() > start_time AND NOW() < end_time) mr"+ 
				" LEFT JOIN (SELECT * FROM read_record WHERE wd_no = ? GROUP BY mess_id,wd_no ) " + 
				"rr on rr.mess_id = mr.id ORDER BY mr.create_time DESC limit ?,?";
		List<Map<String, Object>> mrList = DbManager.wxApp.queryForList(sql, new Object[] { wdNO,(page - 1) * 10, 10 });
		return mrList;
	}
	
	/**
	 * 管理员获取消息列表
	 */
	public List<MessageRecord> getMessageRecordListAdmin(Integer page,String key){
		String sql = "select * from message_record where ";
		if(key != null && key.length() > 0) {
			key = "%"+ key +"%";
			sql = sql + " title like ?";
		}else {
			key = "1";
			sql = sql + " 1 = ?";
		}
		sql = sql + "ORDER BY create_time DESC limit ?,?";
		List<MessageRecord> mrList = DbManager.wxApp.queryForListBean(sql, MessageRecord.class,new Object[] { key, (page - 1) * 10, 10 });
		return mrList;
	}
	
	/**
	 * 根据ID查询消息
	 */
	public MessageRecord getMessageRecordById(String messId) {
		String sql = "SELECT * FROM message_record where id = ?";
		MessageRecord mr = DbManager.wxApp.queryForBean(sql, MessageRecord.class, new Object[] { messId });
		return mr;
	}
	/**
	 * 管理员查看消息阅读状态
	 */
	public List<Map<String, Object>> getMessageRecordReadInfo(String messId,String mcName,String mcId,Integer page,Integer pageSize,Integer status){
		String sql = "SELECT rr.content,rr.image_url imageUrls,rr.rrmt modifyTime,u.nick_name nickName,"+ 
	            "u.head_image headImage,rr.wd_no storeCode,tw.mobile_phone phone,tw.mc_id mcId,tw.mc_name mcName " + 
				"FROM (SELECT * FROM message_record " + 
				"WHERE id = ?) mr " + 
				"LEFT JOIN (SELECT rr.*,s.rrm rrmt FROM read_record rr RIGHT JOIN (SELECT wd_no,MAX(modify_time) "
				+ "rrm FROM read_record WHERE mess_id = ? " + 
				"GROUP BY wd_no) s ON s.rrm = rr.modify_time AND rr.wd_no = s.wd_no) rr " + 
				"ON mr.id = rr.mess_id " + 
				"LEFT JOIN users u " + 
				"ON rr.user_id = u.id RIGHT JOIN (SELECT * FROM t_wd WHERE";
				if("全部".equals(mcName)) {
					sql = sql + " 1 = ? and 1 = ? ";
					mcName = "1";
					mcId = "1";
				}else {
					if(mcName != null && mcName.length() > 0) {
						sql = sql + " o_name = ? ";
					}else {
						sql = sql + " 1 = ? ";
						mcName = "1";
					}
					if(mcId != null && mcId.length() > 0){
						sql = sql + " and mc_id = ? ";
					}else{
						sql = sql + " and 1 = ? ";
						mcId = "1";
					}
				}
				sql = sql + ") tw ON u.wd_no = tw.store_code where ";
				
				//全部
				if(0 == status) {
					sql = sql + "1 = 1";
				}
				
				//未读
				if(1 == status) {
					sql = sql + "rr.rrmt IS NULL";
				}
				
				//已读
				if(2 == status) {
					sql = sql + "rr.rrmt IS NOT NULL";
				}
				
				//未回复
				if(3 == status) {
					sql = sql + "rr.content IS NULL AND rr.image_url IS NULL ";
				}
				
				//已经回复
				if(4 == status) {
					sql = sql + " (rr.content IS NOT NULL OR rr.image_url IS NOT NULL) ";
				}
				
				sql = sql + " ORDER BY rr.rrmt DESC limit ?,?";
		List<Map<String, Object>> result = DbManager.wxApp.queryForList(sql, new Object[] {messId,messId,mcName,mcId,page, pageSize});
		return result;
	}
	/**
	 * 统计消息数量
	 * @param messId
	 * @param mcName
	 * @param mcId
	 * @param type
	 * @return
	 */
	public Integer getReadCordNum(String messId,String mcName,String mcId,Integer type,Integer status) {
		String sql = "SELECT COUNT(1) FROM (SELECT *,MAX(modify_time) rrmt FROM read_record WHERE mess_id = ? GROUP BY wd_no) rr "
				+ "RIGHT JOIN (SELECT * FROM t_wd WHERE";
				if("全部".equals(mcName)) {
					sql = sql + " 1 = ? and 1 = ?";
					mcName = "1";
					mcId = "1";
				}else {
					if(mcName != null && mcName.length() > 0) {

						sql = sql + " o_name = ? ";
					}else {
						sql = sql + " 1 = ? ";
						mcName = "1";
					}
					if(mcId != null && mcId.length() > 0){
						sql = sql + " and mc_id = ? ";
					}else{
						sql = sql + " and 1 = ? ";
						mcId = "1";
					}
				}
				sql = sql + ") tw on rr.wd_no = tw.store_code WHERE ";
				
				//全部
				if(0 == status) {
					sql = sql + "1 = 1";
				}
				
				//未读
				if(1 == status) {
					sql = sql + "rr.rrmt IS NULL";
				}
				
				//已读
				if(2 == status) {
					sql = sql + "rr.rrmt IS NOT NULL";
				}
				
				//未回复
				if(3 == status) {
					sql = sql + "rr.content IS NULL AND rr.image_url IS NULL ";
				}
				
				//已回复
				if(4 == status) {
					sql = sql + "(rr.content IS NOT NULL OR rr.image_url IS NOT NULL) ";
				}
				
				if(type == 1) {
					sql = sql + " and rr.rrmt IS NOT NULL";
				}else if(type == 0){
					sql = sql + " and rr.rrmt IS NULL";
				}else if(type == 2){
					sql = sql + " and (rr.content IS NOT NULL or rr.image_url IS NOT NULL)";
				}
			List<Object[]> obs = DbManager.wxApp.queryForListObjects(sql, new Object[] { messId,mcName,mcId});
		return Integer.parseInt(obs.get(0)[0].toString());
	}
	/**
	 * 新增消息
	 */
	public void addMessageRecord(MessageRecord mr) {
		String sql = "INSERT INTO message_record (id,content,create_time,modify_time,title,create_by,type,status,start_time,end_time) VALUES (?,?,?,?,?,?,?,?,?,?)";
		DbManager.wxApp.insert(sql, new Object[] {mr.getId(),mr.getContent(),new Timestamp(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()),mr.getTitle(),mr.getCreateBy(),mr.getType(),mr.getStatus(),mr.getStartTime(),mr.getEndTime()});
	}
	/**
	 * 删除消息
	 */
	public void deleteMessageRecord(String messId) {
		//删除消息
		String sql = "DELETE FROM message_record WHERE id = ?";
		DbManager.wxApp.update(sql, new Object[] { messId });
		//删除回复
		String sql1 = "DELETE FROM read_record WHERE mess_id = ?";
		DbManager.wxApp.update(sql1, new Object[] { messId });
	}
	/**
	 * 更新消息
	 */
	public void updateMessageRecord(MessageRecord mr) {
		String sql = "UPDATE message_record SET content = ?,modify_time = ?,title = ?,type = ?,status = ?,start_time = ?,end_time = ? WHERE id = ?";
		DbManager.wxApp.update(sql, new Object[] { mr.getContent(),new Timestamp(System.currentTimeMillis()),
				mr.getTitle(),mr.getType(),mr.getStatus(),mr.getStartTime(),mr.getEndTime(),mr.getId()});
	}
	/**
	 * 更新消息
	 */
	public void closeMessageRecord(JSONArray ids) {
		for(Object json : ids) {
			String sql = "UPDATE message_record SET status = 2,modify_time = now() WHERE id = ?";
			DbManager.wxApp.update(sql, new Object[] { json });	
		}
	}
	/**
	 * 查询消息总数
	 */
	public String getMessageRecordCount(String key){
		String sql = "SELECT COUNT(1) count FROM message_record where";
		if(key == null || key.length() == 0) {
			key = "1";
			sql = sql + " 1 = ?";
		}else {
			sql = sql + " title like ?";
			key = "%" + key + "%";
		}
		List<Object[]> count = DbManager.wxApp.queryForListObjects(sql, new Object[] { key });
		if(count != null && count.get(0) != null) {
			return count.get(0)[0].toString();
		}else {
			return null;
		}
	}
	
	/**
	 * 查询消息总数
	 */
	public String getUserMessageRecordCount(){
		String sql = "SELECT COUNT(1) count FROM message_record where `status` != 1 AND NOW() > start_time AND NOW() < end_time";
		List<Object[]> count = DbManager.wxApp.queryForListObjects(sql, new Object[] {});
		if(count != null && count.get(0) != null) {
			return count.get(0)[0].toString();
		}else {
			return null;
		}
	}
	
	/**
	 * 导出报表
	 */
	public ByteArrayOutputStream getMessRecordExport(String messId,String mcName,String mcId) throws IOException {
		String zipName = "网点数据";
		ByteArrayOutputStream os = null;
		try {
			String sql = "SELECT rr.content,rr.rrmt modifyTime,tw.store_code wdNo,rr.image_url imageUrl,tw.mc_id userId,tw.o_name messId"
					+ " FROM(SELECT rr.*,s.rrm rrmt FROM read_record rr RIGHT JOIN (SELECT wd_no,MAX(modify_time) rrm "
					+ "FROM read_record WHERE mess_id = ? " + 
					"GROUP BY wd_no) s ON s.rrm = rr.modify_time AND rr.wd_no = s.wd_no) "
					+ "rr RIGHT JOIN (SELECT * FROM t_wd WHERE ";
			if("全部".equals(mcName)) {
				sql = sql + " 1 = ? and 1 = ?";
				mcName = "1";
				mcId = "1";
			}else {
				if(mcName != null && mcName.length() > 0) {
					sql = sql + " o_name = ? ";
				}else {
					sql = sql + " 1 = ? ";
					mcName = "1";
				}
				if(mcId != null && mcId.length() > 0){
					sql = sql + " and mc_id = ? ";
				}else{
					sql = sql + " and 1 = ? ";
					mcId = "1";
				}
			}
			sql = sql + ") tw on rr.wd_no = tw.store_code";
			List<ReadRecord> rrList =  DbManager.wxApp.queryForListBean(sql, ReadRecord.class, new Object[] { messId,mcName,mcId });
			File file = new File(imageAddress+"/"+zipName);  
			if(!file.exists()){  
			    file.mkdirs();  
			    file = null;
			}else {
				//保证不互相影响
				return null;
			}
			ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
			ArrayList<Object> objs = new ArrayList<Object>();
			objs.add("网点号");
			objs.add("阅读状态");
			objs.add("回复状态");
			objs.add("回复内容");
			objs.add("阅读/回复时间");
			objs.add("片区");
			objs.add("专管员");
			objs.add("图片张数");
			result.add(objs);	
			List<String> imageUrls = new ArrayList<String>();
			if(rrList != null && !rrList.isEmpty()) {
				for(ReadRecord rr : rrList) {
					objs = new ArrayList<Object>();
					objs.add(rr.getWdNo());
					if(rr.getModifyTime() == null) {
						objs.add("未阅读");
					}else {
						objs.add("已阅读");
					}
					if(rr.getContent() == null && rr.getImageUrl() == null){
						objs.add("未回复");
						objs.add("--");
					}else {
						objs.add("已回复");
						if(rr.getContent() == null) {
							objs.add("--");
						}else {
							objs.add(rr.getContent());
						}
					}
					if(rr.getModifyTime() != null) {
						objs.add(rr.getModifyTime());
					}else {
						objs.add("--");
					}
					objs.add(rr.getUserId());
					objs.add(rr.getMessId());
					System.out.println("图片地址---"+rr.getImageUrl());
					if(rr.getImageUrl() != null) {
						String url = rr.getImageUrl().replace("[", "");
						url = url.replace("]", "");
						url = url.replace("\"", "");
						String [] urls = url.split(",");
						//判断图片地址集合是否为空
						if(url.length() > 0) {
							for(int i = 0;i < urls.length; i++) {
								//将图片地址处理成服务器本地地址
								String str = urls[i].replace(imgUrl, imageAddress);
								System.out.println("图片地址："+str);
								logger.info("图片地址："+str);
								imageUrls.add(str);
							}
						}
						objs.add(urls.length);
					}else {
						objs.add(0);
					}
					result.add(objs);
				}
			}
			//导出报表
			ExcelUtil.writeExcel(result, imageAddress+"/"+zipName+"/" + zipName + ".xls");
			System.out.println("图片大小：---------------"+imageUrls.size());
			//移动图片
			for(String url : imageUrls) {
				String newAdress = url.replace(imageAddress, imageAddress+"/"+zipName +"/");
				logger.info("旧地址："+url);
				System.out.println("旧地址："+url);
				logger.info("新图片地址："+newAdress);
				System.out.println("新图片地址："+newAdress);
				FileUtil.copytherFolders(url, newAdress);
			}
			os = FileToZip.fileToZipOutputStream(imageAddress+"/"+zipName, imageAddress, zipName+".zip");
		}catch(Exception e) {
			e.getStackTrace();
		}finally {
			FileUtils.deleteQuietly(new File(imageAddress+"/"+zipName));
			System.out.println("删除---------------");
		}
		return os;
	}
}

