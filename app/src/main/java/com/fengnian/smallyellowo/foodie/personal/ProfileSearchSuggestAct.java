package com.fengnian.smallyellowo.foodie.personal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.emoji.EmojiUtils;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by chenglin on 2017-3-31.
 */

public class ProfileSearchSuggestAct extends BaseActivity<IntentData> {
    private ProfileSearchSuggestAdapter mAdapter;
    private ListView mListView;
    private EditText mEditSearch;
    private View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_search_suggest_layout);
        mListView = findView(R.id.listview);
        mEditSearch = (EditText) findViewById(R.id.et_input);

        mAdapter = new ProfileSearchSuggestAdapter(this);
        addListHeadView();
        mListView.setAdapter(mAdapter);
        setNotitle(true);
        initView();

        getData();
    }

    private void initView() {
        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = mEditSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(keyWord)) {
                        EmojiUtils.hideKeyboard(ProfileSearchSuggestAct.this, mEditSearch);
                        Intent intent = new Intent(ProfileSearchSuggestAct.this, ProfileSearchResultAct.class);
                        intent.putExtra("keyWord", mEditSearch.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("请输入你要搜索的关键字");
                    }
                }
                return false;
            }
        });
    }

    private void getData() {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/myCenter/searchGuide.do",
        post(IUrlUtils.Search.searchGuide,
                "", null, new FFNetWorkCallBack<ProfileSearchSuggestAdapter.ProfileSearchList>() {
                    @Override
                    public void onSuccess(ProfileSearchSuggestAdapter.ProfileSearchList response, FFExtraParams extra) {
                        if (response != null) {
                            mAdapter.setData(response.resultData);
                        }
                        if (mAdapter.getCount() <= 0) {
                            mHeaderView.setVisibility(View.GONE);
                        } else {
                            mHeaderView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                });
    }


    private void addListHeadView() {
        mHeaderView = View.inflate(this, R.layout.profile_search_head_view, null);
        mListView.addHeaderView(mHeaderView);
    }


}
