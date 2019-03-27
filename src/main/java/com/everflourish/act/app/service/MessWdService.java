package com.everflourish.act.app.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.MessWd;
import com.everflourish.act.app.domain.bean.TWd;
import com.everflourish.act.db.DbManager;

@Service
public class MessWdService {
	/**
	 * 加载消息网点
	 * @param messId
	 * @return
	 */
	public List<Map<String, Object>> getTWdByMessId(String messId){
		String sql = "SELECT tw.store_code wdNo,tw.mc_id mcId FROM (SELECT * FROM mess_wd WHERE mess_id = ?) mw LEFT JOIN t_wd tw ON mw.wd_no = tw.store_code";
		List<Map<String, Object>> twList = DbManager.wxApp.queryForList(sql, messId);
		return twList;
	}
	/**
	 * 加载联系人区域
	 * @param messId
	 * @return
	 */
	public List<Object[]> getMcIdByMessId(String messId){
		String sql = "SELECT tw.mc_id mcId FROM (SELECT * FROM mess_wd WHERE mess_id = ?) mw LEFT JOIN t_wd tw ON mw.wd_no = tw.store_code GROUP BY tw.mc_id";
		List<Object[]> mcIdList = DbManager.wxApp.queryForListObjects(sql, messId);
		return mcIdList;
	}
	/**
	 * 获取总数
	 * @param messId
	 * @return
	 */
	public Integer getTotalCount(String messId) {
		String sql = "SELECT COUNT(1) FROM mess_wd WHERE mess_id = ?";
		List<Object[]> obs = DbManager.wxApp.queryForListObjects(sql, messId);
		return Integer.parseInt(obs.get(0)[0].toString());
	}
	/**
	 * 保存联系人
	 * @param msList
	 */
	@Transactional
	public void addMessWd(List<MessWd> msList) {
		String sql = "INSERT INTO mess_wd (id,mess_id,wd_no) VALUES ";
		StringBuffer sb = new StringBuffer(sql);
		if(msList != null && !msList.isEmpty()) {
			//先清空联系人
			String sql1 = "DELETE FROM mess_wd WHERE mess_id = ?";
			DbManager.wxApp.update(sql1, new Object[] {msList.get(0).getMessId()});
			for(MessWd mw : msList) {
				UUID uuid = UUID.randomUUID();
				String id = uuid.toString().replace("-", "");
				sb.append("\r\n('"+id+"','"+mw.getMessId()+"','" + mw.getWdNo() + "'),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";");
			DbManager.wxApp.insert(sb.toString());
		}
	}
}
