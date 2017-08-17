package com.fengnian.smallyellowo.foodie;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClientOption;
import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.bean.CityAreaBean;
import com.fengnian.smallyellowo.foodie.bean.CityListBean;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.SYShopLocationResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fan.framework.widgets.PullToRefreshLayout.supportPull;


public class AddRestActivity extends BaseActivity<IntentData> implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.lv_rests)
    ListView lvRests;
    @Bind(R.id.btn_search)
    TextView btnSearch;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private PullToRefreshLayout prl;
    private MyBaseAdapter<Holder, SYShopLocationResult.SYShopLocationModel> adapter;
    private List<CityListBean.CityBean> list;

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= 23 && !FFUtils.isPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION))
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSON_REQUESTCODE);
        else {
            initLocation();
        }

    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSON_REQUESTCODE) {
//            if (!verifyPermissions(paramArrayOfInt)) {
//
//            }

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                //用户授权
                initLocation();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                showMissingPermissionDialog();
                isNeedCheck = false;
//                showToast("未获得访问权限，不能操作！！");
            }
            return;
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        EnsureDialog.Builder builder = new EnsureDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });
        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        showPop();
    }

    private void search() {
        if (etSearch.getText().toString().trim().length() == 0) {
            showToast("请输入关键字");
            return;
        }
        FFUtils.setSoftInputInvis(etSearch.getWindowToken());
        getData(true, true, etSearch.getText().toString().trim(), 0);
    }


    public static class Holder {
        TextView tv_name;
    }

    private boolean isFromEdit() {
        return isFrom(RichTextEditActivity.class) || isFrom(FastEditActivity.class);
    }

    CityListBean.CityBean mCity;

    private void showPop() {
        // 一个自定义的布局，作为显示的内容
        View contentView = getLayoutInflater().inflate(
                R.layout.pop_add_rest_select_city, null);
        // 设置按钮的点击事件
        LinearLayout button = (LinearLayout) contentView.findViewById(R.id.ll_container);
        btnSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_down, 0);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                FFUtils.getPx(73), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                btnSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_up, 0);
            }
        });
        int i = 0;
        for (final CityListBean.CityBean city : list) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, FFUtils.getPx(39)));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xff444444);
            textView.setTextSize(15);
            textView.setText(city.getAreaName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCity = city;
                    popupWindow.dismiss();
                    btnSearch.setText(city.getAreaName());
                    FFUtils.setSoftInputInvis(etSearch.getWindowToken());
                    getData(true, true, etSearch.length() == 0 ? null : etSearch.getText().toString().trim(), 0);
                }
            });
            if (i > 0) {
                View line = new View(this);
                line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                line.setBackgroundColor(getResources().getColor(R.color.ff_line));
                button.addView(line);
            }
            button.addView(textView);
            i++;

        }
        popupWindow.setTouchable(true);
//
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("mengdd", "onTouch : ");
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(btnSearch, -FFUtils.getPx(38), -FFUtils.getPx(12));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = CityPref.getCityList();
        mCity = CityPref.getSelectedCity();
        setContentView(R.layout.activity_add_rest);
        checkPermissions(needPermissions);


        if (isFromEdit()) {
            setNotitle(true);
        } else {
            findViewById(R.id.iv_back).setVisibility(View.GONE);
//            ((View) findViewById(R.id.iv_back).getParent()).setBackgroundColor(0xffffffff);
            findViewById(R.id.title_line1).setVisibility(View.GONE);
        }


        ButterKnife.bind(this);
        btnSearch.setText(mCity.getAreaName());
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search();
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            Timer lastTime;

            @Override
            public void afterTextChanged(final Editable editable) {
                if (lastTime != null) {
                    lastTime.cancel();
                }
                lastTime = new Timer();
                lastTime.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (etSearch.getText().toString().trim().equals(editable.toString().trim())) {
                                    getData(true, false, editable.toString().trim(), 0);
                                }
                            }
                        });
                        lastTime = null;
                    }
                }, 1500);
            }
        });
        adapter = new MyBaseAdapter<Holder, SYShopLocationResult.SYShopLocationModel>(Holder.class, context(), R.layout.item_add_rest) {

            private boolean isAdd() {
                return isFromEdit() && !FFUtils.isStringEmpty(k);
            }


            @Override
            public int getViewTypeCount1() {
                return super.getViewTypeCount1() + (isAdd() ? 1 : 0) + (isFromEdit() ? 1 : 0);
            }

            @Override
            public SYShopLocationResult.SYShopLocationModel getItem(int position) {
                if (position == 0 && isFromEdit()) {
                    return null;
                }
                if (!isFromEdit()) {
                    return super.getItem(position);
                }
                return super.getItem(position - 1);

            }

            @Override
            public int getCount() {
                return super.getCount() + (isAdd() ? 1 : 0) + (isFromEdit() ? 1 : 0);
            }

            @Override
            public int getItemViewType1(int position) {
                if (isAdd() && position == getCount() - 1) {
                    return 1;
                }
                if (isFromEdit() && position == 0) {
                    return 2;
                }
                return 0;
            }

            @Override
            public int getItemViewId(int position) {
                if (isAdd() && position == getCount() - 1) {//最后一条
                    return R.layout.item_add_rest_add;
                }
                if (isFromEdit() && position == 0) {
                    return R.layout.header_addrest;
                }
                return super.getItemViewId(position);
            }

            @Override
            public void initView(View convertView, Holder holder, int position, final SYShopLocationResult.SYShopLocationModel item) {
                if (getItemViewType(position) == 1) {
                    holder.tv_name.setText("添加 “" + k + "” 为新商户");
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final SYShopLocationResult.SYShopLocationModel bean = new SYShopLocationResult.SYShopLocationModel();
                            bean.setTitle(k);
                            bean.setIsCustom("1");
                            SYShopLocationResult.SYShopLocationInfoModel ad_info = new SYShopLocationResult.SYShopLocationInfoModel();
                            ad_info.setCity(CityPref.getSelectedCity().getAreaName());
                            ad_info.setProvince("");
                            bean.setAd_info(ad_info);
                            if (isFrom(RichTextEditActivity.class)) {
                                DraftModelManager.getCurrentDraft().hotDishList = new ArrayList<String>();
                                DraftModelManager.addPoi(bean);
                                finish();
                            } else if (isFrom(FastEditActivity.class)) {
                                DraftModelManager.addPoi(FastEditActivity.draft, bean, true);
                                finish();
                            }
                        }
                    });
                    return;
                }

                if (getItemViewType(position) == 2) {
                    holder.tv_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getWantEatList(new ArrayList<SYShopLocationResult.SYShopLocationModel>(), null, null, 1);
                        }
                    });
                    return;
                }

                String key = etSearch.getText().toString();
                String title = "";
                if (item != null)
                    title = item.getTitle();
                else {
                    title = "";
                }
                if (key.length() != 0 && title.contains(key) && !isFromEdit()) {
                    SpannableString ss = new SpannableString(title);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.comment_name)), title.indexOf(key), title.indexOf(key) + key.length(), 0);
                    holder.tv_name.setText(ss);
                } else {
                    holder.tv_name.setText(title);
                }
//                holder.tv_address.setText(item.getAddress());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isFrom(RichTextEditActivity.class)) {
                            getLocation(item, new Runnable() {
                                @Override
                                public void run() {

                                    if (item == null || item.getId() == null || item.getId().length() == 0) {
                                        DraftModelManager.getCurrentDraft().hotDishList = new ArrayList<String>();
                                        DraftModelManager.addPoi(item);
                                        finish();
                                        return;
                                    }

                                    post(IUrlUtils.Search.queryShopDishesV250, "", null, new FFNetWorkCallBack<ShopDishResult>() {
                                        @Override
                                        public void onSuccess(ShopDishResult response, FFExtraParams extra) {
                                            DraftModelManager.getCurrentDraft().hotDishList = response.getList();
                                            DraftModelManager.addPoi(item);
                                            finish();
                                        }

                                        @Override
                                        public boolean onFail(FFExtraParams extra) {
                                            DraftModelManager.getCurrentDraft().hotDishList = null;
                                            DraftModelManager.addPoi(item);
                                            finish();
                                            return false;
                                        }
                                    }, "id", item.getId());
                                }
                            });
                            return;
                        } else if (isFrom(FastEditActivity.class)) {
                            getLocation(item, new Runnable() {
                                @Override
                                public void run() {
                                    DraftModelManager.addPoi(FastEditActivity.draft, item);
                                    finish();
                                }
                            });
                            return;
                        }
                    }
                });
            }

        };

        lvRests.setAdapter(adapter);
        prl = supportPull(lvRests, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                List listData = adapter.getData();
                if (listData.size() > 0) {
                    Object object = listData.get(listData.size() - 1);
                    if (object instanceof SYShopLocationResult.SYShopLocationModel) {
                        SYShopLocationResult.SYShopLocationModel model = (SYShopLocationResult.SYShopLocationModel) object;
                        getData(false, false, k, model.getMerchantType());
                    }
                }
            }
        });
        prl.setDoPullUp(false);
        prl.setDoPullDown(false);
    }

    public static class ShopDishResult extends BaseResult {

        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    private void showWantEatDialog(final List<SYShopLocationResult.SYShopLocationModel> list) {

        final View contentView = getLayoutInflater().inflate(
                R.layout.dialog_want_eat_list, null);
        // 设置按钮的点击事件
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        ImageView button = (ImageView) contentView.findViewById(R.id.iv_close);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        final BaseAdapter adapter11 = new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public SYShopLocationResult.SYShopLocationModel getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_dialog_want_eat, parent, false);
                }
                final SYShopLocationResult.SYShopLocationModel item = getItem(position);
                ((TextView) ((ViewGroup) convertView).getChildAt(0)).setText(item.getTitle());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DraftModelManager.addPoi(item);
                        finish();
                    }
                });
                return convertView;
            }
        };
        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        PullToRefreshLayout prl = null;
        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            int page = 1;

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getWantEatList(list, pullToRefreshLayout, new Runnable() {
                    @Override
                    public void run() {
                        adapter11.notifyDataSetChanged();
                    }
                }, ++page);
            }
        });

        prl.setDoPullUp(list.size() == 15);
        prl.setDoPullDown(false);

        listView.setAdapter(adapter11);


        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));

        // 设置好参数之后再show
        popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);

    }


    private void getWantEatList(final List<SYShopLocationResult.SYShopLocationModel> list, final PullToRefreshLayout prl, final Runnable runn, int page) {
        post(IUrlUtils.Search.queryFoodEatShopListV250, "", null, new FFNetWorkCallBack<SYShopLocationResult>() {
            @Override
            public void onSuccess(SYShopLocationResult response, FFExtraParams extra) {
                list.addAll(response.getList());
                if (prl != null) {
                    prl.loadmoreFinish(response.getList().size() < 15 ? PullToRefreshLayout.NO_DATA_SUCCEED : PullToRefreshLayout.SUCCEED);
                }
                if (response.getList().size() < 15 && prl != null) {
                    prl.setDoPullUp(false);
                }
//                if(FFUtils.isListEmpty(response.getList()) && prl != null){
//                    showToast("没有更多了");
//                }
                if (runn != null) {
                    runn.run();
                } else {
                    showWantEatDialog(list);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                if (prl != null) {
                    prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
                return false;
            }
        }, "pageNum", page, "pageSize", "15", "start", "0");
    }

    int currentPage = 1;

    String k;

    private void getLocation(final SYShopLocationResult.SYShopLocationModel item, final Runnable runnable) {
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        String locality = CityPref.getSelectedCity().getAreaName();
//        locality = loc.getCity();
//        if (locality == null) {
//            new EnsureDialog.Builder(this).setMessage("定位失败，请检查网络连接和gps设置然后重新定位。").setPositiveButton("重试定位", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    initLocation(new Runnable() {
//                        @Override
//                        public void run() {
//                            getLocation(item, runnable);
//                        }
//                    });
//                }
//            }).create().show();
//            return;
//        }

        if (locality != null && locality.endsWith("市")) {
            locality = locality.substring(0, locality.length() - 1);
        }
        if (item.getAd_info() == null) {
            item.setAd_info(new SYShopLocationResult.SYShopLocationInfoModel());
        }
        item.getAd_info().setCity(locality);
        runnable.run();
    }

    private void getData(final boolean isInit, final boolean show, final String keyWord, final int merchantType) {
        if (getDestroyed()) {
            return;
        }

        if (loc == null) {
            return;
        }

        k = keyWord;
        if (isInit) {
            currentPage = 1;
        }

        ArrayList<Object> params = new ArrayList<>();

        if (loc.getLatitude() > 0) {
            params.add("latitude");
            params.add(loc.getLatitude());
        }

        if (loc.getLongitude() > 0) {
            params.add("longitude");
            params.add(loc.getLongitude());
        }

        params.add("city");
        params.add(mCity == null ? (loc.getCity() != null ? loc.getCity() : "北京") : mCity.getAreaName());


        params.add("pageSize");
        params.add(15);

        params.add("pageNum");
        params.add(currentPage);

        if (!FFUtils.isStringEmpty(keyWord)) {
            params.add("keyword");
            params.add(keyWord);
        }

        params.add("merchantType");
        params.add(merchantType);

        dismissProgressDialog(dialogId);
        if (FFUtils.isStringEmpty(keyWord)) {
            prl.setDoPullUp(true);
        } else {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
            prl.setDoPullUp(false);
        }

        post(IUrlUtils.Search.search_shop, show ? "" : null, null, new FFNetWorkCallBack<SYShopLocationResult>() {
            @Override
            public void onSuccess(SYShopLocationResult response, FFExtraParams extra) {
                if (k != keyWord) {
                    return;
                }

                if (isInit) {
                    adapter.setData(response.getList());
                } else {
                    adapter.addData(response.getList());
                }

                currentPage++;
                adapter.notifyDataSetChanged();
                prl.loadmoreFinish(prl.SUCCEED);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(prl.FAIL);
                return false;
            }
        }, params.toArray());
    }

    int dialogId;

    @Override
    public void onBackPressed(View v) {
        super.onBackPressed();
    }


    private LocationClient locationClient = null;

    private void initLocation() {
        initLocation(null);
    }

    private void initLocation(final Runnable runn) {
        final int id = showProgressDialog("loading...");
        //获取定位信息

        locationClient = new LocationClient(this);

        //设置定位条件
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);        //是否打开GPS

        option.setCoorType("bd09ll");       //设置返回值的坐标类型。

        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级

        option.setProdName("LocationDemo"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。

//        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒

        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息


        locationClient.setLocOption(option);

        locationClient.start();  // 调用此方法开始定位

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (isFinishing() || getDestroyed()) {
                    locationClient.stop();
                    return;
                }

                if (location == null) {
                    loc = new BDLocation();
                    Address address = new Address.Builder().city("北京").build();
                    loc.getAddress();
                    loc.setLatitude(116.499067);
                    loc.setLongitude(39.979605);
                    AddRestActivity.this.showToast("定位失败");
                    if (runn != null) {
                        runn.run();
                    } else {

                    }
                    dismissProgressDialog(id);
                    return;
                }
                loc = location;
                locationClient.stop();//取消定位
                if (runn != null) {
                    runn.run();
                } else {
                    getData(true, true, null, 0);
                }
                dismissProgressDialog(id);

            }
        });


    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
//    private AMapLocationClientOption getDefaultOption() {
//        AMapLocationClientOption mOption = new AMapLocationClientOption();
//        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
//        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
//        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
//        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
//        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
//        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        return mOption;
//    }

    BDLocation loc;
    /**
     * 定位监听
     */
//    AMapLocationListener locationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation loc) {
//            if (null != loc) {
//                if (loc.getErrorCode() == 0) {
//                    AddRestActivity.this.loc = loc;
//                    getData(true, true, null, 0);
//                } else {//异常
//                    showToast("定位失败！", "异常信息" + loc.getErrorInfo() + " code:" + loc.getErrorCode());
//                }
//            } else {//为空
//                showToast("定位失败！", null);
//            }
//        }
//    };

    // 根据控件的选择，重新设置定位参数
//    private void resetOption() {
//        // 设置是否需要显示地址信息
//        locationOption.setNeedAddress(false);
//        /**
//         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
//         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
//         */
//        locationOption.setGpsFirst(false);
//        // 设置是否开启缓存
//        locationOption.setLocationCacheEnable(true);
//        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
//        locationOption.setOnceLocationLatest(true);
//
//        // 设置网络请求超时时间
//        locationOption.setHttpTimeOut(10000);
//    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
//    private void startLocation() {
//        //根据控件的选择，重新设置定位参数
//        resetOption();
//        // 设置定位参数
//        locationClient.setLocationOption(locationOption);
//        // 启动定位
//        locationClient.startLocation();
//    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
//    private void stopLocation() {
//        // 停止定位
//        locationClient.stopLocation();
//    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
//    private void destroyLocation() {
//        if (null != locationClient) {
//            /**
//             * 如果AMapLocationClient是在当前Activity实例化的，
//             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
//             */
//            locationClient.onDestroy();
//            locationClient = null;
//            locationOption = null;
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时销毁定位
        if (locationClient != null) {
            locationClient.stop();
        }
    }
}
