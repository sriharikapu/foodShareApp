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

public class AddressEditDialog extends Dialog {
    public static final int DIALOG_EDIT = 10;//修改地址
    public static final int DIALOG_NEW = 20;//新建地址

    private BaseActivity mBaseActivity;
    private SYAddressModel mSYAddressModel;
    private AddressCityListDialog mCityDialog;
    private OnFinishListener mSavedListener;
    private EditText mEditPeople;
    private EditText mEditPhone;
    private TextView mTvArea;
    private EditText mEditAddress;
    private Button mBtnSave;
    private TextView mDialogTitle;
    private int mDialogType = -1;

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
                mBtnSave.setEnabled(true);
            } else {
                mBtnSave.setEnabled(false);
            }
        }
    };

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

    public AddressEditDialog(int type, BaseActivity context) {
        this(context, R.style.dialog);
        mDialogType = type;
    }

    public AddressEditDialog(BaseActivity mcontext, int themeResId) {
        super(mcontext, themeResId);
        mBaseActivity = mcontext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.address_edit_layout);
        setCanceledOnTouchOutside(false);

        mEditPeople = (EditText) findViewById(R.id.p_people);
        mEditPhone = (EditText) findViewById(R.id.ed_phone);
        mTvArea = (TextView) findViewById(R.id.tv_area);
        mEditAddress = (EditText) findViewById(R.id.tv_address);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mDialogTitle = (TextView) findViewById(R.id.dialog_title);

        mEditPeople.addTextChangedListener(mTextWatcher);
        mEditPhone.addTextChangedListener(mTextWatcher);
        mTvArea.addTextChangedListener(mTextWatcher);
        mEditAddress.addTextChangedListener(mTextWatcher);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCityDialog == null) {
                    mCityDialog = new AddressCityListDialog(getContext(), mFinishListener);
                }
                mCityDialog.show();
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSYAddressModel == null) {
                    return;
                }

                AddressManager.getInstance().PostAddress(mBaseActivity,
                        new FFNetWorkCallBack<SYAddressListModel>() {
                            @Override
                            public void onSuccess(SYAddressListModel response, FFExtraParams extra) {
                                if (response != null && response.address != null && response.address.size() > 0) {
                                    SYAddressModel syAddressModel = response.address.get(0);

                                    if (mBaseActivity instanceof SkuDetailActivity) {
                                        SkuDetailActivity skuDetailActivity = (SkuDetailActivity) mBaseActivity;
                                        skuDetailActivity.onCostScore(syAddressModel);
                                    }

                                    if (mSavedListener != null) {
                                        mSavedListener.onFinish(null);
                                    }

                                    dismiss();
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

        if (mDialogType == DIALOG_NEW) {
            mDialogTitle.setText(R.string.score_address_input);
            mBtnSave.setEnabled(false);
        } else if (mDialogType == DIALOG_EDIT) {
            mDialogTitle.setText(R.string.score_address_edit);
        }
    }

    public void setSavedListener(OnFinishListener listener) {
        mSavedListener = listener;
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
