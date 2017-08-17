package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.scoreshop.AddressCityListDialog;
import com.fengnian.smallyellowo.foodie.scoreshop.AddressManager;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;
import com.fengnian.smallyellowo.foodie.scoreshop.SYAddressListModel;
import com.fengnian.smallyellowo.foodie.scoreshop.SYAddressModel;
import com.fengnian.smallyellowo.foodie.scoreshop.SkuDetailActivity;

/**
 * Created by chenglin on 2017-5-8.
 */

public class AddressEditActivity extends BaseActivity<IntentData> {
    private AddressCityListDialog mCityDialog;
    private SYAddressModel mSYAddressModel;
    private TextView mMenuTv;
    private EditText mEditPeople;
    private EditText mEditPhone;
    private TextView mTvArea;
    private EditText mEditAddress;

    private OnFinishListener mFinishListener = new OnFinishListener() {
        @Override
        public void onFinish(Object object) {
            SYAddressModel syAddressModel = (SYAddressModel) object;
            if (mSYAddressModel == null) {
                mSYAddressModel = syAddressModel;
            } else {
                mSYAddressModel.province = syAddressModel.province;
                mSYAddressModel.city = syAddressModel.city;
                mSYAddressModel.region = syAddressModel.region;
            }
            mTvArea.setText(mSYAddressModel.province + mSYAddressModel.city + mSYAddressModel.region);
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEditPeople.getText().toString().trim().length() > 0 && mEditPhone.getText().toString().trim().length() > 0
                    && mTvArea.getText().toString().trim().length() > 0 && mEditAddress.getText().toString().trim().length() > 0) {
                mMenuTv.setClickable(true);
                mMenuTv.setTextColor(getResources().getColor(R.color.title_text_color));
            } else {
                mMenuTv.setClickable(false);
                mMenuTv.setTextColor(getResources().getColor(R.color.color_9));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.address_setting_edit_layout);

        mTvArea = (TextView) findViewById(R.id.tv_area);
        mEditPeople = (EditText) findViewById(R.id.p_people);
        mEditPhone = (EditText) findViewById(R.id.ed_phone);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mEditAddress = (EditText) findViewById(R.id.tv_address);

        mEditPeople.addTextChangedListener(mTextWatcher);
        mEditPhone.addTextChangedListener(mTextWatcher);
        mTvArea.addTextChangedListener(mTextWatcher);
        mEditAddress.addTextChangedListener(mTextWatcher);

        mTvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCityDialog == null) {
                    mCityDialog = new AddressCityListDialog(AddressEditActivity.this, mFinishListener);
                }
                mCityDialog.show();
            }
        });

        addMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSYAddressModel == null) {
                    return;
                }

                AddressManager.getInstance().PostAddress(AddressEditActivity.this,
                        new FFNetWorkCallBack<SYAddressListModel>() {
                            @Override
                            public void onSuccess(SYAddressListModel response, FFExtraParams extra) {
                                if (response != null && response.address != null && response.address.size() > 0) {
                                    Intent intent = new Intent(MyActions.ACTION_UPDATE_ADDRESS);
                                    intent.putExtra("isHasAddress", true);
                                    sendBroadcast(intent);

                                    showToast("保存成功");
                                    finish();
                                }
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, mEditPeople.getText().toString()
                        , mEditPhone.getText().toString()
                        , mSYAddressModel.id
                        , mSYAddressModel.province
                        , mSYAddressModel.city
                        , mSYAddressModel.region
                        , mEditAddress.getText().toString().replace("\n"," "));
            }
        });

        ViewGroup menuView = (ViewGroup) findViewById(R.id.ll_actionbar_menucontainer);
        if (menuView.getChildCount() > 0) {
            mMenuTv = (TextView) menuView.getChildAt(0);
        }

        getAddress();
    }


    private void getAddress() {
        AddressManager.getInstance().getAddress(this,true, new FFNetWorkCallBack<SYAddressListModel>() {
            @Override
            public void onSuccess(SYAddressListModel response, FFExtraParams extra) {
                if (isFinishing()) {
                    return;
                }
                if (response != null) {
                    if (SkuDetailActivity.hasAddress(response)) {
                        setAddress(response.address.get(0));
                        mMenuTv.setTextColor(getResources().getColor(R.color.title_text_color));
                        mMenuTv.setClickable(true);
                    } else {
                        mMenuTv.setTextColor(getResources().getColor(R.color.color_9));
                        mMenuTv.setClickable(false);
                    }
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
    }

    public void setAddress(SYAddressModel addressModel) {
        mSYAddressModel = addressModel;
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
            mEditPeople.setText(addressModel.name);
            mEditPhone.setText(addressModel.phone);
            mTvArea.setText(builder);
            mEditAddress.setText(addressModel.address);
        }
    }
}
