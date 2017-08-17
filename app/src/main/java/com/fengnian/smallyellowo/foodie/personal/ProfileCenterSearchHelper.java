package com.fengnian.smallyellowo.foodie.personal;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;

/**
 * Created by chenglin on 2017-3-24.
 */

public class ProfileCenterSearchHelper {
    private ProfileSearchResultFragment mFragment;
    private final int[] TAB_ITEM_ID = {R.id.tab_left, R.id.tab_right};
    private int mSelectedTab = MainUserInfoFragmentHelper.TAB_LEFT;
    private View bgView = null;

    public ProfileCenterSearchHelper(ProfileSearchResultFragment frag) {
        mFragment = frag;
    }

    public void initTabView() {
        setTabSelected(MainUserInfoFragmentHelper.TAB_LEFT);
        for (int index = 0; index < TAB_ITEM_ID.length; index++) {
            RelativeLayout tabView = (RelativeLayout) mFragment.findViewById(TAB_ITEM_ID[index]);
            TextView tabTitle = (TextView) tabView.findViewById(R.id.tab_title);
            RelativeLayout tabChildItem = (RelativeLayout) tabView.findViewById(R.id.tab_item);

            tabTitle.setText(mFragment.TITLES[index]);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabChildItem.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
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

    public void setMyFoodTabTitle(final String title) {
        RelativeLayout tabView = (RelativeLayout) mFragment.findViewById(TAB_ITEM_ID[0]);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tab_title);
        tabTitle.setText(title);
    }

    public void setWantEatTabTitle(final String title) {
        RelativeLayout tabView = (RelativeLayout) mFragment.findViewById(TAB_ITEM_ID[1]);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tab_title);
        tabTitle.setText(title);
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


    public void setShadowBg(boolean isAdd) {
        FrameLayout rootView = (FrameLayout) mFragment.getActivity().findViewById(R.id.ll_baseActivity_content);

        int[] local = new int[2];
        mFragment.mViewPager.getLocationOnScreen(local);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
        params.topMargin = local[1] - FFUtils.getStatusbarHight(mFragment.getActivity());

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

    public boolean isHideKeyboard(BaseFragment fragment, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(view)) {
            fragment.getView().requestFocus();
            inputMethodManager.hideSoftInputFromWindow(fragment.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }
}
