package com.everflourish.act.app.domain.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;


/**
 * TWd entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_wd"
    ,catalog="test"
)

public class TWd  implements java.io.Serializable {


    // Fields    

     private String wdId;
     private String title;
     private String mcId;
     private String mcName;
     private String czId;
     private String czName;
     private String status;
     private String area;
     private String stype;
     private String OName;
     private String PAddress;
     private String gradeId;
     private String gradeName;
     private String OPhone;
     private String mobilePhone;
     private String PPhone;
     private String led;
     private String idCard;
     private String password;
     private Date beginDate;
     private Date endDate;
     private String serviceArea;
     private Integer starId;
     private String starName;
     private String storeCode;
     private String websiteType;
     private Integer originWdId;


    // Constructors

    /** default constructor */
    public TWd() {
    }

    
    /** full constructor */
    public TWd(String title, String mcId, String mcName, String czId, String czName, String status, String area, String stype, String OName, String PAddress, String gradeId, String gradeName, String OPhone, String mobilePhone, String PPhone, String led, String idCard, String password, Date beginDate, Date endDate, String serviceArea, Integer starId, String starName, String storeCode, String websiteType, Integer originWdId) {
        this.title = title;
        this.mcId = mcId;
        this.mcName = mcName;
        this.czId = czId;
        this.czName = czName;
        this.status = status;
        this.area = area;
        this.stype = stype;
        this.OName = OName;
        this.PAddress = PAddress;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.OPhone = OPhone;
        this.mobilePhone = mobilePhone;
        this.PPhone = PPhone;
        this.led = led;
        this.idCard = idCard;
        this.password = password;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.serviceArea = serviceArea;
        this.starId = starId;
        this.starName = starName;
        this.storeCode = storeCode;
        this.websiteType = websiteType;
        this.originWdId = originWdId;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    public String getWdId() {
		return wdId;
	}

	public void setWdId(String wdId) {
		this.wdId = wdId;
	}
    
    public String getTitle() {
        return this.title;
    }
    

	public void setTitle(String title) {
        this.title = title;
    }
    
    @Column(name="mc_id", length=2)

    public String getMcId() {
        return this.mcId;
    }
    
    public void setMcId(String mcId) {
        this.mcId = mcId;
    }
    
    @Column(name="mc_name", length=20)

    public String getMcName() {
        return this.mcName;
    }
    
    public void setMcName(String mcName) {
        this.mcName = mcName;
    }
    
    @Column(name="cz_id", length=4)

    public String getCzId() {
        return this.czId;
    }
    
    public void setCzId(String czId) {
        this.czId = czId;
    }
    
    @Column(name="cz_name", length=60)

    public String getCzName() {
        return this.czName;
    }
    
    public void setCzName(String czName) {
        this.czName = czName;
    }
    
    @Column(name="status", length=20)

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="area", length=30)

    public String getArea() {
        return this.area;
    }
    
    public void setArea(String area) {
        this.area = area;
    }
    
    @Column(name="stype", length=50)

    public String getStype() {
        return this.stype;
    }
    
    public void setStype(String stype) {
        this.stype = stype;
    }
    
    @Column(name="o_name", length=38)

    public String getOName() {
        return this.OName;
    }
    
    public void setOName(String OName) {
        this.OName = OName;
    }
    
    @Column(name="p_address", length=100)

    public String getPAddress() {
        return this.PAddress;
    }
    
    public void setPAddress(String PAddress) {
        this.PAddress = PAddress;
    }
    
    @Column(name="grade_id", length=50)

    public String getGradeId() {
        return this.gradeId;
    }
    
    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }
    
    @Column(name="grade_name", length=50)

    public String getGradeName() {
        return this.gradeName;
    }
    
    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    
    @Column(name="o_phone", length=50)

    public String getOPhone() {
        return this.OPhone;
    }
    
    public void setOPhone(String OPhone) {
        this.OPhone = OPhone;
    }
    
    @Column(name="mobile_phone", length=50)

    public String getMobilePhone() {
        return this.mobilePhone;
    }
    
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    
    @Column(name="p_phone", length=50)

    public String getPPhone() {
        return this.PPhone;
    }
    
    public void setPPhone(String PPhone) {
        this.PPhone = PPhone;
    }
    
    @Column(name="led", length=50)

    public String getLed() {
        return this.led;
    }
    
    public void setLed(String led) {
        this.led = led;
    }
    
    @Column(name="id_card", length=38)

    public String getIdCard() {
        return this.idCard;
    }
    
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    
    @Column(name="password", length=80)

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="begin_date", length=10)

    public Date getBeginDate() {
        return this.beginDate;
    }
    
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="end_date", length=10)

    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @Column(name="service_area", length=50)

    public String getServiceArea() {
        return this.serviceArea;
    }
    
    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }
    
    @Column(name="star_id")

    public Integer getStarId() {
        return this.starId;
    }
    
    public void setStarId(Integer starId) {
        this.starId = starId;
    }
    
    @Column(name="star_name", length=30)

    public String getStarName() {
        return this.starName;
    }
    
    public void setStarName(String starName) {
        this.starName = starName;
    }
    
    @Column(name="store_code", length=50)

    public String getStoreCode() {
        return this.storeCode;
    }
    
    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
    
    @Column(name="website_type", length=30)

    public String getWebsiteType() {
        return this.websiteType;
    }
    
    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }
    
    @Column(name="origin_wd_id")

    public Integer getOriginWdId() {
        return this.originWdId;
    }
    
    public void setOriginWdId(Integer originWdId) {
        this.originWdId = originWdId;
    }
   








}