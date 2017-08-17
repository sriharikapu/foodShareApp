package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-2-27.
 */

public class BriefStyleAdapter extends BaseDetailAdapter {
    private BriefStyleHeadViewHolder headView;
    private final int VIEW_TYPE_COUNT = 3;
    private List<SYRichTextPhotoModel> richTextLists = new ArrayList<SYRichTextPhotoModel>();

    public BriefStyleAdapter(DynamicDetailActivity activity) {
        super(activity);
        headView = new BriefStyleHeadViewHolder(activity, activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief_style_title, null));

        List<SYRichTextPhotoModel> list = activity.data.getFood().getRichTextLists();
        for (SYRichTextPhotoModel mode : list) {
            if (!mode.isTextPhotoModel()) {
                richTextLists.add(mode);
            }
        }

    }

    @Override
    public BaseHeadViewHolder getHeadView() {
        return headView;
    }


    @Override
    public SYRichTextPhotoModel getItem(int position) {
        if (position == 0) {
            return null;
        }
        return richTextLists.get(position - 1);
    }

    @Override
    public View getMView(final int position, View convertView, ViewGroup parent) {
        switch (getMItemViewType(position)) {
            case VIEW_TYPE_HEAD://头
                return headView.getHeadView();
            default: {//文字
                if (convertView == null) {
                    convertView = activity.getLayoutInflater().inflate(R.layout.item_dynamic_detail_brief_text, parent, false);
                }
                TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                FFUtils.setText(tv_content, getItem(position).getContent());
                return convertView;
            }
        }

    }

    @Override
    public int getMAttentionBottomY() {
        int height = headView.getHeadView().getHeight() - DisplayUtil.dip2px(100);
        return height;
    }

    @Override
    public int getMCount() {
        return richTextLists.size() + 1;
    }

    @Override
    public int getMViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getMItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEAD;
        } else {
            return VIEW_TYPE_TEXT;
        }
    }
}
