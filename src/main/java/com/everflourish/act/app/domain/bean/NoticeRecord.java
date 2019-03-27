package com.everflourish.act.app.domain.bean;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  notice_record
* @author yangming 2019-03-14
*/
@Entity
@Table(name="notice_record"
)
public class NoticeRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 主键id
    */
    private Integer id;

    /**
    * 钉钉消息id
    */
    private String ddId;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 专管员姓名
    */
    private String name;

    /**
    * 钉钉消息内容
    */
    private String ddContent;

    /**
    * 关联的文件id
    */
    private Integer fileId;

    /**
    * 创建时间
    */
    private Date createTime;


    public NoticeRecord() {
    }
    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDdId() {
        return ddId;
    }

    public void setDdId(String ddId) {
        this.ddId = ddId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDdContent() {
        return ddContent;
    }

    public void setDdContent(String ddContent) {
        this.ddContent = ddContent;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	public String toString() {
		return "NoticeRecord [id=" + id + ", ddId=" + ddId + ", phone=" + phone + ", name=" + name + ", ddContent="
				+ ddContent + ", fileId=" + fileId + ", createTime=" + createTime + "]";
	}

}