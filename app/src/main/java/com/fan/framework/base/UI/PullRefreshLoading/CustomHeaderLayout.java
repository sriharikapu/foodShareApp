package com.fan.framework.base.UI.PullRefreshLoading;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fengnian.smallyellowo.foodie.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lanbiao on 16/9/6.
 */
public class CustomHeaderLayout extends LoadingLayout {
    private RelativeLayout containerLayout;
    private ImageView animaView;

    private boolean bRefresh = false;
    private Integer index = 0;
    private Timer time;
    private ArrayList idArray = new ArrayList();
    private Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CustomHeaderLayout.this.animaView.setImageResource((Integer) idArray.get(msg.what));
        }
    };

    public CustomHeaderLayout(Context context) {
        this(context,null);
    }

    public CustomHeaderLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CustomHeaderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context){
        containerLayout = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_container_id);
        animaView = (ImageView) findViewById(R.id.pull_to_refresh_header_img_id);

        idArray.add(R.mipmap.xxx01);
        idArray.add(R.mipmap.xxx03);
        idArray.add(R.mipmap.xxx05);
        idArray.add(R.mipmap.xxx07);
        idArray.add(R.mipmap.xxx09);
        idArray.add(R.mipmap.xxx11);
        idArray.add(R.mipmap.xxx13);
        idArray.add(R.mipmap.xxx15);
        idArray.add(R.mipmap.xxx17);
        idArray.add(R.mipmap.xxx19);
        idArray.add(R.mipmap.xxx22);
    }

    void startTimer(){
        if(bRefresh)
            return;
        bRefresh = true;
        if(time == null){
            time = new Timer();
            index = 0;
        }
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                index ++;
                if(index < -1 || index > 21)
                    index = 0;
                Log.d("aaa", String.format("%d",index));
                Message msg = Message.obtain();
                msg.what = index;
                handle.sendMessage(msg);
            }
        },0,50);
    }

    void stopTimer(){
        if(bRefresh){
            if(time != null){
                time.cancel();
                time = null;
            }
            bRefresh = false;
        }
    }

    @Override
    public int getContentSize() {
        if (null != containerLayout) {
            return containerLayout.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        return LayoutInflater.from(context).inflate(R.layout.custom_pull_to_refresh_header,null);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        super.onReset();
        stopTimer();
    }

    @Override
    public void onPull(float scale) {
        super.onPull(scale);
        if(scale > 0){
            startTimer();
        }else {
            stopTimer();
        }
    }

    @Override
    protected void onPullToRefresh() {
        super.onPullToRefresh();
    }

    @Override
    protected void onReleaseToRefresh() {
        super.onReleaseToRefresh();
    }

    @Override
    protected void onRefreshing() {
        super.onRefreshing();
        startTimer();
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        super.setLastUpdatedLabel(label);
    }
}
