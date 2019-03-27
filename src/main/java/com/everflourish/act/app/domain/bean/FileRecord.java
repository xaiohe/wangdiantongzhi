package com.everflourish.act.app.domain.bean;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  上传文件记录表
* @author yangming 2019-03-14
*/
@Entity
@Table(name="file_record"
)
public class FileRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 主键id
    */
    private Integer id;

    /**
    * 文件名
    */
    private String fileName;

    /**
    * 文件地址
    */
    private String filePath;

    /**
    * 上传时间
    */
    private Date createTime;


    public FileRecord() {
    }
    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}