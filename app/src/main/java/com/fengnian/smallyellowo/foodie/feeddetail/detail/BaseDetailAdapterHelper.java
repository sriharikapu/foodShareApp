package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

/**
 * Created by chenglin on 2017-3-22.
 */

public class BaseDetailAdapterHelper {
    BaseDetailAdapter mBaseDetailAdapter;

    public BaseDetailAdapterHelper(BaseDetailAdapter adapter) {
        mBaseDetailAdapter = adapter;
    }
    //动态调整头像上皇冠的图片间距，因为有皇冠和没皇冠需要的间距是不一样的
    public void setPaiseUserImageLayoutParams(SYUser user, ImageView iv_img, TextView tv_num, ImageView ivCrown) {
        boolean isCrownShow = IsAddCrownUtils.checkIsAddCrow(user, ivCrown);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_img.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tv_num.getLayoutParams();
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivCrown.getLayoutParams();
        if (isCrownShow) {
            final int padding = 5;
            params1.leftMargin = DisplayUtil.dip2px(5f);
            params1.rightMargin = DisplayUtil.dip2px(5f);
            params1.topMargin = DisplayUtil.dip2px(5.5f) + padding;

            params2.leftMargin = DisplayUtil.dip2px(5f);
            params2.rightMargin = DisplayUtil.dip2px(5f);
            params2.topMargin = DisplayUtil.dip2px(5.5f) + padding;

            params3.leftMargin = DisplayUtil.dip2px(-5f) + padding;
            params3.topMargin = DisplayUtil.dip2px(-5.5f) - padding;
        } else {
            params1.leftMargin = DisplayUtil.dip2px(3f);
            params1.rightMargin = DisplayUtil.dip2px(3f);
            params1.topMargin = 0;

            params2.leftMargin = DisplayUtil.dip2px(3f);
            params2.rightMargin = DisplayUtil.dip2px(3f);
            params2.topMargin = 0;

            params3.leftMargin = 0;
            params3.topMargin = 0;
        }
        iv_img.setLayoutParams(params1);
        tv_num.setLayoutParams(params2);
        ivCrown.setLayoutParams(params3);
    }
}
