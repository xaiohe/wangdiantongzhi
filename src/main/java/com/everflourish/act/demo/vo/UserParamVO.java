package com.everflourish.act.demo.vo;


/**
 * 请求参数VO
 * @author hzbin
 * @date 2017-6-22 上午11:13:39
 * @version 0.1.0
 */
public class UserParamVO {

	private Integer id;
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserParamVO [id=" + id + ", name=" + name + "]";
	}
}
 