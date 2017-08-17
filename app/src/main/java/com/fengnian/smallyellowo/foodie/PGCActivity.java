package com.fengnian.smallyellowo.foodie;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.config.FFConfig;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.bean.PgcModelsList;
import com.fengnian.smallyellowo.foodie.fragments.PGCFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PGCActivity extends BaseActivity<IntentData> {
    private PgcModelsList pgcModelsList;
    private TabFragmentAdapter mFragAdapter;

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("美食志");
        setContentView(R.layout.activity_pgc);
        ButterKnife.bind(this);
        getData(null);
    }

    /**
     * 获取美食志列表
     */
    public void getData(final OnFinishListener finishListener) {
        String indexKey = CityPref.getCityConfigInfo().getPgc();

        post(IUrlUtils.CommonUrl.PAGE_URL, "", null, new FFNetWorkCallBack<PgcModelsList>() {
                    @Override
                    public void onSuccess(PgcModelsList response, FFExtraParams extra) {
                        if (finishListener != null) {
                            finishListener.onFinish(null);
                        }
                        pgcModelsList = response;
                        if (pgcModelsList != null && pgcModelsList.data != null && pgcModelsList.data.size() > 0) {
                            if (mFragAdapter == null) {
                                initFragmentAdapter();
                            } else {
                                for (int i = 0; i < pgcModelsList.data.size(); i++) {
                                    mFragAdapter.mFragList.get(i).setData(pgcModelsList.data.get(i).itemsPGC);
                                }
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "pageKey", indexKey
                , "debug", !FFConfig.IS_OFFICIAL);
    }

    private void initFragmentAdapter() {
        List<String> titleList = new ArrayList<>();
        List<PGCFragment> fragments = new ArrayList<>();
        for (int i = 0; i < pgcModelsList.data.size(); i++) {
            titleList.add(" " + pgcModelsList.data.get(i).templateStr + " ");

            PGCFragment fragment = new PGCFragment();
            fragment.setData(pgcModelsList.data.get(i).itemsPGC);
            fragments.add(fragment);
        }

        mFragAdapter = new TabFragmentAdapter(fragments, titleList, getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(mFragAdapter);
        final TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        // 将ViewPager和TabLayout绑定
        tablayout.setupWithViewPager(viewPager);
        // 设置tab文本的没有选中（第一个参数）和选中（第二个参数）的颜色
        tablayout.setTabTextColors(getResources().getColor(R.color.ff_text_gray), getResources().getColor(R.color.colorPrimary));

        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout, 13, 13);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(tablayout, 13, 13);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            if (llTab.getChildCount() < 6) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.leftMargin = i == 0 ? left + FFUtils.getPx(2) : left;
                params.rightMargin = right;
                params.weight = 0;
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                child.setLayoutParams(params);
                child.setMinimumWidth(0);
            }

            try {
                Field tvField = child.getClass().getDeclaredField("mTextView");
                tvField.setAccessible(true);
                TextView tv = (TextView) tvField.get(child);
                TextPaint tp = tv.getPaint();
                tp.setFakeBoldText(i == viewPager.getCurrentItem());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            child.setPadding(FFUtils.getPx(3), 0, FFUtils.getPx(3), 0);
            child.invalidate();
        }
    }


    public static class TabFragmentAdapter extends FragmentPagerAdapter {
        private List<String> titleList;
        public List<PGCFragment> mFragList;

        public TabFragmentAdapter(List<PGCFragment> fragments, List<String> titles, FragmentManager fm) {
            super(fm);
            this.mFragList = fragments;
            this.titleList = titles;
        }

        @Override
        public PGCFragment getItem(int position) {
            return mFragList.get(position);
        }

        @Override
        public int getCount() {
            return titleList == null ? 0 : titleList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
