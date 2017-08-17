package com.fengnian.smallyellowo.foodie.bean;

import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by elaine on 2017/7/21.
 */

public class CityAreaBean extends BaseResult {

    private AreaEntity area;
    private CityInfoEntity cityInfo;
    private List<FoodKindEntity> categories; // 品类

    public AreaEntity getArea() {
        return area;
    }

    public void setArea(AreaEntity area) {
        this.area = area;
    }

    public CityInfoEntity getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoEntity cityInfo) {
        this.cityInfo = cityInfo;
    }

    public List<FoodKindEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<FoodKindEntity> categories) {
        this.categories = categories;
    }

    public static class AreaEntity {
        private List<ListEntity> list;

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity {
            /**
             * areaList : [{"id":286,"level":0,"superAreas":null,"superId":0,"areaName":"九眼桥","createTime":null,"imgPath":"http://smallyellowotest.tinydonuts.cn/h5pic/business/three.png"},{"id":302,"level":0,"superAreas":null,"superId":0,"areaName":"人民公园","createTime":null,"imgPath":"http://smallyellowotest.tinydonuts.cn/h5pic/business/three.png"}]
             * areaName : 热门商圈
             * id : 0
             */

            private String areaName;
            private int id;
            private List<AreaListEntity> areaList;

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public List<AreaListEntity> getAreaList() {
                return areaList;
            }

            public void setAreaList(List<AreaListEntity> areaList) {
                this.areaList = areaList;
            }

            public static class AreaListEntity {
                /**
                 * id : 286
                 * level : 0
                 * superAreas : null
                 * superId : 0
                 * areaName : 九眼桥
                 * createTime : null
                 * imgPath : http://smallyellowotest.tinydonuts.cn/h5pic/business/three.png
                 */

                private String id;
                private String level;
                private Object superAreas;
                private String superId;
                private String areaName;
                private Object createTime;
                private String imgPath;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public Object getSuperAreas() {
                    return superAreas;
                }

                public void setSuperAreas(Object superAreas) {
                    this.superAreas = superAreas;
                }

                public String getSuperId() {
                    return superId;
                }

                public void setSuperId(String superId) {
                    this.superId = superId;
                }

                public String getAreaName() {
                    return areaName;
                }

                public void setAreaName(String areaName) {
                    this.areaName = areaName;
                }

                public Object getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(Object createTime) {
                    this.createTime = createTime;
                }

                public String getImgPath() {
                    return imgPath;
                }

                public void setImgPath(String imgPath) {
                    this.imgPath = imgPath;
                }
            }

        }
    }

    public static class CityInfoEntity {
        private String id;
        private String banner;//首页-精选banner
        private String pgc;//发现-美食志
        private String clubUrl;//发现-俱乐部俱乐部
        private String activityUrl;//首页-活动
        private String cityId;//城市ID

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getPgc() {
            return pgc;
        }

        public void setPgc(String pgc) {
            this.pgc = pgc;
        }

        public String getClubUrl() {
            return clubUrl;
        }

        public void setClubUrl(String clubUrl) {
            this.clubUrl = clubUrl;
        }

        public String getActivityUrl() {
            return activityUrl;
        }

        public void setActivityUrl(String activityUrl) {
            this.activityUrl = activityUrl;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }
    }

    public static class FoodKindEntity{

        /**
         * id : null
         * title : 全部美食
         */

        private String id;
        private String title;

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
    }
}
