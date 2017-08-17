package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFNetWorkRequest;

import java.util.List;

/**
 * Created by Administrator on 2016-9-19.
 */
public class SearchRestListResult implements FFBaseBean{
    /**
     * status : 0
     * message : query ok
     * count : 1454
     * request_id : 04900417805093ab4dfd9abd043e5ac5a799870b7894
     * data : [{"id":"11737965741698619842","title":"大鸭梨烤鸭店(劲松店)","address":"北京市朝阳区农光东里34-3天客隆商务大厦B座2楼","tel":"010-67347158","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.88411,"lng":116.469},"pano":{"heading":null,"pitch":null,"zoom":null},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"14719204421273916055","title":"大鸭梨烤鸭店(光彩店)","address":"北京市丰台区光彩路慧时欣园10号楼","tel":"010-87812615","category":"美食:中餐厅:其它中餐厅","type":0,"location":{"lat":39.84752,"lng":116.41421},"pano":{"id":"10011013150209105229500","heading":284,"pitch":0,"zoom":1},"ad_info":{"adcode":110106,"province":"北京市","city":"北京市","district":"丰台区"}},{"id":"2182560473218827971","title":"大鸭梨烤鸭店(右安门分店)","address":"北京市丰台区右安门外开阳里二街5区8号","tel":"010-63516188; 010-83545331","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.86661,"lng":116.36951},"pano":{"id":"10011049131012114757300","heading":176,"pitch":0,"zoom":1},"ad_info":{"adcode":110106,"province":"北京市","city":"北京市","district":"丰台区"}},{"id":"17729748406132126682","title":"正院大宅门(首体旗舰店)","address":"北京市海淀区首体南路甲20号","tel":"010-88356689;010-88356687","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.93362,"lng":116.32666},"pano":{"id":"10011031131214112919300","heading":58,"pitch":0,"zoom":1},"ad_info":{"adcode":110108,"province":"北京市","city":"北京市","district":"海淀区"}},{"id":"10479963409116202795","title":"大槐树","address":"北京市东城区美术馆东街甲23","tel":"010-64008891","category":"美食:烧烤","type":0,"location":{"lat":39.92548,"lng":116.41041},"pano":{"heading":null,"pitch":null,"zoom":null},"ad_info":{"adcode":110101,"province":"北京市","city":"北京市","district":"东城区"}},{"id":"6999013119979630725","title":"大鸭梨烤鸭店(旧宫店)","address":"北京市大兴区旧宫镇商业街61号","tel":"010-67951760","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.80444,"lng":116.43401},"pano":{"id":"10011013150206130921500","heading":349,"pitch":-2,"zoom":1},"ad_info":{"adcode":110115,"province":"北京市","city":"北京市","district":"大兴区"}},{"id":"9400052689641207130","title":"大鸭梨烤鸭店(百子湾店)","address":"北京市朝阳区百子湾路甲16号国锐易购空间1号楼(后现代城北京东站苹果社区九龙山)","tel":"010-87745755","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.9001,"lng":116.48089},"pano":{"id":"10011049131104155547800","heading":177,"pitch":0,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"11057004081616339938","title":"大鸭梨烤鸭(六里桥店)","address":"北京市丰台区西客站南路东局乙8号","tel":"010-63288833","category":"美食:中餐厅:其它中餐厅","type":0,"location":{"lat":39.87466,"lng":116.32066},"pano":{"id":"10011039131104150524700","heading":272,"pitch":-6,"zoom":1},"ad_info":{"adcode":110106,"province":"北京市","city":"北京市","district":"丰台区"}},{"id":"7917046910457822667","title":"正院大宅门(大戏院店)","address":"北京市海淀区西翠路11号","tel":"010-68222256;010-68222953","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.91841,"lng":116.28313},"pano":{"heading":null,"pitch":null,"zoom":null},"ad_info":{"adcode":110108,"province":"北京市","city":"北京市","district":"海淀区"}},{"id":"5479759376463259685","title":"正院大宅门(亚运村总店)","address":"北京市朝阳区惠新北里3号楼","tel":"010-64952166;010-64950018","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.98382,"lng":116.42021},"pano":{"id":"10011026131021150250200","heading":319,"pitch":0,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"690834880557034560","title":"大鸭梨烤鸭店(小营东路店)","address":"北京市朝阳区小营东路甲5号","tel":"010-84368099","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.99384,"lng":116.42744},"pano":{"heading":null,"pitch":null,"zoom":null},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"16914599958782147844","title":"大鸭梨烤鸭店(望京店)","address":"北京市朝阳区花家地东路5号","tel":"010-64716033","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.98251,"lng":116.47715},"pano":{"id":"10011039140429104139100","heading":308,"pitch":0,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"10436564876132344426","title":"大鸭梨烤鸭店(亚运村店)","address":"北京市朝阳区大屯路甲2号院南沙滩小区37号楼1607室","tel":"010-64861646","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":40.00143,"lng":116.37702},"pano":{"id":"10011118120913150328700","heading":165,"pitch":-9,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"7638560008990388879","title":"正院大宅门(团结湖店)","address":"北京市朝阳区甜水园路甲1号","tel":"010-65925866","category":"美食:中餐厅:北京菜","type":0,"location":{"lat":39.92586,"lng":116.47856},"pano":{"id":"10011039131118120050000","heading":85,"pitch":0,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}},{"id":"17464936090310194283","title":"大渔铁板烧(蓝色港湾店)","address":"北京市朝阳区朝阳公园路6号蓝色港湾2栋1层LW-23号","tel":"010-59056235;010-59056237","category":"美食:日韩菜:日本料理","type":0,"location":{"lat":39.95001,"lng":116.4778},"pano":{"id":"10011039131118114140400","heading":230,"pitch":1,"zoom":1},"ad_info":{"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}}]
     * region : {"title":"北京市"}
     */
    private int status;
    private String message;
    private int count;
    private String request_id;
    /**
     * title : 北京市
     */

    private RegionBean region;
    /**
     * id : 11737965741698619842
     * title : 大鸭梨烤鸭店(劲松店)
     * address : 北京市朝阳区农光东里34-3天客隆商务大厦B座2楼
     * tel : 010-67347158
     * category : 美食:中餐厅:北京菜
     * type : 0
     * location : {"lat":39.88411,"lng":116.469}
     * pano : {"heading":null,"pitch":null,"zoom":null}
     * ad_info : {"adcode":110105,"province":"北京市","city":"北京市","district":"朝阳区"}
     */

    private List<RestBean> data;

    @Override
    public boolean judge() {
        return status == 0;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public boolean isConsum(FFNetWorkRequest request) {
        return false;
    }

    @Override
    public boolean isNoData() {
        return false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public RegionBean getRegion() {
        return region;
    }

    public void setRegion(RegionBean region) {
        this.region = region;
    }

    public List<RestBean> getData() {
        return data;
    }

    public void setData(List<RestBean> data) {
        this.data = data;
    }


    public static class RegionBean {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class RestBean {
        private String id;
        private String title;

        public int getIsCustom() {
            return isCustom;
        }

        public void setIsCustom(int isCustom) {
            this.isCustom = isCustom;
        }

        private int isCustom = 0;
        private String address;
        private String tel;
        private String category;
        private int type;
        /**
         * lat : 39.88411
         * lng : 116.469
         */

        private LocationBean location;
        /**
         * heading : null
         * pitch : null
         * zoom : null
         */
        private PanoBean pano;
        /**
         * adcode : 110105
         * province : 北京市
         * city : 北京市
         * district : 朝阳区
         */

        private AdInfoBean ad_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public PanoBean getPano() {
            return pano;
        }

        public void setPano(PanoBean pano) {
            this.pano = pano;
        }

        public AdInfoBean getAd_info() {
            return ad_info;
        }

        public void setAd_info(AdInfoBean ad_info) {
            this.ad_info = ad_info;
        }

        public static class LocationBean {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        public static class PanoBean {
            private Object heading;
            private Object pitch;
            private Object zoom;

            public Object getHeading() {
                return heading;
            }

            public void setHeading(Object heading) {
                this.heading = heading;
            }

            public Object getPitch() {
                return pitch;
            }

            public void setPitch(Object pitch) {
                this.pitch = pitch;
            }

            public Object getZoom() {
                return zoom;
            }

            public void setZoom(Object zoom) {
                this.zoom = zoom;
            }
        }

        public static class AdInfoBean {
            private int adcode;
            private String province;
            private String city;
            private String district;

            public int getAdcode() {
                return adcode;
            }

            public void setAdcode(int adcode) {
                this.adcode = adcode;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }
        }
    }
}
