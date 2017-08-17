package com.fengnian.smallyellowo.foodie.dialogs;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestLocationActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.shopcommiterror.ShopErrorTypeActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-12-20.
 */

public class PopRest extends PopupWindow {
    View view;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.tv_phone)
    TextView tvPhone;


    /**
     * @param activity
     * @param fragment
     * @param phone
     * @param address
     * @param name
     * @param lat
     * @param lng
     * @param shopid
     */
    public PopRest(final Activity activity, final BaseFragment fragment, final String phone, final String address, final String name, final double lat, final double lng, final String shopid) {
        super(activity.getLayoutInflater().inflate(R.layout.pop_rest, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


        view = getContentView();

//        view.findViewById(R.id.v_bottom).getLayoutParams().height = FFUtils.getBottomStatusHeight();
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTouchable(true);

        setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        setBackgroundDrawable(new ColorDrawable(0x45000000));


        view.findViewById(R.id.view_alpha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);

        tvName.setText(name);

        if (!FFUtils.isStringEmpty(phone)) {
            tvPhone.setVisibility(View.VISIBLE);
            tvPhone.setText(phone);
            tvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    new CallDialog(activity, R.style.ActionSheetDialogStyle, phone).show();
                }
            });
        } else {
            tvPhone.setVisibility(View.GONE);
        }

        tvLocation.setText(address);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (fragment != null) {
                    fragment.startActivity(RestLocationActivity.class, new RestLocationIntent().
                            setAddress(address).
                            setName(name).
                            setLng(lng).
                            setLat(lat));
                }else {

                    ((BaseActivity) activity).startActivity(RestLocationActivity.class, new RestLocationIntent().
                            setAddress(address).
                            setName(name).
                            setLng(lng).
                            setLat(lat));
                }
            }
        });

        if(!SP.isLogin()){
            view.findViewById(R.id.iv_commit_error).setVisibility(View.GONE);
        }

        view.findViewById(R.id.iv_commit_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FFUtils.isStringEmpty(SP.getUid())) {
                    LoginIntentData loginIntentData = new LoginIntentData();
                    loginIntentData.setCode("");
                    ((BaseActivity) activity).startActivity(LoginOneActivity.class, loginIntentData);
                    return;
                }
                ShopErrorInfoIntent error = new ShopErrorInfoIntent();
                error.setAddress(address);
                error.setPhone(phone);
                error.setName(name);
                error.setLat(lat);
                error.setLng(lng);
                error.setShopid(shopid);
                ((BaseActivity) activity).startActivity(ShopErrorTypeActivity.class, error);
            }
        });
    }

}
