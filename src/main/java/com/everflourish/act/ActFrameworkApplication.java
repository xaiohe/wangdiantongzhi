package com.everflourish.act;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * spring 启动类
 * 兼servlet初始化类，用于兼容代码，使程序可使用外部tomcat
 * @author hzbin
 * @date 2017-6-22 上午11:09:06
 * @version 0.1.0
 */
@SpringBootApplication
@MapperScan("com.everflourish.act.app.domain.dao")
public class ActFrameworkApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ActFrameworkApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ActFrameworkApplication.class);
	}
}
