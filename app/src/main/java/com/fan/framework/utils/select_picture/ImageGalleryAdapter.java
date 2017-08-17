///**
// * GalleryAdapter.java
// *
// * @version 1.0
// */
//package com.fan.framework.utils.select_picture;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.fan.framework.base.FFContext;
//import com.fan.framework.imageloader.FFImageLoader;
//import com.fan.framework.widgets.ZoomImageView;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appconfig.Constants;
//
//import java.util.List;
//
//public class ImageGalleryAdapter extends BaseAdapter {
//
//    private Context context;
//
//    List<NativeImage> images;
//
//
//  public ImageGalleryAdapter(Context context, List<NativeImage> images) {
//        this.context = context;
//        this.images = images;
//
//    }
//    @Override
//    public int getCount() {
//        return images.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ZoomImageView view;
//        if (convertView != null) {
//            ((ZoomImageView) convertView).clearCache();
//            view = (ZoomImageView) convertView;
//        } else {
//            view = new ZoomImageView(context);
//        }
//        String path = images.get(position).getPath();
//        view.setLayoutParams(new android.widget.Gallery.LayoutParams(android.widget.Gallery.LayoutParams.MATCH_PARENT, android.widget.Gallery.LayoutParams.MATCH_PARENT));
////        NativeImageLoader.loadImage(view, path, -1, -1);
//        FFImageLoader.loadNativeImage((FFContext) context, path, view, Constants.BigImage, Constants.BigImage, R.drawable.alpha);
////        FFImageLoader.loadBigImage((FFContext) context, path, view);
//        return view;
//    }
//
//}
