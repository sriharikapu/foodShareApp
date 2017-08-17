package com.fengnian.smallyellowo.foodie.personal;

import android.text.TextUtils;
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
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;
import com.fengnian.smallyellowo.foodie.utils.Parser;

/**
 * Created by chenglin on 2017-3-24.
 */

public class MainUserInfoFragmentHelper {
    private MainMyUserFragment mFragment;
    public static final int TAB_LEFT = 0, TAB_RIGHT = 1;
    private final int[] TAB_ITEM_ID = {R.id.tab_left, R.id.tab_right};
    private int mSelectedTab = TAB_LEFT;
    private View bgView = null;
    private ImageView emptyGuideView;

    public MainUserInfoFragmentHelper(MainMyUserFragment frag) {
        mFragment = frag;
    }

    public void onViewCreated() {
        RelativeLayout head_view = (RelativeLayout) mFragment.findViewById(R.id.head_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) head_view.getLayoutParams();
        int imageWidth = mFragment.getResources().getDrawable(R.mipmap.profile_head_bg).getIntrinsicWidth();
        int imageHeight = mFragment.getResources().getDrawable(R.mipmap.profile_head_bg).getIntrinsicHeight();
        params.height = (int) (1f * DisplayUtil.screenWidth * imageHeight / imageWidth);
        head_view.setLayoutParams(params);
    }

    public void initTabView() {
        setTabSelected(TAB_LEFT);
        for (int index = 0; index < TAB_ITEM_ID.length; index++) {
            RelativeLayout tabView = (RelativeLayout) mFragment.findViewById(TAB_ITEM_ID[index]);
            TextView tabTitle = (TextView) tabView.findViewById(R.id.tab_title);
            RelativeLayout tabChildItem = (RelativeLayout) tabView.findViewById(R.id.tab_item);

            tabTitle.setText(mFragment.TITLES[index]);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabChildItem.getLayoutParams();
            if (index == TAB_LEFT) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin = DisplayUtil.dip2px(50f);
            } else if (index == TAB_RIGHT) {
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.leftMargin = DisplayUtil.dip2px(50f);
            }
            tabChildItem.setLayoutParams(params);

            final int currentIndex = index;
            tabChildItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTabSelected(currentIndex);
                    mFragment.onTabSelected(currentIndex);
                }
            });
        }
    }

    public int getSelectedTab() {
        return mSelectedTab;
    }

    private void setTabSelected(final int currentIndex) {
        mSelectedTab = currentIndex;
        mFragment.mViewPager.setCurrentItem(currentIndex, false);

        for (int lineIndex = 0; lineIndex < TAB_ITEM_ID.length; lineIndex++) {
            RelativeLayout tab = (RelativeLayout) mFragment.findViewById(TAB_ITEM_ID[lineIndex]);
            TextView tab_title = (TextView) tab.findViewById(R.id.tab_title);
            View tabBottomLine = tab.findViewById(R.id.tab_bottom_line);
            if (currentIndex == lineIndex) {
                tabBottomLine.setVisibility(View.VISIBLE);
                tab_title.setTextColor(mFragment.getResources().getColor(R.color.title_text_color));
            } else {
                tabBottomLine.setVisibility(View.GONE);
                tab_title.setTextColor(mFragment.getResources().getColor(R.color.color_2));
            }
        }
    }

    public void toUserPage(FFContext activity, SYUser syUser) {
        if (syUser == null) {
            return;
        }
        UserInfoIntent info = new UserInfoIntent();
        info.setUser(syUser);
        IsAddCrownUtils.FragmentActivtyStartAct(syUser, info, activity);
    }

    public void toEditProfile(BaseFragment baseFragment, SYUser syUser) {
        if (syUser == null) {
            return;
        }
        UserInfoIntent userInfoIntent = new UserInfoIntent();
        userInfoIntent.setUser(syUser);
        baseFragment.startActivity(PersonalActivty.class, userInfoIntent);
    }


    public void setUserHeaderInfo(MainUserModelClass.UserInfoResult response) {
        ImageView iv_add_crown = (ImageView) mFragment.findViewById(R.id.iv_add_crown);
        ImageView iv_avatar = (ImageView) mFragment.findViewById(R.id.iv_avatar);
        TextView tv_name = (TextView) mFragment.findViewById(R.id.tv_name);
        TextView tv_score = (TextView) mFragment.findViewById(R.id.tv_score);
        ImageView profile_head_food_record = (ImageView) mFragment.findViewById(R.id.profile_head_food_record);
        ImageView tv_sex = (ImageView) mFragment.findViewById(R.id.tv_sex);

        IsAddCrownUtils.checkIsAddCrow(response.user, iv_add_crown);
        FFImageLoader.loadAvatar((FFContext) mFragment.getActivity(), response.user.getHeadImage().getUrl(), iv_avatar);

        if (!TextUtils.isEmpty(response.user.getNickName())) {
            tv_name.setText(response.user.getNickName());
        }else {
            tv_name.setText("你还未填写昵称哦");
        }
        String score = response.user.getUserIntegral();
        tv_score.setText(FFUtils.getSubFloat(Parser.parseDouble(score), 1, true));

        if (response.foodCourse == 0) {
            profile_head_food_record.setVisibility(View.GONE);
        } else {
            profile_head_food_record.setVisibility(View.VISIBLE);
        }

        if (response.user.getSex() == 0) {
            tv_sex.setVisibility(View.VISIBLE);
            tv_sex.setImageResource(R.drawable.profile_boy_icon);
        } else if (response.user.getSex() == 1) {
            tv_sex.setVisibility(View.VISIBLE);
            tv_sex.setImageResource(R.drawable.profile_girl_icon);
        } else {
            tv_sex.setImageResource(0);
            tv_sex.setVisibility(View.GONE);
        }
    }

    public void setGrowthRedDot(boolean isShow) {
        View redDot = mFragment.findViewById(R.id.red_dot);
        if (isShow) {
            View profile_head_food_record = mFragment.findViewById(R.id.profile_head_food_record);
            profile_head_food_record.setVisibility(View.VISIBLE);
            redDot.setVisibility(View.VISIBLE);
        } else {
            redDot.setVisibility(View.GONE);
        }
    }

    public void setShadowBg(boolean isAdd) {
        FrameLayout rootView = (FrameLayout) mFragment.getActivity().findViewById(R.id.fl_cur);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.topMargin = FFUtils.getStatusbarHight(mFragment.getActivity())
                + mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_height)
                + mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_filter_item_height) + 2;
        if (isAdd) {
            if (bgView != null) {
                rootView.removeView(bgView);
            }
            bgView = new View(mFragment.getActivity());
            bgView.setBackgroundColor(mFragment.getResources().getColor(R.color.color_6));
            bgView.setClickable(true);
            rootView.addView(bgView, params);
        } else {
            if (bgView != null) {
                rootView.removeView(bgView);
            }
        }
    }

    public void addEmptyGuideView() {
        removeEmptyGuideView();
        FrameLayout rootView = (FrameLayout) mFragment.getActivity().findViewById(R.id.fl_cur);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = DisplayUtil.dip2px(85f);
        emptyGuideView = new ImageView(mFragment.getActivity());
        emptyGuideView.setImageResource(R.drawable.profile_empty_guide);
        rootView.addView(emptyGuideView, 1, params);
    }

    public void removeEmptyGuideView() {
        if (emptyGuideView != null) {
            FrameLayout rootView = (FrameLayout) mFragment.getActivity().findViewById(R.id.fl_cur);
            rootView.removeView(emptyGuideView);
            emptyGuideView = null;
        }
    }

    public void showOrHideFoodFragmentEmptyGuide() {
        if (mFragment.mMyFoodListFragment == null || mFragment.mMyFoodListFragment.mAdapter == null) {
            return;
        }
        MyFoodFragAdapter adapter = mFragment.mMyFoodListFragment.mAdapter;
        if (getSelectedTab() == MainUserInfoFragmentHelper.TAB_LEFT
                && adapter.getItemCount() == 1
                && adapter.getDataList().get(0).type == DeliciousFoodModel.EMPTY
                && !adapter.isFilterEmpty) {
            addEmptyGuideView();
        } else {
            removeEmptyGuideView();
        }
    }

    public void hideOrShowFilterView(int index) {
        View filterView = mFragment.findViewById(R.id.filter_view);
        if (index == MainUserInfoFragmentHelper.TAB_LEFT) {
            if (mFragment.mMyFoodListFragment == null || mFragment.mMyFoodListFragment.mAdapter == null) {
                return;
            }
            MyFoodFragAdapter adapter = mFragment.mMyFoodListFragment.mAdapter;
            if (adapter.getItemCount() == 1 && adapter.getDataList().get(0).type == DeliciousFoodModel.EMPTY
                    && !adapter.isFilterEmpty) {
                filterView.setVisibility(View.GONE);
            } else {
                filterView.setVisibility(View.VISIBLE);
            }
        } else if (index == MainUserInfoFragmentHelper.TAB_RIGHT) {
            if (mFragment.mWantFoodListFragment == null || mFragment.mWantFoodListFragment.mAdapter == null) {
                return;
            }
            WantFoodFragAdapter adapter = mFragment.mWantFoodListFragment.mAdapter;
            if (adapter.getItemCount() == 1 && adapter.getDataList().get(0).type == DeliciousFoodModel.EMPTY
                    && !adapter.isFilterEmpty && !mFragment.mFoodFilterHelper.isShowWantFoodFilterMenu()) {
                filterView.setVisibility(View.GONE);
            } else {
                filterView.setVisibility(View.VISIBLE);
            }
        }
    }
}
