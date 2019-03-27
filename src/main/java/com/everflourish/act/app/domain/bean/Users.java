package com.everflourish.act.app.domain.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="users")

public class Users  implements java.io.Serializable {


    // Fields    

     private String id;
     private String fromUserName;
     private String nickName;
     private String wdNo;
     private String phone;
     private Timestamp createTime;
     private Timestamp modifyTime;
     private String headImage;

    // Constructors

    /** default constructor */
    public Users() {
    }

	/** minimal constructor */
    public Users(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    
    /** full constructor */
    public Users(String fromUserName, String nickName, String wdNo, String phone, Timestamp createTime, Timestamp modifyTime) {
        this.fromUserName = fromUserName;
        this.nickName = nickName;
        this.wdNo = wdNo;
        this.phone = phone;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
    // Property accessors
    @Id 
    @GeneratedValue
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWdNo() {
		return wdNo;
	}

	public void setWdNo(String wdNo) {
		this.wdNo = wdNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
}