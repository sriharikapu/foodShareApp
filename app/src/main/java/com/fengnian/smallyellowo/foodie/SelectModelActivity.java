package com.fengnian.smallyellowo.foodie;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModel;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SelectModelSingleIntent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_None;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_classic;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_simple;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2_2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Pomo;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard;

public class SelectModelActivity extends BaseActivity {
    @Bind(R.id.rv_list_model)
    RecyclerView rvListModel;

    int lastY = 0;
    int max = FFUtils.getPx(50);
    @Bind(R.id.tv_all)
    TextView tvAll;
    TextView[] bottomTvs;
    View[] bottomVs;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.tv_classic)
    TextView tvClassic;
    @Bind(R.id.tv_modern)
    TextView tvModern;
    @Bind(R.id.tv_simple)
    TextView tvSimple;
    @Bind(R.id.v_all)
    View vAll;
    @Bind(R.id.v_classic)
    View vClassic;
    @Bind(R.id.v_modern)
    View vModern;
    @Bind(R.id.v_simple)
    View vSimple;

    class HolderList extends RecyclerView.ViewHolder {
        ImageView iv_img;
        CheckBox cb_checked;

        public HolderList(View itemView) {
            super(itemView);
            itemView.setTag(this);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            cb_checked = (CheckBox) itemView.findViewById(R.id.cb_checked);
            int width = (FFUtils.getDisWidth() - FFUtils.getPx(23)) / 2;
            int height = width * 1043 / 559;

            iv_img.getLayoutParams().height = height;
            iv_img.getLayoutParams().width = width;
        }
    }

    ArrayList<RichTextModel> models = new ArrayList<>();

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        ButterKnife.bind(this);
        bottomTvs = new TextView[]{tvAll, tvModern, tvClassic, tvSimple};
        bottomVs = new View[]{vAll, vModern, vClassic, vSimple};
        initBottomTvs(tvAll);
        setNotitle(true);
        rvListModel.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvListModel.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new HolderList(getLayoutInflater().inflate(R.layout.item_list_mode, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {
                final HolderList holder = (HolderList) h;
                FFImageLoader.loadBigImage(context(),getImage(models.get(position)),holder.iv_img);
//                holder.iv_img.setImageResource(getImage(models.get(position)));
                int selected = rvListModel.getTag() == null ? -1 : (Integer) rvListModel.getTag();
                holder.cb_checked.setOnCheckedChangeListener(null);
                if (selected == position) {
                    holder.cb_checked.setChecked(true);
                } else {
                    holder.cb_checked.setChecked(false);
                }

                holder.cb_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            rvListModel.setTag(position);
                        } else {
                            rvListModel.setTag(-1);
                        }
                        if (isChecked) {
                            btnNext.setEnabled(true);
                        } else {
                            btnNext.setEnabled(false);
                        }
                        notifyDataSetChanged();
                    }
                });

                holder.iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.cb_checked.setChecked(true);
                        ((StaggeredGridLayoutManager) rvListModel.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                        startActivity(SelectModelSingleActivity.class, new SelectModelSingleIntent(models.get(position).indexCode, 1));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return models.size();
            }
        });

        ScrollSpeedLinearLayoutManger linearLayoutManager = new ScrollSpeedLinearLayoutManger(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        initModelsWithType(SYReleaseTemplateCategore_None);

    }

    public static int getImage(RichTextModel richTextModel) {
        switch (richTextModel.getReleaseTemplateType()) {
            case SYReleaseTemplateType_Standard:
                return R.mipmap.select_model_standard;
            case SYReleaseTemplateType_Brief:
                return R.mipmap.select_model_simple;
            case SYReleaseTemplateType_Brief2:
                return R.mipmap.select_model_semple2;
            case SYReleaseTemplateType_Brief2_2:
                return R.mipmap.select_model_simple2_2;
            case SYReleaseTemplateType_Chinese:
                return R.mipmap.select_model_chinese;
            case SYReleaseTemplateType_Chinese2:
                return R.mipmap.select_model_chinese2;
            case SYReleaseTemplateType_Modern:
                return R.mipmap.select_model_now;
//            case SYReleaseTemplateType_Pomo:
//                return R.mipmap.select_model_pm;
        }
        return R.mipmap.select_model_standard;
    }


    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {

        public ScrollSpeedLinearLayoutManger(Context context) {
            super(context);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            PointF pointF = ScrollSpeedLinearLayoutManger.this
                                    .computeScrollVectorForPosition(targetPosition);
//                            int i = rvImgs.getChildAt(1).getLeft();
//                            pointF.offset(MainHomeUGCFragment.around(i, max, Math.max(1000, Math.abs(i - max) / 20)), 0);
                            return pointF;
                        }

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return super.calculateSpeedPerPixel(displayMetrics);
                        }

                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }


    final int duration = 100;
    long startTime = 0;
    int totalOffset = 0;


    boolean sc = false;

    @OnClick({R.id.iv_return, R.id.btn_next, R.id.tv_all, R.id.tv_classic, R.id.tv_modern, R.id.tv_simple})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.btn_next:
                next();
                break;
            case R.id.tv_all:
                initBottomTvs(view);
                initModelsWithType(SYReleaseTemplateCategore_None);
                break;
            case R.id.tv_classic:
                initBottomTvs(view);
                initModelsWithType(SYReleaseTemplateCategore_classic);
                break;
            case R.id.tv_modern:
                initBottomTvs(view);
                initModelsWithType(SYReleaseTemplateCategore_modern);
                break;
            case R.id.tv_simple:
                initBottomTvs(view);
                initModelsWithType(SYReleaseTemplateCategore_simple);
                break;
        }
    }

    private void initBottomTvs(View tv) {
        int i =0;
        for (TextView tv1 : bottomTvs) {
            if (tv1 == tv) {
                bottomVs[i].setVisibility(View.VISIBLE);
                tv1.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                tv1.setTextColor(0xffaaaaab);
                bottomVs[i].setVisibility(View.GONE);
            }
            i++;
        }
    }

    private void initModelsWithType(int category) {
        models.clear();
        ArrayList<RichTextModel> richTextModels = RichTextModelManager.getModels();
        for (RichTextModel model : richTextModels) {
            if ((model.releaseTemplateCategory | category) == model.releaseTemplateCategory) {
                models.add(model);
            }
        }
        Collections.sort(models, new Comparator<RichTextModel>() {
            @Override
            public int compare(RichTextModel o1, RichTextModel o2) {
                if (o1.indexCode > o2.indexCode) {
                    return 1;
                } else if (o1.indexCode < o2.indexCode) {
                    return -1;
                }
                return 0;
            }
        });
        sc = false;
        rvListModel.getAdapter().notifyDataSetChanged();

        if (rvListModel.getVisibility() == View.VISIBLE) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }

        rvListModel.setTag(-1);

        ((StaggeredGridLayoutManager) rvListModel.getLayoutManager()).scrollToPositionWithOffset(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            next();
        }
    }

    private void next() {
        RichTextEditIntent data = new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_NEW);
        data.setModelIndex(models.get((Integer) rvListModel.getTag()).indexCode);
        startActivity(RichTextEditActivity.class, data);
    }
}
