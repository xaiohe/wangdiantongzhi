package com.everflourish.act.app.domain.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mess_wd"
)
public class MessWd {
    private String id;

    private String messId;

    private Integer wdNo;
    @Id
    @GeneratedValue
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId == null ? null : messId.trim();
    }

    public Integer getWdNo() {
        return wdNo;
    }

    public void setWdNo(Integer wdNo) {
        this.wdNo = wdNo;
    }
}