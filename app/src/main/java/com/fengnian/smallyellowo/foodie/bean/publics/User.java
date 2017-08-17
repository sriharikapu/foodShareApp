package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 老接口的bean
 */

public class User  implements Parcelable {
    private  String id;
    private  String account;
    private  String cnName;
    private  String enName;
    private  String headImage;
    private  String sex;
    private  String constellation;
    private  String emailAddress;
    private  String birthday;
    private  String area;
    private  String nickname;
    private  String personalitySignature;

    private  String label;
    private  String age;
    private  String phonenumbers;
    private  String phone;
    private  String createTime;
    private  String score;
    private  String bgImage;
    private  String status;

    private  int userType ;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPersonalitySignature() {
        return personalitySignature;
    }

    public void setPersonalitySignature(String personalitySignature) {
        this.personalitySignature = personalitySignature;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhonenumbers() {
        return phonenumbers;
    }

    public void setPhonenumbers(String phonenumbers) {
        this.phonenumbers = phonenumbers;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.account);
        dest.writeString(this.cnName);
        dest.writeString(this.enName);
        dest.writeString(this.headImage);
        dest.writeString(this.sex);
        dest.writeString(this.constellation);
        dest.writeString(this.emailAddress);
        dest.writeString(this.birthday);
        dest.writeString(this.area);
        dest.writeString(this.nickname);
        dest.writeString(this.personalitySignature);
        dest.writeString(this.label);
        dest.writeString(this.age);
        dest.writeString(this.phonenumbers);
        dest.writeString(this.phone);
        dest.writeString(this.createTime);
        dest.writeString(this.score);
        dest.writeString(this.bgImage);
        dest.writeString(this.status);
        dest.writeInt(this.userType);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.account = in.readString();
        this.cnName = in.readString();
        this.enName = in.readString();
        this.headImage = in.readString();
        this.sex = in.readString();
        this.constellation = in.readString();
        this.emailAddress = in.readString();
        this.birthday = in.readString();
        this.area = in.readString();
        this.nickname = in.readString();
        this.personalitySignature = in.readString();
        this.label = in.readString();
        this.age = in.readString();
        this.phonenumbers = in.readString();
        this.phone = in.readString();
        this.createTime = in.readString();
        this.score = in.readString();
        this.bgImage = in.readString();
        this.status = in.readString();
        this.userType = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
