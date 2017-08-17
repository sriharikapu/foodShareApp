package com.fengnian.smallyellowo.foodie.scoreshop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by chenglin on 2017-5-4.
 */

public class SkuDetailActivity extends BaseActivity<IntentData> {
    private String mSkuId;
    private float mTotalScore;
    private float mSkuScore;
    private LinearLayout mItemListLinear;
    private ImageView mSkuImage;
    private Button mBtnCost;
    private int exchangeAvailable = -1;//0是可兑换；1是不可兑换;
    private boolean isRequest = false;

    public static void start(Activity activity, String id, String score) {
        Intent intent = new Intent(activity, SkuDetailActivity.class);
        intent.putExtra("sku_id", id);
        intent.putExtra("score", score);
        activity.startActivity(intent);
    }

    /**
     * 发起用积分兑换商品
     */
    public void onCostScore(final SYAddressModel syAddressModel) {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shopmall/shopExchange.do", "",
        post(IUrlUtils.Search.shopExchange, "",
                null, new FFNetWorkCallBack<SkuDetailModel>() {
                    @Override
                    public void onSuccess(SkuDetailModel response, FFExtraParams extra) {
                        if (response != null) {
                            showToast("兑换成功");
                            mTotalScore = mTotalScore - mSkuScore;
                            if (mTotalScore <= 0) {
                                exchangeAvailable = 1;
                                mBtnCost.setText(R.string.score_btn_text_2);
                            }

                            Intent intent = new Intent(MyActions.ACTION_UPDATE_SCORE);
                            intent.putExtra("score", mTotalScore);
                            LocalBroadcastManager.getInstance(SkuDetailActivity.this).sendBroadcast(intent);
                        }
                    }

                    @Override
                    public boolean onFailContext(SkuDetailModel response, FFExtraParams extra) {
                        return super.onFailContext(response, extra);
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        exchangeAvailable = 1;
                        mBtnCost.setText(R.string.score_btn_text_2);
                        return false;
                    }
                }, "mallActivityProductId", mSkuId
                , "name", syAddressModel.name
                , "phone", syAddressModel.phone
                , "id", syAddressModel.id
                , "province", syAddressModel.province
                , "city", syAddressModel.city
                , "region", syAddressModel.region
                , "address", syAddressModel.address);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSkuId = getIntent().getStringExtra("sku_id");
        String totalScore = getIntent().getStringExtra("score");

        if (!TextUtils.isEmpty(totalScore) && TextUtils.isDigitsOnly(totalScore.replace(".", ""))) {
            mTotalScore = Float.parseFloat(totalScore);
        }

        setContentView(R.layout.sku_detail_layout);
        initView();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mItemListLinear = (LinearLayout) findViewById(R.id.item_list);
        mItemListLinear.removeAllViews();

        mBtnCost = (Button) findViewById(R.id.btn_cost);
        mBtnCost.setText("");
        mSkuImage = (ImageView) findViewById(R.id.sku_image);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSkuImage.getLayoutParams();
        params.width = DisplayUtil.screenWidth;
        params.height = DisplayUtil.screenWidth / 2;
        mSkuImage.setLayoutParams(params);

        //立即兑换
        findViewById(R.id.btn_cost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exchangeAvailable == -1) {
                    return;
                }
                if (exchangeAvailable == 0) {
                    getAddress();
                } else {
                    WebInfo info = new WebInfo();
                    info.setUrl(ScoreShopActivity.SCORE_RULE);
                    info.setTitle("积分规则");
                    startActivity(CommonWebviewUtilActivity.class, info);
                }
            }
        });
    }

    private void getData() {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/shopmall/queryMallProductInfo.do", "",
        post(IUrlUtils.Search.queryMallProductInfo, "",
                null, new FFNetWorkCallBack<SkuDetailModel>() {
                    @Override
                    public void onSuccess(SkuDetailModel response, FFExtraParams extra) {
                        if (response != null) {
                            setView(response);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "mallActivityProductId", mSkuId);
    }


    public void getAddress() {
        if (isRequest) {
            return;
        } else {
            isRequest = true;
        }

        AddressManager.getInstance().getAddress(this,true, new FFNetWorkCallBack<SYAddressListModel>() {
            @Override
            public void onSuccess(SYAddressListModel response, FFExtraParams extra) {
                if (isFinishing()) {
                    return;
                }
                if (response != null) {
                    if (hasAddress(response)) {
                        AddressShowDialog addressShowDialog = new AddressShowDialog(SkuDetailActivity.this);
                        addressShowDialog.show();
                        addressShowDialog.setAddress(response.address.get(0));
                    } else {
                        AddressEditDialog addressEditDialog = new AddressEditDialog(AddressEditDialog.DIALOG_NEW, SkuDetailActivity.this);
                        addressEditDialog.show();
                    }
                }

                FFUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isRequest = false;
                    }
                }, 300);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                isRequest = false;
                return false;
            }
        });
    }

    public static boolean hasAddress(SYAddressListModel syAddressListModel) {
        SYAddressModel addressModel = null;
        if (syAddressListModel.address != null && syAddressListModel.address.size() > 0) {
            addressModel = syAddressListModel.address.get(0);
        }

        if (addressModel == null) {
            return false;
        }

        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(addressModel.province)) {
            builder.append(addressModel.province);
        }
        if (!TextUtils.isEmpty(addressModel.city)) {
            builder.append(addressModel.city);
        }
        if (!TextUtils.isEmpty(addressModel.region)) {
            builder.append(addressModel.region);
        }

        if (builder.length() > 0
                && !TextUtils.isEmpty(addressModel.name)
                && !TextUtils.isEmpty(addressModel.phone)
                && !TextUtils.isEmpty(addressModel.address)) {
            return true;
        }
        return false;
    }

    private void setView(SkuDetailModel model) {
        if (model.syGoodsModel == null) {
            return;
        }
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_score = (TextView) findViewById(R.id.tv_score);
        TextView tv_num = (TextView) findViewById(R.id.tv_num);

        tv_title.setText(model.syGoodsModel.goodsTitle);
        tv_num.setText("库存：" + model.syGoodsModel.goodsStock);

        if (!TextUtils.isEmpty(model.syGoodsModel.goodsPoints)) {
            if (TextUtils.isDigitsOnly(model.syGoodsModel.goodsPoints.replace(".", ""))) {
                mSkuScore = Float.parseFloat(model.syGoodsModel.goodsPoints);
            }

            SpannableString spannableString = new SpannableString(model.syGoodsModel.goodsPoints + "积分");
            int color = Color.parseColor("#F9A825");
            spannableString.setSpan(new ForegroundColorSpan(color), 0, model.syGoodsModel.goodsPoints.length(), 0);
            tv_score.setText(spannableString);
        }

        if (model.syGoodsModel.goodsImage != null && !TextUtils.isEmpty(model.syGoodsModel.goodsImage.getUrl())) {
            FFImageLoader.loadBigImage(this, model.syGoodsModel.goodsImage.getUrl(), mSkuImage);
        }

        exchangeAvailable = model.exchangeAvailable;
        if (exchangeAvailable == 0) {
            mBtnCost.setText(R.string.score_btn_text_1);
        } else {
            mBtnCost.setText(R.string.score_btn_text_2);
        }

        if (model.syGoodsModel.goodsIntroductionList != null) {
            mItemListLinear.removeAllViews();
            for (SYGoodsIntroduction info : model.syGoodsModel.goodsIntroductionList) {
                if (info == null) {
                    break;
                }
                View view = View.inflate(this, R.layout.sku_detail_item, null);
                TextView sku_title = (TextView) view.findViewById(R.id.sku_title);
                TextView sku_desc = (TextView) view.findViewById(R.id.sku_desc);
                sku_title.setText(info.goodsIntroductionTitle);

                if (info.goodsIntroductionInfoList != null && info.goodsIntroductionInfoList.length > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < info.goodsIntroductionInfoList.length; i++) {
                        builder.append(info.goodsIntroductionInfoList[i]);
                        if (i != info.goodsIntroductionInfoList.length - 1) {
                            builder.append("\n");
                        }
                    }
                    sku_desc.setText(builder.toString());
                }

                mItemListLinear.addView(view);
            }
        }
    }

    public static final class SkuDetailModel extends BaseResult {
        public int exchangeAvailable; //0是可兑换；1是不可兑换;
        public SYGoodsModel syGoodsModel;
    }
}
