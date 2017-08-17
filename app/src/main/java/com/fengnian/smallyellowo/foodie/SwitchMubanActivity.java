package com.fengnian.smallyellowo.foodie;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModel;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SelectModelSingleIntent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchMubanActivity extends BaseActivity<IntentData> {

    @Bind(R.id.rv_list_model)
    RecyclerView rvListModel;

    int lastY = 0;
    int max = FFUtils.getPx(50);

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,R.anim.switch_muban_activity_out);
    }

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
            cb_checked.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_muban);
        ButterKnife.bind(this);
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
                FFImageLoader.loadMiddleImage(context(), getImage(RichTextModelManager.getModels().get(position)), holder.iv_img);
                holder.iv_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        next(RichTextModelManager.getModels().get(position).indexCode);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return RichTextModelManager.getModels().size();
            }
        });
    }

    public static int getImage(RichTextModel richTextModel) {
        switch (richTextModel.getReleaseTemplateType()) {
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard:
                return R.mipmap.select_model_standard;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief:
                return R.mipmap.select_model_simple;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2:
                return R.mipmap.select_model_semple2;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2_2:
                return R.mipmap.select_model_simple2_2;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese:
                return R.mipmap.select_model_chinese;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese2:
                return R.mipmap.select_model_chinese2;
            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Modern:
                return R.mipmap.select_model_now;
//            case RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Pomo:
//                return R.mipmap.select_model_pm;
        }
        return R.mipmap.select_model_standard;
    }

    private void next(int indexCode) {
        Intent data1 = new Intent();
        data1.putExtra("indexCode", indexCode);
        setResult(RESULT_OK, data1);
        finish();
    }
}
