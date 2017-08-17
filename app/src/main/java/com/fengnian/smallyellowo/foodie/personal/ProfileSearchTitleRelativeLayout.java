package com.fengnian.smallyellowo.foodie.personal;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by chenglin on 2017-4-13.
 */

public class ProfileSearchTitleRelativeLayout extends RelativeLayout implements View.OnClickListener {
    private EditText mEditSearch;
    private View mClearView;

    public ProfileSearchTitleRelativeLayout(Context context) {
        super(context);
    }

    public ProfileSearchTitleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        init();
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mEditSearch = (EditText) findViewById(R.id.et_input);
        mClearView = findViewById(R.id.iv_clear);
        mClearView.setOnClickListener(this);

        if (mEditSearch.length() > 0) {
            mClearView.setVisibility(View.VISIBLE);
        } else {
            mClearView.setVisibility(View.GONE);
        }

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mClearView.setVisibility(View.VISIBLE);
                } else {
                    mClearView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back || v.getId() == R.id.tv_cancel) {
            if (getContext() instanceof Activity) {
                Activity activity = (Activity) getContext();
                activity.finish();
            }
        } else if (v.getId() == R.id.iv_clear) {
            mEditSearch.setText("");
        }
    }
}
