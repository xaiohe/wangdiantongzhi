package com.everflourish.act.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everflourish.act.app.domain.bean.TWd;
import com.everflourish.act.db.DbManager;

@Service
public class TWdService {
	@Autowired
	private DataSource dataSource;
	/**
	 * 根据网点编号查询网点
	 */
	public TWd getTWdByStoreCode(String wdNo,String phone) {
		String sql = "SELECT * FROM t_wd WHERE store_code = ? and mobile_phone = ?";
		TWd twd = DbManager.wxApp.queryForBean(sql, TWd.class, new Object[] { wdNo,phone });
		return twd;
	}
	/**
	 * 批量添加网点信息
	 */
	public void addTWdList(List<TWd> twdList) {
		String sql = "INSERT INTO t_wd (wd_id,o_name,status,id_card,store_code) VALUES ";
		StringBuffer sb = new StringBuffer(sql);
		if(twdList != null && !twdList.isEmpty()) {
			for(TWd twd : twdList) {
				UUID uuid = UUID.randomUUID();
				String id = uuid.toString().replace("-", "");
				sb.append("\r\n('"+id+"','"+twd.getMcName()+"','" + twd.getStatus() + "','"
						+ "" + twd.getIdCard() + "','"+ twd.getStoreCode() +"'),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";");
			DbManager.wxApp.insert(sb.toString());
		}
	}
	/**
	 * 校验专管员
	 */
	public List<TWd> getTWdByMcName(String mcName) {
		String sql = "";
		
		if("全部".equals(mcName)) {
			sql = "SELECT * FROM t_wd WHERE 1 = ?";
			mcName = "1";
		}else {
			sql = "SELECT * FROM t_wd WHERE o_name = ?";
		}
		List<TWd> twds = DbManager.wxApp.queryForListBean(sql, TWd.class, new Object[] { mcName });
		return twds;
	}
	/**
	 * 查询所有区域编号
	 */
	public List<Object[]> getAllMcId() {
		String sql = "SELECT * FROM (SELECT mc_id FROM t_wd ORDER BY mc_id ASC)s GROUP BY s.mc_id";
		List<Object[]> mcIds = DbManager.wxApp.queryForListObjects(sql, new Object[] {});
		return mcIds;
	}
	/**
	 * 根据提交筛选网点
	 */
	public List<Map<String,Object>> getTwd(String wdNo,String phone,String mcId,Integer page){
		List<String> mcIds = new ArrayList<String>();
		String sql = "SELECT store_code wdNo,mc_id mcId FROM t_wd WHERE ";
		if(wdNo != null && wdNo.length() > 0) {
			sql = sql + "store_code = :wdNo";
		}else {
			wdNo = "1";
			sql = sql + "1 = :wdNo";
		}
		
		if(phone != null && phone.length() > 0) {
			sql = sql + " and mobile_phone = :phone";
		}else {
			phone = "1";
			sql = sql + " and 1 = :phone";
		}
		
		if(mcId != null && mcId.length() > 0) {
		     String[] mcs = mcId.split(",");
		     for(int i = 0;i < mcs.length; i++) {
		    	 mcIds.add(mcs[i]);
		     }
			sql = sql + " and mc_id in (:mcId)";
		}else {
			mcId = "1";
			sql = sql + " and 1 = :mcId";
		}
		
		if(page == 0) {
			page = 1;
			sql = sql + " and 1 = :page";
		}else {
			page = (page - 1) * 10;
			sql = sql + " limit :page,10";
		}
		
        Map<String, Object> params = new HashMap<String, Object>();
        
        params.put("wdNo", wdNo);
        params.put("phone", phone);
        params.put("mcId", mcIds);
        params.put("page", page);
        
		List<Map<String,Object>> twds = DbManager.wxApp.queryBatchForList(sql, params);
		return twds;
	}
}
