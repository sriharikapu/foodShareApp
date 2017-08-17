package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fan.framework.base.XData;

import java.util.ArrayList;
import java.util.List;


public class SYUser extends XData {
    private String nickName;// 用户昵称 否
    private int sex;// 用户性别 0男 1女 否
    private int level;// 用户等级 否
    private String tel;// 电话号码 否
    private boolean isFollowMe;// 是否关注我 否
    private boolean isByFollowMe;// 是否被我关注 否
    private SYImage headImage;// 用户头像 否
    private SYImage bgImage;// 背景 否
    private int followCount;// 关注个数 否
    private int fansCount;// 粉丝个数 否
    private int collectionNumber;// 收藏数 否
    private List<String> personalLabel;// 个人标签 否
    private String personalDeclaration;// 个人宣言 否
    private int dynamicNumber;// 动态数 否
    private int thinkEatNumber;// 想吃数 否
    private int eatedNumber;// 已经吃的数 否
    private int inventoryNumber;// 清单数 否
    private String registTime;// 注册时间（YYYY-MM-dd） 否


    private String token;//新接口添加


    private int userType;//0是普通   1是会员

    private String remarkName; //备注名
    private String userIntegral;// 用户积分int
    private boolean isBindWechat;//是否绑定微信号

    public boolean isBindWechat() {
        return isBindWechat;
    }

    public void setBindWechat(boolean bindWechat) {
        isBindWechat = bindWechat;
    }

    public String getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(String userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getRemarkName() {
        return remarkName;
    }


    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {

        if (getRemarkName() != null && getRemarkName().length() > 0) {
            return getRemarkName();
        }
        if (nickName == null) {
            return "";
        }
        return nickName;
    }

    public String getSearchNickName() {

        if (getRemarkName() != null && getRemarkName().length() > 0) {
            if (TextUtils.isEmpty(nickName)){
                return getRemarkName();
            } else {
                return getRemarkName() + "(" + nickName + ")";
            }

        }
        if (nickName == null) {
            return "";
        }
        return nickName;
    }

    public String getTmpNickname() {
        if (nickName == null) {
            return "";
        }
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex <= 0 ? 0 : sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getLevel() {
        return level <= 0 ? 0 : sex;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isFollowMe() {
        return isFollowMe;
    }

    public void setFollowMe(boolean isFollowMe) {
        this.isFollowMe = isFollowMe;
    }

    public boolean isByFollowMe() {
        return isByFollowMe;
    }

    public void setByFollowMe(boolean isByFollowMe) {
        this.isByFollowMe = isByFollowMe;
    }

    public SYUser(boolean isYouke) {
        setNickName("游客");

    }


    public SYImage getHeadImage() {
        if (headImage == null) {
            headImage = new SYImage();
        }
        return headImage;
    }

    public void setHeadImage(SYImage headImage) {
        this.headImage = headImage;
    }

    public SYImage getBgImage() {
        if (bgImage == null)
            return new SYImage();
        return bgImage;
    }

    public void setBgImage(SYImage bgImage) {
        this.bgImage = bgImage;
    }

    public int getFollowCount() {
        return followCount <= 0 ? 0 : followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getFansCount() {
        return fansCount <= 0 ? 0 : fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getCollectionNumber() {
        return collectionNumber <= 0 ? 0 : collectionNumber;
    }

    public void setCollectionNumber(int collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public List<String> getPersonalLabel() {
        return personalLabel;
    }

    public void setPersonalLabel(List<String> personalLabel) {
        this.personalLabel = personalLabel;
    }

    public String getPersonalDeclaration() {
        return personalDeclaration;
    }

    public void setPersonalDeclaration(String personalDeclaration) {
        this.personalDeclaration = personalDeclaration;
    }

    public int getDynamicNumber() {
        return dynamicNumber <= 0 ? 0 : dynamicNumber;
    }

    public void setDynamicNumber(int dynamicNumber) {
        this.dynamicNumber = dynamicNumber;
    }

    public int getThinkEatNumber() {
        return thinkEatNumber <= 0 ? 0 : thinkEatNumber;
    }

    public void setThinkEatNumber(int thinkEatNumber) {
        this.thinkEatNumber = thinkEatNumber;
    }

    public int getEatedNumber() {
        return eatedNumber <= 0 ? 0 : eatedNumber;
    }

    public void setEatedNumber(int eatedNumber) {
        this.eatedNumber = eatedNumber;
    }

    public int getInventoryNumber() {
        return inventoryNumber <= 0 ? 0 : eatedNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getUserintegral() {
        return userintegral;
    }

    public void setUserintegral(String userintegral) {
        this.userintegral = userintegral;
    }

    private String userintegral;// 用户积分 否

    public int getUserType() {
        return userType <= 0 ? 0 : userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public int getSex(String sex1) {
        return sex;
    }

    public int getLevel(String level1) {
        return level;
    }

    public int getFollowCount(String followCount1) {
        return followCount;
    }

    public int getFansCount(String fansCount1) {
        return fansCount;
    }

    public int getCollectionNumber(String collectionNumber1) {
        return collectionNumber;
    }

    public int getDynamicNumber(String dynamicNumber1) {
        return dynamicNumber;
    }

    public int getThinkEatNumber(String thinkEatNumber1) {
        return thinkEatNumber;
    }

    public int getInventoryNumber(String inventoryNumber1) {
        return inventoryNumber;
    }


    public static Creator<SYUser> getCREATOR() {
        return CREATOR;
    }

    public SYUser() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.nickName);
        dest.writeInt(this.sex);
        dest.writeInt(this.level);
        dest.writeString(this.tel);
        dest.writeByte(this.isFollowMe ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isByFollowMe ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.headImage, flags);
        dest.writeParcelable(this.bgImage, flags);
        dest.writeInt(this.followCount);
        dest.writeInt(this.fansCount);
        dest.writeInt(this.collectionNumber);
        dest.writeStringList(this.personalLabel);
        dest.writeString(this.personalDeclaration);
        dest.writeInt(this.dynamicNumber);
        dest.writeInt(this.thinkEatNumber);
        dest.writeInt(this.eatedNumber);
        dest.writeInt(this.inventoryNumber);
        dest.writeString(this.registTime);
        dest.writeString(this.token);
        dest.writeInt(this.userType);
        dest.writeString(this.remarkName);
        dest.writeString(this.userIntegral);
        dest.writeByte(this.isBindWechat ? (byte) 1 : (byte) 0);
        dest.writeString(this.userintegral);
    }

    protected SYUser(Parcel in) {
        super(in);
        this.nickName = in.readString();
        this.sex = in.readInt();
        this.level = in.readInt();
        this.tel = in.readString();
        this.isFollowMe = in.readByte() != 0;
        this.isByFollowMe = in.readByte() != 0;
        this.headImage = in.readParcelable(SYImage.class.getClassLoader());
        this.bgImage = in.readParcelable(SYImage.class.getClassLoader());
        this.followCount = in.readInt();
        this.fansCount = in.readInt();
        this.collectionNumber = in.readInt();
        this.personalLabel = in.createStringArrayList();
        this.personalDeclaration = in.readString();
        this.dynamicNumber = in.readInt();
        this.thinkEatNumber = in.readInt();
        this.eatedNumber = in.readInt();
        this.inventoryNumber = in.readInt();
        this.registTime = in.readString();
        this.token = in.readString();
        this.userType = in.readInt();
        this.remarkName = in.readString();
        this.userIntegral = in.readString();
        this.isBindWechat = in.readByte() != 0;
        this.userintegral = in.readString();
    }

    public static final Creator<SYUser> CREATOR = new Creator<SYUser>() {
        @Override
        public SYUser createFromParcel(Parcel source) {
            return new SYUser(source);
        }

        @Override
        public SYUser[] newArray(int size) {
            return new SYUser[size];
        }
    };


    public static final SYUser createOrUpdateWithJsonObject(SYUser user, JSONObject jsonObject) {

        if (jsonObject != null) {
            Boolean bUpdate = true;
            if (user == null) {
                user = new SYUser();
                bUpdate = false;
            }

            if (jsonObject.containsKey("id")) {
                user.setId(jsonObject.getString("id"));
            } else {
                if (!bUpdate)
                    user.setId(null);
            }


            if (jsonObject.containsKey("nickName")) {
                user.setNickName(jsonObject.getString("nickName"));
            } else {
                if (!bUpdate)
                    user.setNickName(null);
            }

            if (jsonObject.containsKey("sex")) {
                Integer sex = jsonObject.getInteger("sex");
                if (sex != null) {
                    sex = jsonObject.getIntValue("sex");
                    user.setSex(sex);
                }
            } else {
                if (!bUpdate)
                    user.setSex(-1);
            }

            if (jsonObject.containsKey("level")) {
                Integer level = jsonObject.getInteger("level");
                if (level != null) {
                    level = jsonObject.getIntValue("level");
                    user.setLevel(level);
                }
            } else {
                if (!bUpdate)
                    user.setLevel(-1);
            }

            if (jsonObject.containsKey("tel")) {
                user.setTel(jsonObject.getString("tel"));
            } else {
                if (!bUpdate)
                    user.setTel(null);
            }

            if (jsonObject.containsKey("isFollowMe")) {
                Boolean isFollowMe = jsonObject.getBoolean("isFollowMe");
                if (isFollowMe != null) {
                    isFollowMe = jsonObject.getBooleanValue("isFollowMe");
                    user.setFollowMe(isFollowMe);
                }
            } else {
//            user.setFollowMe(false);
            }

            if (jsonObject.containsKey("isByFollowMe")) {
                Boolean isByFollowMe = jsonObject.getBoolean("isByFollowMe");
                if (isByFollowMe != null) {
                    isByFollowMe = jsonObject.getBooleanValue("isByFollowMe");
                    user.setByFollowMe(isByFollowMe);
                }
            } else {
//            user.setByFollowMe(false);
            }

            if (jsonObject.containsKey("headImage")) {
                JSONObject headImageObject = jsonObject.getJSONObject("headImage");
                SYImage headImage = SYImage.createOrUpdateWithJsonObject(null, headImageObject);
                user.setHeadImage(headImage);
            } else {
                if (!bUpdate)
                    user.setHeadImage(null);
            }

            if (jsonObject.containsKey("bgImage")) {
                JSONObject bgImageObject = jsonObject.getJSONObject("bgImage");
                SYImage bgImage = SYImage.createOrUpdateWithJsonObject(null, bgImageObject);
                user.setBgImage(bgImage);
            } else {
                if (!bUpdate)
                    user.setBgImage(null);
            }

            if (jsonObject.containsKey("followCount")) {
                Integer followCount = jsonObject.getInteger("followCount");
                if (followCount != null) {
                    followCount = jsonObject.getIntValue("followCount");
                    user.setFollowCount(followCount);
                }
            } else {
                if (!bUpdate)
                    user.setFollowCount(-1);
            }

            if (jsonObject.containsKey("fansCount")) {
                Integer fansCount = jsonObject.getInteger("fansCount");
                if (fansCount != null) {
                    fansCount = jsonObject.getIntValue("fansCount");
                    user.setFansCount(fansCount);
                }
            } else {
                if (!bUpdate)
                    user.setFansCount(-1);
            }

            if (jsonObject.containsKey("collectionNumber")) {
                Integer collectionNumber = jsonObject.getInteger("collectionNumber");
                if (collectionNumber != null) {
                    collectionNumber = jsonObject.getIntValue("collectionNumber");
                    user.setCollectionNumber(collectionNumber);
                }
            } else {
                if (!bUpdate)
                    user.setCollectionNumber(-1);
            }

            ArrayList<String> personalList = new ArrayList<>();
            if (jsonObject.containsKey("personalLabel")) {
                JSONArray jsonArray = jsonObject.getJSONArray("personalLabel");
                if (jsonArray != null) {
                    for (int index = 0; index < jsonArray.size(); index++) {
                        String personal = jsonArray.getObject(index, String.class);
                        personalList.add(personal);
                    }
                }
                user.setPersonalLabel(personalList);
            } else {
                if (!bUpdate)
                    user.setPersonalLabel(null);
            }

            if (jsonObject.containsKey("personalDeclaration")) {
                user.setPersonalDeclaration(jsonObject.getString("personalDeclaration"));
            } else {
                if (!bUpdate)
                    user.setPersonalDeclaration(null);
            }

            if (jsonObject.containsKey("dynamicNumber")) {
                Integer dynamicNumber = jsonObject.getInteger("dynamicNumber");
                if (dynamicNumber != null) {
                    dynamicNumber = jsonObject.getIntValue("dynamicNumber");
                    user.setDynamicNumber(dynamicNumber);
                }
            } else {
                if (!bUpdate)
                    user.setDynamicNumber(-1);
            }

            if (jsonObject.containsKey("thinkEatNumber")) {
                Integer thinkEatNumber = jsonObject.getInteger("thinkEatNumber");
                if (thinkEatNumber != null) {
                    thinkEatNumber = jsonObject.getIntValue("thinkEatNumber");
                    user.setThinkEatNumber(thinkEatNumber);
                }
            } else {
                if (!bUpdate)
                    user.setThinkEatNumber(-1);
            }

            if (jsonObject.containsKey("eatedNumber")) {
                Integer eatedNumber = jsonObject.getInteger("eatedNumber");
                if (eatedNumber != null) {
                    eatedNumber = jsonObject.getIntValue("eatedNumber");
                    user.setEatedNumber(eatedNumber);
                }
            } else {
                if (!bUpdate)
                    user.setEatedNumber(-1);
            }

            if (jsonObject.containsKey("inventoryNumber")) {
                Integer inventoryNumber = jsonObject.getInteger("inventoryNumber");
                if (inventoryNumber != null) {
                    inventoryNumber = jsonObject.getIntValue("inventoryNumber");
                    user.setInventoryNumber(inventoryNumber);
                }
            } else {
                if (!bUpdate)
                    user.setInventoryNumber(-1);
            }

            if (jsonObject.containsKey("registTime")) {
                user.setRegistTime(jsonObject.getString("registTime"));
            } else {
                if (!bUpdate)
                    user.setRegistTime(null);
            }

            if (jsonObject.containsKey("token")) {
                user.setToken(jsonObject.getString("token"));
            } else {
                if (!bUpdate)
                    user.setToken(null);
            }

            if (jsonObject.containsKey("userType")) {
                Integer userType = jsonObject.getInteger("userType");
                if (userType != null) {
                    userType = jsonObject.getIntValue("userType");
                    user.setUserType(userType);
                }
            } else {
                if (!bUpdate)
                    user.setUserType(-1);
            }

            if (jsonObject.containsKey("remarkName")) {
                user.setRemarkName(jsonObject.getString("remarkName"));
            } else {
                if (!bUpdate)
                    user.setRemarkName(null);
            }

            if (jsonObject.containsKey("userIntegral")) {
                user.setUserIntegral(jsonObject.getString("userIntegral"));
            } else {
                if (!bUpdate)
                    user.setUserIntegral(null);
            }

            if (jsonObject.containsKey("userintegral")) {
                user.setUserintegral(jsonObject.getString("userintegral"));
            } else {
                if (!bUpdate)
                    user.setUserintegral(null);
            }

            if (jsonObject.containsKey("isBindWechat")) {
                Boolean isBindWechat = jsonObject.getBoolean("isBindWechat");
                if (isBindWechat != null) {
                    isBindWechat = jsonObject.getBooleanValue("isBindWechat");
                    user.setBindWechat(isBindWechat);
                }
            } else {
//            user.setBindWechat(false);
            }
        }

        return user;
    }
}
