package com.everflourish.act.app.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.exceptions.ClientException;
import com.everflourish.act.app.domain.bean.FileRecord;
import com.everflourish.act.app.domain.bean.NoticeRecord;
import com.everflourish.act.app.domain.bean.TWd;
import com.everflourish.act.app.util.DingTalkUtil;
import com.everflourish.act.app.util.ExcelUtil;
import com.everflourish.act.common.vo.ResultHelper;
import com.everflourish.act.common.vo.ResultVO;
import com.everflourish.act.db.DbManager;
import com.taobao.api.ApiException;

/**  
 * @Title:  FileRecordService.java   
 * @Package com.everflourish.act.app.service   
 * @Description: 上传文件业务层
 * @author: yangming
 * @date:   2019年3月14日 上午11:07:20   
 * @version V1.0  
 */
@Service
public class FileRecordService {
	@Value("${file.address}")
	private String fileAddress;
	@Autowired
	PhoneService phoneService;
	/**
	 * @throws ApiException 
	 * @throws ClientException 
	 * @Title: uploadFile   
	 * @Description: 保存上传文件
	 * @param: @param files
	 * @param: @return
	 * @param: @throws IllegalStateException
	 * @param: @throws IOException      
	 * @return: ResultVO<Object>      
	 * @throws
	 */
	@Transactional
	public ResultVO<Object> uploadFile(@RequestParam("files") MultipartFile[] files) throws IllegalStateException, IOException, ClientException, ApiException{
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (file != null && !file.isEmpty() && file.getSize() > 0) {
					File filepath = new File(fileAddress);
					if (!filepath.exists())
						filepath.mkdirs();
					// 文件保存路径
					String savePath = fileAddress + file.getOriginalFilename();
					// 转存文件
					file.transferTo(new File(savePath));
					
					FileRecord fr = new FileRecord();
					fr.setFileName(file.getOriginalFilename());
					fr.setFilePath(savePath);
					//插入上传文件记录
					Integer id = insertFileRecord(fr);
					fr.setId(id);
					//发送消息
					List<NoticeRecord> nrList = sendMess(savePath,fr.getId());
					insertNoticeRecord(nrList);
				}
			}
		}
		return ResultHelper.success();
	}
	
	/**
	 * @Title: insertFileRecord   
	 * @Description: 插入上传文件记录
	 * @param: @param fr      
	 * @return: void      
	 * @throws
	 */
	public Integer insertFileRecord(FileRecord fr) {
		String sql = "insert into file_record (file_name,file_path,create_time) values (?,?,?)";
		Integer id = (Integer) DbManager.wxApp.insert(sql, new Object[] {fr.getFileName(),fr.getFilePath(),new Date()});
		return id;
	}
	/**
	 * @Title: insertNoticeRecord   
	 * @Description:批量插入消息
	 * @param: @param nrList      
	 * @return: void      
	 * @throws
	 */
	public void insertNoticeRecord(List<NoticeRecord> nrList) {
		String sql = "INSERT INTO notice_record (dd_id,phone,name,dd_content,file_id,create_time) VALUES ";
		StringBuffer sb = new StringBuffer(sql);
		if(nrList != null && !nrList.isEmpty()) {
			for(NoticeRecord nr : nrList) {
				sb.append("\r\n('"+nr.getDdId()+"','"+nr.getPhone()+"','" + nr.getName() + "','"
						+ "" + nr.getDdContent() + "','"+ nr.getFileId() +"',now()),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";");
			DbManager.wxApp.insert(sb.toString());
		}
	}
	/**
	 * @throws ApiException 
	 * @Title: sendMess   
	 * @Description: 发送短信和钉钉群消息
	 * @param: @param path
	 * @param: @throws ClientException      
	 * @return: void      
	 * @throws
	 */
	public List<NoticeRecord> sendMess(String path,Integer fileId) throws ClientException, ApiException {
		List<NoticeRecord> nrList = new ArrayList<NoticeRecord>();
		List<String[]> res = null;
		if(path.contains(".xlsx")) {
			res = ExcelUtil.readXslx(new File(path), 0);
		}else {
			res = ExcelUtil.readXsl(new File(path));
		}
		if(res != null && !res.isEmpty()) {
			for(int i = 1; i < res.size(); i++) {
				NoticeRecord nr = new NoticeRecord();
				String[] strs = res.get(i);
				//业主手机号
				phoneService.sendMessageByAliDayu(strs[1]);
				String messId = DingTalkUtil.sendDingTalk(strs[6]);
				
				nr.setCreateTime(new Date());
				nr.setDdContent(strs[6]);
				nr.setDdId(messId);
				nr.setFileId(fileId);
				nr.setName(strs[5]);
				nr.setPhone(strs[4]);
				nrList.add(nr);
			}
		}
		return nrList;
	}
	
	/**
	 * @Title: getFileRecordSever   
	 * @Description: 查询文件集合
	 * @param: @return      
	 * @return: List<FileRecord>      
	 * @throws
	 */
	public List<FileRecord> getFileRecordSever(Integer page,Integer pageSize){
		String sql = "SELECT * FROM file_record ORDER BY create_time DESC limit ?,?";
		List<FileRecord> result = DbManager.wxApp.queryForListBean(sql, FileRecord.class, new Object[] {(page -1) * pageSize,pageSize});
		return result;
	}
	/**
	 * @Title: getNoticeRecordCount   
	 * @Description: 获取总条数
	 * @param: @param fileId
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public String getFileRecordCount() {
		String sql = "SELECT count(1) num FROM file_record";
		Map<String,Object> result = DbManager.wxApp.queryForMap(sql, new Object[] {});
		return result.get("num").toString();
	}
}
