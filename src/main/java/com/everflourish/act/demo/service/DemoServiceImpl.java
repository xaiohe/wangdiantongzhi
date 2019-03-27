package com.everflourish.act.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everflourish.act.common.vo.CommonResultVO;
import com.everflourish.act.common.vo.ResultCode;
import com.everflourish.act.demo.domain.UserTRepository;
import com.everflourish.act.demo.domain.bean.UserT;

/**
 * demo 服务层
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@Service
public class DemoServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

	@Autowired
	private UserTRepository userTRepository;
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	public List<UserT> getUsers(){
		return userTRepository.findAll();
	}
	
	/**
	 * 通过id获取用户信息
	 * @param id
	 * @return 
	 */
	public UserT getUserById(Integer id){
		return userTRepository.findOne(id);
	}
	
	/**
	 * 通过id获取用户信息
	 * @param id
	 * @return
	 */
	public CommonResultVO<UserT> getUserById2(Integer id){
		
		//逻辑1
//		CommonResultVO<UserT> result = new CommonResultVO<UserT>();
//		UserT user = userTRepository.findOne(id);
//		if(user == null){
//			logger.debug("user not found userid={}",id);
//			result = new CommonResultVO<UserT>(ResultCode.USER_NOT_FOUND,"user not found");
//		}else{
//			result.setResultData(user);
//		}
//		return result;
		
		//逻辑2
		UserT user = userTRepository.findOne(id);
		if(user == null){
			logger.warn("user not found userid={}",id);
			return new CommonResultVO<UserT>(ResultCode.USER_NOT_FOUND,"user not found");
		}else{
			return new CommonResultVO<UserT>(user);
		}
	}
}
