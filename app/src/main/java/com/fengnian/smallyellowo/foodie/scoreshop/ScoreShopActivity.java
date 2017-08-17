package com.fengnian.smallyellowo.foodie.scoreshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.Parser;
import com.fengnian.smallyellowo.foodie.widgets.GridAdapter;

import java.util.List;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-5-3.
 */

public class ScoreShopActivity extends BaseActivity<IntentData> {
    public static final String SCORE_RULE = "http://static.tinydonuts.cn/Integral_rule.html";
    private String mLuckLotteryUrl = "http://m.tinydonuts.cn/lottery/1/#/index.html";
    private BroadcastReceiver mBroadcastReceiver;
    private ScoreShopModel mScoreShopModel;
    private ListView mListView;
    private ScoreShopAdapter mAdapter;
    private TextView tv_score;
    private ImageView score_lottery;
    private View mHeaderView;
    private String mLastId;
    private PullToRefreshLayout prl;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
        setContentView(R.layout.score_shop_layout);

        mListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new ScoreShopAdapter(this, 2);
        mHeaderView = View.inflate(this, R.layout.score_shop_header, null);
        tv_score = (TextView) mHeaderView.findViewById(R.id.tv_score);
        score_lottery = (ImageView) mHeaderView.findViewById(R.id.score_lottery);
        mListView.addHeaderView(mHeaderView);
        mListView.setAdapter(mAdapter);

        addMenu("积分规则", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    WebInfo info = new WebInfo();
                    info.setUrl(SCORE_RULE);
                    info.setTitle("积分规则");
                    startActivity(CommonWebviewUtilActivity.class, info);
                }
            }
        });

        mAdapter.setOnItemClickListener(new GridAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                if (!isClicked) {
                    isClicked = true;
                    mAdapter.startSkuDetail(position, mScoreShopModel.totalPoints);
                }
            }
        });

        mHeaderView.findViewById(R.id.left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    ScoreDetailActivity.start(ScoreShopActivity.this, 0);
                }
            }
        });

        mHeaderView.findViewById(R.id.right_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    ScoreDetailActivity.start(ScoreShopActivity.this, 1);
                }
            }
        });

        score_lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mLuckLotteryUrl)) {
                    if (!isClicked) {
                        isClicked = true;
                        WebInfo info = new WebInfo();
                        info.setUrl(mLuckLotteryUrl);
                        info.setTitle("幸运大转盘");
//                        info.setMenuVisible(false);
                        startActivity(CommonWebviewUtilActivity.class, info);
                    }
                }
            }
        });

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mLastId = null;
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullDown(false);
        prl.setDoPullUp(true);

        getData(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClicked = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void registerBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyActions.ACTION_UPDATE_SCORE)) {
                    float totalScore = intent.getFloatExtra("score", -100f);
                    if (totalScore != -100f) {
                        setScoreTextSpan(totalScore + "");
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyActions.ACTION_UPDATE_SCORE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {
        if (mBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        }
    }

    private void setScoreTextSpan(String score) {
        SpannableString spannableString = new SpannableString("积分" + FFUtils.getSubFloat(Parser.parseDouble(score), 1, true));
        int color = Color.parseColor("#F9A825");
        spannableString.setSpan(new ForegroundColorSpan(color), 2, score.length() + 2, 0);
        tv_score.setText(spannableString);
    }

    private void getData(final boolean isRefresh) {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shopmall/mallIndex.do", "",
        post(IUrlUtils.Search.mallIndex, "",
                null, new FFNetWorkCallBack<ScoreShopModel>() {
                    @Override
                    public void onSuccess(ScoreShopModel response, FFExtraParams extra) {
                        mScoreShopModel = response;
                        if (response != null) {
                            if (isRefresh) {
                                mAdapter.setDataList(response.goodsList);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.goodsList);
                            }

                            if (response.goodsList != null && response.goodsList.size() > 0) {
                                mLastId = response.goodsList.get(response.goodsList.size() - 1).goodsId;
                            }

                            if (response.goodsList.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }

                            if (!TextUtils.isEmpty(response.totalPoints)) {
                                setScoreTextSpan(response.totalPoints);
                            } else {
                                setScoreTextSpan("0");
                            }

                            //处理抽奖头图的逻辑--现在没有动态控制，为了以后打算，不过也许永远用不到
                            if (response.pointsDrawModel != null) {
                                if (!TextUtils.isEmpty(response.pointsDrawModel.drawImage)
                                        && !TextUtils.isEmpty(response.pointsDrawModel.drawUrl)) {
                                    FFImageLoader.loadBigImage(ScoreShopActivity.this, response.pointsDrawModel.drawImage, score_lottery);
                                    mLuckLotteryUrl = response.pointsDrawModel.drawUrl;
                                }
                            }
                        }

                        if (mAdapter.getItemCount() <= 0) {
                            prl.setDoPullUp(false);
                        } else {
                            prl.setDoPullUp(true);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "lastMallActivityProductId", mLastId,
                "pageSize", 20);
    }

    public static final class ScoreShopModel extends BaseResult {
        public String totalPoints;//总积分
        public SYPointsDrawModel pointsDrawModel; //积分奖Model
        public List<SYGoodsModel> goodsList;
    }
}
