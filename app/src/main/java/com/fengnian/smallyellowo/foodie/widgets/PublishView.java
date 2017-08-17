package com.fengnian.smallyellowo.foodie.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.FastEditActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RichTextEditActivity;
import com.fengnian.smallyellowo.foodie.SelectModelActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.FastEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;

/**
 * Created by wwl on 16/9/30.
 * 这个 就是开关的那个布局 里面实现窗帘那种感觉
 */
public class PublishView extends FrameLayout {
    private Context context;
    private View topView;
    private View botomView;
    public static final String tag = "CURVIEW";
    public static final int duration = 300;
    LayoutParams topParams;
    LayoutParams bottomParams;
    private ViewGroup mParentView;
    public boolean isShow = false;

    public PublishView(Context context) {
        super(context);
        init(context);
    }

    public PublishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PublishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mParentView = (ViewGroup) getParent();
        initView();
    }

    private void initView() {
        int layoutHeight = mParentView.getMeasuredHeight();
        int layoutWidth = mParentView.getMeasuredWidth();

        LayoutInflater inflater = LayoutInflater.from(context);
        topView = inflater.inflate(R.layout.view_top, null);
        botomView = inflater.inflate(R.layout.view_bottom, null);
        Log.e(tag, "height " + layoutHeight);
        Log.e(tag, "width " + layoutWidth);
        topParams = new LayoutParams(layoutWidth, layoutHeight / 2);
        topParams.topMargin = -layoutHeight / 2;
        bottomParams = new LayoutParams(layoutWidth, layoutHeight / 2);
        bottomParams.topMargin = layoutHeight;
        addView(topView, topParams);
        addView(botomView, bottomParams);
        setListener();
    }

    long lastClick = 0;

    public void setListener() {
        topView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClick < 1000) {
                    return;
                }
                lastClick = System.currentTimeMillis();
                close();
                ((BaseActivity) context).startActivity(FastEditActivity.class, new FastEditIntent());
            }
        });


        botomView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClick < 1000) {
                    return;
                }
                lastClick = System.currentTimeMillis();
                if (DraftModelManager.hasDraft()) {
                    new EnsureDialog.Builder(context).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FFUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    close();
                                }
                            }, 0);
                        }
                    }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((BaseActivity) context).startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                            FFUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    close();
                                }
                            }, 0);
                        }
                    }).setNeutralButton("创建新编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            ((BaseFragmentActivity) context).startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_NEW));
                            ((BaseActivity) context).startActivity(SelectModelActivity.class, new IntentData());
                            FFUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    close();
                                }
                            }, 0);
                        }
                    }).create().show();
                } else {
                    FFUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            close();
                        }
                    }, 0);
//                    ((BaseFragmentActivity) context).startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_NEW));
                    ((BaseActivity) context).startActivity(SelectModelActivity.class, new IntentData());
                }
            }
        });

        ImageView iv_colse = (ImageView) botomView.findViewById(R.id.iv_colse1);


        iv_colse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClick < 1000) {
                    return;
                }
                lastClick = System.currentTimeMillis();
                close();
            }
        });
    }

    /**
     *
     */
    public void show() {
        isShow = true;
        ValueAnimator animator = ValueAnimator.ofInt(0, mParentView.getMeasuredHeight() / 2);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                topParams.topMargin = -mParentView.getMeasuredHeight() / 2 + value;
                bottomParams.topMargin = mParentView.getMeasuredHeight() - value;
                topView.setLayoutParams(topParams);
                botomView.setLayoutParams(bottomParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                topParams.topMargin = 0;
                bottomParams.topMargin = mParentView.getMeasuredHeight() / 2;
                topView.setLayoutParams(topParams);
                botomView.setLayoutParams(bottomParams);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    public void close() {
        isShow = false;
        ValueAnimator animator = ValueAnimator.ofInt(0, mParentView.getMeasuredHeight() / 2);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                topParams.topMargin = -value;
                bottomParams.topMargin = mParentView.getMeasuredHeight() / 2 + value;
                topView.setLayoutParams(topParams);
                botomView.setLayoutParams(bottomParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                topParams.topMargin = -mParentView.getMeasuredHeight() / 2;
                bottomParams.topMargin = mParentView.getMeasuredHeight();
                topView.setLayoutParams(topParams);
                botomView.setLayoutParams(bottomParams);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

}
