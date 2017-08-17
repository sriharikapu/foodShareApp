package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageBucket;

import java.util.List;

/**
 * Created by Administrator on 2016-9-30.
 */

public class AlbumPhotoAdapter extends BaseAdapter {
    private List<ImageBucket>  list;
    private FFContext mcontext;
    public AlbumPhotoAdapter(FFContext context, List<ImageBucket> list) {
        this.list=list;
        this.mcontext=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        viewholder holder=null;
        if(view==null){
            holder=new viewholder();
            view=View.inflate((Context) mcontext, R.layout.item_album_photo,null);
            holder.iv_img= (ImageView) view.findViewById(R.id.iv_avator);
            holder.tv_name= (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);

        }else{
            holder= (viewholder) view.getTag();
        }
        final ImageBucket  bucket=list.get(i);
        String name=bucket.getBucketName();
        int count=bucket.getCount();

        if (bucket.imageList != null && bucket.imageList.size() > 0) {
            String url=bucket.getImageList().get(0).getImagePath();
            FFImageLoader.loadMiddleImage(mcontext, url,holder.iv_img)  ;//"file://" +
        }
        holder.tv_name.setText(name+"   ("+count+")");
        return view;
    }

    private  class     viewholder{
        private ImageView  iv_img;
        private TextView  tv_name;
    }
}
