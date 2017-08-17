package com.fengnian.smallyellowo.foodie.bean.results;

import java.util.List;

/**
 * Created by elaine on 2017/7/20.
 * 绑定城市，获取此城市的商圈
 */

public class BusinessBean extends BaseResult {


    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * streetName : 朝阳区
         * areaList : [{"id":4,"superId":3,"level":4,"areaName":"全部朝阳区"},{"id":4,"superId":3,"level":4,"areaName":"朝阳区"},{"id":6,"superId":4,"level":5,"areaName":"望京"},{"id":7,"superId":4,"level":5,"areaName":"国贸"},{"id":8,"superId":4,"level":5,"areaName":"朝外大街"},{"id":9,"superId":4,"level":5,"areaName":"亚运村"},{"id":10,"superId":4,"level":5,"areaName":"亮马桥/三元桥"},{"id":11,"superId":4,"level":5,"areaName":"大望路"},{"id":12,"superId":4,"level":5,"areaName":"双井"},{"id":13,"superId":4,"level":5,"areaName":"十里堡"},{"id":14,"superId":4,"level":5,"areaName":"朝阳公园/团结湖"},{"id":15,"superId":4,"level":5,"areaName":"建外大街"},{"id":16,"superId":4,"level":5,"areaName":"酒仙桥"},{"id":17,"superId":4,"level":5,"areaName":"青年路"},{"id":18,"superId":4,"level":5,"areaName":"劲松/潘家园"},{"id":19,"superId":4,"level":5,"areaName":"管庄"},{"id":20,"superId":4,"level":5,"areaName":"安贞"},{"id":21,"superId":4,"level":5,"areaName":"太阳宫"},{"id":22,"superId":4,"level":5,"areaName":"朝阳其他"},{"id":23,"superId":4,"level":5,"areaName":"左家庄"},{"id":24,"superId":4,"level":5,"areaName":"常营"},{"id":25,"superId":4,"level":5,"areaName":"北苑家园"},{"id":26,"superId":4,"level":5,"areaName":"对外经贸"},{"id":27,"superId":4,"level":5,"areaName":"十八里店"},{"id":28,"superId":4,"level":5,"areaName":"百子湾"},{"id":29,"superId":4,"level":5,"areaName":"蓝色港湾"},{"id":30,"superId":4,"level":5,"areaName":"工人体育场"},{"id":31,"superId":4,"level":5,"areaName":"首都机场"},{"id":32,"superId":4,"level":5,"areaName":"东坝"},{"id":33,"superId":4,"level":5,"areaName":"小营"},{"id":34,"superId":4,"level":5,"areaName":"大屯"},{"id":35,"superId":4,"level":5,"areaName":"双桥"},{"id":36,"superId":4,"level":5,"areaName":"四惠"},{"id":37,"superId":4,"level":5,"areaName":"慈云寺/八里庄"},{"id":38,"superId":4,"level":5,"areaName":"定福庄"},{"id":39,"superId":4,"level":5,"areaName":"北京欢乐谷"},{"id":40,"superId":4,"level":5,"areaName":"马泉营"},{"id":41,"superId":4,"level":5,"areaName":"石佛营"},{"id":42,"superId":4,"level":5,"areaName":"798/大山子"},{"id":43,"superId":4,"level":5,"areaName":"十里河"},{"id":44,"superId":4,"level":5,"areaName":"霄云路"},{"id":45,"superId":4,"level":5,"areaName":"立水桥"},{"id":46,"superId":4,"level":5,"areaName":"甜水园"},{"id":47,"superId":4,"level":5,"areaName":"高碑店"},{"id":48,"superId":4,"level":5,"areaName":"姚家园"},{"id":49,"superId":4,"level":5,"areaName":"北沙滩"},{"id":50,"superId":4,"level":5,"areaName":"传媒大学/二外"},{"id":51,"superId":4,"level":5,"areaName":"小庄/红庙"},{"id":52,"superId":4,"level":5,"areaName":"孙河"},{"id":53,"superId":4,"level":5,"areaName":"燕莎/农业展览馆"},{"id":54,"superId":4,"level":5,"areaName":"北京东站"},{"id":5,"superId":4,"level":5,"areaName":"三里屯"}]
         */

        private String streetName;
        private List<AreaListEntity> areaList;

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public List<AreaListEntity> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaListEntity> areaList) {
            this.areaList = areaList;
        }

        public static class AreaListEntity {
            /**
             * id : 4
             * superId : 3
             * level : 4
             * areaName : 全部朝阳区
             */

            private String id;
            private String superId;
            private String level;
            private String areaName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSuperId() {
                return superId;
            }

            public void setSuperId(String superId) {
                this.superId = superId;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }
        }
    }
}
