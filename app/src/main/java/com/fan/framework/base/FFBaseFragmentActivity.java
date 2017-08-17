//package com.fan.framework.base;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.fan.framework.config.FFConfig;
//import com.fan.framework.http.FFBaseBean;
//import com.fan.framework.http.FFExtraParams;
//import com.fan.framework.http.FFNetWork;
//import com.fan.framework.http.FFNetWorkCallBack;
//import com.fan.framework.utils.FFLogUtil;
//import com.fan.framework.utils.FFUtils;
//import com.fan.framework.widgets.ResizeLayout;
//import com.fan.framework.widgets.ResizeLayout.OnResizeListener;
//import com.fengnian.smallyellowo.foodie.MainActivity;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//import com.fengnian.smallyellowo.foodie.appbase.BaseFragmentActivity;
//import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
//
//import java.util.ArrayList;
//
//import static com.fengnian.smallyellowo.foodie.R.id.iv_actionbar_backIcon;
//
//public abstract class FFBaseFragmentActivity<T extends IntentData> extends FragmentActivity implements FFContext {
//
//    private FFNetWork mNet;
//    private boolean mDestroyed = false;
//    private String mTAG;
//    private int mActivity_base = R.layout.ff_activity_base;
//    private ResizeLayout mContainer;
//    private TextView mTitleTextView;
//    private View titleView;
//    private LinearLayout mMenuContainer;
//    private ProgressBar mLoadingProgressBar;
//
//    public ArrayList<String> getTags() {
//        return tags;
//    }
//
//    FFLayoutInflaterFactory factory = new FFLayoutInflaterFactory();
//
//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        View view = factory.onCreateView(name, context, attrs);
//        if(view != null){
//            return view;
//        }
//        return super.onCreateView(name, context, attrs);
//    }
//
//    public ArrayList<String> getChildTags() {
//        return childTags;
//    }
//
//    //    private final static HashMap<String, ArrayList<Activity>> map = new HashMap<String, ArrayList<Activity>>();
//    private final ArrayList<String> tags = new ArrayList<>();
//    private final ArrayList<String> childTags = new ArrayList<>();
//
//    protected void addChildTag(String tag) {
//        addTag(tag);
//        childTags.add(tag);
//    }
//
//
//    protected void addTag(String tag) {
//        tags.add(tag);
//    }
//
//    protected void removeTag(String tag) {
//        tags.remove(tag);
//    }
//
//    public void finishAllActivitysByTag(String tag) {
//
//        for (int i = 0; i < allActivis.size(); i++) {
//            if (((FFContext) allActivis.get(i)).getTags().contains(tag)) {
//                Activity remove = (Activity) allActivis.remove(i);
//                if (allActivis.isEmpty()) {
//                    if (remove instanceof BaseActivity) {
//                        ((BaseActivity) remove).startFragmentActivity(MainActivity.class, new IntentData());
//                    } else {
//                        ((BaseFragmentActivity) remove).startFragmentActivity(MainActivity.class, new IntentData());
//                    }
//                }
//                remove.finish();
//                i--;
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        displayMetrics.scaledDensity = displayMetrics.density;
//        // 初始化TAG
//        mTAG = getClass().getSimpleName();
//        i("onCreate");
//        // 初始化contentView
//        super.setContentView(mActivity_base);
//        mContainer = getView(R.id.ll_baseActivity_content);
//        mLoadingProgressBar = getView(R.id.pb_actionbar_loading);
//        mContainer = getView(R.id.ll_baseActivity_content);
//        titleView = getView(R.id.ll_actionBar);
//        mTitleTextView = getView(R.id.tv_actionbar_title);
//        mMenuContainer = getView(R.id.ll_actionbar_menucontainer);
//        mNet = new FFNetWork(this);
//        initResizeListener();
//        setTitle(getTitle());
//        allActivis.add(this);
//        ArrayList<String> list = getIntent().getStringArrayListExtra("childTags_baseActivity");
//        if (list != null) {
//            childTags.addAll(list);
//            tags.addAll(list);
//        }
//    }
//
//    public void setActionBarOverlap() {
//        mActivity_base = R.layout.ff_activity_base_fullscreen;
//    }
//
//    public View getTitleView() {
//        return titleView;
//    }
//
//
//    public void setNotitle(boolean notitle) {
//        if (notitle) {
//            findViewById(R.id.ll_actionBar).setVisibility(View.GONE);
//        } else {
//            findViewById(R.id.ll_actionBar).setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    public void setBackVisible(boolean visible) {
//        View backIcon = findViewById(iv_actionbar_backIcon);
//        if (visible) {
//            backIcon.setVisibility(View.VISIBLE);
//            ((View) backIcon.getParent()).setClickable(true);
//        } else {
//            backIcon.setVisibility(View.GONE);
//            ((View) backIcon.getParent()).setClickable(false);
//        }
//    }
//
//    @Override
//    public void setTitle(CharSequence title) {
//        mTitleTextView.setText(title);
//    }
//
//    public TextView getTitleTextView() {
//        return mTitleTextView;
//    }
//
//    public void setLoading(boolean loading) {
//        if (loading) {
//            mLoadingProgressBar.setVisibility(View.VISIBLE);
//        } else {
//            mLoadingProgressBar.setVisibility(View.GONE);
//        }
//    }
//
//    public void initBottom() {
//        View v_baseActivity_bottom = findViewById(R.id.v_baseActivity_bottom);
//        if (FFUtils.isHasNavigationBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            v_baseActivity_bottom.getLayoutParams().height = FFUtils.getBottomStatusHeight();
//        } else {
//            v_baseActivity_bottom.getLayoutParams().height = 0;
//        }
//    }
//
//    /**
//     * 添加一个ActionBar Menu
//     *
//     * @param iconId   Menu的图片资源Id
//     * @param listener Menu的点击事件监听
//     * @return
//     */
//    public ImageButton addMenu(int iconId, OnClickListener listener) {
//        ImageButton menu = (ImageButton) getLayoutInflater().inflate(
//                R.layout.ff_view_menu, mMenuContainer, false);
//        menu.setLayoutParams(new LinearLayout.LayoutParams(getResources()
//                .getDimensionPixelSize(R.dimen.ff_actionBar_menuWidth),
//                getResources().getDimensionPixelSize(R.dimen.ff_actionbarHight)));
//        int padding = (getResources().getDimensionPixelSize(
//                R.dimen.ff_actionbarHight) - getResources()
//                .getDimensionPixelSize(R.dimen.ff_actionBar_icon_size)) / 2;
//        menu.setPadding(padding, padding, padding, padding);
//        menu.setOnClickListener(listener);
//        menu.setImageResource(iconId);
//        mMenuContainer.addView(menu);
//        return menu;
//    }
//
//    /**
//     * 添加一个ActionBar Menu
//     *
//     * @param text     Menu文字
//     * @param listener Menu的点击事件监听
//     * @return
//     */
//    public Button addMenu(String text, OnClickListener listener) {
//        Button menu = (Button) getLayoutInflater().inflate(
//                R.layout.ff_view_menu_text, mMenuContainer, false);
//        int actionBarHight = getResources().getDimensionPixelSize(
//                R.dimen.ff_actionbarHight);
//        int border = getResources().getDimensionPixelSize(R.dimen.ff_border);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT, actionBarHight);
//        menu.setLayoutParams(params);
//        menu.setPadding(border, 0, border, 0);
//        menu.setMinimumWidth(actionBarHight);
//        menu.setOnClickListener(listener);
//        menu.setText(text);
//        mMenuContainer.addView(menu);
//        return menu;
//    }
//
//    private void initResizeListener() {
//        mContainer.setOnResizeListener(new OnResizeListener() {
//
//            @Override
//            public void OnResize(int w, int h, int oldw, int oldh) {
//                int abs = Math.abs(oldh - h);
//                if (abs > FFUtils.getDisHight() * 0.7) {
//                    return;
//                }
//
//                if (abs < FFUtils.getPx(80)) {
//                    return;
//                }
//                if (oldh < h) {// 软键盘消失
//                    if(oldh < FFUtils.getDisHight() * 0.25){
//                        return;
//                    }
//                    onSoftInputMethInvis(abs);
//                }
//                if (oldh > h) {// 软键盘显示
//                    if(h < FFUtils.getDisHight() * 0.25){
//                        return;
//                    }
//                    onSoftInputMethVis(abs);
//                }
//            }
//
//            @Override
//            public boolean interceptTouchEvent(MotionEvent ev) {
//                return onInterceptTouchEvent(ev);
//            }
//        });
//    }
//
//    public FFBaseFragmentActivity context() {
//        return this;
//    }
//
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        d("onStart");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        displayMetrics.scaledDensity = displayMetrics.density;
//        d("onRestart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        d("onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        d("onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        d("onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        d("onDestroy");
//        mDestroyed = true;
//        mNet.onDestory();
//        if (isFinishing())
//        allActivis.remove(this);
//    }
//
//    /**
//     * 当actionBar上的返回键被点击
//     *
//     * @param v
//     */
//    public void onBackPressed(View v) {
//        onBackPressed();
//    }
//
//    @Override
//    public void showToast(CharSequence msg, CharSequence debugMsg) {
//        FFApplication.showToast(msg, debugMsg);
//    }
//
//    @Override
//    public void showToast(CharSequence text) {
//        showToast(text, null);
//    }
//
//    @Override
//    public void showToast(int resId, CharSequence text, CharSequence debugMsg) {
//        if (FFConfig.SHOW_DEBUG_TOAST && debugMsg != null) {
//            SpannableString ss = new SpannableString(debugMsg + "\n" + text);
//            ss.setSpan(new ForegroundColorSpan(0xffff8888), 0, debugMsg != null ? debugMsg.toString().length() : 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            showToast(resId, ss);
//        } else {
//            if (text != null) {
//                showToast(resId, text);
//                return;
//            }
//        }
//    }
//
//    @Override
//    public boolean getDestroyed() {
//        return mDestroyed;
//    }
//
//    public void d(String log) {
//        FFLogUtil.d(mTAG, log);
//    }
//
//    public void e(String log) {
//        FFLogUtil.e(mTAG, log);
//    }
//
//    public void i(String log) {
//        FFLogUtil.i(mTAG, log);
//    }
//
//    public <T extends FFBaseBean> void post(String url, String words,
//                                            FFExtraParams extra, FFNetWorkCallBack<T> callBack,
//                                            Object... params) {
//        mNet.post(url, words, extra, callBack, params);
//    }
//
//    public <T extends FFBaseBean> void get(String url, String words,
//                                           FFExtraParams extra, FFNetWorkCallBack<T> callBack,
//                                           Object... params) {
//        mNet.get(url, words, extra, callBack, params);
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        mContainer.removeAllViews();
//        mContainer.addView(getLayoutInflater().inflate(layoutResID, null));
//        onSetContentView();
//    }
//
//    protected void onSetContentView(){};
//
//    @Override
//    public void setContentView(View view) {
//        mContainer.removeAllViews();
//        mContainer.addView(view);
//        onSetContentView();
//    }
//
//    @Override
//    public void setContentView(View view, LayoutParams params) {
//        mContainer.removeAllViews();
//        mContainer.addView(view);
//        onSetContentView();
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends View> T getView(int id) {
//        View view = findViewById(id);
//        if (view == null) {
//            return null;
//        }
//        return (T) view;
//    }
//
//    /**
//     * 判断当前activity是否来自某activity
//     *
//     * @param activity
//     * @return
//     */
//    public boolean isFrom(Class<? extends Activity> activity) {
//        return activity.getName().equals(getOriginName());
//    }
//
//    /**
//     * 判断当前activity是否来自某activity的某fragment
//     *
//     * @param activity
//     * @param fragment
//     * @return
//     */
//    public boolean isFrom(Class<? extends Activity> activity,
//                          Class<? extends Fragment> fragment) {
//        return activity.getName().equals(getOriginName())
//                && fragment.getName().equals(getOriginFragmentName());
//    }
//
//    /**
//     * 获取来源activity的名称
//     *
//     * @return
//     */
//    private String getOriginName() {
//        return getIntent().getStringExtra("origin_baseActivity");
//    }
//
//    /**
//     * 获取来源activity的名称
//     *
//     * @return
//     */
//    private String getOriginFragmentName() {
//        return getIntent().getStringExtra("origin_fragment_baseActivity");
//    }
//
//    @Override
//    public int showProgressDialog(CharSequence word) {
//        return showProgressDialog(word, true);
//    }
//
//    @Override
//    public int showProgressDialog(CharSequence word, boolean cancelAble) {
//        if (getDestroyed()) {
//            return -1;
//        }
//        return MyProgressDialog.show(this, cancelAble, word, FFConfig.getNetTimeOut() * 3);
//    }
//
//    @Deprecated
//    @Override
//    public void startActivity(Intent intent) {
//        if (intent == null) {
//            return;
//        }
//        intent.putExtra("origin_baseActivity", this.getClass().getName());
//        intent.putExtra("childTags_baseActivity", childTags);
//        super.startActivity(intent);
//    }
//
//
//    @Deprecated
//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        if (intent == null) {
//            return;
//        }
//        intent.putExtra("origin_baseActivity", this.getClass().getName());
//        intent.putExtra("childTags_baseActivity", childTags);
//        super.startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    public void showToast(int resId, CharSequence text) {
//        Toast toast = new Toast(this);
//        View view = getLayoutInflater().inflate(R.layout.ff_toast, null, false);
//        ((TextView) view.findViewById(R.id.tv_msg)).setText(text);
//        ((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(resId);
//        toast.setView(view);
//        toast.show();
//    }
//
//    @Override
//    public void dismissProgressDialog(int id) {
//        MyProgressDialog.Dismiss(this, id);
//    }
//
//    public void onSoftInputMethVis(int softInputHight) {
//        showToast(null, "软键盘弹出");
//    }
//
//    public void onSoftInputMethInvis(int softInputHight) {
//        showToast(null, "软键盘消失");
//    }
//
//    public ResizeLayout getContainer() {
//        return mContainer;
//    }
//
//    public void setContainer(ResizeLayout mContainer) {
//        this.mContainer = mContainer;
//    }
//}
