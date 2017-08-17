package com.fengnian.smallyellowo.foodie.diningcase;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestLocationActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by chenglin on 2017-6-21.
 */

public class WorkDiningItemDialog extends Dialog implements View.OnClickListener {
    private BaseActivity mActivity;
    private ImageView mImage;
    private String mShopId = "0";
    private SYFindMerchantInfo mSYFindMerchantInfo;

    public WorkDiningItemDialog(BaseActivity context) {
        this(context, R.style.dialog);
        mActivity = context;
    }

    public WorkDiningItemDialog(BaseActivity context, int themeResId) {
        super(context, themeResId);
        mActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Window window = this.getWindow();
//        window.getDecorView().setPadding(0, -DisplayUtil.dip2px(45f), 0, 0);

        setCanceledOnTouchOutside(false);
        setContentView(R.layout.work_dining_item_dialog_layout);
        mImage = (ImageView) findViewById(R.id.image);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_phone).setOnClickListener(this);
        findViewById(R.id.tv_address).setOnClickListener(this);
        findViewById(R.id.dining_get_item).setOnClickListener(this);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImage.getLayoutParams();
        params.height = (int) ((DisplayUtil.screenWidth - DisplayUtil.dip2px(30f) * 2) * 0.8f);
        mImage.setLayoutParams(params);
    }

    public void getData() {
        if (mSYFindMerchantInfo != null && !TextUtils.isEmpty(mSYFindMerchantInfo.getMerchantUid())) {
            mShopId = mSYFindMerchantInfo.getMerchantUid();
        } else {
            mShopId = "0";
        }

//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/getShopRandomByAccount.do",
        mActivity.post(IUrlUtils.WorkDining.getShopRandomByAccount,
                "", null, new FFNetWorkCallBack<GetItemModel>() {
                    @Override
                    public void onSuccess(GetItemModel response, FFExtraParams extra) {
                        if (response != null && response.data != null) {
                            setData(null);
                            mSYFindMerchantInfo = response.data;
                            setData(response.data);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "openGPS", "0"
                , "shopId", mShopId
                , "longitude", Value.mLongitude
                , "latitude", Value.mLatitude);
    }

    private void setData(SYFindMerchantInfo item) {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        RatingBar rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        TextView tv_level = (TextView) findViewById(R.id.tv_level);
        TextView tv_distance = (TextView) findViewById(R.id.tv_distance);
        TextView tv_avarge_money = (TextView) findViewById(R.id.tv_avarge_money);
        TextView tv_business_name = (TextView) findViewById(R.id.tv_business_name);
        TextView tv_address = (TextView) findViewById(R.id.tv_address);
        TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        View line2 = findViewById(R.id.line2);

        if (item == null) {
            tv_title.setText("");
            rating_bar.setRating(0f);
            tv_level.setText("");
            tv_distance.setText("");
            tv_avarge_money.setText("");
            tv_business_name.setText("");
            tv_address.setText("");
            tv_phone.setText("");
            imageView.setImageResource(0);
        } else {
            if (!TextUtils.isEmpty(item.getMerchantName())) {
                tv_title.setText(item.getMerchantName());
            } else {
                tv_title.setText("");
            }

            if (item.getStartLevel() > 0) {
                tv_level.setVisibility(View.VISIBLE);
                rating_bar.setVisibility(View.VISIBLE);
                tv_level.setText(item.getStartLevel() + "");
                rating_bar.setRating(item.getStartLevel());
            } else {
                tv_level.setVisibility(View.GONE);
                rating_bar.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.getMerchantDistance())) {
                tv_distance.setVisibility(View.VISIBLE);
                tv_distance.setText(item.getMerchantDistance());
            } else {
                tv_distance.setVisibility(View.GONE);
                tv_distance.setText("");
            }

            if (item.getMerchantPrice() > 0) {
                tv_avarge_money.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                tv_avarge_money.setText("¥" + item.getMerchantPrice() + "/人");
            } else {
                tv_avarge_money.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                tv_avarge_money.setText("");
            }

            if (!TextUtils.isEmpty(item.getMerchantKind())) {
                tv_business_name.setText(item.getMerchantKind());
            } else {
                tv_business_name.setText("");
            }

            if (!TextUtils.isEmpty(item.getMerchantAddress())) {
                tv_address.setVisibility(View.VISIBLE);
                tv_address.setText(item.getMerchantAddress());
            } else {
                tv_address.setVisibility(View.GONE);
                tv_address.setText("");
            }

            if (!TextUtils.isEmpty(item.getMerchantPhone())) {
                tv_phone.setVisibility(View.VISIBLE);
                tv_phone.setText(item.getMerchantPhone());
            } else {
                tv_phone.setVisibility(View.GONE);
                tv_phone.setText("");
            }


            if (item.getMerchantImage() != null && !TextUtils.isEmpty(item.getMerchantImage().getThumbUrl())) {
                FFImageLoader.loadMiddleImage(mActivity, item.getMerchantImage().getThumbUrl(), imageView);
            } else {
                imageView.setImageResource(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            dismiss();
        } else if (v.getId() == R.id.tv_phone) {
            if (mSYFindMerchantInfo != null && !TextUtils.isEmpty(mSYFindMerchantInfo.getMerchantPhone())) {
                Uri telUri = Uri.parse("tel:" + mSYFindMerchantInfo.getMerchantPhone());
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                mActivity.startActivity(intent);
            }

        } else if (v.getId() == R.id.tv_address) {
            if (mSYFindMerchantInfo != null && !TextUtils.isEmpty(mSYFindMerchantInfo.getMerchantAddress())) {
                mActivity.startActivity(RestLocationActivity.class,
                        new RestLocationIntent().
                                setAddress(mSYFindMerchantInfo.getMerchantAddress()).
                                setName(mSYFindMerchantInfo.getMerchantName()).
                                setLng(mSYFindMerchantInfo.getMerchantPoi().getLongitude()).
                                setLat(mSYFindMerchantInfo.getMerchantPoi().getLatitude()));
            }

        } else if (v.getId() == R.id.dining_get_item) {
            getData();
        }
    }

    private static final class GetItemModel extends BaseResult {
        public SYFindMerchantInfo data;
    }
}
