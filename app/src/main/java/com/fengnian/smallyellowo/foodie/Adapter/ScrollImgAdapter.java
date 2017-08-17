package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fan.framework.base.FFContext;
import com.fan.framework.config.Value;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.imageloader.PercentImageView;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;
import com.fengnian.smallyellowo.foodie.bean.publics.PhotoInfo;
import com.fengnian.smallyellowo.foodie.release.ScrollPhotoActvity;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.List;


/**
 * Created by Administrator on 2016-10-6.
 */

public class ScrollImgAdapter extends BaseAdapter {

    private Context mcontext;
    private int sty;
    private  PhotoInfo  photoInfo;
    public ScrollImgAdapter(Context mcontext, int sttt, PhotoInfo info) {
        this.mcontext = mcontext;
        this.sty= sttt;
        this.photoInfo=info;
    }

    @Override
    public int getCount() {
        return Value.PICS_LIST.size();
    }

    @Override
    public Object getItem(int i) {
        return Value.PICS_LIST.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
     /*   viewholder holder=null;
        if(view==null){
            holder=new viewholder();
            view=View.inflate((Context) mcontext,R.layout.item_scroll_img,null);
            holder.iv_img= (ImageView) view.findViewById(R.id.iv_img);
            view.setTag(holder);
        }else{
            holder= (viewholder) view.getTag();
    }*/
//        PercentImageView view;
//        if (convertView != null) {
//            ((PercentImageView) convertView).clearCache();
//            view = (PercentImageView) convertView;
//        } else {
            PercentImageView  view = new PercentImageView(mcontext);
//        }

         final ImageItem  item=Value.PICS_LIST.get(i);


        ScrollPhotoActvity.iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pictureCount = RichTextModelManager.getConfigByIndex(sty).picture.pictureCount;
                if(item.isSelected)
                {  ScrollPhotoActvity.iv_right.setImageResource(R.mipmap.picture_no_selected);
                    item.setSelected(false);
                    for(int x=0;x<Config.list.size();x++){
                        ImageItem imgitem=Config.list.get(x);
                        if(item.getImageId().equals(imgitem.getImageId())){
                            Config.list.remove(imgitem);
                        }

                    }
                }
                else{
                    if(pictureCount !=-1){
                        if(((Config.list.size()+photoInfo.getNum())> pictureCount)){
                            APP.app.showToast("最多选择"+ pictureCount +"张图片哦",null);
                            return;
                        }
                    }
                    ScrollPhotoActvity.iv_right.setImageResource(R.mipmap.picture_selected);
                    item.setSelected(true);

                    Config.list.add(item);

                }
                int len=photoInfo.getNum()+Config.list.size();
                if(pictureCount !=-1){
                    ScrollPhotoActvity.tv_select.setText("下一步("+len+"/"+pictureCount+")");
                }else{
                    ScrollPhotoActvity.tv_select.setText("下一步("+len+")");
                }

            }
        });
        String url=Value.PICS_LIST.get(i).getImagePath();

        view.setLayoutParams(new android.widget.Gallery.LayoutParams(android.widget.Gallery.LayoutParams.MATCH_PARENT, android.widget.Gallery.LayoutParams.MATCH_PARENT));
        FFImageLoader.loadBigImage((FFContext) mcontext, url, view,0);
        return view;
    }
//    private class  viewholder{
//
//        private ImageView iv_img;
//    }
}
