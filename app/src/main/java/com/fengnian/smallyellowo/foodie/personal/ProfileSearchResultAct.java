package com.fengnian.smallyellowo.foodie.personal;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by chenglin on 2017-4-1.
 */

public class ProfileSearchResultAct extends BaseActivity<IntentData> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNotitle(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProfileSearchResultFragment fragment = new ProfileSearchResultFragment();
        Bundle argBundle = new Bundle();
        argBundle.putString("keyWord",getIntent().getStringExtra("keyWord"));
        transaction.add(R.id.ll_baseActivity_content, fragment);
        transaction.commit();
    }


}
