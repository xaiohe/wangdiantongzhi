package com.everflourish.act.app.domain.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * MessageRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="message_record"
)

public class MessageRecord  implements java.io.Serializable {


    // Fields    

     private String id;
     private String content;
     private Timestamp createTime;
     private Timestamp modifyTime;
     private String createBy;
     private String title;
     private Integer type;
     private Integer status;
     private Timestamp startTime;
     private Timestamp endTime;

    // Constructors

    /** default constructor */
    public MessageRecord() {
    }

    
    /** full constructor */
    public MessageRecord(String content, Timestamp createTime, Timestamp modifyTime, String createBy, String title, Integer type) {
        this.content = content;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.createBy = createBy;
        this.title = title;
        this.type = type;
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

	@Column(name = "content", length = 65535, columnDefinition="TEXT")
	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
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


	public String getCreateBy() {
		return createBy;
	}


	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Timestamp getStartTime() {
		return startTime;
	}


	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}