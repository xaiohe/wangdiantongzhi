package com.everflourish.act.app.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everflourish.act.app.domain.bean.FileRecord;
import com.everflourish.act.app.domain.bean.MessWd;
import com.everflourish.act.app.domain.bean.MessageRecord;
import com.everflourish.act.app.domain.bean.TWd;
import com.everflourish.act.app.service.DeclareRecordService;
import com.everflourish.act.app.service.FileRecordService;
import com.everflourish.act.app.service.MessWdService;
import com.everflourish.act.app.service.MessageRecordService;
import com.everflourish.act.app.service.NoticeRecordService;
import com.everflourish.act.app.service.ReadRecordService;
import com.everflourish.act.app.service.TWdService;
import com.everflourish.act.app.service.UsersService;
import com.everflourish.act.app.util.Base64Image;
import com.everflourish.act.app.util.ExceptionUtil;
import com.everflourish.act.app.vo.NoticeRecordVO;
import com.everflourish.act.app.vo.PageRequest;
import com.everflourish.act.common.vo.ResultCode;
import com.everflourish.act.common.vo.ResultHelper;
import com.everflourish.act.common.vo.ResultVO;

@Controller
@RequestMapping("wxadmin/")
public class WxAdminController {
	private static final Logger logger = LoggerFactory.getLogger(WxAdminController.class);
	@Autowired
	UsersService usersService;
	@Autowired
	TWdService tWdService;
	@Autowired
	MessageRecordService messageRecordService;
	@Autowired
	ReadRecordService readRecordService;
	@Autowired
	MessWdService messWdService;
	@Autowired
	DeclareRecordService declareRecordService;
	@Autowired
	FileRecordService fileRecordService;
	@Autowired
	NoticeRecordService noticeRecordService;
	@Value("${image.address}")
	private String imageAddress;

	/**
	 * 3.用户消息列表
	 */
	@RequestMapping(value = "get/mess/list/{page}", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO<JSONObject> getMessRecord(@PathVariable("page") Integer page ,@RequestBody JSONObject requst) {
		try {

			if (page == null || page == 0 || page < 0) {
				logger.info("用户消息列表参数为空：" + page);
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			String key = requst.getString("key");
			List<MessageRecord> mrList = messageRecordService.getMessageRecordListAdmin(page,key);
			String count = messageRecordService.getMessageRecordCount(key);
			List<Object[]> mcIds = tWdService.getAllMcId();
			JSONObject json = new JSONObject();
			json.put("mcIds", mcIds);
			json.put("mrList", mrList);
			json.put("count", count);
			logger.info("用户消息列表---" + page);
			System.out.println("用户消息列表");
			return ResultHelper.success(json);
		} catch (Exception e) {
			logger.error("获取消息列表：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 1.消息详情页（用户阅读状态，上传状态）
	 */
	@RequestMapping(value = "get/mess", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<JSONObject> getMessRecordInfo(@RequestBody JSONObject request) {
		try {
			String messId = request.getString("messId");
			String mcName = request.getString("mcName");
			String mcId = request.getString("mcId");
			Integer page = request.getInteger("page");
			Integer pageSize = request.getInteger("pageSize");
			Integer status = request.getInteger("status");
			if (messId == null) {
				logger.info("消息详情页参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			} else {
				if (mcName != null && mcName.length() > 0) {
					List<TWd> twds = tWdService.getTWdByMcName(mcName);
					if (twds.isEmpty()) {
						return ResultHelper.fail(ResultCode.ADMIN_NOT_FOUND);
					}
				}
				JSONObject json = new JSONObject();
				MessageRecord mr = messageRecordService.getMessageRecordById(messId);
				List<Map<String, Object>> rrList = messageRecordService.getMessageRecordReadInfo(messId, mcName, mcId,
						page,pageSize,status);
				Integer isReadNum = messageRecordService.getReadCordNum(messId, mcName, mcId, 1,status);
				Integer notReadNum = messageRecordService.getReadCordNum(messId, mcName, mcId, 0,status);
				Integer isReplyNum = messageRecordService.getReadCordNum(messId, mcName, mcId, 2,status);
				json.put("isReadNum", isReadNum);
				json.put("notReadNum", notReadNum);
				json.put("isReplyNum", isReplyNum);
				json.put("rrList", rrList);
				json.put("mr", mr);
				json.put("count", isReadNum + notReadNum);
				return ResultHelper.success(json);
			}
		} catch (Exception e) {
			logger.error("消息详情页：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 2.新增消息
	 */
	@RequestMapping(value = "add/mess", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addReadRecordInfo(@RequestBody JSONObject request) {
		try {
			MessageRecord mr = JSONObject.parseObject(request.getJSONObject("mr").toJSONString(), MessageRecord.class);
			messageRecordService.addMessageRecord(mr);
			return ResultHelper.success();
		} catch (Exception e) {
			logger.error("新增消息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 3.删除消息
	 */
	@RequestMapping(value = "delete/mess/{messId}", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO<Object> deleteMessRecordInfo(@PathVariable("messId") String messId) {
		try {
			if (messId == null) {
				logger.info("删除消息参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			} else {
				messageRecordService.deleteMessageRecord(messId);
				return ResultHelper.success();
			}
		} catch (Exception e) {
			logger.error("删除消息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 4.修改消息
	 */
	@RequestMapping(value = "update/mess", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> updateReadRecordInfo(@RequestBody JSONObject request) {
		try {
			MessageRecord mr = JSONObject.parseObject(request.getJSONObject("mr").toJSONString(), MessageRecord.class);
			Boolean isPush = request.getBoolean("isPush");
			if (isPush) {
				mr.setStartTime(new Timestamp(System.currentTimeMillis()));
			}
			messageRecordService.updateMessageRecord(mr);
			return ResultHelper.success();
		} catch (Exception e) {
			logger.error("新增消息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 5.上传网点信息
	 */
	@RequestMapping(value = "add/wd/list", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addWdList(@RequestBody JSONArray request) {
		try {
			List<TWd> twdList = JSONArray.parseArray(request.toJSONString(), TWd.class);
			tWdService.addTWdList(twdList);
			return ResultHelper.success();
		} catch (Exception e) {
			logger.error("新增消息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 8.上传图片
	 */
	@RequestMapping(value = { "upload/file" }, method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> uploadFile(@RequestBody JSONObject request) throws IOException {
		try {
			String imgStr = request.getString("imgStr");
			String imageName = request.getString("imageName");
			String imageUrl = imageAddress + imageName + ".png";
			// 解码图片保存到服务器
			Base64Image.generateImage(imgStr, imageUrl);
			return ResultHelper.success(imageUrl);
		} catch (Exception e) {
			logger.error("上传图片：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR);
		}
	}

	/**
	 * 导出在线打分报表
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "report/online", method = RequestMethod.GET)
	public void exportReportOnline(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String messId = request.getParameter("messId");
		String mcName = request.getParameter("mcName");
		String mcId = request.getParameter("mcId");
		ByteArrayOutputStream bos = messageRecordService.getMessRecordExport(messId, mcName, mcId);
		try {
			byte[] buffer = null;
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("网点数据.zip", "UTF-8"));
			response.setHeader("Content-Length", String.valueOf(bos.size()));
			buffer = bos.toByteArray();
			response.getOutputStream().write(buffer);
		} catch (Exception e) {
			// ...
		} finally {
			bos.close();
		}
	}

	/**
	 * 9.加载消息联系人
	 */
	@RequestMapping(value = "get/contacts/{messId}", method = RequestMethod.GET)
	@ResponseBody
	public ResultVO<JSONObject> getContacts(@PathVariable("messId") String messId) {
		try {

			if (messId == null) {
				logger.info("加载消息联系人参数为空：" + messId);
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			List<Object[]> mcIds = messWdService.getMcIdByMessId(messId);
			Integer count = messWdService.getTotalCount(messId);
			List<Map<String, Object>> twList = messWdService.getTWdByMessId(messId);
			JSONObject json = new JSONObject();
			json.put("mcIds", mcIds);
			json.put("twList", twList);
			json.put("count", count);
			return ResultHelper.success(json);
		} catch (Exception e) {
			logger.error("加载消息联系人：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 11.根据区域筛选条件加载网点
	 */
	@RequestMapping(value = "get/twd", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<JSONObject> getTWd(@RequestBody JSONObject request) {
		try {
			String mcId = request.getString("mcId");
			String wdNo = request.getString("wdNo");
			String phone = request.getString("phone");
			Integer page = request.getInteger("page");
			String messId = request.getString("messId");
			List<Object[]> mcIds = messWdService.getMcIdByMessId(messId);
			List<Map<String, Object>> twds = tWdService.getTwd(wdNo, phone, mcId, page);
			JSONObject json = new JSONObject();
			json.put("twds", twds);
			json.put("mcIds", mcIds);
			return ResultHelper.success(json);
		} catch (Exception e) {
			logger.error("根据区域筛选条件加载网点：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 12.保存消息联系人
	 */
	@RequestMapping(value = "add/mess/wd", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addMessWd(@RequestBody JSONArray request) {
		try {
			List<MessWd> mwList = JSONArray.parseArray(request.toJSONString(), MessWd.class);
			messWdService.addMessWd(mwList);
			return ResultHelper.success();
		} catch (Exception e) {
			logger.error("保存消息联系人：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 13.批量关闭消息
	 */
	@RequestMapping(value = "close/mess", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> closeMess(@RequestBody JSONArray request) {
		try{
			JSONArray ids = JSONArray.parseArray(request.toJSONString());
			if(ids == null || ids.isEmpty()) {
				logger.info("批量关闭消息参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			messageRecordService.closeMessageRecord(ids);
			return ResultHelper.success();
		}catch(Exception e) {
			logger.error("批量关闭消息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 14.加载申报消息
	 */
	@RequestMapping(value = "get/dec/record", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<JSONObject> getDeclareRecord(@RequestBody JSONObject request) {
		try {
			String key = request.getString("key");
			String mcId = request.getString("mcId");
			Integer page = request.getInteger("page");
			Integer pageSize = request.getInteger("pageSize");
			JSONObject json = new JSONObject();
			List<Map<String, Object>> decList = declareRecordService.adminGetDeclareRecord(key, mcId, page, pageSize);
			Integer count = declareRecordService.getCount(key, mcId);
			json.put("count", count);
			json.put("decList", decList);
			return ResultHelper.success(json);
		} catch (Exception e) {
			logger.error("保存消息联系人：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 15.上传通知文件
	 */
	@RequestMapping(value = { "upload/file" })
	@ResponseBody
	public ResultVO<JSONObject> uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request)
			throws IOException {
		try {
			fileRecordService.uploadFile(files);
			return ResultHelper.success();
		} catch (Exception e) {
			logger.error("上传图片：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * @Title: getFileList   
	 * @Description: 获取上传文件列表
	 * @param: @param request
	 * @param: @return
	 * @param: @throws IOException      
	 * @return: ResultVO<JSONObject>      
	 * @throws
	 */
	@RequestMapping(value = { "file/list" })
	@ResponseBody
	public ResultVO<JSONObject> getFileList(@RequestBody PageRequest request)
			throws IOException {
		try {
			JSONObject data = new JSONObject();
			List<FileRecord> frList = fileRecordService.getFileRecordSever(request.getPage(), request.getPageSize());
			String num = fileRecordService.getFileRecordCount();
			data.put("frList", frList);
			data.put("num", num);
			return ResultHelper.success(data);
		} catch (Exception e) {
			logger.error("获取上传文件列表：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	
	/**
	 * @Title: getNoticeList   
	 * @Description: 获取用户阅读记录列表
	 * @param: @param request
	 * @param: @param fileId
	 * @param: @return
	 * @param: @throws IOException      
	 * @return: ResultVO<JSONObject>      
	 * @throws
	 */
	@RequestMapping(value = { "notice/list/{fileId}" })
	@ResponseBody
	public ResultVO<JSONObject> getNoticeList(@RequestBody PageRequest request,@PathVariable("fileId") String fileId)
			throws IOException {
		try {
			JSONObject data = new JSONObject();
			List<NoticeRecordVO> frList = noticeRecordService.getNoticeRecordList(fileId,request.getPage(), request.getPageSize());
			String num = noticeRecordService.getNoticeRecordCount(fileId);
			data.put("nrList", frList);
			data.put("num", num);
			return ResultHelper.success(data);
		} catch (Exception e) {
			logger.error("获取用户阅读记录列表：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
}
