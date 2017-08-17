/**
 * GalleryAdapter.java
 *
 * @version 1.0
 */
package com.fengnian.smallyellowo.foodie.bigpicture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.imageloader.PercentImageView;
import com.fengnian.smallyellowo.foodie.widgets.MyPercenImageView;

import java.util.ArrayList;

public class ImageGalleryAdapter extends BaseAdapter {

    ArrayList<BitPictureIntent.ImageMap> images;
    private Context context;

    public ImageGalleryAdapter(Context context, ArrayList<BitPictureIntent.ImageMap> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public BitPictureIntent.ImageMap getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyPercenImageView view;
        if (convertView != null) {
            ((MyPercenImageView) convertView).clearCache();
            view = (MyPercenImageView) convertView;
        } else {
            view = new MyPercenImageView(context);
        }

        final BitPictureIntent.ImageMap data = getItem(position);
        String path = data.getPath();
        view.setLayoutParams(new android.widget.Gallery.LayoutParams(android.widget.Gallery.LayoutParams.MATCH_PARENT, android.widget.Gallery.LayoutParams.MATCH_PARENT));
        FFImageLoader.loadBigImage((FFContext) context, path, view,0);
        view.setDishName(data.getDishName());
        return view;
    }

    public interface LoadImagePercentListener {
        void onLoadImagePercent(int nowPercent, int total, int position, boolean fail);
    }

}