package com.fengnian.smallyellowo.foodie.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.fragments.RelationshipFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by Administrator on 2017-3-9.
 */

public class MyAttionFragmentActivty extends BaseActivity<IntentData> {
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;

    private BaseFragment relationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();

        setContentView(R.layout.fragment_activty_my_attion);
        fragmentTransaction = fm.beginTransaction();
        if(relationFragment==null){
            relationFragment=new RelationshipFragment(this);
            fragmentTransaction.add(R.id.fl_frame, relationFragment);
        } else {
            fragmentTransaction.show(relationFragment);
        }
        fragmentTransaction.commit();
    }
}
