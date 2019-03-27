package com.everflourish.act.app.domain.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="declare_read_record")
public class DeclareReadRecord {
    private String id;

    private String userId;

    private String decId;

    private Date createTime;

    private String content;

    private Date modifyTime;

    private String wdNo;

    private String phone;

    private String createBy;

    private Integer usReadStatus;

    private Integer ceReadStatus;

    private String imageUrl;

    // Property accessors
    @Id
    @GeneratedValue
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getDecId() {
        return decId;
    }

    public void setDecId(String decId) {
        this.decId = decId == null ? null : decId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Column(name = "content", length = 65535, columnDefinition="TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getWdNo() {
        return wdNo;
    }

    public void setWdNo(String wdNo) {
        this.wdNo = wdNo == null ? null : wdNo.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

	public Integer getUsReadStatus() {
		return usReadStatus;
	}

	public void setUsReadStatus(Integer usReadStatus) {
		this.usReadStatus = usReadStatus;
	}

	public Integer getCeReadStatus() {
		return ceReadStatus;
	}

	public void setCeReadStatus(Integer ceReadStatus) {
		this.ceReadStatus = ceReadStatus;
	}

	public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }
}