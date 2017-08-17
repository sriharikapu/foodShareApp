//package com.fan.framework.base.UI;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.fan.framework.base.FFBaseAdapter;
//import com.fan.framework.base.FFBaseFragmentActivity;
//import com.fan.framework.base.UI.PullRefreshLoading.PullToRefreshBase;
//import com.fan.framework.base.UI.PullRefreshLoading.PullToRefreshListView;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.APP;
//import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
//
///**
// * Created by lanbiao on 16/9/8.
// */
//public class PullRefreshLoadingFragme extends BaseFragment{
//
//    private BaseAdapter mAdapter;
//    private ListView mListView;
//    private PullToRefreshListView mPullListView;
//
//    public static class DemoHolder{
//        TextView demo_item;
//    }
//
//
//
//    /**
//     * 签名打包是报错   需添加 默认构造
//     */
//    public PullRefreshLoadingFragme() {
//    }
//
//    public PullRefreshLoadingFragme(FFBaseFragmentActivity context, BaseAdapter mAdapter, ListView mListView, PullToRefreshListView mPullListView) {
//        super(context);
//        this.mAdapter = mAdapter;
//        this.mListView = mListView;
//        this.mPullListView = mPullListView;
//    }
//
//    public PullRefreshLoadingFragme(BaseAdapter mAdapter, ListView mListView, PullToRefreshListView mPullListView) {
//        this.mAdapter = mAdapter;
//        this.mListView = mListView;
//        this.mPullListView = mPullListView;
//    }
//
////    public PullRefreshLoadingFragme(FFBaseFragmentActivity context) {
////        super(context);
////    }
//
//    public PullToRefreshListView getmPullListView() {
//        if(mPullListView == null){
//            mPullListView = new PullToRefreshListView(APP.app);
//            mPullListView.setPullRefreshEnabled(true);
//            mPullListView.setScrollLoadEnabled(true);
//
//            mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//                @Override
//                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                }
//
//                @Override
//                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//                }
//            });
//        }
//        return mPullListView;
//    }
//
//    @Override
//    public void onFindView() {
//        mListView = getmPullListView().getRefreshableView();
//        mListView.setAdapter(new FFBaseAdapter<DemoHolder,Object>(DemoHolder.class,R.layout.pull_refresh_demo_item,null) {
//
//            @Override
//            public void setViewData(Object viewHolder, int position, Object model) {
//
//            }
//        });
//    }
//
//    @Override
//    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return getmPullListView();
//    }
//}
