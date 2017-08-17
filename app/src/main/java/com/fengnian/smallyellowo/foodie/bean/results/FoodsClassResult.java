package com.fengnian.smallyellowo.foodie.bean.results;

import java.util.List;

/**
 * Created by Administrator on 2016-8-25.
 */
public class FoodsClassResult extends  BaseResult{

    /**
     * result : success
     * count : 13
     * syFoodNotesClassifications : [{"imgUrl":null,"type":"韩国料理","counts":3},{"imgUrl":null,"type":"日本菜","counts":1},{"imgUrl":null,"type":"小吃快餐","counts":1},{"imgUrl":null,"type":"北京菜","counts":1},{"imgUrl":null,"type":"粤菜","counts":1},{"imgUrl":null,"type":"湘菜","counts":1}]
     */

    private String result;
    private int count;
    /**
     * imgUrl : null
     * type : 韩国料理
     * counts : 3
     */

    private List<SyFoodNotesClassificationsBean> syFoodNotesClassifications;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SyFoodNotesClassificationsBean> getSyFoodNotesClassifications() {
        return syFoodNotesClassifications;
    }

    public void setSyFoodNotesClassifications(List<SyFoodNotesClassificationsBean> syFoodNotesClassifications) {
        this.syFoodNotesClassifications = syFoodNotesClassifications;
    }

    public static class SyFoodNotesClassificationsBean {
        private Object imgUrl;
        private String type;
        private int counts;

        public Object getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(Object imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }
    }
}
