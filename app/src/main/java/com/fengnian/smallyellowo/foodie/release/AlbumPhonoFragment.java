package com.fengnian.smallyellowo.foodie.release;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fengnian.smallyellowo.foodie.Adapter.AlbumPhotoAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageBucket;
import com.fengnian.smallyellowo.foodie.bean.publics.PhotoInfo;

import java.util.List;

/**
 * 相册专辑列表
 */

public class AlbumPhonoFragment extends BaseFragment {
    private ListView lv_listview;

    private List<ImageBucket> list;

    private AlbumPhotoAdapter adapter;
    private String ismemo;  //1 是水单 ，other 不是

    private  int temp_index;

    private GalleryInfo galleryInfo;
    public void setAlbumPhono(List<ImageBucket> list, GalleryInfo info) {
        this.list = list;
        this.ismemo = info.getIsmemo();
        this.temp_index=info.getIndex();
        this.galleryInfo=info;
    }

    public AlbumPhonoFragment() {
    }

    @Override
    public void onFindView() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        adapter = new AlbumPhotoAdapter(getBaseActivity(), list);
        lv_listview.setAdapter(adapter);


        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageBucket bucket = list.get(i);
                PhotoInfo info = new PhotoInfo();
                if ("1".equals(ismemo)) {
                    info.setIsmemo("1");
                } else {
                    info.setIsmemo("2");
                }
                info.setList(bucket.getImageList());
                info.setIndex(((GalleryAndPhotoActvity) activity).getIntentData().getIndex());
                info.setTemplate_type(galleryInfo.getTemplate_type());
                info.setRequestCode(10086);
                startActivity(PicPhotoActvity.class, info);
            }
        });
    }


    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activty_album_photo, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        int len=galleryInfo.getNum()+Config.s
//        GalleryAndPhotoActvity.tv_right_title.setText("完成(" + +Config.list.size() + ")");
//        GalleryAndPhotoActvity.refreshCompleteUI();
    }
}
