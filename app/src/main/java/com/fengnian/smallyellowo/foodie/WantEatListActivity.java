//package com.fengnian.smallyellowo.foodie;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.fan.framework.base.MyBaseAdapter;
//import com.fan.framework.http.FFExtraParams;
//import com.fan.framework.http.FFNetWorkCallBack;
//import com.fan.framework.imageloader.FFImageLoader;
//import com.fan.framework.utils.FFUtils;
//import com.fan.framework.widgets.PullToRefreshLayout;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//import com.fengnian.smallyellowo.foodie.appconfig.Constants;
//import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;
//import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
//import com.fengnian.smallyellowo.foodie.bean.results.WantEatResult;
//import com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty;
//import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
//import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
//import com.fengnian.smallyellowo.foodie.intentdatas.WanEatDetailIntent;
//import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class WantEatListActivity extends BaseActivity<IntentData> {
//
//    private MyBaseAdapter<WantEatHolder, SYBusiness> adapterEated;
//    private MyBaseAdapter<WantEatHolder, SYBusiness> adapterNoEated;
//    private PullToRefreshLayout prlEated;
//    private PullToRefreshLayout prlNoeated;
//
//    public static class WantEatHolder {
//        private ImageView iv_img;
//        private TextView tv_name;
//        private TextView tv_location;
//        private TextView tv_class;
//    }
//
//
//    @Bind(R.id.tv_eated)
//    TextView tvEated;
//    @Bind(R.id.tv_no_eated)
//    TextView tvNoEated;
//    @Bind(R.id.tab_bottom1)
//    View tabBottom1;
//    @Bind(R.id.tab_bottom2)
//    View tabBottom2;
//    @Bind(R.id.lv_eated)
//    ListView lvEated;
//    @Bind(R.id.lv_noeated)
//    ListView lvNoeated;
//    private int lastEatId = 0;
//    private int lastNoEatId = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_want_eat_list);
//        ButterKnife.bind(this);
//        setTitle("想吃清单");
//        if (android.os.Build.VERSION.SDK_INT >= 23 && !FFUtils.isPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
//        } else {
//            if (latitude == 0 && longitude == 0) {
//                initLoaction();
//            }
//        }
//
//
//        addMenu(R.mipmap.add_want_eat, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(AddRestActivity.class, new IntentData().setRequestCode(1));
//            }
//        });
//
//        prlEated = PullToRefreshLayout.supportPull(lvEated, new PullToRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                prlEated.refreshFinish(prlEated.SUCCEED);
//            }
//
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                getData(false, true);
//            }
//        });
//        prlNoeated = PullToRefreshLayout.supportPull(lvNoeated, new PullToRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                prlNoeated.refreshFinish(prlNoeated.SUCCEED);
//            }
//
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                getData(false, false);
//            }
//        });
//        onClick(tvNoEated);
//        prlEated.setDoPullUp(true);
//        prlNoeated.setDoPullUp(true);
//        prlEated.setDoPullDown(false);
//        prlNoeated.setDoPullDown(false);
////        getData(true, false);
////        getData(true, true);
//
//        adapterEated = new WantEatAdapter(WantEatHolder.class, R.layout.item_want_eat);
//        adapterNoEated = new WantEatAdapter(WantEatHolder.class, R.layout.item_want_eat);
//        lvEated.setAdapter(adapterEated);
//        lvNoeated.setAdapter(adapterNoEated);
//
//
//        lvEated.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                getPopWindow(adapterEated.getData().get(i));
//                return true;
//            }
//        });
//        lvEated.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SYBusiness bus_eat = adapterEated.getData().get(i);
//                WanEatDetailIntent eat = new WanEatDetailIntent();
//                eat.setBus(bus_eat);
//                startActivity(WantEatDetailActivity.class, eat);
//            }
//        });
//
//
//        lvNoeated.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                getPopWindow(adapterNoEated.getData().get(i));
//                return true;
//            }
//        });
//        lvNoeated.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SYBusiness bus_no_eat = adapterNoEated.getData().get(i);
//
//                WanEatDetailIntent no_eat = new WanEatDetailIntent();
//                no_eat.setBus(bus_no_eat);
//                startActivity(WantEatDetailActivity.class, no_eat);
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            SYBusiness sb = data.getParcelableExtra("rest");
//            adapterNoEated.getData().add(0, sb);
//            adapterNoEated.notifyDataSetChanged();
//            //.addDataWithNotify(list);
//        }
//    }
//
//    private LocationClient locationClient = null;
//
//    private double latitude, longitude;
//
//    private void initLoaction() {
//        //获取定位信息
//
//        locationClient = new LocationClient(this);
//
//        //设置定位条件
//        LocationClientOption option = new LocationClientOption();
//
//        option.setOpenGps(true);        //是否打开GPS
//
//        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
//
//        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
//
//        option.setProdName("LocationDemo"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
//
////        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
//
//        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
//
//
//        locationClient.setLocOption(option);
//
//        locationClient.start();  // 调用此方法开始定位
//
//        locationClient.registerLocationListener(new BDLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation location) {
//                if (location == null) {
//                    WantEatListActivity.this.showToast("定位失败");
//                    return;
//                }
////                LatLng  latLng=new LatLng(location.getLatitude(),location.getLongitude());
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//
//                getData(true, false);
//                getData(true, true);
//
//                locationClient.stop();//取消定位
//            }
//        });
//    }
//
//    public void getData(boolean isInit, final boolean isEated) {
////        if(android.os.Build.VERSION.SDK_INT>=23) ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_COARSE_LOCATION}, AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
////        else{
////            if(latitude==0&&longitude==0)
////            initLoaction();
////        }
//
//        if (latitude != 0 && longitude != 0) {
////            post(Constants.shareConstants().getNetHeaderAdress() + "/eat/queryFoodEatListV250.do", null, null, new FFNetWorkCallBack<WantEatResult>() {
//            post(IUrlUtils.Search.queryFoodEatListV250, null, null, new FFNetWorkCallBack<WantEatResult>() {
//                @Override
//                public void onSuccess(WantEatResult response, FFExtraParams extra) {
//                    if (response.getErrorCode() == 0) {
//                        if (isEated) {
//                            adapterEated.addData(response.getList());
//                            adapterEated.notifyDataSetChanged();
//                            prlEated.loadmoreFinish(prlEated.SUCCEED);
//                            prlEated.refreshFinish(prlEated.SUCCEED);
//                            prlEated.setDoPullUp(!FFUtils.isListEmpty(response.getList()));
//                            if (FFUtils.isListEmpty(response.getList())) {
//                                adapterEated.setHasMore(false);
//                                adapterEated.setLoadMore(true);
//                            } else {
//                                lastEatId = response.getList().get(response.getList().size() - 1).getEatId();
//                                adapterEated.setHasMore(true);
//                                adapterEated.setLoadMore(false);
//                            }
//                        } else {
//                            adapterNoEated.addData(response.getList());
//                            adapterNoEated.notifyDataSetChanged();
//                            prlNoeated.loadmoreFinish(prlNoeated.SUCCEED);
//                            prlNoeated.refreshFinish(prlNoeated.SUCCEED);
//                            prlNoeated.setDoPullUp(!FFUtils.isListEmpty(response.getList()));
//                            if (FFUtils.isListEmpty(response.getList())) {
//                                adapterNoEated.setHasMore(false);
//                                adapterNoEated.setLoadMore(true);
//                            } else {
//                                lastNoEatId = response.getList().get(response.getList().size() - 1).getEatId();
//                                adapterNoEated.setHasMore(true);
//                                adapterNoEated.setLoadMore(false);
//                            }
//                        }
//                        tvEated.setText("吃过  " + response.getEatNumber());
//                        tvNoEated.setText("未吃  " + response.getUnEatNumber());
//                    } else {
//                        showToast(response.getErrorMessage());
//                    }
//
//                }
//
//                @Override
//                public boolean onFail(FFExtraParams extra) {
//                    if (isEated) {
//                        prlEated.loadmoreFinish(prlEated.FAIL);
//                        prlEated.refreshFinish(prlEated.FAIL);
//                    } else {
//                        prlNoeated.loadmoreFinish(prlNoeated.FAIL);
//                        prlNoeated.refreshFinish(prlNoeated.FAIL);
//                    }
//                    return false;
//                }
//                //"39.9732487114026"   "116.4859683446921"
//            }, "isEat", isEated ? 1 : 0, "lastEatId", isEated ? lastEatId : lastNoEatId, "latitude", latitude + "", "longitude", longitude, "pageSize", "15");
//        }
//    }
//
//    private String eatStatus = "0";
//
//    public class WantEatAdapter extends MyBaseAdapter<WantEatHolder, SYBusiness> {
//
//        public WantEatAdapter(Class<? extends WantEatHolder> clazz, int layoutId) {
//            super(clazz, context(), layoutId);
//        }
//
//        @Override
//        public void initView(View convertView, WantEatHolder holder, int position, final SYBusiness item) {
//            if (item.getImage() != null) {
//                FFImageLoader.loadSmallImage(context(), item.getImage().getUrl(), holder.iv_img);
//            } else {
//                holder.iv_img.setImageResource(R.drawable.alpha);
//            }
//
//            //分类行有两个权值4种情况
//            if (item.getPoi() != null && !FFUtils.isStringEmpty(item.getPoi().getCategory()) && item.getPerAverage() != 0) {
//                holder.tv_class.setText(item.getPoi().getCategory() + "  |  ￥" + FFUtils.getSubFloat(item.getPerAverage()) + "/人");
//                holder.tv_class.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.want_eat_class, 0, 0, 0);
//            } else if (item.getPoi() != null && !FFUtils.isStringEmpty(item.getPoi().getCategory()) && item.getPerAverage() != 0) {
//                holder.tv_class.setText("￥" + FFUtils.getSubFloat(item.getPerAverage()) + "/人");
//                holder.tv_class.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            } else if (item.getPerAverage() != 0) {
//                holder.tv_class.setText(item.getPoi().getCategory());
//                holder.tv_class.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.want_eat_class, 0, 0, 0);
//            } else {
//                holder.tv_class.setText("");
//                holder.tv_class.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            }
//
//            //距离行有两种权值4种情况
//            if (item.getPoi() != null && !FFUtils.isStringEmpty(item.getPoi().getAddress()) && !FFUtils.isStringEmpty(item.getDistance())) {
//                holder.tv_location.setText(item.getPoi().getAddress() + "  ·  " + item.getDistance());
//            } else if (item.getPoi() != null && !FFUtils.isStringEmpty(item.getPoi().getAddress())) {
//                holder.tv_location.setText(item.getPoi().getAddress());
//            } else if (!FFUtils.isStringEmpty(item.getDistance())) {
//                holder.tv_location.setText(item.getDistance());
//            } else {
//                holder.tv_location.setText("");
//            }
//            if (item.getPoi() != null)
//                holder.tv_name.setText(item.getPoi().getTitle());
//            else holder.tv_name.setText("");
//
////            convertView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    SYFindMerchantInfo data = new SYFindMerchantInfo();
////                    data.setMerchantUid(item_image_gallery.getMerchantId());
////                    data.setMerchantName(item_image_gallery.getPoi().getTitle());
////                    data.setMerchantAddress(item_image_gallery.getDetailAddress());
////                    data.setMerchantDistance(item_image_gallery.getDistance());
////                    RestInfoIntent intent = new RestInfoIntent();
////                    intent.setInfo(data);
////                    startActivity(RestInfoActivity.class, intent);
////                }
////            });
//
////            convertView.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View view) {
////                      getPopWindow(item_image_gallery);
////                      return true;
////                }
////            });
//        }
//    }
//
//    private void getPopWindow(final SYBusiness item) {
//        EnsureDialog.Builder dialog = new EnsureDialog.Builder(WantEatListActivity.this);
//        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                FFUtils.getHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //删除对应的条目即可
//                        deleteitem(item);
//                    }
//                }, 500);
//            }
//        }).create().show();
//        dialog.setCancelable(true);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //退出时销毁定位
//        if (locationClient != null) {
//            locationClient.stop();
//        }
//    }
//
//    //删除条目
//    private void deleteitem(final SYBusiness item) {
//        FFExtraParams extra = new FFExtraParams();
//        extra.setDoCache(true);
//        extra.setSynchronized(false);
//        extra.setKeepWhenActivityFinished(false);
//        extra.setProgressDialogcancelAble(true);
////        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/delFoodEatV250.do", "", extra, new FFNetWorkCallBack<BaseResult>() {
//        post(IUrlUtils.Search.delFoodEatV250, "", extra, new FFNetWorkCallBack<BaseResult>() {
//            @Override
//            public void onSuccess(BaseResult response, FFExtraParams extra) {
//                if (0 == response.getErrorCode()) {
//                    showToast("删除成功");
//                    if ("0".equals(eatStatus)) {
//                        adapterNoEated.getData().remove(item);
//                        adapterNoEated.notifyDataSetChanged();
//
//                    } else {
//                        adapterEated.getData().remove(item);
//                        adapterEated.notifyDataSetChanged();
//                    }
//                } else showToast(response.getErrorMessage());
//            }
//
//            @Override
//            public boolean onFail(FFExtraParams extra) {
//                return false;
//            }
//        }, "eatId", item.getEatId(), "merchantId", item.getMerchantId(), "shopType", item.getShopId(), "eatStatus", eatStatus);
//
//    }
//
//
//    @OnClick({R.id.tv_eated, R.id.tv_no_eated})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_no_eated:
//                tabBottom1.setVisibility(View.VISIBLE);
//                tabBottom2.setVisibility(View.GONE);
//                tvEated.setTextColor(getResources().getColor(R.color.ff_text_gray));
//                tvNoEated.setTextColor(getResources().getColor(R.color.ff_text_black));
//                prlNoeated.setVisibility(View.VISIBLE);
//                prlEated.setVisibility(View.GONE);
//                eatStatus = "0";
//                break;
//            case R.id.tv_eated:
//                eatStatus = "1";
//                tabBottom2.setVisibility(View.VISIBLE);
//                tabBottom1.setVisibility(View.GONE);
//                tvEated.setTextColor(getResources().getColor(R.color.ff_text_black));
//                tvNoEated.setTextColor(getResources().getColor(R.color.ff_text_gray));
//                prlNoeated.setVisibility(View.GONE);
//                prlEated.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    /**
//     * 动态申请  权限后用用的选择
//     *
//     * @param requestCode
//     * @param permissions
//     * @param grantResults
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//
//        switch (requestCode) {
//            case AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    //用户授权
//                    if (latitude == 0 && longitude == 0)
//                        initLoaction();
//                } else {
//                    showToast("未获得访问权限，不能查看！！");
//                }
//                return;
//            }
//
//        }
//    }
//
//}
