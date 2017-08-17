package com.fengnian.smallyellowo.foodie.bean.publish;

/**
 * Created by Administrator on 2016-11-14.
 */

public class GoodFriendWantEatBean {
    private String id;
    private String nickName;
    private HeadImage headImage;
    private String personalDeclaration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public HeadImage getHeadImage() {
        return headImage;
    }

    public void setHeadImage(HeadImage headImage) {
        this.headImage = headImage;
    }

    public String getPersonalDeclaration() {
        return personalDeclaration;
    }

    public void setPersonalDeclaration(String personalDeclaration) {
        this.personalDeclaration = personalDeclaration;
    }
}
