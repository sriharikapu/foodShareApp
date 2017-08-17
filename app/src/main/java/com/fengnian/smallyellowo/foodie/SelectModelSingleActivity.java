package com.fengnian.smallyellowo.foodie;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.AbstractViewPagerAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModel;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.DetailAdapterUtils;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SelectModelSingleIntent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectModelSingleActivity extends BaseActivity<SelectModelSingleIntent> {

    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.listView)
    ListView listView;
    private RichTextModel model;

    public static class HolderMoban {
        public ImageView iv_img;
        public ViewPager vp_data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        setContentView(R.layout.activity_select_model_single);
        ButterKnife.bind(this);

        model = RichTextModelManager.getConfigByIndex(getIntentData().getModelIndex());

        tvTitle.setText(model.templateName);
        List<ConfigResult.MubanImages.ResListBean> resList = SP.getConfig().getConfig().getMoban().getResList();
        ConfigResult.MubanImages.ResListBean data = resList.get(0);
        for (int i = 0; i < resList.size(); i++) {
            data = resList.get(i);
            if (data.getType() == model.getReleaseTemplateType()) {
                break;
            }
        }
        final int startindex = data.getStartIndex();
        final List<ConfigResult.MubanImages.ResListBean.ItemsBean> items = data.getItems();
        if (startindex == 0) {
            listView.setAdapter(new MyBaseAdapter<HolderMoban, ConfigResult.MubanImages.ResListBean.ItemsBean>(HolderMoban.class, this, R.layout.item_moban, data.getItems()) {
                @Override
                public void initView(View convertView, HolderMoban holder, int position, ConfigResult.MubanImages.ResListBean.ItemsBean item) {
                    holder.iv_img.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(24)) * item.getImage().getHeight() / item.getImage().getWidth();
                    FFImageLoader.loadBigImage(context(), item.getImage().getUrl().replaceAll(" ", ""), holder.iv_img);
                }
            });
        } else {
            listView.setAdapter(new MyBaseAdapter<HolderMoban, ConfigResult.MubanImages.ResListBean.ItemsBean>(HolderMoban.class, this, R.layout.item_moban, data.getItems()) {
                @Override
                public void initView(View convertView, HolderMoban holder, int position, ConfigResult.MubanImages.ResListBean.ItemsBean item) {
                    if (getItemViewType(position) == 1) {
                        return;
                    }
                    holder.iv_img.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(24)) * item.getImage().getHeight() / item.getImage().getWidth();
                    FFImageLoader.loadBigImage(context(), item.getImage().getUrl().replaceAll(" ", ""), holder.iv_img);
                }

                @Override
                public void onGetView(final HolderMoban holder) {
                    if (holder.vp_data != null) {
                        ArrayList<ConfigResult.MubanImages.ResListBean.ItemsBean> data = new ArrayList<ConfigResult.MubanImages.ResListBean.ItemsBean>();
                        data.addAll(items);
                        while (data.size() > startindex) {
                            data.remove(startindex);
                        }
                        holder.vp_data.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(24)) * data.get(0).getImage().getHeight() / data.get(0).getImage().getWidth();
                        holder.vp_data.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                            @Override
                            public void onPageSelected(int position) {
                                ConfigResult.MubanImages.ResListBean.ItemsBean item = ((AbstractViewPagerAdapter<ConfigResult.MubanImages.ResListBean.ItemsBean>)holder.vp_data.getAdapter()).getItem(position);
                                int width = FFUtils.getDisWidth() - FFUtils.getPx(24);
                                int height = width * item.getImage().getHeight() / item.getImage().getWidth();
                                onViewPagerLayoutParamsChange(width, height);
                            }

                            public void onViewPagerLayoutParamsChange(int BitmapWidth, int BitmapHeight) {
                                final ViewGroup.LayoutParams params = holder.vp_data.getLayoutParams();
                                int newHeight = BitmapHeight;

                                //写个简单动画，让体验好一点，不忽高忽低 by chenglin
                                ValueAnimator valueAnimator = ValueAnimator.ofInt(params.height, newHeight);
                                valueAnimator.setInterpolator(new LinearInterpolator());
                                valueAnimator.setDuration(200);
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        params.height = (int) animation.getAnimatedValue();
                                        holder.vp_data.setLayoutParams(params);
                                    }
                                });
                                valueAnimator.start();
                            }
                        });
                        holder.vp_data.setAdapter(new AbstractViewPagerAdapter<ConfigResult.MubanImages.ResListBean.ItemsBean>(data) {
                            @Override
                            public View newView() {
                                return getLayoutInflater().inflate(R.layout.item_moban, holder.vp_data, false);
                            }

                            @Override
                            public void refreshView(View view, int position) {
                                ImageView iv = (ImageView) view;
                                ConfigResult.MubanImages.ResListBean.ItemsBean item = getItem(position);
                                iv.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(24)) * item.getImage().getHeight() / item.getImage().getWidth();
                                FFImageLoader.loadBigImage(context(), item.getImage().getUrl().replaceAll(" ", ""), iv);
                            }
                        });
                    }
                }

                @Override
                public int getCount() {
                    return super.getCount() - (startindex == 0 ? 0 : (startindex - 1));
                }

                @Override
                public int getViewTypeCount1() {
                    return 2;
                }

                @Override
                public int getItemViewType1(int position) {
                    if (position == 0 && startindex != 0) {
                        return 1;
                    }
                    return 0;
                }

                @Override
                public int getItemViewId(int position) {
                    if (position == 0 && startindex != 0) {
                        return R.layout.item_model_pager;
                    }
                    return super.getItemViewId(position);
                }

                @Override
                public ConfigResult.MubanImages.ResListBean.ItemsBean getItem(int position) {
                    return super.getItem(position + (startindex == 0 ? 0 : (startindex - 1)));
                }
            });
        }

    }

    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                RichTextEditIntent data = new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_NEW);
                data.setModelIndex(getIntentData().getModelIndex());
                startActivity(RichTextEditActivity.class, data);
                break;
        }
    }


}
