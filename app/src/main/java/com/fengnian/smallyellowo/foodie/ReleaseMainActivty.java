package com.fengnian.smallyellowo.foodie;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

/**
 * Created by Administrator on 2016-9-28.
 */

public class ReleaseMainActivty extends BaseActivity<IntentData> {
    private LinearLayout rl_100;
    RelativeLayout  rl_food_edit;
    private ImageView iv_food_fast_record,iv_food_edit,iv_cancle;
    int   heigh,width;
    int  half_heigh;
     ImageView  iv1,iv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_main);

         setNotitle(true);
//        rl_100= (LinearLayout)findViewById(R.id.rl_100);
//
//        rl_food_edit= (RelativeLayout) findViewById(R.id.rl_food_edit);
//        iv_food_fast_record= (ImageView) findViewById(R.id.iv_food_fast_record);
//        iv_food_edit= (ImageView) findViewById(R.id.iv_food_edit);
//        iv_cancle= (ImageView) findViewById(R.id.iv_cancle);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        heigh= dm.heightPixels;
       width= dm.widthPixels;
        half_heigh=heigh/2;
        LinearLayout.LayoutParams   parms=new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,half_heigh);
        iv1=new ImageView(this);
         iv2=new ImageView(this);
        iv1.setLayoutParams(parms);
        iv2.setLayoutParams(parms);
//        iv_food_fast_record.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,half_heigh));
//        iv_food_edit.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,half_heigh));
//        iv_cancle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,80));
////        parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,R.id.iv_cancle);
//        rl_food_edit.setLayoutParams(parms);


        ObjectAnimator animator1 = ObjectAnimator.ofInt(iv1, "pivotY",	-half_heigh, 0);
//                animator1.setTarget(rl_food_edit);
//                rl_food_edit.layout(0,heigh,width,3*heigh/2);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams
                        layoutParams = (ViewGroup.MarginLayoutParams) iv1.getLayoutParams();
                // 重新修改布局高度
                layoutParams.topMargin =curValue;
                iv1.setLayoutParams(layoutParams);
            }
        });
        animator1.setDuration(2000).start();
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                iv1.layout(0,-half_heigh,width,-half_heigh);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                iv1.layout(0,0,width,half_heigh);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

//        ObjectAnimator animator2 = ObjectAnimator.ofInt(iv2, "pivotY",heigh, half_heigh);
////                animator1.setTarget(rl_food_edit);
////                rl_food_edit.layout(0,heigh,width,3*heigh/2);
//        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int curValue = (int)animation.getAnimatedValue();
//                ViewGroup.MarginLayoutParams
//                        layoutParams = (ViewGroup.MarginLayoutParams) iv2.getLayoutParams();
//                // 重新修改布局高度
//                layoutParams.bottomMargin =curValue;
//                iv2.setLayoutParams(layoutParams);
//            }
//        });
//        animator2.setDuration(2000).start();


    }

}
