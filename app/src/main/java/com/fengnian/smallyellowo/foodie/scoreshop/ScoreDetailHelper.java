package com.fengnian.smallyellowo.foodie.scoreshop;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.personal.DeliciousFoodModel;
import com.fengnian.smallyellowo.foodie.personal.MainMyUserFragment;
import com.fengnian.smallyellowo.foodie.personal.MainUserModelClass;
import com.fengnian.smallyellowo.foodie.personal.MyFoodFragAdapter;
import com.fengnian.smallyellowo.foodie.personal.PersonalActivty;
import com.fengnian.smallyellowo.foodie.personal.WantFoodFragAdapter;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

/**
 * Created by chenglin on 2017-3-24.
 */

public class ScoreDetailHelper {
    private ScoreDetailActivity mActivity;
    public static final int TAB_LEFT = 0, TAB_RIGHT = 1;
    private final int[] TAB_ITEM_ID = {R.id.tab_left, R.id.tab_right};
    private int mSelectedTab = TAB_LEFT;
    private View bgView = null;
    private ImageView emptyGuideView;

    public ScoreDetailHelper(ScoreDetailActivity activity) {
        mActivity = activity;
    }


    public void initTabView() {
        setTabSelected(TAB_LEFT);
        mActivity.mViewPager.setCurrentItem(TAB_LEFT, false);

        for (int index = 0; index < TAB_ITEM_ID.length; index++) {
            RelativeLayout tabView = (RelativeLayout) mActivity.findViewById(TAB_ITEM_ID[index]);
            TextView tabTitle = (TextView) tabView.findViewById(R.id.tab_title);
            RelativeLayout tabChildItem = (RelativeLayout) tabView.findViewById(R.id.tab_item);

            tabTitle.setText(mActivity.TITLES[index]);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabChildItem.getLayoutParams();
            if (index == TAB_LEFT) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin = DisplayUtil.dip2px(60f);
            } else if (index == TAB_RIGHT) {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = DisplayUtil.dip2px(60f);
            }
            tabChildItem.setLayoutParams(params);

            final int currentIndex = index;
            tabChildItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTabSelected(currentIndex);
                    mActivity.onTabSelected(currentIndex);
                    mActivity.mViewPager.setCurrentItem(currentIndex, true);
                }
            });
        }
    }

    public int getSelectedTab() {
        return mSelectedTab;
    }

    public void setTabSelected(final int currentIndex) {
        mSelectedTab = currentIndex;

        for (int lineIndex = 0; lineIndex < TAB_ITEM_ID.length; lineIndex++) {
            RelativeLayout tab = (RelativeLayout) mActivity.findViewById(TAB_ITEM_ID[lineIndex]);
            TextView tab_title = (TextView) tab.findViewById(R.id.tab_title);
            View tabBottomLine = tab.findViewById(R.id.tab_bottom_line);
            if (currentIndex == lineIndex) {
                tabBottomLine.setVisibility(View.VISIBLE);
                tab_title.setTextColor(mActivity.getResources().getColor(R.color.title_text_color));
            } else {
                tabBottomLine.setVisibility(View.GONE);
                tab_title.setTextColor(mActivity.getResources().getColor(R.color.color_2));
            }
        }
    }


}
