package com.fengnian.smallyellowo.foodie.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.fan.framework.config.Value;
import com.fengnian.smallyellowo.foodie.Adapter.ImageBucketAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageBucket;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 相片缩略图
 */

public class PicGalleryFragment extends BaseFragment {
    private GridView gridview;
    private ImageBucketAdapter adapter;// 自定义的适配器
    public List<ImageBucket> dataLists;

    private String ismemo;//是否是水单文件

    private int temp_index;
    private  int sty;

    private GalleryInfo  info;
    public PicGalleryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Value.PICS_LIST.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Value.PICS_LIST.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Value.PICS_LIST.clear();
    }

    public void setPicGallery(List<ImageBucket> list, GalleryInfo info) {
        this.dataLists = list;
        this.ismemo = info.getIsmemo();
        this.temp_index=info.getIndex();
        this.sty=info.getTemplate_type();
        this.info=info;

    }


    @Override
    public void onFindView() {

        initList();
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(getBaseActivity(), info);
        gridview.setAdapter(adapter);

//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//               PhotoInfo info=new PhotoInfo();
//                info.setList(llist);
//                info.setPos(i);
//                startActivity(ScrollPhotoActvity.class,info);
//            }
//        });

//        DraftModelManager.onImagesPicked(null);
    }
    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pic_gallery, null);
        return view;
    }

    private void initList() {
        Value.PICS_LIST.clear();
        int length = dataLists.size();
        for (int i = 0; i < length; i++) {
            List<ImageItem> list = dataLists.get(i).getImageList();
            Value.PICS_LIST.addAll(list);
        }
        Collections.sort(Value.PICS_LIST, new Comparator<ImageItem>() {
            @Override
            public int compare(ImageItem o1, ImageItem o2) {
                if (o1.modify > o2.modify) {
                    return -1;
                }
                if (o1.modify < o2.modify) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIsSelect();
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示

     }
             else {
            // 重新显示到最前端中
            checkIsSelect();
            adapter.notifyDataSetChanged();
        }

    }

    void    checkIsSelect(){


//            for(int x=0;x<llist.size();x++){
//                for (int i = 0; i< Config.list.size(); i++){
//                ImageItem temp_item=Config.list.get(i);
//                ImageItem item_image_gallery=llist.get(x);
//                if(temp_item.getImageId().equals(item_image_gallery.getImageId())) {
//                    item_image_gallery.setSelected(true);
//                }else{
//
//                }
//            }
//        }

        if(Config.list.size()==0){
            for(int x=0;x<Value.PICS_LIST.size();x++){
                ImageItem item=Value.PICS_LIST.get(x);
                    item.setSelected(false);

            }
        }

        int len=Config.list.size()+info.getNum();
        if(RichTextModelManager.getConfigByIndex(sty).picture.pictureCount!=-1){

                GalleryAndPhotoActvity.tv_right_title.setText("下一步(" +len + "/9)");

        }else{
            GalleryAndPhotoActvity.tv_right_title.setText("下一步(" + len + ")");
        }
    }


}