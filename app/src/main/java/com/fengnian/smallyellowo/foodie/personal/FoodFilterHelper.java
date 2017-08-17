package com.fengnian.smallyellowo.foodie.personal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.StickyNavLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.LocationHelper;
import com.fengnian.smallyellowo.foodie.utils.PopupWindowUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenglin on 2017-3-29.
 */

public class FoodFilterHelper {
    public static final int PROFILE_CENTER = 1, PROFILE_SEARCH = 2;
    private final String[] TAB_FILTER_TITLE = {"品类", "区域", "筛选"};
    private final int PROFILE_CENTER_LEFT = 1, PROFILE_CENTER_RIGHT = 2;
    private final int[] TAB_FILTER_ID = {R.id.tv_food_type, R.id.tv_food_area, R.id.tv_food_filter};
    private final int[] FILTER_PROFILE_LEFT_ID = {R.id.shortcut, R.id.richtext, R.id.btn_un_save, R.id.btn_un_share, R.id.btn_shared};
    private final int[] FILTER_PROFILE_RIGHT_ID = {R.id.btn_no_eated, R.id.btn_eated, R.id.btn_daren,
            R.id.btn_dynamic, R.id.btn_other, R.id.btn_time, R.id.btn_score, R.id.btn_distance};
    private HashMap<Integer, Boolean> mProfileCenterFilterMap = new HashMap<>();
    private BaseFragment mFragment;
    private View mFilterView;
    private View mResetBtn;
    private String mKeyWord;
    private int POPUP_MAX_HEIGHT = 0;
    private PopupWindow.OnDismissListener mDismissListener;
    private ProfileFilterModel mProfileFilterModel;
    private int mFromWhere = PROFILE_CENTER;
    private onTitleClickListener mOnTitleClickListener;

    //过滤项
    private ChooseAdapter mProfileCenterLeftFoodNoteAdapter = null;
    private ChooseAdapter mProfileCenterLeftFoodNoteStreetParentAdapter = null;
    private ChooseAdapter mProfileCenterLeftFoodNoteStreetChildAdapter = null;
    private ChooseAdapter mProfileCenterRightFoodNoteAdapter = null;
    private ChooseAdapter mProfileCenterRightFoodNoteStreetParentAdapter = null;
    private ChooseAdapter mProfileCenterRightFoodNoteStreetChildAdapter = null;
    private View mProfileCenterLeftFilterLayout = null, mProfileCenterRightFilterLayout = null;
    private String mProfileCenterStreetId = "";
    private Map<String, String> mShowTitleMap = new HashMap<>();


    public FoodFilterHelper(BaseFragment baseFragment, int from) {
        mFragment = baseFragment;
        mFromWhere = from;
    }

    public void setOnTitleClickListener(onTitleClickListener listener) {
        mOnTitleClickListener = listener;
    }

    private int getFilterShowType() {
        int type = -1;
        if (mFromWhere == PROFILE_CENTER) {
            MainMyUserFragment mainUserInfoFragment = (MainMyUserFragment) mFragment;
            int selectedTab = mainUserInfoFragment.getSelectedTab();
            if (selectedTab == MainUserInfoFragmentHelper.TAB_LEFT) {
                type = PROFILE_CENTER_LEFT;
            } else {
                type = PROFILE_CENTER_RIGHT;
            }
        } else if (mFromWhere == PROFILE_SEARCH) {
            ProfileSearchResultFragment searchFragment = (ProfileSearchResultFragment) mFragment;
            int selectedTab = searchFragment.getSelectedTab();
            if (selectedTab == MainUserInfoFragmentHelper.TAB_LEFT) {
                type = PROFILE_CENTER_LEFT;
            } else {
                type = PROFILE_CENTER_RIGHT;
            }
        }
        return type;
    }

    public void initFilterLayout() {
        POPUP_MAX_HEIGHT = DisplayUtil.screenHeight - DisplayUtil.dip2px(200f);
        mFilterView = mFragment.findViewById(R.id.filter_view);
        mResetBtn = mFilterView.findViewById(R.id.tv_reset);
        if (mResetBtn != null) {
            mResetBtn.setVisibility(View.GONE);
        }

        getFilterData();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mFromWhere == PROFILE_CENTER) {
                    MainMyUserFragment mainUserInfoFragment = (MainMyUserFragment) mFragment;
                    if (!mainUserInfoFragment.mStickyNavLayout.isToTop()) {
                        mainUserInfoFragment.mStickyNavLayout.scrollToTop(true);
                        FFUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setFilterItemClickEvent(v);
                            }
                        }, StickyNavLayout.SCROLL_TOP_DURATION + 80);
                    } else {
                        setFilterItemClickEvent(v);
                    }
                } else if (mFromWhere == PROFILE_SEARCH) {
                    setFilterItemClickEvent(v);
                }
            }
        };

        for (int i = 0; i < TAB_FILTER_ID.length; i++) {
            TextView textView = (TextView) mFilterView.findViewById(TAB_FILTER_ID[i]);
            if (textView != null) {
                textView.setText(TAB_FILTER_TITLE[i]);
                textView.setOnClickListener(onClickListener);
            }
        }

        //重置
        if (mResetBtn != null) {
            mResetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShowTitleMap.clear();
                    mProfileCenterFilterMap.clear();
                    initFilterMenuView(getFilterLayout());
                    setFilterTitle();
                    TextView textView = (TextView) mFilterView.findViewById(R.id.tv_food_filter);
                    textView.setTextColor(mFragment.getResources().getColor(R.color.color_3));

                    if (mFromWhere == PROFILE_CENTER) {
                        ChooseAdapter[] chooseAdapters = new ChooseAdapter[3];
                        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                            resetFilterMenuTag(mProfileCenterLeftFilterLayout);
                            chooseAdapters[0] = mProfileCenterLeftFoodNoteAdapter;
                            chooseAdapters[1] = mProfileCenterLeftFoodNoteStreetParentAdapter;
                            chooseAdapters[2] = mProfileCenterLeftFoodNoteStreetChildAdapter;
                        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                            resetFilterMenuTag(mProfileCenterRightFilterLayout);
                            chooseAdapters[0] = mProfileCenterRightFoodNoteAdapter;
                            chooseAdapters[1] = mProfileCenterRightFoodNoteStreetParentAdapter;
                            chooseAdapters[2] = mProfileCenterRightFoodNoteStreetChildAdapter;
                        }

                        for (int i = 0; i < chooseAdapters.length; i++) {
                            if (chooseAdapters[i] != null) {
                                chooseAdapters[i].setCurrentItem(null);
                                chooseAdapters[i].notifyDataSetChanged();
                                if (i == 2) {
                                    //清除儿子的数据
                                    chooseAdapters[i].clear();
                                }
                            }
                        }

                        MainMyUserFragment mainUserInfoFragment = (MainMyUserFragment) mFragment;
                        mainUserInfoFragment.resetFilterParams();
                    }
                }
            });
        }

    }

    public void setDismissListener(PopupWindow.OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
    }

    public void onDismissListener() {
        checkFilterMenuView(getFilterLayout());
        if (mFragment instanceof MainMyUserFragment && mResetBtn != null) {
            if (mResetBtn.getVisibility() == View.VISIBLE) {
                mResetBtn.animate().alpha(1f).setDuration(250);
            }
        }
    }

    public void setRestBtnVisible() {
        if (mFragment instanceof MainMyUserFragment && mResetBtn != null) {
            MainMyUserFragment userFragment = (MainMyUserFragment) mFragment;
            if (userFragment.isFilterChanged()) {
                mResetBtn.setVisibility(View.VISIBLE);
            } else {
                mResetBtn.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @return 过滤菜单View
     */
    private View getFilterLayout() {
        View filterLayout = null;
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            filterLayout = mProfileCenterLeftFilterLayout;
        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            filterLayout = mProfileCenterRightFilterLayout;
        }
        return filterLayout;
    }

    public void getFilterData() {
        if (!FFUtils.checkNet()) {
            return;
        }

//        mFragment.post(Constants.shareConstants().getNetHeaderAdress() + "/myCenter/queryMyCenterPtypesAndStreets.do",
        mFragment.post(IUrlUtils.Search.queryMyCenterPtypesAndStreets,
                null, null, new FFNetWorkCallBack<ProfileFilterModel>() {
                    @Override
                    public void onSuccess(ProfileFilterModel model, FFExtraParams extra) {
                        mProfileFilterModel = model;

                        //得到数据后发广播通知
                        Intent intent = new Intent(MyActions.ACTION_PROFILE);
                        intent.putExtra("type", "type_is_show_want_food_filter");
                        LocalBroadcastManager.getInstance(mFragment.getActivity()).sendBroadcast(intent);

                        //处理搜索关键字自动选中筛选项
                        if (!TextUtils.isEmpty(mKeyWord)) {
                            getFoodStreetList(PROFILE_CENTER_LEFT);
                            getFoodStreetList(PROFILE_CENTER_RIGHT);
                            getFoodTypeList(PROFILE_CENTER_LEFT);
                            getFoodTypeList(PROFILE_CENTER_RIGHT);
                            setFilterTitle();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                });

    }

    private void setFilterItemClickEvent(View v) {
        TextView textView = null;
        if (v instanceof TextView) {
            textView = (TextView) v;
        }
        if (textView == null) {
            return;
        }

        if (mFromWhere == PROFILE_CENTER) {
            MainMyUserFragment mainUserInfoFragment = (MainMyUserFragment) mFragment;
            mainUserInfoFragment.setShadowBg(true);
        } else if (mFromWhere == PROFILE_SEARCH) {
            ProfileSearchResultFragment searchFragment = (ProfileSearchResultFragment) mFragment;
            searchFragment.setShadowBg(true);
        }

        setFilterViewSelected(v);

        View filter_view_line = mFragment.findViewById(R.id.filter_view_line);
        View popupFilterListView = null;
        PopupWindow popupWindow = null;
        List<FilterItem> dataList = null;

        if (mResetBtn != null && mResetBtn.getVisibility() == View.VISIBLE) {
            mResetBtn.animate().alpha(0f).setDuration(250);
        }

        if (mOnTitleClickListener != null) {
            mOnTitleClickListener.onClick();
        }

        switch (v.getId()) {
            case R.id.tv_food_type:
                ChooseAdapter chooseAdapter = null;
                String key1 = "";
                if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                    chooseAdapter = mProfileCenterLeftFoodNoteAdapter;
                    dataList = getFoodTypeList(PROFILE_CENTER_LEFT);
                    key1 = "left_0";
                } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                    chooseAdapter = mProfileCenterRightFoodNoteAdapter;
                    dataList = getFoodTypeList(PROFILE_CENTER_RIGHT);
                    key1 = "right_0";
                }

                final String myKey1 = key1;
                ChooseListener foodChooseListener = new ChooseListener() {
                    @Override
                    public void onFinish(FilterItem filterItem) {
                        mShowTitleMap.put(myKey1, filterItem.title);
                        setFilterTitle();
                    }
                };

                popupFilterListView = View.inflate(mFragment.getActivity(), R.layout.profile_filter_listview, null);
                popupFilterListView.findViewById(R.id.left_listView).setVisibility(View.GONE);
                ListView listView = (ListView) popupFilterListView.findViewById(R.id.right_listView);
                if (chooseAdapter == null) {
                    chooseAdapter = new ChooseAdapter(mFragment.getActivity(), foodChooseListener);
                    if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                        mProfileCenterLeftFoodNoteAdapter = chooseAdapter;
                    } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                        mProfileCenterRightFoodNoteAdapter = chooseAdapter;
                    }
                }
                listView.setAdapter(chooseAdapter);
                chooseAdapter.setData(dataList);

                int height = -2;
                if (chooseAdapter.getCount() > 10) {
                    height = POPUP_MAX_HEIGHT;
                }
                popupWindow = PopupWindowUtils.show(mFragment.getActivity(), filter_view_line, popupFilterListView, height, -2);
                chooseAdapter.setPopWindow(popupWindow);
                popupWindow.setOnDismissListener(mDismissListener);
                setAdapterSelected();
                break;
            case R.id.tv_food_area:
                ChooseAdapter chooseParentAdapter = null, chooseChildAdapter = null;
                String key = "";
                if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                    chooseParentAdapter = mProfileCenterLeftFoodNoteStreetParentAdapter;
                    chooseChildAdapter = mProfileCenterLeftFoodNoteStreetChildAdapter;
                    dataList = getFoodStreetList(PROFILE_CENTER_LEFT);
                    key = "left_1";
                } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                    chooseParentAdapter = mProfileCenterRightFoodNoteStreetParentAdapter;
                    chooseChildAdapter = mProfileCenterRightFoodNoteStreetChildAdapter;
                    dataList = getFoodStreetList(PROFILE_CENTER_RIGHT);
                    key = "right_1";
                }

                popupFilterListView = View.inflate(mFragment.getActivity(), R.layout.profile_filter_listview, null);
                ListView leftListView = (ListView) popupFilterListView.findViewById(R.id.left_listView);
                ListView rightListView = (ListView) popupFilterListView.findViewById(R.id.right_listView);
                leftListView.setBackgroundColor(mFragment.getResources().getColor(R.color.color_4));

                final String myKey = key;
                ChooseListener chooseListener = new ChooseListener() {
                    @Override
                    public void onFinish(FilterItem filterItem) {
                        mProfileCenterStreetId = filterItem.id;
                        mShowTitleMap.put(myKey, filterItem.title);
                        setFilterTitle();

                        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                            if (filterItem.level == ChooseAdapter.LEVEL_FIRST) {
                                mProfileCenterLeftFoodNoteStreetParentAdapter.setCurrentItem(filterItem);
                                mProfileCenterLeftFoodNoteStreetChildAdapter.setCurrentItem(null);
                            } else if (filterItem.level == ChooseAdapter.LEVEL_SECOND) {
                                mProfileCenterLeftFoodNoteStreetChildAdapter.setCurrentItem(filterItem);
                            }
                        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                            if (filterItem.level == ChooseAdapter.LEVEL_FIRST) {
                                mProfileCenterRightFoodNoteStreetParentAdapter.setCurrentItem(filterItem);
                                mProfileCenterRightFoodNoteStreetChildAdapter.setCurrentItem(null);
                            } else if (filterItem.level == ChooseAdapter.LEVEL_SECOND) {
                                mProfileCenterRightFoodNoteStreetChildAdapter.setCurrentItem(filterItem);
                            }
                        }
                    }
                };

                if (chooseParentAdapter == null) {
                    chooseParentAdapter = new ChooseAdapter(mFragment.getActivity(), chooseListener);
                    if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                        mProfileCenterLeftFoodNoteStreetParentAdapter = chooseParentAdapter;
                    } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                        mProfileCenterRightFoodNoteStreetParentAdapter = chooseParentAdapter;
                    }
                }
                leftListView.setAdapter(chooseParentAdapter);
                chooseParentAdapter.setData(dataList);

                if (chooseChildAdapter == null) {
                    chooseChildAdapter = new ChooseAdapter(mFragment.getActivity(), chooseListener);
                    if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                        mProfileCenterLeftFoodNoteStreetChildAdapter = chooseChildAdapter;
                    } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                        mProfileCenterRightFoodNoteStreetChildAdapter = chooseChildAdapter;
                    }
                }
                rightListView.setAdapter(chooseChildAdapter);
                chooseParentAdapter.setChildAdapter(chooseChildAdapter);
                popupWindow = PopupWindowUtils.show(mFragment.getActivity(), filter_view_line, popupFilterListView, -2, -2);
                chooseParentAdapter.setPopWindow(popupWindow);
                chooseChildAdapter.setPopWindow(popupWindow);
                popupWindow.setOnDismissListener(mDismissListener);
                setAdapterSelected();
                break;
            case R.id.tv_food_filter:
                View filterLayout = null;
                int filterLayoutId = 0;
                if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                    filterLayout = mProfileCenterLeftFilterLayout;
                    filterLayoutId = R.layout.profile_food_list_filter_layout;
                } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                    filterLayout = mProfileCenterRightFilterLayout;
                    filterLayoutId = R.layout.profile_want_eat_list_filter_layout;
                }

                if (filterLayout == null) {
                    filterLayout = View.inflate(mFragment.getActivity(), filterLayoutId, null);
                    initFilterMenuView(filterLayout);

                    if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                        mProfileCenterLeftFilterLayout = filterLayout;
                    } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                        mProfileCenterRightFilterLayout = filterLayout;
                    }
                }
                popupWindow = PopupWindowUtils.show(mFragment.getActivity(), filter_view_line, filterLayout, -2, -2);
                popupWindow.setOnDismissListener(mDismissListener);
                setFilterMenuClickListener(popupWindow, filterLayout);
                break;
        }
    }

    /**
     * 筛选的重置、完成按钮
     */
    private void setFilterMenuClickListener(final PopupWindow popupWindow, final View filterLayout) {
        if (popupWindow == null) {
            return;
        }

        final int[] filterMenuIdRes = {R.id.btn_finish, R.id.btn_reset};
        for (int i = 0; i < filterMenuIdRes.length; i++) {
            View menuView = filterLayout.findViewById(filterMenuIdRes[i]);
            menuView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] array = null;
                    if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                        array = FILTER_PROFILE_LEFT_ID;
                    } else {
                        array = FILTER_PROFILE_RIGHT_ID;
                    }

                    if (v.getId() == R.id.btn_finish) {
                        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
                            CheckBox checkbox = (CheckBox) filterLayout.findViewById(R.id.checkbox);
                            mProfileCenterFilterMap.put(R.id.checkbox, checkbox.isChecked());
                        }

                        for (int i = 0; i < array.length; i++) {
                            TextView filterTextView = (TextView) filterLayout.findViewById(array[i]);
                            if (filterTextView.isSelected()) {
                                mProfileCenterFilterMap.put(array[i], true);
                            } else {
                                mProfileCenterFilterMap.put(array[i], false);
                            }
                        }
                        popupWindow.dismiss();
                    } else if (v.getId() == R.id.btn_reset) {
                        resetFilterMenuTag(filterLayout);
                    }
                }
            });
        }
    }

    /**
     * 初始化筛选列表功能
     */
    private void initFilterMenuView(final View filterLayout) {
        if (filterLayout == null) {
            return;
        }

        int[] array = null;
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            array = FILTER_PROFILE_LEFT_ID;
        } else {
            array = FILTER_PROFILE_RIGHT_ID;
        }

        for (int i = 0; i < array.length; i++) {
            final TextView filterTextView = (TextView) filterLayout.findViewById(array[i]);
            filterTextView.setSelected(false);

            if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                TextView btn_no_eated = (TextView) filterLayout.findViewById(R.id.btn_no_eated);
                TextView btn_time = (TextView) filterLayout.findViewById(R.id.btn_time);
                btn_no_eated.setSelected(true);
                btn_time.setSelected(true);

                //记录点击之前的选择状态，以便恢复
                mProfileCenterFilterMap.put(R.id.btn_no_eated, true);
                mProfileCenterFilterMap.put(R.id.btn_time, true);
            }

            filterTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterTextView.setSelected(!filterTextView.isSelected());

                    if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
                        TextView btn_no_eated = (TextView) filterLayout.findViewById(R.id.btn_no_eated);
                        TextView btn_eated = (TextView) filterLayout.findViewById(R.id.btn_eated);
                        TextView btn_time = (TextView) filterLayout.findViewById(R.id.btn_time);
                        TextView btn_score = (TextView) filterLayout.findViewById(R.id.btn_score);
                        TextView btn_distance = (TextView) filterLayout.findViewById(R.id.btn_distance);

                        if (v.getId() == R.id.btn_no_eated) {
                            btn_eated.setSelected(false);
                            btn_no_eated.setSelected(true);
                        } else if (v.getId() == R.id.btn_eated) {
                            btn_eated.setSelected(true);
                            btn_no_eated.setSelected(false);
                        } else if (v.getId() == R.id.btn_time) {
                            btn_score.setSelected(false);
                            btn_distance.setSelected(false);
                        } else if (v.getId() == R.id.btn_score) {
                            btn_time.setSelected(false);
                            btn_distance.setSelected(false);
                        } else if (v.getId() == R.id.btn_distance) {
                            btn_score.setSelected(false);
                            btn_time.setSelected(false);
                        }

                        //如果定位权限被禁止，这里重新请求定位权限
                        if (v.getId() == R.id.btn_distance) {
                            if (!LocationHelper.hasLocationPermission(mFragment.getActivity())) {
                                ActivityCompat.requestPermissions(mFragment.getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                            }
                        }
                    }

                }
            });
        }
    }

    /**
     * 重新检查一遍筛选列表的按钮状态，因为不点完成就要恢复原状
     */
    private void checkFilterMenuView(final View filterLayout) {
        if (filterLayout == null) {
            return;
        }
        int[] array = null;
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            array = FILTER_PROFILE_LEFT_ID;
        } else {
            array = FILTER_PROFILE_RIGHT_ID;
        }

        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            CheckBox checkbox = (CheckBox) filterLayout.findViewById(R.id.checkbox);
            Boolean isChecked = mProfileCenterFilterMap.get(R.id.checkbox);
            if (isChecked != null && isChecked) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }
        }

        for (int i = 0; i < array.length; i++) {
            final TextView filterTextView = (TextView) filterLayout.findViewById(array[i]);
            //根据之前点击设置的tag来恢复没点完成时的按钮状态
            Boolean isSelect = mProfileCenterFilterMap.get(array[i]);
            if (isSelect != null && isSelect) {
                filterTextView.setSelected(true);
            } else {
                filterTextView.setSelected(false);
            }
        }
    }

    /**
     * 重置筛选列表的按钮Tag
     */
    private void resetFilterMenuTag(final View filterLayout) {
        if (filterLayout == null) {
            return;
        }
        int[] array = null;
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            CheckBox checkbox = (CheckBox) filterLayout.findViewById(R.id.checkbox);
            checkbox.setChecked(false);
            mProfileCenterFilterMap.remove(R.id.checkbox);

            array = FILTER_PROFILE_LEFT_ID;
        } else {
            array = FILTER_PROFILE_RIGHT_ID;
        }
        for (int i = 0; i < array.length; i++) {
            final TextView filterTextView = (TextView) filterLayout.findViewById(array[i]);
            filterTextView.setSelected(false);
        }

        if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            TextView btn_no_eated = (TextView) filterLayout.findViewById(R.id.btn_no_eated);
            TextView btn_time = (TextView) filterLayout.findViewById(R.id.btn_time);
            btn_no_eated.setSelected(true);
            btn_time.setSelected(true);
        }
    }

    /**
     * 得到美食记录或者想吃清单的品类列表
     */
    private List<FilterItem> getFoodTypeList(final int type) {
        List<FilterItem> dataList = new ArrayList<>();
        if (mProfileFilterModel == null) {
            return dataList;
        }

        List<ProfileFilterModel.FilterItem> tempList = null;
        String myKey = "";

        if (type == PROFILE_CENTER_LEFT) {
            tempList = mProfileFilterModel.foodNotePtypes;
            myKey = "left_0";
        } else if (type == PROFILE_CENTER_RIGHT) {
            tempList = mProfileFilterModel.wantEatPtypes;
            myKey = "right_0";
        }

        if (tempList.size() > 0) {
            for (ProfileFilterModel.FilterItem filterItem : tempList) {
                FilterItem item = new FilterItem();
                item.id = filterItem.key;
                item.title = filterItem.value;
                item.level = ChooseAdapter.LEVEL_FIRST;
                dataList.add(item);

                //处理搜索关键字自动选中筛选项
                if (item.title.equals(mKeyWord)) {
                    mShowTitleMap.put(myKey, mKeyWord);
                }
            }
        }

        return dataList;
    }

    /**
     * 得到美食记录的区域列表
     */
    private List<FilterItem> getFoodStreetList(final int type) {
        List<FilterItem> dataList = new ArrayList<>();
        if (mProfileFilterModel == null) {
            return dataList;
        }

        List<ProfileFilterModel.ParentFilterItem> tempList = null;
        String myKey = "";

        if (type == PROFILE_CENTER_LEFT) {
            tempList = mProfileFilterModel.foodNoteStreets;
            myKey = "left_1";
        } else if (type == PROFILE_CENTER_RIGHT) {
            tempList = mProfileFilterModel.wantEatStreets;
            myKey = "right_1";
        }


        if (tempList.size() > 0) {
            for (ProfileFilterModel.ParentFilterItem parentFilterItem : tempList) {
                FilterItem parentItem = new FilterItem();
                parentItem.id = parentFilterItem.key;
                parentItem.title = parentFilterItem.value;
                parentItem.level = ChooseAdapter.LEVEL_FIRST;

                if (parentFilterItem.syStreets != null && parentFilterItem.syStreets.size() > 0) {
                    parentItem.childList = new ArrayList<>();
                    for (ProfileFilterModel.FilterItem childFilterItem : parentFilterItem.syStreets) {
                        FilterItem childItem = new FilterItem();
                        childItem.level = ChooseAdapter.LEVEL_SECOND;
                        childItem.title = childFilterItem.value;
                        childItem.id = childFilterItem.key;
                        parentItem.childList.add(childItem);

                        //处理搜索关键字自动选中筛选项
                        if (childItem.title.equals(mKeyWord)) {
                            mShowTitleMap.put(myKey, mKeyWord);
                        }
                    }
                } else {
                    parentItem.parentId = parentFilterItem.key;
                    parentItem.parentTitle = parentFilterItem.value;
                    parentItem.childList = new ArrayList<>();

                    //处理搜索关键字自动选中筛选项
                    if (parentItem.parentTitle.equals(mKeyWord)) {
                        mShowTitleMap.put(myKey, mKeyWord);
                    }
                }
                dataList.add(parentItem);
            }
        }
        return dataList;
    }

    /**
     * 设置过滤项谁被选中，如果v = null ，那么就是重置
     */
    public void setFilterViewSelected(View v) {
        if (v == null) {
            for (int i = 0; i < TAB_FILTER_ID.length; i++) {
                TextView filterTextView = (TextView) mFilterView.findViewById(TAB_FILTER_ID[i]);
                if (filterTextView != null) {
                    filterTextView.setTextColor(mFragment.getResources().getColor(R.color.color_3));
                    filterTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.profile_filter_down_icon, 0);
                    filterTextView.setTag(R.mipmap.profile_filter_down_icon);
                }
            }
            return;
        }

        TextView textView = (TextView) v;
        if (textView.getTag() == null) {
            textView.setTag(R.mipmap.profile_filter_down_icon);
        }
        int resDrawableId = (int) textView.getTag();

        if (resDrawableId == R.mipmap.profile_filter_up_icon) {
            textView.setTextColor(mFragment.getResources().getColor(R.color.color_3));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.profile_filter_down_icon, 0);
            textView.setTag(R.mipmap.profile_filter_down_icon);
        } else {
            textView.setTextColor(mFragment.getResources().getColor(R.color.colorPrimary));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.profile_filter_up_icon, 0);
            textView.setTag(R.mipmap.profile_filter_up_icon);
        }
        for (int i = 0; i < TAB_FILTER_ID.length; i++) {
            if (v.getId() != TAB_FILTER_ID[i]) {
                TextView filterTextView = (TextView) mFilterView.findViewById(TAB_FILTER_ID[i]);
                if (filterTextView != null) {
                    filterTextView.setTextColor(mFragment.getResources().getColor(R.color.color_3));
                    filterTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.profile_filter_down_icon, 0);
                    filterTextView.setTag(R.mipmap.profile_filter_down_icon);
                }
            }
        }
    }


    /**
     * 构建个人中心的美食记录的搜索参数
     */
    public MainUserModelClass.ProfileCenterLeftRequestParams buildProfileCenterLeftParams() {
        MainUserModelClass.ProfileCenterLeftRequestParams params = new MainUserModelClass.ProfileCenterLeftRequestParams();
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            if (mProfileCenterLeftFoodNoteAdapter != null && mProfileCenterLeftFoodNoteAdapter.getCurrentItem() != null) {
                params.ptype = mProfileCenterLeftFoodNoteAdapter.getCurrentItem().id;
            }
            params.streetId = mProfileCenterStreetId;
            if (mProfileCenterLeftFilterLayout != null) {
                CheckBox checkbox = (CheckBox) mProfileCenterLeftFilterLayout.findViewById(R.id.checkbox);
                TextView shortcut = (TextView) mProfileCenterLeftFilterLayout.findViewById(R.id.shortcut);
                TextView richtext = (TextView) mProfileCenterLeftFilterLayout.findViewById(R.id.richtext);
                TextView btn_un_save = (TextView) mProfileCenterLeftFilterLayout.findViewById(R.id.btn_un_save);
                TextView btn_un_share = (TextView) mProfileCenterLeftFilterLayout.findViewById(R.id.btn_un_share);
                TextView btn_shared = (TextView) mProfileCenterLeftFilterLayout.findViewById(R.id.btn_shared);

                //是否精选（1只精选,-1全选或者全不选）
                if (checkbox.isChecked()) {
                    params.isElite = "1";
                } else {
                    params.isElite = "-1";
                }

                //1速记、2编辑、-1全选或者全不选
                if (shortcut.isSelected() && !richtext.isSelected()) {
                    params.pubType = "1";
                } else if (!shortcut.isSelected() && richtext.isSelected()) {
                    params.pubType = "2";
                } else {
                    params.pubType = "-1";
                }

                //1分享、0未分享、-1全选
                if (!btn_un_share.isSelected() && btn_shared.isSelected()) {
                    params.status = "1";
                } else if (btn_un_share.isSelected() && !btn_shared.isSelected()) {
                    params.status = "0";
                } else {
                    params.status = "-1";
                }

                if (btn_un_save.isSelected()) {
                    params.isUnSaved = true;
                } else {
                    params.isUnSaved = false;
                }
            }
        }

        Log.v("tag_2", params.toString());
        return params;
    }

    /**
     * 构建个人中心的想吃清单的搜索参数
     */
    public MainUserModelClass.ProfileCenterRightRequestParams buildProfileCenterRightParams() {
        MainUserModelClass.ProfileCenterRightRequestParams params = new MainUserModelClass.ProfileCenterRightRequestParams();
        if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            if (mProfileCenterRightFoodNoteAdapter != null && mProfileCenterRightFoodNoteAdapter.getCurrentItem() != null) {
                params.ptype = mProfileCenterRightFoodNoteAdapter.getCurrentItem().id;
            }
            params.streetId = mProfileCenterStreetId;
            if (mProfileCenterRightFilterLayout != null) {
                TextView btn_no_eated = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_no_eated);
                TextView btn_eated = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_eated);
//                TextView btn_daren = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_daren);
//                TextView btn_dynamic = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_dynamic);
//                TextView btn_other = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_other);
                TextView btn_time = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_time);
                TextView btn_score = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_score);
                TextView btn_distance = (TextView) mProfileCenterRightFilterLayout.findViewById(R.id.btn_distance);

                //想吃类型（1未吃、2吃过 , 0全选；默认0）
                if (btn_no_eated.isSelected() && !btn_eated.isSelected()) {
                    params.eatType = 1;
                } else if (!btn_no_eated.isSelected() && btn_eated.isSelected()) {
                    params.eatType = 2;
                } else {
                    params.eatType = 0;
                }

                //来源（1达人荐，2动态， 3其他 默认空）传来源字符串，逗号分割(1,2--按达人荐和动态搜)
                final int[] sourceArray = {R.id.btn_daren, R.id.btn_dynamic, R.id.btn_other};
                final String[] sourceId = {"1", "2", "3"};
                StringBuilder build = new StringBuilder();
                for (int i = 0; i < sourceArray.length; i++) {
                    TextView textView = (TextView) mProfileCenterRightFilterLayout.findViewById(sourceArray[i]);
                    if (textView.isSelected()) {
                        if (build.length() > 0) {
                            build.append("," + sourceId[i]);
                        } else {
                            build.append(sourceId[i]);
                        }
                    }
                }
                if (build.length() <= 0) {
                    params.eatSource = "1,2,3";
                } else {
                    params.eatSource = build.toString();
                }

                //排序（0按时间、1按评分 , 2按距离， 默认按时间0）
                if (btn_time.isSelected() && !btn_score.isSelected() && !btn_distance.isSelected()) {
                    params.eatSort = 0;
                } else if (!btn_time.isSelected() && btn_score.isSelected() && !btn_distance.isSelected()) {
                    params.eatSort = 1;
                } else if (!btn_time.isSelected() && !btn_score.isSelected() && btn_distance.isSelected()) {
                    params.eatSort = 2;
                } else {
                    params.eatSort = 0;
                }
            }
        }

        Log.v("tag_2", params.toString());
        return params;
    }

    /**
     * 是否是初始化状态
     */
    public boolean isInitState() {
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            Boolean isChecked = mProfileCenterFilterMap.get(R.id.checkbox);
            if (isChecked != null && isChecked) {
                return false;
            }

            for (int i = 0; i < FILTER_PROFILE_LEFT_ID.length; i++) {
                Boolean isSelect = mProfileCenterFilterMap.get(FILTER_PROFILE_LEFT_ID[i]);
                if (isSelect != null && isSelect) {
                    return false;
                }
            }
        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            for (int i = 0; i < FILTER_PROFILE_RIGHT_ID.length; i++) {
                Boolean isSelect = mProfileCenterFilterMap.get(FILTER_PROFILE_RIGHT_ID[i]);
                if (FILTER_PROFILE_RIGHT_ID[i] == R.id.btn_no_eated
                        || FILTER_PROFILE_RIGHT_ID[i] == R.id.btn_time) {
                    if (isSelect != null && !isSelect) {
                        return false;
                    }
                } else {
                    if (isSelect != null && isSelect) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setFilterBtnColor() {
        TextView textView = (TextView) mFilterView.findViewById(R.id.tv_food_filter);
        if (!isInitState()) {
            textView.setTextColor(mFragment.getResources().getColor(R.color.colorPrimary));
        } else {
            textView.setTextColor(mFragment.getResources().getColor(R.color.color_3));
        }
    }

    public void setFilterTitle() {
        TextView foodTypeTv = (TextView) mFilterView.findViewById(R.id.tv_food_type);
        TextView foodAreaTv = (TextView) mFilterView.findViewById(R.id.tv_food_area);
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            if (!TextUtils.isEmpty(mShowTitleMap.get("left_0"))) {
                foodTypeTv.setText(mShowTitleMap.get("left_0"));
            } else {
                foodTypeTv.setText(TAB_FILTER_TITLE[0]);
            }
            if (!TextUtils.isEmpty(mShowTitleMap.get("left_1"))) {
                foodAreaTv.setText(mShowTitleMap.get("left_1"));
            } else {
                foodAreaTv.setText(TAB_FILTER_TITLE[1]);
            }
        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            if (!TextUtils.isEmpty(mShowTitleMap.get("right_0"))) {
                foodTypeTv.setText(mShowTitleMap.get("right_0"));
            } else {
                foodTypeTv.setText(TAB_FILTER_TITLE[0]);
            }
            if (!TextUtils.isEmpty(mShowTitleMap.get("right_1"))) {
                foodAreaTv.setText(mShowTitleMap.get("right_1"));
            } else {
                foodAreaTv.setText(TAB_FILTER_TITLE[1]);
            }
        }
    }

    private void setAdapterSelected() {
        if (getFilterShowType() == PROFILE_CENTER_LEFT) {
            if (!TextUtils.isEmpty(mKeyWord) && mProfileCenterLeftFoodNoteAdapter != null
                    && mProfileCenterLeftFoodNoteAdapter.getCurrentItem() == null) {
                for (FilterItem item : mProfileCenterLeftFoodNoteAdapter.getData()) {
                    if (item.title.equals(mKeyWord)) {
                        mProfileCenterLeftFoodNoteAdapter.setCurrentItem(item);
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(mKeyWord)
                    && mKeyWord.equals(mShowTitleMap.get("left_1"))
                    && mProfileCenterLeftFoodNoteStreetParentAdapter != null
                    && mProfileCenterLeftFoodNoteStreetParentAdapter.getCurrentItem() == null) {
                for (FilterItem parentItem : mProfileCenterLeftFoodNoteStreetParentAdapter.getData()) {
                    if (parentItem.childList != null && parentItem.childList.size() > 0) {
                        for (FilterItem childItem : parentItem.childList) {
                            if (childItem.title.equals(mKeyWord)) {
                                mProfileCenterLeftFoodNoteStreetParentAdapter.setCurrentItem(parentItem);
                                mProfileCenterLeftFoodNoteStreetParentAdapter.getChildAdapter().setData(parentItem.childList);
                                mProfileCenterLeftFoodNoteStreetParentAdapter.getChildAdapter().setCurrentItem(childItem);
                                break;
                            }
                        }
                    }
                }
            } else if (mProfileCenterLeftFoodNoteStreetParentAdapter != null
                    && mProfileCenterLeftFoodNoteStreetParentAdapter.getCurrentItem() == null
                    && mProfileCenterLeftFoodNoteStreetParentAdapter.getData().size() >= 2
                    && mProfileCenterLeftFoodNoteStreetParentAdapter.getData().get(1).childList != null
                    && mProfileCenterLeftFoodNoteStreetParentAdapter.getData().get(1).childList.size() > 0) {
                FilterItem parentItem = mProfileCenterLeftFoodNoteStreetParentAdapter.getData().get(1);
                mProfileCenterLeftFoodNoteStreetParentAdapter.setCurrentItem(parentItem);
                mProfileCenterLeftFoodNoteStreetParentAdapter.getChildAdapter().setData(parentItem.childList);
            }
        } else if (getFilterShowType() == PROFILE_CENTER_RIGHT) {
            if (!TextUtils.isEmpty(mKeyWord) && mProfileCenterRightFoodNoteAdapter != null
                    && mProfileCenterRightFoodNoteAdapter.getCurrentItem() == null) {
                for (FilterItem item : mProfileCenterRightFoodNoteAdapter.getData()) {
                    if (item.title.equals(mKeyWord)) {
                        mProfileCenterRightFoodNoteAdapter.setCurrentItem(item);
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(mKeyWord)
                    && mKeyWord.equals(mShowTitleMap.get("right_1"))
                    && mProfileCenterRightFoodNoteStreetParentAdapter != null
                    && mProfileCenterRightFoodNoteStreetParentAdapter.getCurrentItem() == null) {
                for (FilterItem parentItem : mProfileCenterRightFoodNoteStreetParentAdapter.getData()) {
                    if (parentItem.childList != null && parentItem.childList.size() > 0) {
                        for (FilterItem childItem : parentItem.childList) {
                            if (childItem.title.equals(mKeyWord)) {
                                mProfileCenterRightFoodNoteStreetParentAdapter.setCurrentItem(parentItem);
                                mProfileCenterRightFoodNoteStreetParentAdapter.getChildAdapter().setData(parentItem.childList);
                                mProfileCenterRightFoodNoteStreetParentAdapter.getChildAdapter().setCurrentItem(childItem);
                                break;
                            }
                        }
                    }
                }
            } else if (mProfileCenterRightFoodNoteStreetParentAdapter != null
                    && mProfileCenterRightFoodNoteStreetParentAdapter.getCurrentItem() == null
                    && mProfileCenterRightFoodNoteStreetParentAdapter.getData().size() >= 2
                    && mProfileCenterRightFoodNoteStreetParentAdapter.getData().get(1).childList != null
                    && mProfileCenterRightFoodNoteStreetParentAdapter.getData().get(1).childList.size() > 0) {
                FilterItem parentItem = mProfileCenterRightFoodNoteStreetParentAdapter.getData().get(1);
                mProfileCenterRightFoodNoteStreetParentAdapter.setCurrentItem(parentItem);
                mProfileCenterRightFoodNoteStreetParentAdapter.getChildAdapter().setData(parentItem.childList);
            }
        }
    }

    public void setFilterTitleKeyWord(final String keyWord) {
        mKeyWord = keyWord;
    }

    public boolean isShowWantFoodFilterMenu() {
        if (mProfileFilterModel != null) {
            boolean bool_1 = mProfileFilterModel.wantEatStreets != null && mProfileFilterModel.wantEatStreets.size() > 1;
            boolean bool_2 = mProfileFilterModel.wantEatPtypes != null && mProfileFilterModel.wantEatPtypes.size() > 1;
            if (bool_1 || bool_2) {
                return true;
            }
        }
        return false;
    }

    public interface onTitleClickListener {
        void onClick();
    }
}
