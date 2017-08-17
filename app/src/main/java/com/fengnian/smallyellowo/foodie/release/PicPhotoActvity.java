package com.fengnian.smallyellowo.foodie.release;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fengnian.smallyellowo.foodie.Adapter.ImageBucketAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;
import com.fengnian.smallyellowo.foodie.bean.publics.PhotoInfo;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册专辑下的集合图片展示
 */

public class PicPhotoActvity extends BaseActivity<PhotoInfo> implements View.OnClickListener {

    private GridView gridview;
    private ImageBucketAdapter adapter;// 自定义的适配器

    private String ismemo;//   1 是水单  ，other 不是
    private TextView tv_ismemo_text, tv_middle_title;
    public static TextView tv_right_title;
    private ImageView iv_colse;
    private static int enableColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Value.PICS_LIST.clear();
        setNotitle(true);
        PhotoInfo info = getIntentData();
        if (info == null) info = new PhotoInfo();
        ismemo = info.getIsmemo();

        List<ImageItem> mlist = info.getList();
        if (mlist == null) mlist = new ArrayList<>();
        Value.PICS_LIST.addAll(mlist);
        setContentView(R.layout.activity_pic_gallery);

        findView(R.id.rl_1).setVisibility(View.VISIBLE);

        iv_colse = findView(R.id.iv_colse);
        iv_colse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        tv_middle_title=findView(R.id.tv_middle_title);
//        tv_middle_title.setText(info.set);
        tv_right_title = findView(R.id.tv_right_title);
        tv_right_title.setText("完成(" + Config.list.size() + ")");
        tv_right_title.setOnClickListener(this);
        enableColor = tv_right_title.getCurrentTextColor();
        refreshCompleteUI();

        tv_ismemo_text = findView(R.id.tv_ismemo_text);
        if ("1".equals(ismemo)) {
            tv_ismemo_text.setVisibility(View.VISIBLE);
        }

        gridview = (GridView) findViewById(R.id.gridview);

        GalleryInfo gall=new GalleryInfo();
        gall.setIndex(info.getIndex());
        gall.setTemplate_type(info.getTemplate_type());
        adapter = new ImageBucketAdapter(this,gall);
        gridview.setAdapter(adapter);

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                  PhotoInfo info=new PhotoInfo();
//                  info.setList(llist);
//                  info.setPos(i);
//                  startActivity(ScrollPhotoActvity.class,info);
//            }
//        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkIsSelect();
        adapter.notifyDataSetChanged();
        tv_right_title.setText("完成(" + Config.list.size() + ")");
    }

    void    checkIsSelect(){
//        for(int x=0;x<llist.size();x++){
//            for (int i = 0; i< Config.list.size(); i++){
//                ImageItem temp_item=Config.list.get(i);
//                ImageItem item_image_gallery=llist.get(x);
//                if(temp_item.getImageId().equals(item_image_gallery.getImageId())) {
//                    item_image_gallery.setSelected(true);
//                }
//            }
//        }

        if(Config.list.size()==0){
            for(int x=0;x<Value.PICS_LIST.size();x++){
                ImageItem item=Value.PICS_LIST.get(x);
                item.setSelected(false);

            }
        }

        GalleryAndPhotoActvity.tv_right_title.setText("完成(" + Config.list.size() + ")");
        GalleryAndPhotoActvity.refreshCompleteUI();


            boolean enable = false;
            if (Config.list.size() > 0) {
                enable = true;
            } else {
                enable = false;
            }
            if (tv_right_title != null) {
                tv_right_title.setEnabled(enable);
                if (enable) {
                    tv_right_title.setTextColor(enableColor);
                } else {
                    tv_right_title.setTextColor(Color.GRAY);
                }
            }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title:
                DraftModelManager.onImagesPicked1(this, DraftModelManager.getCurrentDraft(), Config.getStringlist(),getIntentData().getIndex());
                Config.list.clear();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Value.PICS_LIST.clear();
    }

    public static void refreshCompleteUI() {
        boolean enable = false;
        if (Config.list.size() > 0) {
            enable = true;
        } else {
            enable = false;
        }

        if (tv_right_title != null) {
            tv_right_title.setEnabled(enable);
            if (enable) {
                tv_right_title.setTextColor(enableColor);
            } else {
                tv_right_title.setTextColor(Color.GRAY);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            setResult(RESULT_OK);
           finish();
        }
    }
}
