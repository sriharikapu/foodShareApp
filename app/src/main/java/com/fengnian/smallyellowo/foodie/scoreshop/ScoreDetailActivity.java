package com.fengnian.smallyellowo.foodie.scoreshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.View.StickyNavLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.personal.AddressEditActivity;
import com.fengnian.smallyellowo.foodie.utils.Parser;

import java.util.List;

/**
 * Created by chenglin on 2017-5-3.
 */

public class ScoreDetailActivity extends BaseActivity<IntentData> {
    public static final String[] TITLES = {"积分明细", "兑换记录"};
    private ScoreDetailHelper mHelper;
    private StickyNavLayout mStickyNavLayout;
    public ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private ScoreDetailFragment mScoreDetailFragment;
    private ScoreCostFragment mScoreCostFragment;


    public static void start(BaseActivity activity, int index) {
        Intent intent = new Intent(activity, ScoreDetailActivity.class);
        intent.putExtra("index", index);
        activity.startActivity(intent);
    }

    public static void start(BaseActivity activity) {
        activity.startActivity(ScoreDetailActivity.class, new IntentData());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_detail_layout);

        mHelper = new ScoreDetailHelper(this);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mStickyNavLayout = (StickyNavLayout) findViewById(R.id.stick_nav_layout);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mHelper.initTabView();

        mScoreDetailFragment = (ScoreDetailFragment) mAdapter.getItem(0);
        mScoreCostFragment = (ScoreCostFragment) mAdapter.getItem(1);

        int index = getIntent().getIntExtra("index", -1);
        if (index >= 0 && index <= 1) {
            mViewPager.setCurrentItem(index, false);
            mHelper.setTabSelected(index);
        } else {
            mViewPager.setCurrentItem(0, false);
            mHelper.setTabSelected(0);
        }

        addMenu("收货地址", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddressEditActivity.class, new IntentData());
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mHelper.setTabSelected(position);
            }
        });
    }

    public void onTabSelected(int tabIndex) {

    }

    public void setScore(String score) {
        TextView tv_score = (TextView) findViewById(R.id.tv_score);
        if (score.length() >= 5) {
            tv_score.setTextSize(32f);
        }
        tv_score.setText(FFUtils.getSubFloat(Parser.parseDouble(score), 1, true));
    }

    public static final class ScoreDetailModel extends BaseResult {
        public String totalPoints;
        public List<SYNewPerPointsListModel> pointsInfoList;
    }

    public static final class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        BaseFragment[] mFragList = new BaseFragment[TITLES.length];

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragList[0] = ScoreDetailFragment.newInstance();
            mFragList[1] = ScoreCostFragment.newInstance();
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragList[position];
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View line_top, line_bottom, line_top_2;
        RecyclerLoadMoreFooterView footerView;
        TextView tv_title, tv_time, tv_score;
        ImageView emptyImageView;
        LinearLayout convertView;

        public MyViewHolder(View view) {
            super(view);
            convertView = (LinearLayout) view;
            line_top = view.findViewById(R.id.line_top);
            line_top_2 = view.findViewById(R.id.line_top_2);
            line_bottom = view.findViewById(R.id.line_bottom);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_score = (TextView) view.findViewById(R.id.tv_score);
            footerView = (RecyclerLoadMoreFooterView) view.findViewById(R.id.recycler_load_footer);
        }
    }
}
