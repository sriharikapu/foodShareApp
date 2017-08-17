package com.fengnian.smallyellowo.foodie.contact;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.RelationSearchAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYThinkUser;
import com.fengnian.smallyellowo.foodie.bean.results.RelationSearchResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-9.
 */

public class RelationSearchActivity extends BaseActivity<IntentData> implements View.OnClickListener{
    private EditText ed_search;
    private ListView lv_listview;
    private ImageView iv_no_result, iv_delete;
    private List<SYThinkUser> mlist;
    private RelationSearchAdapter adapter;
    public static String keyword;
    private RelativeLayout rl_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationsearch);
        rl_1=findView(R.id.rl_1);
        View view = View.inflate(this, R.layout.relationship_search_item, null);
        ed_search = (EditText) view.findViewById(R.id.ed_search);
        iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        ed_search.addTextChangedListener(watcher);
        lv_listview = findView(R.id.lv_listview);
        iv_no_result = findView(R.id.iv_no_result);
        lv_listview.addHeaderView(view);
        mlist = new ArrayList<>();
        ed_search.requestFocus();
        show();
        int color=0;
        if(android.os.Build.VERSION.SDK_INT<23){//6.0之前
            color= getResources().getColor(R.color.search_color);
        }else{
             color=ContextCompat.getColor(this, R.color.search_color);
        }
        adapter = new RelationSearchAdapter(mlist, this,color);
        lv_listview.setAdapter(adapter);
        iv_delete.setOnClickListener(this);



    }

  void show(){
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
  }

    public void hideKey() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void getSearchRelationResult(final String s) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/seachThink.do", null, extra, new FFNetWorkCallBack<RelationSearchResult>() {
        post(IUrlUtils.Search.seachThink, null, extra, new FFNetWorkCallBack<RelationSearchResult>() {
            @Override
            public void onSuccess(RelationSearchResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    mlist.clear();
                    if (response.getThinkUser().size() == 0) {
                        iv_no_result.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        iv_no_result.setVisibility(View.GONE);
                    }

                    mlist.addAll(response.getThinkUser());
                    keyword = s;
                    adapter.notifyDataSetChanged();
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                if(mlist.size()==0){
                    iv_no_result.setVisibility(View.GONE);
                }

                return false;
            }
        }, "keyword", s);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                iv_delete.setVisibility(View.VISIBLE);
            } else {
                iv_delete.setVisibility(View.GONE);
            }
            if (editable.length() == 0)
                iv_no_result.setVisibility(View.GONE);
            if (ed_search.getText().toString().trim().length() > 0)
                getSearchRelationResult(editable.toString());
        }
    };


    public static class nickname_viewholer {

        public ImageView iv_header;
        public TextView tv_name;
        public TextView tv_sigur;

    }

    public static class phone_viewholer {
        public ImageView iv_header;
        public TextView tv_name;
        public TextView tv_phone;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_delete:
                ed_search.setText("");
                iv_no_result.setVisibility(View.GONE);
                mlist.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
