package com.fengnian.smallyellowo.foodie.bean.publics;

/**
 * Created by Administrator on 2016-9-19.
 */

public class SendVerificationCodeBean {

    private  String  id ;
    private  String  phoneNumber ;
    private  String  validateCode ;
    private  String  maxTime ;
    private  String  createTime ;
    private  int  type ;
    private  int  validatetype ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValidatetype() {
        return validatetype;
    }

    public void setValidatetype(int validatetype) {
        this.validatetype = validatetype;
    }
}
