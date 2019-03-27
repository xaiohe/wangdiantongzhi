package com.everflourish.act.demo.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everflourish.act.BaseTest;


/**
 *
 * @author hzbin
 * @date 2017-6-22 上午11:30:10
 * @version 0.1.0
 */
public class DemoServiceImplTest extends BaseTest {

	@Autowired
	private DemoServiceImpl demoServiceImpl;
	
	@Test
	public void testGetUsers() {
		System.out.println(demoServiceImpl.getUsers());
	}

}
 