package com.everflourish.act.app.domain.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * ReadRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="read_record")

public class ReadRecord  implements java.io.Serializable {


    // Fields    

     private String id;
     private String userId;
     private String messId;
     private Timestamp createTime;
     private String content;
     private String imageUrl;
     private Timestamp modifyTime;
     private String wdNo;
     private String phone;

    // Constructors

    /** default constructor */
    public ReadRecord() {
    }

    
    /** full constructor */
    public ReadRecord(String userId, String messId, Timestamp createTime, String content, String imageUrl, Timestamp modifyTime) {
        this.userId = userId;
        this.messId = messId;
        this.createTime = createTime;
        this.content = content;
        this.imageUrl = imageUrl;
        this.modifyTime = modifyTime;
    }


    
    @Id
    @GeneratedValue
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getMessId() {
		return messId;
	}


	public void setMessId(String messId) {
		this.messId = messId;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "image_url", length = 65535, columnDefinition="TEXT")
	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Timestamp getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
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
    
}