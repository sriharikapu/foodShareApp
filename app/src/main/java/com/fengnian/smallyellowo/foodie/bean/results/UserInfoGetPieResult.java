package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * Created by Administrator on 2016-8-19.
 */
public class UserInfoGetPieResult {
    /**
     * serverMsg : 接口请求成功
     * errorCode : 0
     * user : {"id":"0e52d561c4bcecf9797893b357132301","nickName":"brandy18","sex":1,"level":null,"tel":"18600155626","isFollowMe":false,"isByFollowMe":false,"headImage":{"id":null,"url":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg","thumbUrl":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg","height":null,"width":null},"bgImage":{"id":null,"url":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg","thumbUrl":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg","height":null,"width":null},"followCount":16,"fansCount":8,"collectionNumber":null,"personalLabel":null,"personalDeclaration":"","dynamicNumber":30,"thinkEatNumber":null,"eatedNumber":null,"inventoryNumber":null,"registTime":null,"userIntegral":null}
     */

    private String serverMsg;
    private int errorCode;
    /**
     * id : 0e52d561c4bcecf9797893b357132301
     * nickName : brandy18
     * sex : 1
     * level : null
     * tel : 18600155626
     * isFollowMe : false
     * isByFollowMe : false
     * headImage : {"id":null,"url":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg","thumbUrl":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg","height":null,"width":null}
     * bgImage : {"id":null,"url":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg","thumbUrl":"http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg","height":null,"width":null}
     * followCount : 16
     * fansCount : 8
     * collectionNumber : null
     * personalLabel : null
     * personalDeclaration :
     * dynamicNumber : 30
     * thinkEatNumber : null
     * eatedNumber : null
     * inventoryNumber : null
     * registTime : null
     * userIntegral : null
     */

    private UserBean user;

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        private String id;
        private String nickName;
        private int sex;
        private Object level;
        private String tel;
        private boolean isFollowMe;
        private boolean isByFollowMe;
        /**
         * id : null
         * url : http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg
         * thumbUrl : http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/0d40578f-e4eb-454d-bbc4-812734649816.jpg
         * height : null
         * width : null
         */

        private HeadImageBean headImage;
        /**
         * id : null
         * url : http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg
         * thumbUrl : http://ruixuesoftpicture.oss-cn-beijing.aliyuncs.com/38436e17f5864c7d88576872dbd4fcc9/2fc7ca86-77a4-4e12-a885-377eaaf56232.jpg
         * height : null
         * width : null
         */

        private BgImageBean bgImage;
        private int followCount;
        private int fansCount;
        private Object collectionNumber;
        private Object personalLabel;
        private String personalDeclaration;
        private int dynamicNumber;
        private Object thinkEatNumber;
        private Object eatedNumber;
        private Object inventoryNumber;
        private Object registTime;
        private Object userIntegral;

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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public Object getLevel() {
            return level;
        }

        public void setLevel(Object level) {
            this.level = level;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public boolean isIsFollowMe() {
            return isFollowMe;
        }

        public void setIsFollowMe(boolean isFollowMe) {
            this.isFollowMe = isFollowMe;
        }

        public boolean isIsByFollowMe() {
            return isByFollowMe;
        }

        public void setIsByFollowMe(boolean isByFollowMe) {
            this.isByFollowMe = isByFollowMe;
        }

        public HeadImageBean getHeadImage() {
            return headImage;
        }

        public void setHeadImage(HeadImageBean headImage) {
            this.headImage = headImage;
        }

        public BgImageBean getBgImage() {
            return bgImage;
        }

        public void setBgImage(BgImageBean bgImage) {
            this.bgImage = bgImage;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public Object getCollectionNumber() {
            return collectionNumber;
        }

        public void setCollectionNumber(Object collectionNumber) {
            this.collectionNumber = collectionNumber;
        }

        public Object getPersonalLabel() {
            return personalLabel;
        }

        public void setPersonalLabel(Object personalLabel) {
            this.personalLabel = personalLabel;
        }

        public String getPersonalDeclaration() {
            return personalDeclaration;
        }

        public void setPersonalDeclaration(String personalDeclaration) {
            this.personalDeclaration = personalDeclaration;
        }

        public int getDynamicNumber() {
            return dynamicNumber;
        }

        public void setDynamicNumber(int dynamicNumber) {
            this.dynamicNumber = dynamicNumber;
        }

        public Object getThinkEatNumber() {
            return thinkEatNumber;
        }

        public void setThinkEatNumber(Object thinkEatNumber) {
            this.thinkEatNumber = thinkEatNumber;
        }

        public Object getEatedNumber() {
            return eatedNumber;
        }

        public void setEatedNumber(Object eatedNumber) {
            this.eatedNumber = eatedNumber;
        }

        public Object getInventoryNumber() {
            return inventoryNumber;
        }

        public void setInventoryNumber(Object inventoryNumber) {
            this.inventoryNumber = inventoryNumber;
        }

        public Object getRegistTime() {
            return registTime;
        }

        public void setRegistTime(Object registTime) {
            this.registTime = registTime;
        }

        public Object getUserIntegral() {
            return userIntegral;
        }

        public void setUserIntegral(Object userIntegral) {
            this.userIntegral = userIntegral;
        }

        public static class HeadImageBean {
            private Object id;
            private String url;
            private String thumbUrl;
            private Object height;
            private Object width;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbUrl() {
                return thumbUrl;
            }

            public void setThumbUrl(String thumbUrl) {
                this.thumbUrl = thumbUrl;
            }

            public Object getHeight() {
                return height;
            }

            public void setHeight(Object height) {
                this.height = height;
            }

            public Object getWidth() {
                return width;
            }

            public void setWidth(Object width) {
                this.width = width;
            }
        }

        public static class BgImageBean {
            private Object id;
            private String url;
            private String thumbUrl;
            private Object height;
            private Object width;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbUrl() {
                return thumbUrl;
            }

            public void setThumbUrl(String thumbUrl) {
                this.thumbUrl = thumbUrl;
            }

            public Object getHeight() {
                return height;
            }

            public void setHeight(Object height) {
                this.height = height;
            }

            public Object getWidth() {
                return width;
            }

            public void setWidth(Object width) {
                this.width = width;
            }
        }
    }
}
