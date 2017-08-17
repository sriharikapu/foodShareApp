package com.fengnian.smallyellowo.foodie.bean;


import com.fan.framework.http.FFBaseBean;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

public class DynamiDrawer extends BaseResult {


    /**
     * serverMsg : 接口请求成功
     * buinessDetail : {"merchantUid":"11612446436687736903","merchantImage":null,"merchantName":"秦渭(酒仙桥店)","merchantDistance":null,"merchantIsWant":false,"merchantIsRelation":false,"merchantIsDa":false,"merchantKind":null,"merchantPrice":null,"merchantAddress":"将台西路酒仙桥路18号(近798艺术街)","friendShares":null,"merchantArea":null,"merchantPhone":null,"merchantPoi":{"id":null,"title":null,"address":null,"tel":null,"category":null,"type":0,"latitude":"39.96935","longitude":"116.49054","province":null,"city":null,"region":null,"adCode":null,"isCustom":null}}
     * errorCode : 0
     */

    private String serverMsg;
    private BuinessDetailBean buinessDetail;
    private int errorCode;

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public BuinessDetailBean getBuinessDetail() {
        return buinessDetail;
    }

    public void setBuinessDetail(BuinessDetailBean buinessDetail) {
        this.buinessDetail = buinessDetail;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static class BuinessDetailBean {
        /**
         * merchantUid : 11612446436687736903
         * merchantImage : null
         * merchantName : 秦渭(酒仙桥店)
         * merchantDistance : null
         * merchantIsWant : false
         * merchantIsRelation : false
         * merchantIsDa : false
         * merchantKind : null
         * merchantPrice : null
         * merchantAddress : 将台西路酒仙桥路18号(近798艺术街)
         * friendShares : null
         * merchantArea : null
         * merchantPhone : null
         * merchantPoi : {"id":null,"title":null,"address":null,"tel":null,"category":null,"type":0,"latitude":"39.96935","longitude":"116.49054","province":null,"city":null,"region":null,"adCode":null,"isCustom":null}
         */

        private String merchantUid;
        private Object merchantImage;
        private String merchantName;
        private Object merchantDistance;
        private boolean merchantIsWant;
        private boolean merchantIsRelation;
        private boolean merchantIsDa;
        private Object merchantKind;
        private Object merchantPrice;
        private String merchantAddress;
        private Object friendShares;
        private Object merchantArea;
        private Object merchantPhone;
        private MerchantPoiBean merchantPoi;

        public String getMerchantUid() {
            return merchantUid;
        }

        public void setMerchantUid(String merchantUid) {
            this.merchantUid = merchantUid;
        }

        public Object getMerchantImage() {
            return merchantImage;
        }

        public void setMerchantImage(Object merchantImage) {
            this.merchantImage = merchantImage;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public Object getMerchantDistance() {
            return merchantDistance;
        }

        public void setMerchantDistance(Object merchantDistance) {
            this.merchantDistance = merchantDistance;
        }

        public boolean isMerchantIsWant() {
            return merchantIsWant;
        }

        public void setMerchantIsWant(boolean merchantIsWant) {
            this.merchantIsWant = merchantIsWant;
        }

        public boolean isMerchantIsRelation() {
            return merchantIsRelation;
        }

        public void setMerchantIsRelation(boolean merchantIsRelation) {
            this.merchantIsRelation = merchantIsRelation;
        }

        public boolean isMerchantIsDa() {
            return merchantIsDa;
        }

        public void setMerchantIsDa(boolean merchantIsDa) {
            this.merchantIsDa = merchantIsDa;
        }

        public Object getMerchantKind() {
            return merchantKind;
        }

        public void setMerchantKind(Object merchantKind) {
            this.merchantKind = merchantKind;
        }

        public Object getMerchantPrice() {
            return merchantPrice;
        }

        public void setMerchantPrice(Object merchantPrice) {
            this.merchantPrice = merchantPrice;
        }

        public String getMerchantAddress() {
            return merchantAddress;
        }

        public void setMerchantAddress(String merchantAddress) {
            this.merchantAddress = merchantAddress;
        }

        public Object getFriendShares() {
            return friendShares;
        }

        public void setFriendShares(Object friendShares) {
            this.friendShares = friendShares;
        }

        public Object getMerchantArea() {
            return merchantArea;
        }

        public void setMerchantArea(Object merchantArea) {
            this.merchantArea = merchantArea;
        }

        public Object getMerchantPhone() {
            return merchantPhone;
        }

        public void setMerchantPhone(Object merchantPhone) {
            this.merchantPhone = merchantPhone;
        }

        public MerchantPoiBean getMerchantPoi() {
            return merchantPoi;
        }

        public void setMerchantPoi(MerchantPoiBean merchantPoi) {
            this.merchantPoi = merchantPoi;
        }

        public static class MerchantPoiBean {
            /**
             * id : null
             * title : null
             * address : null
             * tel : null
             * category : null
             * type : 0
             * latitude : 39.96935
             * longitude : 116.49054
             * province : null
             * city : null
             * region : null
             * adCode : null
             * isCustom : null
             */

            private Object id;
            private Object title;
            private Object address;
            private Object tel;
            private Object category;
            private int type;
            private String latitude;
            private String longitude;
            private Object province;
            private Object city;
            private Object region;
            private Object adCode;
            private Object isCustom;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public Object getTitle() {
                return title;
            }

            public void setTitle(Object title) {
                this.title = title;
            }

            public Object getAddress() {
                return address;
            }

            public void setAddress(Object address) {
                this.address = address;
            }

            public Object getTel() {
                return tel;
            }

            public void setTel(Object tel) {
                this.tel = tel;
            }

            public Object getCategory() {
                return category;
            }

            public void setCategory(Object category) {
                this.category = category;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public Object getProvince() {
                return province;
            }

            public void setProvince(Object province) {
                this.province = province;
            }

            public Object getCity() {
                return city;
            }

            public void setCity(Object city) {
                this.city = city;
            }

            public Object getRegion() {
                return region;
            }

            public void setRegion(Object region) {
                this.region = region;
            }

            public Object getAdCode() {
                return adCode;
            }

            public void setAdCode(Object adCode) {
                this.adCode = adCode;
            }

            public Object getIsCustom() {
                return isCustom;
            }

            public void setIsCustom(Object isCustom) {
                this.isCustom = isCustom;
            }
        }
    }
}


