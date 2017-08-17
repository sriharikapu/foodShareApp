package com.fengnian.smallyellowo.foodie.release;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fengnian.smallyellowo.foodie.Adapter.ScrollImgAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;
import com.fengnian.smallyellowo.foodie.bean.publics.PhotoInfo;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bigpicture.ImageGallery;
import com.fengnian.smallyellowo.foodie.bigpicture.ImageGalleryAdapter;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-10-6.
 */

public class ScrollPhotoActvity extends BaseActivity<PhotoInfo> implements View.OnClickListener, ImageGalleryAdapter.LoadImagePercentListener{
    private ImageGallery gallery;
    private  ScrollImgAdapter adapter;


    ViewPager view_page;
    int  selecpos;//
    private RelativeLayout rl_1;

    private ImageView iv_back;
    public  static  ImageView  iv_right;
    public  static TextView tv_select;

    private FrameLayout fl_title,fl_select;
    private  int sty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        sty= getIntentData().getSty();
        PhotoInfo info=getIntentData();
        selecpos=info.getPos();
        setContentView(R.layout.activity_scroll_pic);
        rl_1=findView(R.id.rl_1);
        fl_title=findView(R.id.fl_title);
        fl_select=findView(R.id.fl_select);
        gallery=findView(R.id.gallery);
        adapter=new ScrollImgAdapter(this,sty,getIntentData());
        gallery.setAdapter(adapter);
        gallery.setSelection(selecpos);

        iv_back=findView(R.id.iv_back);
        iv_back.setOnClickListener(this);

        iv_right=findView(R.id.iv_right);

        tv_select=findView(R.id.tv_select);


        view_page=findView(R.id.view_page);
//        iv_right.setOnClickListener(this);
//        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                ScrollPhotoActvity.this.finish();
//            }
//        });
        int len=getIntentData().getNum()+Config.list.size();
        if(RichTextModelManager.getConfigByIndex(sty).picture.pictureCount!=-1){
            tv_select.setText("下一步("+len+"/9)");
        }else{
            tv_select.setText("下一步("+len+")");
        }

        tv_select.setOnClickListener(this);

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageItem item=Value.PICS_LIST.get(position);
                if(item.isSelected)
                    ScrollPhotoActvity.iv_right.setImageResource(R.mipmap.picture_selected);
                else{
                    ScrollPhotoActvity.iv_right.setImageResource(R.mipmap.picture_no_selected);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_select:
                if( ( Config.list.size()+getIntentData().getNum())==0) {
                    showToast("未选择哦！");
                    return;
                }
                DraftModelManager.onImagesPicked1(this, DraftModelManager.getCurrentDraft(), Config.getStringlist(), getIntentData().getIndex());
                Config.list.clear();
                break;

        }
    }

    @Override
    public void onLoadImagePercent(int nowPercent, int total, int position, boolean fail) {
        if(position != gallery.getSelectedItemPosition()){
            return;
        }
    }
}
