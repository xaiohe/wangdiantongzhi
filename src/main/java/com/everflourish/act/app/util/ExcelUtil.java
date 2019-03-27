package com.everflourish.act.app.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public static List<String[]> read(String fileName, InputStream in) {
		if (fileName.endsWith(".xls")) {
			return readXsl(in);
		}
		if (fileName.endsWith(".xlsx")) {
			return readXslx(in,0);
		}
		throw new Error("Excel 读取异常！");
	}

	public static List<String[]> read(String fileName, InputStream in,
			String sheetName) {
		if (fileName.endsWith(".xls")) {
			return readXsl(in, sheetName);
		}
//		if (fileName.endsWith(".xlsx")) {
//			return readXslx(in, sheetName);
//		}
		throw new Error("Excel 读取异常！");
	}

//	public static List<String[]> readXslx(InputStream in, String sheetName) {
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(in);
//			XSSFSheet sheet = workbook.getSheet("sheetName");
//			return readXslx(in, sheet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public static List<String[]> readXslx(InputStream in) {
//
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(in);
////			XSSFSheet sheet = workbook.getSheetAt(0);
//			return readXslx(in);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	public static List<String[]> readXslx(InputStream in,Integer sheetNum){
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(sheetNum);
			XSSFRow row = sheet.getRow(0);
			int coloumNum= row.getLastCellNum();
			int rowNum= sheet.getLastRowNum()+1;
			
			List<String[]> list = new ArrayList<String[]>(rowNum);
			for(int i=0;i<rowNum;++i){
				row = sheet.getRow(i);
				String[] colObj = new String[coloumNum];
				boolean flag = false;
				for(int j=0;j<coloumNum;++j){
					XSSFCell cell = row.getCell(j);
					if(cell==null)continue;	
					flag = true;
					String obj = null;
					switch (cell.getCellType()) {

					case XSSFCell.CELL_TYPE_STRING:
						obj = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						obj = cell.getCellFormula();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						
						if (HSSFDateUtil.isCellDateFormatted(cell)) {   
					        //  如果是date类型则 ，获取该cell的date值   
							obj = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();   
					    } else { // 纯数字   
					    	//obj = String.valueOf(cell.getNumericCellValue());
					    	/*HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
							String cellFormatted = dataFormatter.formatCellValue(cell);
							obj = cellFormatted;*/
					    	DecimalFormat df = new DecimalFormat("0");  
					    	obj = df.format(cell.getNumericCellValue());
					    }
						break;
					case HSSFCell.CELL_TYPE_ERROR:
						break;
					}
					colObj[j] = obj;
				}
				if(flag){
					list.add(colObj);
				}
			}
			
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				in.close();
			} catch (IOException e1) { 
				e1.printStackTrace();
			}
		}
	}

	public static List<String[]> readXslx(File file,Integer sheet) {
		try {
			return readXslx(new FileInputStream(file),sheet);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String[]> readXsl(InputStream in, String sheetName) {
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(in);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			return readXsl(in, sheet);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static List<String[]> readXsl(InputStream in) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			HSSFSheet sheet = workbook.getSheetAt(0);
			return readXsl(in, sheet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static List<String[]> readXsl(InputStream in, HSSFSheet sheet) {
		try {
			HSSFRow row = sheet.getRow(0); // 获取第一行
			int coloumNum = row.getLastCellNum();// 获取共有多少列
			int rowNum = sheet.getLastRowNum() + 1;// 获取共有多少行

			List<String[]> list = new ArrayList<String[]>(rowNum);
			for (int i = 0; i < rowNum; ++i) {
				row = sheet.getRow(i);
				String[] colObj = new String[coloumNum];
				boolean flag = false;
				for (int j = 0; j < coloumNum; ++j) {
					if (row == null) {
						continue;
					}
					HSSFCell cell = row.getCell(j);
					if (cell == null)
						continue;
					flag = true;
					String obj = null;
					switch (cell.getCellType()) {

					case HSSFCell.CELL_TYPE_STRING:
						obj = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						obj = cell.getCellFormula();
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
						String cellFormatted = dataFormatter
								.formatCellValue(cell);
						obj = cellFormatted;
						break;

					case HSSFCell.CELL_TYPE_BLANK:
						break;
					case HSSFCell.CELL_TYPE_ERROR:
						break;
					}
					colObj[j] = obj;
				}
				if (flag) {
					list.add(colObj);
				}
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static List<String[]> readXsl(File file) {
		try {
			return readXsl(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * public static void exportXsl(HttpServletResponse response,List<String[]>
	 * list,String fileName){ OutputStream output = null; try { output =
	 * response.getOutputStream(); HSSFWorkbook wb = new HSSFWorkbook();
	 * HSSFSheet sheet = wb.createSheet("sheet1"); int i = 0; HSSFRow row =
	 * null; for(String[] s:list){ row = sheet.createRow(i++); HSSFCell cell=
	 * null; int j = 0; for(String str:s){ cell = row.createCell(j++);
	 * if(str==null)continue; cell.setCellValue(str); } }
	 * response.setContentType("application/vnd.ms-excel");
	 * response.setHeader("Content-disposition",
	 * "attachment;filename="+fileName+".xls"); wb.write(output); } catch
	 * (IOException e) { e.printStackTrace(); }finally{ if(output!=null){ try {
	 * output.flush(); } catch (IOException e) { e.printStackTrace(); } try {
	 * output.close(); } catch (IOException e) { e.printStackTrace(); } } } }
	 */
	public static void exportXsl(HttpServletResponse response,
			List<? extends Object[]> list, String fileName) {
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("sheet1");
			int i = 0;
			HSSFRow row = null;
			for (Object[] s : list) {
				row = sheet.createRow(i++);
				HSSFCell cell = null;
				int j = 0;
				for (Object param : s) {
					cell = row.createCell(j++);
					if (param == null)
						continue;
					if (param instanceof Date) {
						Date date = (Date) param;
						cell.setCellValue(date);
					} else {
						cell.setCellValue(String.valueOf(param));
					}
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName + ".xls");
			wb.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void exportXsl(HttpServletResponse response,String[]
	// header,String fileName,String querySql,Map<String,Object> paramsMap){
	// SqlInfo queryInfo = SqlHolder.getSqlInfo(querySql, paramsMap);
	// List<Object[]> body = DbManager.queryForListObject(queryInfo.getSql(),
	// queryInfo.getSqlParamsArray());
	// exportXsl(response, header, body, fileName);
	// }
	// public static void exportXsl(HttpServletResponse response,String[]
	// header,String querySql,Map<String,Object> paramsMap){
	// exportXsl(response,header,DateUtil.str(new Date(),
	// DateUtil.DAY_MILLISECONDS),querySql,paramsMap);
	// }
	public static void exportXsl(HttpServletResponse response, String[] header,
			List<Object[]> body, String fileName) {
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("sheet1");
			int i = 0;
			HSSFRow row = sheet.createRow(i++);

			for (int n = 0; n < header.length; ++n) {
				HSSFCell cell = row.createCell(n);
				cell.setCellValue(header[n]);
			}
			for (Object[] s : body) {
				row = sheet.createRow(i++);
				HSSFCell cell = null;
				int j = 0;
				for (Object param : s) {
					cell = row.createCell(j++);
					if (param == null)
						continue;
					if (param instanceof Date) {
						Date date = (Date) param;
						cell.setCellValue(date);
					} else {
						cell.setCellValue(String.valueOf(param));
					}
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileName + ".xls");
			wb.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ByteArrayOutputStream exportExcel(ArrayList<ArrayList<Object>> result){  
        if(result == null){  
            return null;  
        }  
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("sheet");  
        for(int i = 0 ;i < result.size() ; i++){  
             HSSFRow row = sheet.createRow(i);  
            if(result.get(i) != null){  
                for(int j = 0; j < result.get(i).size() ; j ++){  
                    HSSFCell cell = row.createCell(j);  
                    cell.setCellValue(result.get(i).get(j).toString());  
                }  
            }  
        }  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        try  
        {  
            wb.write(os);  
        } catch (IOException e){  
            e.printStackTrace();  
        }  
		return os;             
    }  
	public static void writeExcel(ArrayList<ArrayList<Object>> result,String path){  
        if(result == null){  
            return;  
        }  
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        for(int i = 0 ;i < result.size() ; i++){  
             HSSFRow row = sheet.createRow(i);  
            if(result.get(i) != null){  
                for(int j = 0; j < result.get(i).size() ; j ++){  
                    HSSFCell cell = row.createCell(j);  
                    cell.setCellValue(result.get(i).get(j).toString());  
                }  
            }  
        }  
        ByteArrayOutputStream os = new ByteArrayOutputStream();  
        try  
        {  
            wb.write(os);  
        } catch (IOException e){  
            e.printStackTrace();  
        }  
        byte[] content = os.toByteArray();  
        File file = new File(path);//Excel文件生成后存储的位置。  
        OutputStream fos  = null;  
        try  
        {  
            fos = new FileOutputStream(file);  
            fos.write(content);  
            os.close();  
            fos.close();  
        }catch (Exception e){  
            e.printStackTrace();  
        }             
    }  
	/*
	 * private void getCell(HSSFCell cell) { switch (cell.getCellType()) {
	 * 
	 * case HSSFCell.CELL_TYPE_STRING:
	 * 
	 * System.out.println(cell.getStringCellValue());
	 * 
	 * break;
	 * 
	 * case HSSFCell.CELL_TYPE_FORMULA:
	 * 
	 * System.out.println(cell.getCellFormula());
	 * 
	 * break;
	 * 
	 * case HSSFCell.CELL_TYPE_NUMERIC:
	 * 
	 * HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
	 * 
	 * String cellFormatted = dataFormatter.formatCellValue(cell);
	 * 
	 * System.out.println(cellFormatted); break;
	 * 
	 * case HSSFCell.CELL_TYPE_ERROR:
	 * 
	 * break;
	 * 
	 * } }
	 */
}
