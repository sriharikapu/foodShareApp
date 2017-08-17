package com.fengnian.smallyellowo.foodie;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.HotSearchWordResult;
import com.fengnian.smallyellowo.foodie.bean.results.RestListResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestSearchResultIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SearchRestIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestSearchResultActivity extends BaseActivity<RestSearchResultIntent> implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.fl_search)
    FrameLayout flSearch;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_pick)
    TextView tvPickw;
    @Bind(R.id.tv_pick1)
    TextView tvPick1;
    @Bind(R.id.tv_sort)
    TextView tvSort;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.btn_ok)
    ImageView btnOk;
    @Bind(R.id.btn_reset)
    ImageView btnReset;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.lv_sort)
    ListView lvSort;
    @Bind(R.id.lv_class)
    ListView lvClass;
    @Bind(R.id.lv_pick)
    ListView lvPick;
    @Bind(R.id.rl_pick_sort)
    RelativeLayout rlPickSort;
    @Bind(R.id.rl_pick)
    RelativeLayout rlPick;
    @Bind(R.id.ll_sort)
    LinearLayout llSort;
    @Bind(R.id.lv_area_left)
    ListView lvAreaLeft;
    @Bind(R.id.lv_area_right)
    ListView lvAreaRight;
    @Bind(R.id.ll_area)
    LinearLayout llArea;
    @Bind(R.id.ll_class)
    LinearLayout llClass;
    @Bind(R.id.tv_keyword)
    TextView tvKeyword;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private String keyword;
    int areaParent = 0;
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
    private String nowWeight;

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();

        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 100);
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                HotSearchWordResult.HotWord word = data.getParcelableExtra("word");

                int type = word.getType();
                if (type == 0) {//class
                    foodClazz = word.getName();
                    tvClass.setText(foodClazz);
                } else if (type == 1) {//area
                    area = new SiftBean.BusinessListBean.BusinessGroup.Business();
                    area.setContent(word.getName());
                    area.setId(word.getId());
                    tvArea.setText(area.getContent());
                    int i = 0;
                    aa:
                    for (SiftBean.BusinessListBean.BusinessGroup group : this.data.getBusiness().getList()) {
                        for (SiftBean.BusinessListBean.BusinessGroup.Business bun : group.getList()) {
                            if (bun.getId().equals(group.getId())) {
                                areaParent = i;
                                break aa;
                            }
                        }
                        i++;
                    }
                }
            } else if (resultCode == 10) {
                if (data == null) {
                    keyword = "";
                } else {
                    keyword = data.getStringExtra("word");
                }
                tvKeyword.setText(keyword);
            }
            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    prl.autoRefresh();
                }
            }, 500);
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
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
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
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
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
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private MyBaseAdapter<Holder, SYFindMerchantInfo> adapter;

    public static class Holder {
        ImageView iv_img;
        TextView tv_name;
        TextView share_num;
        TextView tv_distance;
        TextView tv_area;
        TextView tv_class_and_per_people;
        ImageView iv_xiang;
        ImageView iv_guan;
        ImageView iv_da;
        RatingBar ratingBar;
        TextView tv_score;
    }

    public static class SiftHolder {
        TextView textView;
        ImageView imageView;
    }

    SiftBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        ConfigResult conf = SP.getConfig();
        if (conf != null) {
            data = conf.getConfig().getSift();
        }


        /*for (SiftBean.FilterListBean.FilterBean filt : data.getFilter().getList()) {
            filt.setChecked(false);
        }*/
        if (data == null) {
            showToast("请稍等，正在获取配置信息");
            finish();
            return;
        }
        setContentView(R.layout.activity_rest_search_result);

        initLocation();
        ButterKnife.bind(this);

        btnReset.setEnabled(false);

        adapter = new MyBaseAdapter<Holder, SYFindMerchantInfo>(Holder.class, context(), R.layout.item_search_rest_result) {
            String getSubFloat(double f) {
                DecimalFormat fnum = new DecimalFormat("##0.0");
                String string = fnum.format(f);
                return string.equals("-0") ? "0" : string;
            }

            @Override
            public void initView(View convertView, Holder holder, int position, SYFindMerchantInfo item) {
                FFImageLoader.loadSmallImage(context(), item.getMerchantImage().getThumbUrl(), holder.iv_img);
                holder.tv_area.setText(item.getMerchantArea());
                if (FFUtils.getSubFloat(item.getMerchantPrice()).equals("0")) {
                    String jj = item.getMerchantKind();
                    if (FFUtils.isStringEmpty(jj)) {
                        holder.tv_class_and_per_people.setVisibility(View.INVISIBLE);
                    } else {
                        holder.tv_class_and_per_people.setVisibility(View.VISIBLE);
                        holder.tv_class_and_per_people.setText(jj);
                    }
                } else {
                    holder.tv_class_and_per_people.setVisibility(View.VISIBLE);
                    String jj = item.getMerchantKind() + "  |  ";
                    if (item.getMerchantKind() == null || item.getMerchantKind().length() == 0) {
                        jj = "";
                    }
                    holder.tv_class_and_per_people.setText(jj + "￥" + FFUtils.getSubFloat(item.getMerchantPrice()) + "/人");
                }
                holder.tv_distance.setText(item.getMerchantDistance());
                holder.tv_name.setText(item.getMerchantName());
                holder.share_num.setText(item.getFriendShares() + "位圈友分享过");
                if (item.getStartLevel() <= 0) {
                    holder.ratingBar.setVisibility(View.GONE);
                    holder.tv_score.setVisibility(View.GONE);
                } else {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.tv_score.setVisibility(View.VISIBLE);
                    holder.ratingBar.setRating(item.getStartLevel());
                    holder.tv_score.setText(getSubFloat(item.getStartLevel()));
                }
                if (item.isMerchantIsWant()) {
                    holder.iv_xiang.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_xiang.setVisibility(View.GONE);
                }
                if (item.isMerchantIsRelation()) {
                    holder.iv_guan.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_guan.setVisibility(View.GONE);
                }
                if (item.isMerchantIsDa()) {
                    holder.iv_da.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_da.setVisibility(View.GONE);
                }

            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pushEvent107(position + "", adapter.getItem(position));
                RestInfoIntent intent = new RestInfoIntent();
                intent.setInfo(adapter.getItem(position));
                startActivity(RestInfoActivity.class, intent);
            }
        });
        //商圈列表城市区域
        lvAreaLeft.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getBusiness().getList()) {

            @Override
            public void initView(View convertView, SiftHolder holder, final int position, final SiftBean.BusinessListBean.BusinessGroup item) {
                if (areaParent == position) {
                    convertView.setBackgroundColor(0xffffffff);
                } else {
                    convertView.setBackgroundColor(0xffeeeeee);
                }
                holder.textView.setText(item.getContent());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areaParent = position;
                        notifyDataSetChanged();
                        MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business> adapter = ((MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business>) lvAreaRight.getAdapter());
                        adapter.setData(item.getList());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        int hight = Math.min(FFUtils.getDisHight() - FFUtils.getPx(25 + 48 + 49 + 48 + 20), data.getFilter().getList().size() * (FFUtils.getPx(48) + 1));
        lvPick.getLayoutParams().height = hight;

        //商圈列表城市商圈
        lvAreaRight.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.BusinessListBean.BusinessGroup.Business>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getBusiness().getList().get(0).getList()) {

            @Override
            public void setData(List<SiftBean.BusinessListBean.BusinessGroup.Business> data) {
                super.setData(data);
            }

            @Override
            public void onGetView(SiftHolder holder) {
                super.onGetView(holder);
                holder.imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void initView(View convertView, SiftHolder holder, int position, final SiftBean.BusinessListBean.BusinessGroup.Business item) {
                if (area != null && area.getId().equals(item.getId())) {
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.imageView.setVisibility(View.INVISIBLE);
                    holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                }
                holder.textView.setPadding(0, 0, 0, 0);
                holder.textView.setText(item.getContent());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyDataSetChanged();
                        llArea.setVisibility(View.GONE);
                        initAllArrow();
                        lastVis = null;

                        area = item;
                        RestSearchResultActivity.this.getData(true);
                        prl.autoRefresh();

                        tvArea.setText(item.getContent());
                    }
                });
            }
        });

        //品类列表
        lvClass.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.FoodKindListBean.FoodKindBean>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getFoodKind().getFoodKind()) {
            @Override
            public void initView(View convertView, SiftHolder holder, final int position, final SiftBean.FoodKindListBean.FoodKindBean item) {
                holder.textView.setText(item.getContent());
                if (foodClazz != null && foodClazz.equals(item.getContent())) {
                    holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.mipmap.restrestult_checked);
                } else {
                    holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                    holder.imageView.setVisibility(View.GONE);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llClass.setVisibility(View.GONE);
                        initAllArrow();
                        lastVis = null;

                        foodClazz = item.getContent();
                        RestSearchResultActivity.this.getData(true);
                        prl.autoRefresh();

                        tvClass.setText(item.getContent());
                        notifyDataSetChanged();
                    }
                });
            }
        });
        // 筛选列表
        lvPick.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.FilterListBean.FilterBean>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getFilter().getList()) {
            @Override
            public void initView(View convertView, SiftHolder holder, int position, final SiftBean.FilterListBean.FilterBean item) {
                holder.textView.setText(item.getContent());

                if (item.isChecked()) {
                    holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.mipmap.restrestult_selected);
                } else {
                    holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                    holder.imageView.setVisibility(View.GONE);
                }


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item.setChecked(!item.isChecked());
                        notifyDataSetChanged();
                        int count = 0;
                        for (int i = 0; i < data.getFilter().getList().size(); i++) {
                            if (data.getFilter().getList().get(i).isChecked()) {
                                count++;
                            }
                        }
                        if (count <= 1) {
                            tvPick1.setText("");
                        } else {
                            tvPick1.setText(count + "");
                        }

                        boolean has = false;
                        for (SiftBean.FilterListBean.FilterBean bean : data.getFilter().getList()) {
                            has = has || bean.isChecked();
                        }

                        btnReset.setEnabled(has);
                    }
                });

            }
        });
        //排序列表
        lvSort.setAdapter(new MyBaseAdapter<SiftHolder, SiftBean.SortListBean.SortBean>(SiftHolder.class, context(), R.layout.item_rest_sift, data.getSort().getList()) {
            @Override
            public void initView(View convertView, SiftHolder holder, int position, final SiftBean.SortListBean.SortBean item) {
                holder.textView.setText(item.getContent());
                if (sortCode == item.getCode()) {
                    holder.textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.imageView.setImageResource(R.mipmap.restrestult_checked);
                } else {
                    holder.textView.setTextColor(getResources().getColor(R.color.ff_text_black));
                    holder.imageView.setVisibility(View.GONE);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortCode = item.getCode();
                        tvSort.setText(item.getContent());
                        RestSearchResultActivity.this.getData(true);
                        prl.autoRefresh();
                        llSort.setVisibility(View.GONE);
                        initAllArrow();
                        lastVis = null;
                        tvSort.setText(item.getContent());
                        notifyDataSetChanged();
                    }
                });
            }
        });

        tvSort.setText("智能排序");
        sortCode = 0;

        if (getIntentData().getType() == RestSearchResultIntent.TYPE_HOT_WORD) {
            int type = getIntentData().getWord().getType();
            if (type == 0) {//class
                foodClazz = getIntentData().getWord().getName();
                tvClass.setText(foodClazz);
            } else if (type == 1) {//area
                area = new SiftBean.BusinessListBean.BusinessGroup.Business();
                area.setContent(getIntentData().getWord().getName());
                area.setId(getIntentData().getWord().getId());
                tvArea.setText(area.getContent());
                int i = 0;
                aa:
                for (SiftBean.BusinessListBean.BusinessGroup group : data.getBusiness().getList()) {
                    for (SiftBean.BusinessListBean.BusinessGroup.Business bun : group.getList()) {
                        if (bun.getId().equals(group.getId())) {
                            areaParent = i;
                            break aa;
                        }
                    }
                    i++;
                }
            }
        } else if (getIntentData().getType() == RestSearchResultIntent.TYPE_AREA) {
            area = getIntentData().getArea();
            tvArea.setText(area.getContent());
        } else if (getIntentData().getType() == RestSearchResultIntent.TYPE_CLASS) {
            foodClazz = getIntentData().getContent();
            tvClass.setText(foodClazz);
        } else if (getIntentData().getType() == RestSearchResultIntent.TYPE_NEARBY) {//附近
            tvSort.setText("距离最近");
            sortCode = 1;
        } else if (getIntentData().getType() == RestSearchResultIntent.TYPE_Key_WORD) {
            keyword = getIntentData().getKeyWord();
            tvKeyword.setText(keyword);
        }

        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            boolean isFirst = true;

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (!isFirst) {
                    getData(true);
                } else {
                    isFirst = false;
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData(false);
            }
        });

        prl.setDoPullUp(true);
        prl.setDoPullDown(true);
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 500);

    }

    private int nextPage = 1;

    /**
     * 分类
     */
    private String foodClazz = "";
    /**
     * 商圈
     */
    private SiftBean.BusinessListBean.BusinessGroup.Business area = null;
    /**
     * 排序方式
     */
    private int sortCode = 0;

    private final int pageSize = 15;

    private void getData(final boolean isInit) {
        if (isInit) {
            nextPage = 1;
            nowWeight = null;
        }
        if (adapter.getCount() == 0) {
            nextPage = 1;
        }
        String XGD = "";
        StringBuilder filterName = new StringBuilder();
        for (SiftBean.FilterListBean.FilterBean filt : data.getFilter().getList()) {
            if (filt.isChecked()) {
                XGD += filt.getCode();
                filterName.append(filt.getContent()).append(",");
                filt.setRealyChecked(filt.isChecked());
            }
        }

        ArrayList<Object> params = new ArrayList<>();
        params.add("openGPS");
        params.add("0");
        params.add("pageNum");
        params.add(nextPage);
        if (nextPage != 1 && adapter.getCount() > 0) {
            params.add("merchantUid");
            params.add(adapter.getData().get(adapter.getCount() - 1).getMerchantUid());
            if (sortCode == 0) {
                params.add("latitude");
                params.add(0);
                params.add("longitude");
                params.add(0);
            } else {
//                params.add("latitude");
//                params.add(CityPref.getSelectedCity().getLatitude());
//                params.add("longitude");
//                params.add(CityPref.getSelectedCity().getLongitude());
//                params.add(CityPref.getSelectedCity().getAreaName());
                if (loc != null && loc.getCity().replaceAll("市", "").equals(CityPref.getSelectedCity().getAreaName().replaceAll("市", ""))) {
                    params.add("latitude");
                    params.add(loc.getLatitude());
                    params.add("longitude");
                    params.add(loc.getLongitude());
                } else {
                    params.add("latitude");
                    params.add(CityPref.getSelectedCity().getLatitude());
                    params.add("longitude");
                    params.add(CityPref.getSelectedCity().getLongitude());
                }
            }
            params.add("nowWeight");
            params.add(nowWeight);
        } else {
            if (loc != null && loc.getCity().replaceAll("市", "").equals(CityPref.getSelectedCity().getAreaName().replaceAll("市", ""))) {
                params.add("latitude");
                params.add(loc.getLatitude());
                params.add("longitude");
                params.add(loc.getLongitude());
            } else {
                params.add("latitude");
                params.add(CityPref.getSelectedCity().getLatitude());
                params.add("longitude");
                params.add(CityPref.getSelectedCity().getLongitude());
            }
        }
        params.add("city");
        params.add(CityPref.getSelectedCity().getAreaName());
        params.add("pageSize");
        params.add(pageSize);
        params.add("screeningConditions");//筛选
        params.add(XGD);
        params.add("sortType");//排序方式
        params.add(sortCode);
        if (area != null && !area.getId().equals("-1")) {
            params.add("street");
            params.add(area.getId());
        }
        if (!FFUtils.isStringEmpty(foodClazz) && !"全部美食".equals(foodClazz)) {
            params.add("ptype");
            params.add(foodClazz);
        }
        if (!FFUtils.isStringEmpty(keyword)) {//关键字
            params.add("shopName");
            params.add(keyword);
        }

        if (isInit) {

            pushEvent106(TextUtils.isEmpty(foodClazz) ? "全部美食" : foodClazz,
                    filterName.toString(),
                    area == null ? "" : area.getContent());
        }

//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/discoverV260.do", null, null, new FFNetWorkCallBack<RestListResult>() {
        post(IUrlUtils.Search.discoverV260, null, null, new FFNetWorkCallBack<RestListResult>() {
            @Override
            public void onSuccess(RestListResult response, FFExtraParams extra) {
                if (isInit) {
                    adapter.setData(response.getMerchantList());
                } else {
                    adapter.addData(response.getMerchantList());
                }
                nowWeight = response.getNowWeight();
                nextPage++;
                if (response.getMerchantList().size() < pageSize) {
                    adapter.setLoadMore(true);
                    adapter.setHasMore(false);
                    prl.setDoPullUp(false);
                } else {
                    adapter.setLoadMore(false);
                    adapter.setHasMore(true);
                    prl.setDoPullUp(true);
                }
                prl.loadmoreFinish(prl.SUCCEED);
                prl.refreshFinish(prl.SUCCEED);
                adapter.notifyDataSetChanged();
                listView.setSelection(0);

            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.refreshFinish(prl.FAIL);
                prl.loadmoreFinish(prl.FAIL);
                return false;
            }
        }, params.toArray());
    }

    View lastVis;

    private void initAllArrow() {
        unselectTv(tvClass);
        unselectTv(tvArea);
        unselectTv(tvSort);

        tvPickw.setTextColor(getResources().getColor(R.color.ff_text_black));
        tvPick1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_down, 0);

        int count = 0;
        for (int i = 0; i < data.getFilter().getList().size(); i++) {
            if (data.getFilter().getList().get(i).isChecked()) {
                count++;
            }
        }
        if (count == 0) {
            tvPick1.setText("");
            tvPickw.setTextColor(getResources().getColor(R.color.ff_text_black));
        } else if (count == 1) {
            tvPick1.setText("");
            tvPickw.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            tvPick1.setText("" + count);
            tvPickw.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void selectTv(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_up, 0);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void unselectTv(TextView tv) {
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_down, 0);
        tv.setTextColor(getResources().getColor(R.color.ff_text_black));
    }


    @OnClick({R.id.fl_search, R.id.rl_class, R.id.fl_area, R.id.fl_pick, R.id.fl_sort, R.id.btn_ok, R.id.btn_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_search:
                if (lastVis != null) {
                    setLastVis();
                    initAllArrow();
                    lastVis = null;
                }
                startActivity(SearchRestActivity.class, new SearchRestIntent(2, keyword));
                break;
            case R.id.rl_class:
                if (lastVis != null) {
                    setLastVis();
                    initAllArrow();
                }
                if (lastVis != llClass) {
                    llClass.setVisibility(View.VISIBLE);
                    selectTv(tvClass);
                    lastVis = llClass;
                } else {
                    lastVis = null;
                }
                break;
            case R.id.fl_area:
                if (lastVis != null) {
                    setLastVis();
                    initAllArrow();
                }
                if (lastVis != llArea) {
                    llArea.setVisibility(View.VISIBLE);
                    selectTv(tvArea);
                    lastVis = llArea;
                } else {
                    lastVis = null;
                }
                break;
            case R.id.fl_pick:
                if (lastVis != null) {
                    setLastVis();
                    initAllArrow();
                }
                if (lastVis != rlPick) {
                    rlPick.setVisibility(View.VISIBLE);
                    tvPickw.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvPick1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.rest_result_arrow_up, 0);
                    lastVis = rlPick;
                } else {
                    lastVis = null;
                }
                break;
            case R.id.fl_sort:
                if (lastVis != null) {
                    setLastVis();
                    initAllArrow();
                }
                if (lastVis != llSort) {
                    llSort.setVisibility(View.VISIBLE);
                    selectTv(tvSort);
                    lastVis = llSort;
                } else {
                    lastVis = null;
                }
                break;
            case R.id.btn_ok:
                rlPick.setVisibility(View.GONE);
                initAllArrow();
                lastVis = null;
                getData(true);
                break;
            case R.id.btn_reset:
                resetPick(false);
                break;
        }
    }

    private void setLastVis() {
        if (lastVis == rlPick) {
            resetPick(true);
        }
        lastVis.setVisibility(View.GONE);
    }

    private void resetPick(boolean bo) {
        boolean boo = false;
        for (SiftBean.FilterListBean.FilterBean bean : data.getFilter().getList()) {
            boolean checked = bean.isRealyChecked() && bo;
            boo = boo || checked;
            bean.setChecked(checked);
        }
        tvPick1.setText("");
        tvPickw.setTextColor(getResources().getColor(R.color.ff_text_black));
        btnReset.setEnabled(boo);
        ((BaseAdapter) lvPick.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (lastVis != null && lastVis.getVisibility() == View.VISIBLE) {
            setLastVis();
            initAllArrow();
            lastVis = null;
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onBackPressed(View v) {
        if (v.getId() == R.id.iv_back) {
            super.onBackPressed();
        } else {
            onBackPressed();
        }
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    AMapLocation loc;
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                if (loc.getErrorCode() == 0) {
                    RestSearchResultActivity.this.loc = loc;
                } else {//异常
                    showToast("定位异常！", null);
                }
            } else {//为空
                showToast("定位失败！", null);
            }
            getData(true);
        }
    };

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);

        // 设置网络请求超时时间
        locationOption.setHttpTimeOut(10000);
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    private void pushEvent106(String type, String filter, String area) {
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("channel", Tool.getChannelName(RestSearchResultActivity.this));
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("type", type);
        event.put("area", area);
        event.put("filter", filter);
        event.put("sort", tvSort.getText().toString().trim());
        pushEventAction("Yellow_106", event);
    }

    private void pushEvent107(String position, SYFindMerchantInfo info) {
        if (info != null){
            HashMap<String, String> event = new HashMap<String, String>();
            event.put("account", SP.getUid());
            event.put("channel", Tool.getChannelName(this));
            event.put("city", CityPref.getSelectedCity().getAreaName());
            event.put("shop_name", info.getMerchantName());
            event.put("shop_id", info.getMerchantUid());
            event.put("score", info.getStartLevel() + "");
            event.put("position", position);
            event.put("range", info.getMerchantDistance());
            event.put("price", info.getMerchantPrice() + "");
            event.put("from", this.getClass().getName());
            pushEventAction("Yellow_107", event);
        }
    }
}
