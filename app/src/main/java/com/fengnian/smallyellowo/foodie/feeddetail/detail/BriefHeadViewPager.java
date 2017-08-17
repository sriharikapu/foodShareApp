package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-3-1.
 */

public class BriefHeadViewPager extends ViewPager {
    private MyPagerAdapter mViewPagerAdapter;
    private Activity activity;

    public BriefHeadViewPager(Activity context, BriefStyleHeadViewHolder headViewHolder, SYFeed syFeed) {
        super(context);
        activity = context;
        mViewPagerAdapter = new MyPagerAdapter(activity, syFeed);
        mViewPagerAdapter.mBriefStyleHeadViewHolder = headViewHolder;
        setAdapter(mViewPagerAdapter);
        setIndexText();

        this.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int bitmapWidth = DetailAdapterUtils.getImageWidth(getSYRichTextPhotoModelList().get(position));
                int bitmapHeight = DetailAdapterUtils.getImageHeight(getSYRichTextPhotoModelList().get(position));

                if (mViewPagerAdapter.mBriefStyleHeadViewHolder != null) {
                    setIndexText();
                    MyPagerAdapter.currentIndex = position;
                    mViewPagerAdapter.mBriefStyleHeadViewHolder.onViewPagerLayoutParamsChange((int) bitmapWidth, (int) bitmapHeight);
                }
            }
        });
    }

    private void setIndexText() {
        if (mViewPagerAdapter.mBriefStyleHeadViewHolder != null) {
            String indexStr = (getCurrentItem() + 1) + "/" + mViewPagerAdapter.getCount();
            mViewPagerAdapter.mBriefStyleHeadViewHolder.view_index.setText(indexStr);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyPagerAdapter.currentIndex = 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MyPagerAdapter.currentIndex = 0;
    }

    public List<SYRichTextPhotoModel> getSYRichTextPhotoModelList() {
        return mViewPagerAdapter.richTextLists;
    }

    public static class MyPagerAdapter extends PagerAdapter {
        public static int currentIndex = 0;
        private BriefStyleHeadViewHolder mBriefStyleHeadViewHolder;
        private Activity activity;
        private SYFeed data;
        public List<SYRichTextPhotoModel> richTextLists = new ArrayList<SYRichTextPhotoModel>();
        private final static int MAX_COUNT = 9;
        private List<View> mViewList = new ArrayList<View>();

        public MyPagerAdapter(Activity context, SYFeed _data) {
            richTextLists.clear();
            activity = context;
            data = _data;
            List<SYRichTextPhotoModel> list = data.getFood().getRichTextLists();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isTextPhotoModel()) {
                    richTextLists.add(list.get(i));
                }
            }

            for (int i = 0; i < getCount(); i++) {
                mViewList.add(createItemView(i));
            }
        }

        @Override
        public int getCount() {
            if (richTextLists.size() >= MAX_COUNT) {
                return MAX_COUNT;
            } else {
                return richTextLists.size();
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        public View createItemView(final int index) {
            View itemView = View.inflate(activity, R.layout.brief_head_viewpager_item_layout, null);
            final DynamicImageView iv_img = (DynamicImageView) itemView.findViewById(R.id.iv_img);
            TextView tv_dish_name = (TextView) itemView.findViewById(R.id.tv_dish_name);
            ImageView iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
            TextView tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            View tv_content_bg = itemView.findViewById(R.id.tv_content_bg);

            String dishName = richTextLists.get(index).getDishesName();
            if (!TextUtils.isEmpty(dishName)) {
                tv_dish_name.setVisibility(View.VISIBLE);
                tv_dish_name.setText(dishName);
            } else {
                tv_dish_name.setVisibility(View.GONE);
                tv_dish_name.setText("");
            }

            String content = richTextLists.get(index).getContent();
            if (!TextUtils.isEmpty(content)) {
                tv_content_bg.setVisibility(View.VISIBLE);
                tv_content.setVisibility(View.VISIBLE);
                FFUtils.setText(tv_content, content);
            } else {
                tv_content_bg.setVisibility(View.GONE);
                tv_content.setVisibility(View.GONE);
                tv_content.setText("");
            }

            final SYImage img = richTextLists.get(index).getPhoto().getImageAsset().pullProcessedImage().getImage();

            //处理第一张图显示宽高异常的问题
            if (index == 0) {
                int bitmapWidth = DetailAdapterUtils.getImageWidth(richTextLists.get(index));
                int bitmapHeight = DetailAdapterUtils.getImageHeight(richTextLists.get(index));

                if (mBriefStyleHeadViewHolder != null) {
                    mBriefStyleHeadViewHolder.onViewPagerLayoutParamsChange(bitmapWidth, bitmapHeight);
                }
            }

            if (activity instanceof FFContext) {
                FFContext ffContext = (FFContext) activity;
                FFImageLoader.loadBigImage(ffContext, img.getUrl(), iv_img);
            }

            setCommentData(richTextLists.get(index).getPhoto().getImageComment(), iv_comment);

            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof DynamicDetailActivity) {
                        DynamicDetailActivity dynamicDetailActivity = (DynamicDetailActivity) activity;
                        if (dynamicDetailActivity.adapter != null) {
                            dynamicDetailActivity.adapter.toPicturePreview(img);
                        }
                    }
                }
            });

            return itemView;
        }

        private void setCommentData(int commentLevel, ImageView imageView) {
            int[] iconArray = BaseDetailAdapter.CommentLevel.ICON_BRIEF;

            switch (commentLevel) {
                case 0:
                    imageView.setVisibility(View.GONE);
                    imageView.setImageResource(iconArray[0]);
                    break;
                case 1:
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(iconArray[1]);
                    break;
                case 2:
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(iconArray[3]);
                    break;
                case 3:
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(iconArray[3]);
                    break;
            }
        }
    }
}
