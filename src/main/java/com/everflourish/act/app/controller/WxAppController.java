package com.everflourish.act.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.fastjson.JSONObject;
import com.everflourish.act.app.domain.bean.DeclareReadRecord;
import com.everflourish.act.app.domain.bean.DeclareRecord;
import com.everflourish.act.app.domain.bean.MessageRecord;
import com.everflourish.act.app.domain.bean.ReadRecord;
import com.everflourish.act.app.domain.bean.TWd;
import com.everflourish.act.app.domain.bean.Users;
import com.everflourish.act.app.service.AuthorService;
import com.everflourish.act.app.service.DeclareReadRecordService;
import com.everflourish.act.app.service.DeclareRecordService;
import com.everflourish.act.app.service.MessageRecordService;
import com.everflourish.act.app.service.ReadRecordService;
import com.everflourish.act.app.service.TWdService;
import com.everflourish.act.app.service.UsersService;
import com.everflourish.act.app.util.ExceptionUtil;
import com.everflourish.act.common.vo.ResultCode;
import com.everflourish.act.common.vo.ResultHelper;
import com.everflourish.act.common.vo.ResultVO;
import com.sun.jmx.snmp.Timestamp;
import com.vdurmont.emoji.EmojiParser;

@Controller
@RequestMapping("wxapp/")
public class WxAppController {
	private static final Logger logger = LoggerFactory.getLogger(WxAppController.class);
	@Autowired
	UsersService usersService;
	@Autowired
	TWdService tWdService;
	@Autowired
	MessageRecordService messageRecordService;
	@Autowired
	ReadRecordService readRecordService;
	@Autowired
	AuthorService authorService;
	@Autowired
	DeclareRecordService declareRecordService;
	@Autowired
	DeclareReadRecordService declareReadRecordService;
	@Value("${image.address}")
	private String imageAddress;
	@Value("${image.url}")
	private String imgUrl;

	/**
	 * 1.查询用户信息
	 */
	@RequestMapping(value = "get/userInfo/{code}", method = RequestMethod.GET)
	@ResponseBody
	public ResultVO<Users> getUserInfo(HttpServletRequest req, @PathVariable("code") String code) {
		try {
			String fromUserId = authorService.getOppenId(req, code);
			if (fromUserId == null) {
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			Users u = usersService.getUsersByFromUserName(fromUserId);
			if (u == null) {
				u = new Users();
				//进行编码
				u.setFromUserName(fromUserId);
				usersService.addUsers(u);
			}
			logger.info("获取用户成功：" + code + "----" + fromUserId);
			if(u.getNickName() != null) {
				//表情解码
				u.setNickName(EmojiParser.parseToUnicode(u.getNickName()));
			}
			return ResultHelper.success(u, req.getSession().getId());
		} catch (Exception e) {
			logger.error("获取用户：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.USER_NOT_FOUND, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 3.用户消息列表
	 */
	@RequestMapping(value = "get/mess/list/{page}", method = RequestMethod.GET)
	@ResponseBody
	public ResultVO<JSONObject> getMessRecord(HttpServletRequest req, @PathVariable("page") Integer page) {
		try {

			if (page == null || page == 0 || page < 0) {
				logger.info("用户消息列表参数为空：" + page);
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}

			String fromUserId = authorService.getOppenId(req, null);
			if (fromUserId == null) {
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			Users u = usersService.getUsersByFromUserName(fromUserId);
			if (u == null) {
				logger.info("用户阅读状态未找到用户：" + fromUserId);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			List<Map<String, Object>> mrList = messageRecordService.getMessageRecordList(u.getWdNo(), page);
			String count = messageRecordService.getUserMessageRecordCount();
			JSONObject json = new JSONObject();
			json.put("mrList", mrList);
			json.put("count", count);
			return ResultHelper.success(json);
		} catch (Exception e) {
			logger.error("获取消息列表：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 4.消息详情
	 */
	@RequestMapping(value = "get/mess/info/{messId}", method = RequestMethod.GET)
	@ResponseBody
	public ResultVO<JSONObject> getMessRecordInfo(HttpServletRequest req, @PathVariable("messId") String messId) {
		try {
			if (messId == null) {
				logger.info("消息详情参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			} else {
				String fromUserId = authorService.getOppenId(req, null);
				if (fromUserId == null) {
					return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
				}
				Users u = usersService.getUsersByFromUserName(fromUserId);
				if (u == null || u.getWdNo() == null) {
					logger.info("消息详情未找到用户：" + fromUserId);
					return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
				}
				MessageRecord mr = messageRecordService.getMessageRecordById(messId);
				ReadRecord rr = readRecordService.getReadRecordListByMessId(messId, u.getWdNo());
				JSONObject json = new JSONObject();
				json.put("mr", mr);
				json.put("rr", rr);
				return ResultHelper.success(json);
			}
		} catch (Exception e) {
			logger.error("获取消息详情：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 5.用户阅读状态
	 */
	@RequestMapping(value = "add/read/record", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addReadRecordInfo(HttpServletRequest req, @RequestBody JSONObject request) {
		try {
			String messId = request.getString("messId");
			if (messId == null || messId.length() == 0) {
				logger.info("用户阅读状态参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("用户阅读状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null || u.getWdNo() == null) {
				logger.info("用户阅读状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}

			// 校验是否已读
			ReadRecord rr = readRecordService.getReadRecordListByMessId(messId, u.getWdNo());
			if (rr != null) {
				logger.info("用户已读：" + u.getWdNo());
				return ResultHelper.success(req.getSession().getId());
			} else {
				readRecordService.addReadRecord(u, messId);
				return ResultHelper.success(req.getSession().getId());
			}
		} catch (Exception e) {
			logger.error("用户阅读状态：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 6.上传文件（评论）
	 */
	@RequestMapping(value = "update/mess/record", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> updateReadRecordInfo(HttpServletRequest req, @RequestBody JSONObject request) {
		try {
			ReadRecord rr = JSONObject.parseObject(request.toJSONString(), ReadRecord.class);
			String messId = request.getString("messId");
			String wdNo = request.getString("wdNo");
			String phone = request.getString("phone");
			if (messId == null || wdNo == null || phone == null) {
				logger.info("上传文件（评论）参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}

			// 校验网点编号手机号
			TWd twd = tWdService.getTWdByStoreCode(wdNo, phone);
			if (twd == null) {
				logger.info("上传文件（评论）网点未找到：" + wdNo);
				return ResultHelper.fail(ResultCode.WD_NOT_FOUND);
			}

			// 获取微信ID

			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("上传文件（评论）未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null) {
				logger.info("上传文件（评论）未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}

			rr.setUserId(u.getId());
			readRecordService.updateReadRecord(rr);
			return ResultHelper.success(req.getSession().getId());
		} catch (Exception e) {
			logger.error("上传文件（评论）：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 7.更新用户信息（网点，手机号）
	 */
	@RequestMapping(value = "update/user", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> updateUser(HttpServletRequest req, @RequestBody JSONObject request) {
		try {
			Users user = JSONObject.parseObject(request.toJSONString(), Users.class);
			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("更新用户信息（网点，手机号）未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null) {
				logger.info("更新用户信息（网点，手机号）未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}

			TWd twd = tWdService.getTWdByStoreCode(user.getWdNo(), user.getPhone());
			if (twd == null) {
				logger.info("更新用户信息网点未找到：" + user.getWdNo());
				return ResultHelper.fail(ResultCode.WD_NOT_FOUND);
			}
			user.setId(u.getId());
	        user.setNickName(EmojiParser.parseToHtmlDecimal(user.getNickName()));
	        usersService.updateUsers(user);
			return ResultHelper.success(req.getSession().getId());
		} catch (Exception e) {
			logger.error("更新用户信息（网点，手机号）：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR, ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 8.上传图片
	 */
	@RequestMapping(value = { "upload/image" })
	@ResponseBody
	public ResultVO<JSONObject> uploadFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request)
			throws IOException {
		try {
			String fromUserName = authorService.getOppenId(request, null);
			if (fromUserName == null) {
				logger.info("上传图片未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null) {
				logger.info("上传图片未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			String imageUrl = imgUrl;
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					MultipartFile file = files[i];
					if (file != null && !file.isEmpty() && file.getSize() > 0) {
						File filepath = new File(imageAddress);
						if (!filepath.exists())
							filepath.mkdirs();
						Long now = System.currentTimeMillis();
						// 文件保存路径
						String savePath = imageAddress + u.getWdNo() + "-" + now + ".jpg";
						// 转存文件
						file.transferTo(new File(savePath));
						imageUrl = imageUrl + u.getWdNo() + "-" + now + ".jpg";
					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("imageUrl", imageUrl);
			// 解码图片保存到服务器
			return ResultHelper.success(json,request.getSession().getId());
		} catch (Exception e) {
			logger.error("上传图片：系统异常", e);
			e.getStackTrace();
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 8.根据网点加载申报记录
	 */
	@RequestMapping(value = "get/dec/{page}", method = RequestMethod.GET)
	@ResponseBody
	public ResultVO<JSONObject> getDeclareRecord(HttpServletRequest req, @PathVariable("page")Integer page) {
		try {
			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("根据网点加载申报记录未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null || u.getWdNo() == null) {
				logger.info("根据网点加载申报记录未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			//默认第一页
			if(page == null) {
				page = 1;
			}
			
			JSONObject json = new JSONObject();
			List<Map<String, Object>> drList = declareRecordService.getDeclareRecord(u.getWdNo(), page);
			Integer count = declareRecordService.getTotalCount(u.getWdNo());
			json.put("drList", drList);
			json.put("count", count);
			return ResultHelper.success(json,req.getSession().getId());
		}catch(Exception e) {
			logger.error("根据网点加载申报记录：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 9.根据申报记录ID加载聊天记录
	 */
	@RequestMapping(value = "get/dec/read", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<JSONObject> getDeclareReadRecord(@RequestBody JSONObject request){
		try {
			Integer page = request.getInteger("page");
			String decId = request.getString("decId");
			if( page == null || decId == null) {
				logger.info("根据申报记录ID加载聊天记录参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			JSONObject json = new JSONObject();
			List<Map<String, Object>> drrList = declareReadRecordService.getDeclareReadRecord(decId, page);
			Integer count = declareReadRecordService.getTotalCount(decId);
			json.put("drrList", drrList);
			json.put("count", count);
			return ResultHelper.success(json);
		}catch(Exception e) {
			logger.error("根据申报记录ID加载聊天记录：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 10.更新聊天记录用户状态
	 */
	@RequestMapping(value = "update/dec/read", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> updateDecRead(@RequestBody JSONObject request,HttpServletRequest req) {
		try {
			
			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("更新聊天记录用户状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null || u.getWdNo() == null) {
				logger.info("更新聊天记录用户状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			String decId = request.getString("decId");
			if( decId == null) {
				logger.info("更新聊天记录用户状态参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			declareReadRecordService.updateDeclareReadRecordUReadStatus(decId,u.getWdNo());
			return ResultHelper.success();
		}catch(Exception e) {
			logger.error("更新聊天记录用户状态：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 11.更新申报记录状态
	 */
	@RequestMapping(value = "update/dec", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> updateDec(@RequestBody JSONObject request,HttpServletRequest req) {
		try {
			
			String fromUserName = authorService.getOppenId(req, null);
			if (fromUserName == null) {
				logger.info("更新申报记录状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			// 查询用户Id
			Users u = usersService.getUsersByFromUserName(fromUserName);
			if (u == null || u.getWdNo() == null) {
				logger.info("更新申报记录状态未找到用户：" + fromUserName);
				return ResultHelper.fail(ResultCode.USER_NOT_FOUND);
			}
			
			String decId = request.getString("decId");
			if( decId == null) {
				logger.info("更新申报记录状态参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			declareRecordService.uodateDecStatus(decId,u.getWdNo());
			return ResultHelper.success();
		}catch(Exception e) {
			logger.error("更新申报记录状态：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 12.添加申报信息
	 */
	@RequestMapping(value = "add/dec", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addDec(@RequestBody JSONObject request) {
		try {
			DeclareRecord dr = JSONObject.parseObject(request.getJSONObject("dr").toJSONString(), DeclareRecord.class);
			DeclareReadRecord drr = JSONObject.parseObject(request.getJSONObject("drr").toJSONString(), DeclareReadRecord.class);
			if( dr == null || drr == null) {
				logger.info("添加申报信息参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			UUID uuid = UUID.randomUUID();
			String drid = uuid.toString().replace("-", "");
			dr.setCreateTime(new Date());
			dr.setId(drid);
			String drrid = uuid.toString().replace("-", "");
			drr.setCreateTime(new Date());
			drr.setId(drrid);
			drr.setDecId(drid);
			declareRecordService.addDec(dr,drr);
			return ResultHelper.success();
		}catch(Exception e) {
			logger.error("添加申报信息：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 13.添加聊天记录
	 */
	@RequestMapping(value = "add/dec/read", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public ResultVO<Object> addDecRead(@RequestBody JSONObject request) {
		try {
			DeclareReadRecord record = JSONObject.parseObject(request.toJSONString(), DeclareReadRecord.class);
			if( record == null) {
				logger.info("添加聊天记录参数为空");
				return ResultHelper.fail(ResultCode.PARAMS_IS_EMPTY);
			}
			UUID uuid = UUID.randomUUID();
			String drid = uuid.toString().replace("-", "");
			record.setId(drid);
			record.setCreateTime(new Date());
			declareReadRecordService.addDeclareReadRecord(record);
			return ResultHelper.success();
		}catch(Exception e) {
			logger.error("添加聊天记录：系统异常", e);
			return ResultHelper.fail(ResultCode.SYSTEM_ERROR,ExceptionUtil.getStackTrace(e));
		}
	}
}
