package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.FoodsClassResult;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by Administrator on 2017-3-2.
 */

public class TestActivity extends BaseActivity<UserInfoIntent> {


    SYUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_footview);
        user = getIntentData().getUser();
        setbotomview();
    }

    private RadioGroup rg_tab;
    private LinearLayout fl_area;
    private RelativeLayout fl_class, rl_no;
    private com.fengnian.smallyellowo.foodie.widgets.PieChart pc_class;
    private com.fengnian.smallyellowo.foodie.widgets.ArcVisView avv_meng;
    private LinearLayout ll_class_info;
    private ImageView iv_top;

    void setbotomview() {
//        iv_club_dongtai_fail= (ImageView) Bottom_view.findViewById(iv_club_dongtai_fail);
//        rl_1_1= (RelativeLayout) Bottom_view.findViewById(rl_1_1);
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);

        fl_area = (LinearLayout) findViewById(R.id.fl_area);

        fl_class = (RelativeLayout) findViewById(R.id.fl_class);
        rl_no = (RelativeLayout) findViewById(R.id.rl_no);

        pc_class = (com.fengnian.smallyellowo.foodie.widgets.PieChart) findViewById(R.id.pc_class);
        avv_meng = (com.fengnian.smallyellowo.foodie.widgets.ArcVisView) findViewById(R.id.avv_meng);
        ll_class_info = (LinearLayout) findViewById(R.id.ll_class_info);
        iv_top = (ImageView) findViewById(R.id.iv_top);


        pc_class.setCenterTexts(ll_class_info);

        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_class) {
                    fl_area.setVisibility(View.GONE);
                    fl_class.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rb_area) {
                    fl_area.setVisibility(View.VISIBLE);
                    fl_class.setVisibility(View.GONE);
                }
            }
        });
        set_other_userinfo();
    }

    private static final int[] AllColors = new int[]{0xffFF9933, 0xffFFCC44, 0xffFF8800, 0xffFFD426, 0xffF87400, 0xffFF9900, 0xffFFBB00, 0xffFF9911, 0xffEB4E2F, 0xffCC3300};

    private void set_other_userinfo() {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/queryOthersFoodNotesClassificationV250.do", "", null, new FFNetWorkCallBack<FoodsClassResult>() {
        post(IUrlUtils.UserCenter.queryOthersFoodNotesClassificationV250, "", null, new FFNetWorkCallBack<FoodsClassResult>() {
            @Override
            public void onSuccess(FoodsClassResult response, FFExtraParams extra) {
                if (response.getSyFoodNotesClassifications() == null || response.getSyFoodNotesClassifications().size() == 0) {
                    rl_no.setVisibility(View.VISIBLE);
                    iv_top.setVisibility(View.GONE);
                    ll_class_info.setVisibility(View.GONE);


                    return;
                } else {
                    String[] titles = new String[response.getSyFoodNotesClassifications().size()];
                    double[] percent = new double[titles.length];
                    int[] colors = new int[titles.length];
                    int i = 0;
                    for (FoodsClassResult.SyFoodNotesClassificationsBean clazz : response.getSyFoodNotesClassifications()) {
                        titles[i] = clazz.getType();
                        percent[i] = clazz.getCounts();
                        colors[i] = AllColors[i % AllColors.length];
                        i++;
                    }
                    pc_class.setTitles(titles);
                    pc_class.setValues(percent);
                    pc_class.setColors(colors);
                    pc_class.invalidate();

                    avv_meng.start();
                    int sum = titles.length;
                    if (sum > 10) {
                        sum = 10;
                    }
                    rl_no.setVisibility(View.GONE);
                    if (sum > 0) {
                        iv_top.setVisibility(View.VISIBLE);
                        iv_top.setImageResource(new int[]{R.mipmap.top1, R.mipmap.top2, R.mipmap.top3, R.mipmap.top4, R.mipmap.top5, R.mipmap.top6, R.mipmap.top7, R.mipmap.top8, R.mipmap.top9, R.mipmap.top10}[sum - 1]);
                        ll_class_info.setVisibility(View.VISIBLE);
                    } else {
                        iv_top.setVisibility(View.GONE);
                        ll_class_info.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "userId", user.getId(), "type", "1");
//        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/queryOthersFoodNotesClassificationV250.do", "", null, new FFNetWorkCallBack<FoodsClassResult>() {
        post(IUrlUtils.UserCenter.queryOthersFoodNotesClassificationV250, "", null, new FFNetWorkCallBack<FoodsClassResult>() {
            @Override
            public void onSuccess(FoodsClassResult response, FFExtraParams extra) {
                if (response.getSyFoodNotesClassifications() != null) {
                    int i = 0;
                    int[] imgs = new int[]{R.mipmap.top1, R.mipmap.top2, R.mipmap.top3, R.mipmap.top4, R.mipmap.top5};
                    int[] bgs = new int[]{R.drawable.top5_1, R.drawable.top5_2, R.drawable.top5_3, R.drawable.top5_4, R.drawable.top5_5};
                    int first = 0;
                    int maxLength = 0;
                    for (FoodsClassResult.SyFoodNotesClassificationsBean clazz : response.getSyFoodNotesClassifications()) {
                        if (i >= imgs.length) {
                            break;
                        }
                        View convertView = getLayoutInflater().inflate(R.layout.item_userinfo_topten, fl_area, false);
                        ImageView iv_top_number = (ImageView) convertView.findViewById(R.id.iv_top_number);
                        TextView tv_top_count = (TextView) convertView.findViewById(R.id.tv_top_count);
                        TextView tv_top_address = (TextView) convertView.findViewById(R.id.tv_top_address);
                        fl_area.addView(convertView);
                        if (i == 0) {
                            first = clazz.getCounts();
                            maxLength = FFUtils.getDisWidth() - FFUtils.getPx(20 + 16 + 42) - FFUtils.getTextLength(12) * 5;
//                            maxLength = FFUtils.getDisWidth() - FFUtils.getPx(20 + 16 + 42) - FFUtils.getTextLength(14) * 5;
                            tv_top_count.getLayoutParams().width = maxLength;
                        } else {
                            tv_top_count.getLayoutParams().width = clazz.getCounts() * maxLength / first;
                        }
                        iv_top_number.setImageResource(imgs[i]);
                        tv_top_count.setText(String.valueOf(clazz.getCounts()));
                        tv_top_address.setText(clazz.getType());
                        tv_top_count.setBackgroundResource(bgs[i]);
                        if ((maxLength - tv_top_count.getLayoutParams().width) / FFUtils.getTextLength(14) > (clazz.getType().length() - 5)) {
                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        } else {
                            String type = clazz.getType().substring(0, clazz.getType().length() / 2 + clazz.getType().length() % 2) + "\r\n" + clazz.getType().substring(clazz.getType().length() / 2 + clazz.getType().length() % 2);
                            tv_top_address.setText(type);
                            tv_top_address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                        }
                        i++;
                    }
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "userId", user.getId(), "type", "2");

    }
}
