package com.everflourish.act.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.everflourish.act.common.vo.CommonResultVO;
import com.everflourish.act.common.vo.ResultCode;
import com.everflourish.act.demo.domain.bean.UserT;
import com.everflourish.act.demo.service.DemoServiceImpl;
import com.everflourish.act.demo.vo.UserParamVO;

/**
 * demo 控制层
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@Controller
public class DemoController {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Autowired
	private DemoServiceImpl demoServiceImpl;
	
	/**
	 * http://localhost:8080/hello
	 * @param userParam
	 * @return
	 */
	@RequestMapping("/hello")
	@ResponseBody
	public String hello(UserParamVO userParam){
		return demoServiceImpl.getUsers().toString();
	}
	
	/**
	 * 通过id查询用户信息
	 * http://localhost:8080/user?id=1
	 * @param userParam
	 * @return
	 */
	@RequestMapping("/user")
	@ResponseBody
	public UserT getUserById(UserParamVO userParam){
		return demoServiceImpl.getUserById(userParam.getId());
	}
	
	/**
	 * 通过id查询用户信息
	 * http://localhost:8080/user/1
	 * @param userId
	 * @return
	 */
	@RequestMapping("/user/{userId}")
	@ResponseBody
	public UserT getUserById2(@PathVariable Integer userId){
		return demoServiceImpl.getUserById(userId);
	}
	
	/**
	 * 通过id查询用户信息
	 * http://localhost:8080/user3/1
	 * http://localhost:8080/user3/5
	 * @param userId
	 * @return
	 */
	@RequestMapping("/user3/{userId}")
	@ResponseBody
	public CommonResultVO<UserT> getUserById3(@PathVariable Integer userId){
		try{
			return demoServiceImpl.getUserById2(userId);
		}catch (Exception e) {
			logger.error("getuserbyid error,userId={}",userId,e);
			return new CommonResultVO<UserT>(ResultCode.SYSTEM_ERROR,e.getMessage());
		}
	}
}
