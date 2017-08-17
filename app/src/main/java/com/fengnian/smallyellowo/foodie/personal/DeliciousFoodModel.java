package com.fengnian.smallyellowo.foodie.personal;

import android.text.TextUtils;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.List;

/**
 * Created by chenglin on 2017-3-26.
 */

public class DeliciousFoodModel extends BaseResult {
    public transient static int MODEL_NET = 0;
    public transient static int MODEL_NATIVE = 1;
    public transient static int EMPTY = 100;

    public static final class FoodListModel {
        public int type = 0;
        public SYSearchUserFoodModel sYSearchUserFoodModel; //网络数据
        public SYFeed nativeRichTextFood;//本地数据


        /**
         * 得到发布时间，用于排序
         */
        public final long getPublishTime() {
            if (type == MODEL_NATIVE && nativeRichTextFood != null) {
                return nativeRichTextFood.getTimeStamp();
            } else if (type == MODEL_NET && sYSearchUserFoodModel != null
                    && TextUtils.isDigitsOnly(sYSearchUserFoodModel.pushTime)) {
                return Long.parseLong(sYSearchUserFoodModel.pushTime);
            } else {
                return System.currentTimeMillis();
            }
        }
    }


    /**
     * 文档地址：http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915341
     */
    public static final class SYSearchUserFoodModel {
        public transient int type = 0;
        public String foodNoteId;//美食记录ID
        public String wantEatId;//想吃ID
        public String foodImage;//美食图片
        public int foodImageCount;//图片张数
        public String foodTitle;//美食记录名称
        public String merchantName;//店铺名称
        public String merchantPrice;//商铺人均
        public boolean isShorthandFood;//YES 是美食速记，NO美食富文本
        public boolean hasEat;//YES 吃过，NO未吃过
        public String distance;//距离
        public String pushTime;//发布时间
        public boolean isSharedToAct;//是否分享
        public int releaseTemplate;//0标准 1现代 2泼墨 3中式 4中式2 5简短 6简短2 7简短2-2
        public String ptype;//品类
        public String street;//商圈
        public double starLevel;//星级
        public String merchantId;//商户ID
        public String recordId;//美食记录id（仅美食详情和列表取消想吃记录需要传
        public String shopType;// 0--腾讯地图商户；1-自定义商户
        public String eatNumber;//多少位好友想吃
        public String merTeleNumber;//商户电话号码多于一个 用逗号隔开
        public double latitude;//商户的纬度
        public double longitude;//商户的经度
        public String shopAddress;//商户详情地址
        public int isOnline;//是否商户在线，1-在线 0-下架
    }

    /**
     * 接收网络数据
     */
    public static final class FoodNetList extends BaseResult {
        public List<SYSearchUserFoodModel> sySearchUserFoodModels;//网络数据
        public int foodNotesCount;
        public int eatNumber;
    }
}
