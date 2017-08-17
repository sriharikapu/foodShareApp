package com.fengnian.smallyellowo.foodie.appbase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.fengnian.smallyellowo.foodie.fragments.IterationSocialFragment;
import com.fengnian.smallyellowo.foodie.fragments.MainDiscoverFragment;
import com.fengnian.smallyellowo.foodie.homepage.MainMyHomeFragment;
import com.fengnian.smallyellowo.foodie.personal.MainMyUserFragment;

public class FragmentTabAdapter implements CompoundButton.OnCheckedChangeListener {
    private static final int TAB_SIZE = 4;
    private final RadioButton[] rbs;
    private Fragment[] mFragArray = new Fragment[TAB_SIZE];
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id
    private int currentTab; // 当前Tab页面索引
    private OnTabCheckedListener mOnCheckedListener; // 用于让调用者在切换tab时候增加新的功能

    public MainMyHomeFragment mMainMyHomeFragment;
    public MainDiscoverFragment mMainDiscoverFragment;
    public IterationSocialFragment mIterationSocialFragment;
    public MainMyUserFragment mMainMyUserFragment;

    public FragmentTabAdapter(FragmentActivity fragmentActivity, int fragmentContentId, RadioButton[] rbs) {
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        showTab(0);
        for (RadioButton rb : rbs) {
            rb.setOnCheckedChangeListener(this);
        }
        this.rbs = rbs;
    }


    /**
     * 切换tab
     *
     * @param index
     */
    public void showTab(int index) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (index == 0) {
            if (mMainMyHomeFragment == null) {
                mMainMyHomeFragment = new MainMyHomeFragment();
                mFragArray[index] = mMainMyHomeFragment;
            }
            addFragment(ft, mMainMyHomeFragment, index);
        } else if (index == 1) {
            if (mMainDiscoverFragment == null) {
                mMainDiscoverFragment = new MainDiscoverFragment();
                mFragArray[index] = mMainDiscoverFragment;
            }
            addFragment(ft, mMainDiscoverFragment, index);
        } else if (index == 2) {
            if (mIterationSocialFragment == null) {
                mIterationSocialFragment = new IterationSocialFragment();
                mFragArray[index] = mIterationSocialFragment;
            }
            addFragment(ft, mIterationSocialFragment, index);
        } else if (index == 3) {
            if (mMainMyUserFragment == null) {
                mMainMyUserFragment = new MainMyUserFragment();
                mFragArray[index] = mMainMyUserFragment;
            }
            addFragment(ft, mMainMyUserFragment, index);
        }

        showFragment(ft, index);
        ft.commitAllowingStateLoss();
        currentTab = index; // 更新目标tab为当前tab
    }

    private void showFragment(FragmentTransaction ft, final int index) {
        for (int i = 0; i < mFragArray.length; i++) {
            if (i == index) {
                if (mFragArray[i] != null) {
                    ft.show(mFragArray[i]);
                }
            } else {
                if (mFragArray[i] != null) {
                    ft.hide(mFragArray[i]);
                }
            }
        }
    }

    private void addFragment(FragmentTransaction ft, Fragment fragment, int fragmentTag) {
        if (!fragment.isAdded() && fragment.getTag() == null) {
            ft.add(fragmentContentId, fragment, fragmentTag + "");
        }
    }


    public int getCurrentTab() {
        return currentTab;
    }


    public OnTabCheckedListener getOnTabCheckedListener() {
        return mOnCheckedListener;
    }

    public void setOnTabCheckedListener(OnTabCheckedListener listener) {
        this.mOnCheckedListener = listener;
    }

    public void checkChange(CompoundButton buttonView){
        for (int i = 0; i < rbs.length; i++) {
            if (rbs[i] != buttonView) {
                rbs[i].setChecked(false);
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }

        for (int index = 0; index < rbs.length; index++) {
            if (rbs[index] == buttonView) {
                showTab(index);
                if (null != mOnCheckedListener) {
                    mOnCheckedListener.OnChecked(buttonView, isChecked, index);
                }
            } else {
                rbs[index].setChecked(false);
            }
        }
    }

    /**
     * 切换tab额外功能功能接口
     */
    public interface OnTabCheckedListener {
        void OnChecked(CompoundButton button, boolean checkedId, int index);
    }

}
