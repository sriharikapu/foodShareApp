package com.fengnian.smallyellowo.foodie.scoreshop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.widgets.GridAdapter;

/**
 * meilishuo -- weichenglin create in 15/9/17
 */
public class ScoreShopAdapter extends GridAdapter<SYGoodsModel> {
    private BaseActivity mActivity;

    public ScoreShopAdapter(Context context) {
        super(context);
    }

    /**
     * @param context Context
     * @param columns gird一行要显示的列数
     */
    public ScoreShopAdapter(BaseActivity context, int columns) {
        super(context, columns);
        mActivity = context;
    }


    @Override
    protected View getItemView(final int position, View convertView) {
        super.getItemView(position, convertView);
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.score_shop_item_layout, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.sku_title = (TextView) convertView.findViewById(R.id.sku_title);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            holder.right_line = convertView.findViewById(R.id.right_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SYGoodsModel skuItem = getItem(position);

        holder.sku_title.setText(position + "");
        setLine(position, holder);
        setSkuInfo(skuItem, holder);
        setImage(skuItem, holder);

        return convertView;
    }

    private void setLine(int position, ViewHolder holder) {
        if (position % 2 == 0) {
            holder.right_line.setVisibility(View.VISIBLE);
        } else {
            holder.right_line.setVisibility(View.GONE);
        }
    }

    private void setSkuInfo(SYGoodsModel skuItem, ViewHolder holder) {
        if (skuItem != null && !TextUtils.isEmpty(skuItem.goodsTitle)) {
            if (skuItem.goodsTitle.contains("\n")) {
                skuItem.goodsTitle = skuItem.goodsTitle.replace("\n", "");
            } else if (skuItem.goodsTitle.contains("\r")) {
                skuItem.goodsTitle = skuItem.goodsTitle.replace("\r", "");
            }
            holder.sku_title.setText(skuItem.goodsTitle);
        } else {
            holder.sku_title.setText("");
        }

        if (skuItem != null && !TextUtils.isEmpty(skuItem.goodsPoints)) {
            SpannableString spannableString = new SpannableString(skuItem.goodsPoints + "积分");
            int color = Color.parseColor("#F9A825");
            spannableString.setSpan(new ForegroundColorSpan(color), 0, skuItem.goodsPoints.length(), 0);
            holder.tv_score.setText(spannableString);
        } else {
            holder.tv_score.setText("");
        }
    }

    private void setImage(SYGoodsModel skuItem, ViewHolder holder) {
        if (skuItem != null && skuItem.goodsImage != null && !TextUtils.isEmpty(skuItem.goodsImage.getUrl())) {
            FFImageLoader.loadSmallImage(mActivity, skuItem.goodsImage.getThumbUrl(), holder.image);
        } else {
            holder.image.setImageResource(0);
        }
    }

    public void startSkuDetail(int position, String score) {
        SkuDetailActivity.start(mActivity, getItem(position).goodsId, score);
    }

    static class ViewHolder {
        ImageView image;
        TextView sku_title;
        TextView tv_score;
        View bottom_line, right_line;
    }
}
