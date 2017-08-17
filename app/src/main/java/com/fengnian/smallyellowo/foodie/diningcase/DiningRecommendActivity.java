package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.config.Tool;
import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.TimeUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.util.ArrayList;
import java.util.HashMap;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;

/**
 * Created by chenglin on 2017-6-15.
 */

public class DiningRecommendActivity extends BaseActivity<IntentData> implements View.OnClickListener {
    public final static int TYPE_SINGLE = 1;
    public final static int TYPE_MULTIPLE = 2;
    private ListView mListView;
    private DiningRecommendAdapter mAdapter;
    private PullToRefreshLayout prl;
    private RecomRequestModel mParams = new RecomRequestModel();
    private int mFromWhere = TYPE_SINGLE;
    private ReplaceViewHelper mReplaceViewHelper;
    private String myUrl = "";
    private TextView TvWeixin, TvWantEat;

    /**
     * 来自于[一人食]
     */
    public static void startFromSingle(Context context, ArrayList<SYFindMerchantInfo> list, RecomRequestModel requestParams) {
        Intent intent = new Intent(context, DiningRecommendActivity.class);
        intent.putExtra("from_where", TYPE_SINGLE);
        intent.putExtra("list", list);
        intent.putExtra("params", requestParams);
        context.startActivity(intent);
    }

    /**
     * 来自于[朋友聚餐]
     */
    public static void startFromMultiple(Context context, ArrayList<SYFindMerchantInfo> list, RecomRequestModel requestParams) {
        Intent intent = new Intent(context, DiningRecommendActivity.class);
        intent.putExtra("from_where", TYPE_MULTIPLE);
        intent.putExtra("list", list);
        intent.putExtra("params", requestParams);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFromWhere = getIntent().getIntExtra("from_where", TYPE_SINGLE);
        ArrayList<SYFindMerchantInfo> list = getIntent().getParcelableArrayListExtra("list");
        mParams = (RecomRequestModel) getIntent().getSerializableExtra("params");

        setContentView(R.layout.dining_recommend_result_layout);
        setTitle(getString(R.string.dining_recommend_act_title));
        mReplaceViewHelper = new ReplaceViewHelper(this);

        mListView = findView(R.id.list_view);
        TvWeixin = findView(R.id.dining_share_to_weixin_textview);
        TvWantEat = findView(R.id.dining_share_to_want_eat_textview);

        mAdapter = new DiningRecommendAdapter(this, mFromWhere);
        mListView.setAdapter(mAdapter);

        if (mFromWhere == TYPE_SINGLE) {
//            myUrl = "/discover/onePersonFood/onePersonFoodList.do";
            myUrl = IUrlUtils.WorkDining.onePersonFoodList;
            findViewById(R.id.view_bottom).setVisibility(View.GONE);
            findViewById(R.id.line_bottom).setVisibility(View.GONE);
        } else if (mFromWhere == TYPE_MULTIPLE) {
//            myUrl = "/discover/multipleMeals/multipleMealsRecommendedList.do";
            myUrl = IUrlUtils.WorkDining.multipleMealsRecommendedList;
            findViewById(R.id.dining_share_to_weixin).setOnClickListener(this);
            findViewById(R.id.dining_share_to_want_eat).setOnClickListener(this);
            mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.normal_bg)));
            mListView.setDividerHeight(DisplayUtil.dip2px(7f));
            setCheckedTextShow(0);
        }

        prl = supportPull(mListView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(true);

        if (list != null && list.size() > 0) {
            mAdapter.setDataList(list);
        } else {
            getData(true);
        }
    }

    private void getData(final boolean isRefresh) {
        if (isRefresh) {
            mParams.shopId = "0";
        }

        String latitude = Value.mLatitude + "";
        String longitude = Value.mLongitude + "";

        if (!TextUtils.isEmpty(mParams.latitude)) {
            latitude = mParams.latitude;
        }
        if (!TextUtils.isEmpty(mParams.longitude)) {
            longitude = mParams.longitude;
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + myUrl,
        post(myUrl,"", null, new FFNetWorkCallBack<RecomResultModel>() {
                    @Override
                    public void onSuccess(RecomResultModel response, FFExtraParams extra) {
                        if (response != null && response.syFindMerchantInfos != null) {
                            if (isRefresh) {
                                mAdapter.getCheckedList().clear();
                                mAdapter.setDataList(response.syFindMerchantInfos);
                                setCheckedTextShow(0);
                                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                mAdapter.appendDataList(response.syFindMerchantInfos);
                            }

                            if (response.syFindMerchantInfos != null && response.syFindMerchantInfos.size() > 0) {
                                int count = response.syFindMerchantInfos.size();
                                mParams.shopId = response.syFindMerchantInfos.get(count - 1).getMerchantUid();
                            }

                            if (response.syFindMerchantInfos.size() > 0) {
                                prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                            }

                            if (mAdapter.getCount() <= 0) {
                                mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
                                prl.setDoPullUp(false);
                            } else {
                                mReplaceViewHelper.removeView();
                                prl.setDoPullUp(true);
                            }
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                        return false;
                    }
                }, "pageSize", 15
                , "shopId", mParams.shopId
                , "longitude", longitude
                , "latitude", latitude
                , "peopleTypeName", mParams.peopleTypeName
                , "ptypes", mParams.ptypes
                , "city", "北京"
                , "tjID", mParams.tjID
                , "openGPS", "0"
        );

    }

    public void setCheckedTextShow(final int count) {
        TvWeixin.setText(getString(R.string.dining_recommend_share_1) + "(" + count + ")");
        TvWantEat.setText(getString(R.string.dining_recommend_share_2) + "(" + count + ")");
    }

    private void shareToWeixin() {
        if (mAdapter.getCheckedList().size() <= 0) {
            showToast("至少选择一个哦~");
        } else if (mAdapter.getCheckedList().size() == 1) {
            getShopUrl();
        } else {
            getVoteUrl();
        }
    }

    /**
     * 生成单项店铺的URL
     */
    private void getShopUrl() {
        final String clientTime = TimeUtils.getTime("yyyyMMddhhmmssSSS", System.currentTimeMillis());
        final SYFindMerchantInfo model = mAdapter.getCheckedList().get(0);

//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/createDisShopDetailsHtmlV260.do",
        post(IUrlUtils.Search.createDisShopDetailsHtmlV260, "", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        String shopUrl = ShareUrlTools.getRestInfoUrl(model.getMerchantUid(), clientTime);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dining_share_icon);
                        WeixinOpen.getInstance().share2weixin(shopUrl, getShareContent(),
                                getString(R.string.dining_recommend_share_3), bitmap);
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "clientTime", clientTime
                , "merchantId", model.getMerchantUid()
                , "shopImg", model.getMerchantImage().getUrl()
                , "pageSize", 3
                , "recordId", model.getFeedId());
    }

    /**
     * 生成投票H5的URL
     */
    private void getVoteUrl() {
        StringBuilder builder = new StringBuilder();
        for (SYFindMerchantInfo item : mAdapter.getCheckedList()) {
            if (builder.length() <= 0) {
                builder.append(item.getMerchantUid());
            } else {
                builder.append("," + item.getMerchantUid());
            }
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/createVoteActivity.do",
        post(IUrlUtils.WorkDining.createVoteActivity,
                "", null, new FFNetWorkCallBack<ShareLinkModel>() {
                    @Override
                    public void onSuccess(ShareLinkModel response, FFExtraParams extra) {
                        if (response != null && !TextUtils.isEmpty(response.data)) {
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dining_share_icon);
                            WeixinOpen.getInstance().share2weixin(response.data, getShareContent(),
                                    getString(R.string.dining_recommend_share_3), bitmap);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "list", builder.toString()
                , "description", getShareDesc());
    }

    /**
     * 加入到想吃清单
     * http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915879
     */
    private void addToWantEatList() {
        StringBuilder shopIdBuilder = new StringBuilder();
        StringBuilder recordIdBuilder = new StringBuilder();
        StringBuilder shopNameBuilder = new StringBuilder();

        for (SYFindMerchantInfo item : mAdapter.getCheckedList()) {
            if (shopIdBuilder.length() <= 0) {
                shopIdBuilder.append(item.getMerchantUid());
            } else {
                shopIdBuilder.append("," + item.getMerchantUid());
            }

            if (recordIdBuilder.length() <= 0) {
                recordIdBuilder.append(item.getFeedId());
            } else {
                recordIdBuilder.append("," + item.getFeedId());
            }

            if (shopNameBuilder.length() <= 0) {
                shopNameBuilder.append(item.getMerchantName());
            } else {
                shopNameBuilder.append("," + item.getMerchantName());
            }
        }

        //数据统计
        final HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("shop_id", shopIdBuilder.toString());
        event.put("shop_name", shopNameBuilder.toString());
        event.put("record_id", "");
        event.put("from", getClass().getSimpleName());


        //isResource：想吃来源，0-无来源；1-首页达人荐；2-动态列表；3-动态详情 4.pgc美食志h5详情 5.发现页商户详情 7.朋友聚餐-批量添加想吃
//        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/addBatchFoodEatV250.do",
        post(IUrlUtils.WorkDining.addBatchFoodEatV250,
                "", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if (response != null) {
                            pushEventAction("Yellow_051", event);
                            showToast("添加成功");
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "shopIds", shopIdBuilder.toString()
                , "recordIds", recordIdBuilder.toString()
                , "isResource", "7");
    }

    private String getShareContent() {
        StringBuilder builder = new StringBuilder();
        builder.append(mParams.ptypes + "、").append(mParams.peopleTypeName + "、").append(mAdapter.getCheckedList().size() + "个餐厅");
        return builder.toString();
    }

    private String getShareDesc() {
        StringBuilder builder = new StringBuilder();
        builder.append("人数：" + mParams.ptypes + " | ").append(mParams.address + " | ").append("品类：" + mParams.ptypes);
        return builder.toString();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dining_share_to_weixin) {
            shareToWeixin();
        } else if (v.getId() == R.id.dining_share_to_want_eat) {
            addToWantEatList();
        }
    }

    private static final class ShareLinkModel extends BaseResult {
        public String data;
    }

    public void pushEvent107(String position, SYFindMerchantInfo info){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("channel", Tool.getChannelName(this));
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("shop_name", info.getMerchantName());
        event.put("shop_id", info.getMerchantUid());
        event.put("score", info.getStartLevel()+"");
        event.put("position", position);
        event.put("range", info.getMerchantDistance());
        event.put("price", info.getMerchantPrice()+"");
        event.put("from", this.getClass().getName());
        pushEventAction("Yellow_107", event);
    }
}
