package com.everflourish.act.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WdUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String[]> objs = ExcelUtil.readXsl(new File("D:\\exam\\门店信息 8.31.xls"));
		String sql = "INSERT INTO t_wd (wd_id,store_code,status,mc_id,mc_name,mobile_phone,o_name,cz_name) VALUES ";
		StringBuffer sb = new StringBuffer(sql);
		int i = 1;
		for(String[] strArr : objs) {
			sb.append("\r\n('"+i+"','"+strArr[1]+"','" + strArr[2] + "','" + strArr[3] + 
					"','" + strArr[4] + "','" + strArr[5] + "','" 
					+ strArr[6] + "','" + strArr[7] + "'),");
			i++;
		}
		//记录日志
		sb.deleteCharAt(sb.length() - 1);
		sb.append(";");
		String path ="D:\\exam\\t_wd.sql";
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
