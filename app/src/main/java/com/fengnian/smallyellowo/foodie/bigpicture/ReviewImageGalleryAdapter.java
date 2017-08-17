/**
 * GalleryAdapter.java
 *
 * @version 1.0
 */
package com.fengnian.smallyellowo.foodie.bigpicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.imageloader.PercentImageView;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.ReviewImagesActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.fragments.MyCameraFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ReviewImageGalleryAdapter extends BaseAdapter {
    private ReviewImagesActivity context;

    public ReviewImageGalleryAdapter(ReviewImagesActivity context) {
        this.context = context;
    }

    @Override
    public abstract int getCount();

    @Override
    public abstract MyCameraFragment.TempPic getItem(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public HashMap<Integer, PercentImageView> allItems = new HashMap<>();


    private class HolderImageGallery {
        private PercentImageView img;
        private LinearLayout rl_dish_container;
        private TextView tv_dish;
        private View itemView;
        private SYRichTextPhotoModel currentModel;

        public <T extends View> T findViewById(int id) {
            return (T) itemView.findViewById(id);
        }

        public HolderImageGallery(final ReviewImagesActivity context, ViewGroup parent) {
            final LayoutInflater from = LayoutInflater.from(context);
            itemView = from.inflate(R.layout.item_image_gallery, parent, false);
            img = findViewById(R.id.img);
            rl_dish_container = findViewById(R.id.rl_dish_container);
            tv_dish = findViewById(R.id.tv_dish);

            rl_dish_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.rv_hot_dish.getAdapter().notifyDataSetChanged();
                    context.et_dish1.setText(currentModel.getDishesName());
                    context.rl_dish_container1.setVisibility(View.VISIBLE);
                    context.et_dish1.requestFocus();
                    context.tvOk.setText("确定");
                    FFUtils.setSoftInputVis(context.et_dish1, true);
                    context.et_dish1.setSelection(context.et_dish1.length());
                    rl_dish_container.setVisibility(View.GONE);
                }
            });
        }


        private void refreshDishName() {
            if (currentModel != null && !FFUtils.isStringEmpty(currentModel.pullDishesName())) {
                tv_dish.setText(currentModel.pullDishesName());
                rl_dish_container.setVisibility(View.VISIBLE);
            } else {
                rl_dish_container.setVisibility(View.GONE);
            }
        }

        public void onBind(int position) {
            currentModel = getItem(position).getModel();
            refreshDishName();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderImageGallery holder;
        if (convertView == null) {
            holder = new HolderImageGallery(context, parent);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (HolderImageGallery) convertView.getTag();
        }
        final MyCameraFragment.TempPic item = getItem(position);
        holder.onBind(position);
        FFImageLoader.loadBigImage((FFContext) context, item.getPath(), holder.img, 0);
        Set<Map.Entry<Integer, PercentImageView>> set = allItems.entrySet();
        for (Map.Entry<Integer, PercentImageView> entry : set) {
            if (entry.getValue() == convertView) {
                allItems.remove(entry.getKey());
            }
        }
        allItems.remove(position);
        allItems.put(position, holder.img);
        return holder.itemView;
    }
}