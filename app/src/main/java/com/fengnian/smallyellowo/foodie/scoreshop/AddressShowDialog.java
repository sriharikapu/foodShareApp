package com.fengnian.smallyellowo.foodie.scoreshop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;

/**
 * Created by chenglin on 2017-5-5.
 */

public class AddressShowDialog extends Dialog {
    private BaseActivity mBaseActivity;
    private SYAddressModel mSYAddressModel;
    private TextView mEditPeople;
    private TextView mEditPhone;
    private TextView mTvArea, mTvCity;
    private Button mBtnEdit, mBtnSure;


    public AddressShowDialog(BaseActivity context) {
        this(context, R.style.dialog);
    }

    public AddressShowDialog(BaseActivity context, int themeResId) {
        super(context, themeResId);
        mBaseActivity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.address_show_layout);
        setCanceledOnTouchOutside(false);

        mEditPeople = (TextView) findViewById(R.id.p_people);
        mEditPhone = (TextView) findViewById(R.id.ed_phone);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mBtnEdit = (Button) findViewById(R.id.btn_edit);
        mBtnSure = (Button) findViewById(R.id.btn_sure);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //修改地址
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressEditDialog addressEditDialog = new AddressEditDialog(AddressEditDialog.DIALOG_EDIT, mBaseActivity);
                addressEditDialog.show();
                addressEditDialog.setAddress(mSYAddressModel);

                addressEditDialog.setSavedListener(new OnFinishListener() {
                    @Override
                    public void onFinish(Object object) {
                        dismiss();
                    }
                });
            }
        });

        //确定--积分兑换商品
        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mBaseActivity instanceof SkuDetailActivity) {
                    SkuDetailActivity skuDetailActivity = (SkuDetailActivity) mBaseActivity;
                    skuDetailActivity.onCostScore(mSYAddressModel);
                }
            }
        });
    }

    public void setAddress(SYAddressModel addressModel) {
        mSYAddressModel = addressModel;
        StringBuilder builderCity = new StringBuilder();
        if (!TextUtils.isEmpty(addressModel.province)) {
            builderCity.append(addressModel.province);
        }
        if (!TextUtils.isEmpty(addressModel.city)) {
            builderCity.append(addressModel.city);
        }
        if (!TextUtils.isEmpty(addressModel.region)) {
            builderCity.append(addressModel.region);
        }
        mTvCity.setText(builderCity);

        if (!TextUtils.isEmpty(addressModel.address)) {
            mTvArea.setText(addressModel.address);
        }

        if (!TextUtils.isEmpty(addressModel.name)) {
            mEditPeople.setText(addressModel.name);
        }

        if (!TextUtils.isEmpty(addressModel.phone)) {
            mEditPhone.setText(addressModel.phone);
        }
    }
}
