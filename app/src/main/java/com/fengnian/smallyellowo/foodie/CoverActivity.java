package com.fengnian.smallyellowo.foodie;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-11-24.
 */

public class CoverActivity extends CoverBaseActivity {
    @Bind(R.id.rv_imgs)
    protected RecyclerView rvImgs;
    @Bind(R.id.v_info_bg)
    protected View vInfoBg;
    @Bind(R.id.tv_poi_title)
    protected TextView tvPoiTitle;
    @Bind(R.id.iv_avator)
    protected ImageView ivAvator;
    @Bind(R.id.iv_add_crown)
    protected ImageView ivAdd_crown;
    private GalleryAdapter mAdapter;

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImgs.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(this);
        rvImgs.setAdapter(mAdapter);


        IsAddCrownUtils.checkIsAddCrow(SP.getUser(),ivAdd_crown);
        FFImageLoader.loadAvatar(this, SP.getUser().getHeadImage().getUrl(), ivAvator);
        setText(tvPoiTitle, draft.getFeed().getFood().getPoi() == null ? null : draft.getFeed().getFood().getPoi().getTitle());
    }

    public static void setText(TextView tv, String text) {
        if (FFUtils.isStringEmpty(text)) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    @Override
    public void onSoftInputMethInvis(int softInputHight) {
        super.onSoftInputMethInvis(softInputHight);
    }

    @Override
    public void onSoftInputMethVis(int softInputHight) {
        super.onSoftInputMethVis(softInputHight);
    }
}
