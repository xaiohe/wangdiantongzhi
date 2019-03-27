package com.everflourish.act.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.everflourish.act.app.domain.bean.NoticeRecord;
import com.everflourish.act.app.util.DingTalkUtil;
import com.everflourish.act.app.vo.NoticeRecordVO;
import com.everflourish.act.app.vo.ReadStatusVO;
import com.everflourish.act.db.DbManager;
import com.taobao.api.ApiException;

/**  
 * @Title:  NoticeRecordService.java   
 * @Package com.everflourish.act.app.service   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: yangming
 * @date:   2019年3月14日 下午5:56:43   
 * @version V1.0  
 */
@Service
public class NoticeRecordService {

	/**
	 * @throws ApiException 
	 * @Title: getNoticeRecordList   
	 * @Description: 查询消息通知阅读记录
	 * @param: @param fileId
	 * @param: @return      
	 * @return: List<NoticeRecord>      
	 * @throws
	 */
	public List<NoticeRecordVO> getNoticeRecordList(String fileId,Integer page,Integer pageSize) throws ApiException{
		List<NoticeRecordVO> nrVoList = new ArrayList<NoticeRecordVO>();
		String sql = "SELECT * FROM notice_record WHERE file_id = ? ORDER BY create_time DESC limit ?,?";
		Map<String,String> uerList = DingTalkUtil.getGroupUser();
		List<NoticeRecord> nrList = DbManager.wxApp.queryForListBean(sql, NoticeRecord.class, new Object[] { fileId,(page - 1)*pageSize,pageSize });
		for(NoticeRecord nr : nrList) {
			NoticeRecordVO nrVO = new NoticeRecordVO();
			nrVO.setNr(nr);
			JSONArray arr = DingTalkUtil.getAlreadyRead(nr.getDdId());
			List<ReadStatusVO> rsList = new ArrayList<ReadStatusVO>();
			for(String key : uerList.keySet()) {
				ReadStatusVO rs = new ReadStatusVO();
				rs.setName(uerList.get(key));
				if(arr.contains(key)) {
					rs.setStatus("已阅读");
				}else {
					rs.setStatus("未阅读");
				}
				rsList.add(rs);
			}
			nrVO.setRsList(rsList);
			nrVoList.add(nrVO);
		}
		return nrVoList;
	}
	/**
	 * @Title: getNoticeRecordCount   
	 * @Description: 获取总条数
	 * @param: @param fileId
	 * @param: @return      
	 * @return: Integer      
	 * @throws
	 */
	public String getNoticeRecordCount(String fileId) {
		String sql = "SELECT count(1) num FROM notice_record WHERE file_id = ?";
		Map<String,Object> result = DbManager.wxApp.queryForMap(sql, new Object[] { fileId });
		return result.get("num").toString();
	}
}
