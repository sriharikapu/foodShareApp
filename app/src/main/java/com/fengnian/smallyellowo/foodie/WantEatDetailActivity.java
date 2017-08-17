package com.fengnian.smallyellowo.foodie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.Adapter.WantEatDetailAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.WanDetailDyn;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.GetWantDetailInfoOne;
import com.fengnian.smallyellowo.foodie.bean.results.WanDetailResult;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.dialogs.CallDialog;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.RemarkManager;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.ShopErrorInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WanEatDetailIntent;
import com.fengnian.smallyellowo.foodie.shopcommiterror.ShopErrorTypeActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-11.
 */
public class WantEatDetailActivity extends BaseActivity<WanEatDetailIntent> implements View.OnClickListener {
    private ImageView iv_header, iv_10, iv_back, iv_right, iv_commit;
    private TextView tv_name, tv_species, tv_money, tv_distance, tv_location, tv_phone, tv_friend_like_eat;
    private RelativeLayout rl_no_data, rl_like_eat, rl_adress, rl_phone;
    private SYBusiness info; //todo 到时候分享取值
    private ListView lv_list;
    WantEatDetailAdapter adapter;
    SYPoi poi;
    List<WanDetailDyn> list;
    private RatingBar rb_level;

    private TextView tv_start;

    View view;

    RemarkManager rm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        WanEatDetailIntent inten = getIntentData();
        info = inten.getBus();
        if (info == null) info = new SYBusiness();
        setContentView(R.layout.activity_wanteat_detail);

//      isFrom(GrowthHistoryActivity.class)
        iv_back = findView(R.id.iv_back);
        iv_back.setOnClickListener(this);
        iv_right = findView(R.id.iv_right);
        iv_right.setOnClickListener(this);
        iv_commit = findView(R.id.iv_commit);
        if (!SP.isLogin()) {
            iv_commit.setVisibility(View.GONE);
        }
        iv_commit.setOnClickListener(this);

        view = View.inflate(this, R.layout.head_want_eat_detail, null);
        rm = new RemarkManager(view);
        iv_10 = (ImageView) view.findViewById(R.id.iv_10);

        rb_level = (RatingBar) view.findViewById(R.id.rb_level);
        tv_start = (TextView) view.findViewById(R.id.tv_start);

        iv_header = (ImageView) view.findViewById(R.id.iv_header);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_species = (TextView) view.findViewById(R.id.tv_species);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);

        rl_like_eat = (RelativeLayout) view.findViewById(R.id.rl_like_eat);
        rl_like_eat.setOnClickListener(this);
        tv_friend_like_eat = (TextView) view.findViewById(R.id.tv_friend_like_eat);

        rl_adress = (RelativeLayout) view.findViewById(R.id.rl_adress);
        rl_adress.setOnClickListener(this);
        tv_location = (TextView) view.findViewById(R.id.tv_location);

        rl_phone = (RelativeLayout) view.findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);

        lv_list = findView(R.id.lv_list);
        list = new ArrayList<>();
        lv_list.addHeaderView(view);
        adapter = new WantEatDetailAdapter(list, this);
        lv_list.setAdapter(adapter);
        rl_no_data = findView(R.id.rl_no_data);

        if (isFrom(GrowthHistoryActivity.class)) {
            tv_distance.setText("很远");
        } else {
            setui(getIntentData().getBus());

            SYPoi pp = getIntentData().getBus().getPoi();
            String str1 = TextUtils.isEmpty(pp.getAddress()) ? "" : pp.getAddress();
            String str2 = TextUtils.isEmpty(getIntentData().getBus().getDistance()) ? "" : getIntentData().getBus().getDistance();
            tv_distance.setText(str1 + "·" + str2);
        }
        getWantInfo(getIntentData());
        initclick();
    }

    /**
     * 根据shopid  和eatid 获取 用户信息
     */
    void getWantInfo(WanEatDetailIntent want_info) {
        //     /eat/getMerchantDetailsV280.do
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/getMerchantDetailsV280.do", "", extra, new FFNetWorkCallBack<GetWantDetailInfoOne>() {
        post(IUrlUtils.Search.getMerchantDetailsV280, "", extra, new FFNetWorkCallBack<GetWantDetailInfoOne>() {
            @Override
            public void onSuccess(GetWantDetailInfoOne response, FFExtraParams extra) {
                if (isFrom(GrowthHistoryActivity.class)) {
                    info = response.getwEatModel();
                    setui(response.getwEatModel());
                    rm.setData(response.getFindMerchantTags());
                } else {
                }

                if (response.getFoodRecords().size() > 0) {
                    list.addAll(response.getFoodRecords());
                    adapter.notifyDataSetChanged();
                    rl_no_data.setVisibility(View.GONE);
                } else {
                    rl_no_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "merchantId", want_info.getBusinessId(), "lastRecordId", want_info.getRecordId(), "eatId", want_info.getEatId());//, "eatId",
    }


    private void initclick() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(list.get(i - 1).getRecordId()));
            }
        });
    }


    private void setui(SYBusiness info) {
        FFImageLoader.loadSmallImage(this, info.getImage().getUrl(), iv_header);

        poi = info.getPoi();

//        if(poi==null){
//            view.setVisibility(View.INVISIBLE);
//            return;
//        }else{
//            view.setVisibility(View.VISIBLE);
//        }
        if (poi == null) poi = new SYPoi();
        tv_name.setText(poi.getTitle());
        if (TextUtils.isEmpty(info.getStarLevel())) {
            info.setStarLevel("0");
        }
        float start = Float.valueOf(info.getStarLevel());
        if (start > 0 && !TextUtils.isEmpty(info.getStarLevel())) {
            rb_level.setRating(start);
            tv_start.setText(info.getStarLevel());
        } else {
            rb_level.setVisibility(View.GONE);
            tv_start.setVisibility(View.GONE);
        }


        tv_species.setText(info.getCategory());
        if (info.getPerAverage() > 0) {
            tv_money.setText("| ¥ " + info.getPerAverage() + "/人");
        } else {
            tv_money.setVisibility(View.GONE);
        }
        /**
         * 下次需要
         */
//        String str1=TextUtils.isEmpty(poi.getAddress())?"":poi.getAddress();
//        String str2=TextUtils.isEmpty(info.getDistance())?"":info.getDistance();
//        tv_distance.setText(str1 +" "+str2);
        if ("0".equals(info.getEatNumber())) {
            rl_like_eat.setVisibility(View.GONE);
        } else {
            rl_like_eat.setVisibility(View.VISIBLE);
            tv_friend_like_eat.setText(info.getEatNumber() + "位好友想吃");
        }
        if (!TextUtils.isEmpty(info.getDetailAddress())) {
            rl_adress.setVisibility(View.VISIBLE);
            tv_location.setText(info.getDetailAddress());
        } else {
            rl_adress.setVisibility(View.GONE);
        }
        if ("".equals(info.getMerTeleNumber()) || info.getMerTeleNumber() == null) {
            rl_phone.setVisibility(View.GONE);
            tv_phone.setVisibility(View.GONE);
        } else {
            rl_phone.setVisibility(View.VISIBLE);
            tv_phone.setVisibility(View.VISIBLE);
            tv_phone.setText(info.getMerTeleNumber());
        }


    }


    /**
     * 该餐厅的美食动态
     */
    private void getDynicInfo(SYBusiness info) {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/getMerchantDetailsV250.do", null, extra, new FFNetWorkCallBack<WanDetailResult>() {
        post(IUrlUtils.Search.getMerchantDetailsV250, null, extra, new FFNetWorkCallBack<WanDetailResult>() {
            @Override
            public void onSuccess(WanDetailResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    if (response.getFoodRecords().size() > 0) {
                        list.addAll(response.getFoodRecords());
                        adapter.notifyDataSetChanged();
                        rl_no_data.setVisibility(View.GONE);
                    } else {
                        rl_no_data.setVisibility(View.VISIBLE);
                    }

                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "eatId", info.getEatId(), "lastRecordId", "0", "merchantId", info.getMerchantId());  //lastRecordId  是传的是 美食记录id
    }


    private void share(final View view) {
        final String clientTime = TimeUtils.getTime("yyyyMMddhhmmssSSS", System.currentTimeMillis());
//        post(Constants.shareConstants().getNetHeaderAdress() + "/discover/createDisShopDetailsHtmlV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
        post(IUrlUtils.Search.createDisShopDetailsHtmlV250, "", null, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                final String url = ShareUrlTools.getRestInfoUrl(info.getMerchantId(), clientTime);


                View contentView = LayoutInflater.from(context()).inflate(
                        R.layout.pop_share, null);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                // 设置按钮的点击事件
                TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
                TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
                TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);
                final TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                ((View) tv_del.getParent()).setVisibility(View.GONE);
                contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                final String title = info.getPoi().getTitle();
                StringBuilder textBuilder = new StringBuilder();
                if (info.getPerAverage() != 0) {
                    textBuilder.append("￥" + FFUtils.getSubFloat(info.getPerAverage()) + "/人 | ");
                }
                if (!FFUtils.isStringEmpty(info.getCategory())) {
                    textBuilder.append(info.getCategory() + " | ");
                }
                textBuilder.append(info.getPoi().getAddress());
                final String text = textBuilder.toString();
                tv_pyq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(context(), info.getImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                WeixinOpen.getInstance().share2weixin_pyq(url, text, title, bitmap);
                            }

                            @Override
                            public void onDownLoadProgress(int downloaded, int contentLength) {

                            }
                        });
                    }
                });

                tv_py.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(context(), info.getImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                WeixinOpen.getInstance().share2weixin(url, text, title, bitmap);
                            }

                            @Override
                            public void onDownLoadProgress(int downloaded, int contentLength) {

                            }
                        });
                    }
                });
                tv_sina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(context(), info.getImage().getUrl(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                SinaOpen.share(context(), bitmap, title, text, url);
                            }

                            @Override
                            public void onDownLoadProgress(int downloaded, int contentLength) {

                            }
                        });
                    }
                });
//                }


                popupWindow.setTouchable(true);

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.i("mengdd", "onTouch : ");

                        return false;
                    }
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
                popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "clientTime", clientTime, "merchantId", info.getMerchantId(), "shopImg", info.getImage().getUrl(), "pageSize", 3);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_right:
                share(view);
                break;
            case R.id.rl_like_eat:
                //有几位好友想吃
                Intent intent = new Intent(this, GoodFriendsWantEatActivity.class);
                intent.putExtra("merchantUid", getIntentData().getBusinessId());
                startActivity(intent);
                break;
            case R.id.rl_adress:
                if (info.getIsOnline() != 1) {
                    showToast("该商户已下线！");
                    return;
                }
                //地理位置
                if (poi.getIsCustom() == 1) {
                    showToast("该商户为自定义创建，暂无地址信息");
                    return;
                }
                if (poi != null) {
//                    new PopRest(context(),null,  poi.getTel(),
//                            poi.getAddress(), poi.getTitle(),
//                            poi.getLatitude(), poi.getLongitude(),poi.getId()).showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);


                    startActivity(RestLocationActivity.class, new RestLocationIntent().
                            setAddress(info.getDetailAddress()).
                            setName(poi.getTitle()).
                            setLng(poi.getLongitude()).
                            setLat(poi.getLatitude()));
                }
                break;
            case R.id.rl_phone:
                //拨打电话
                new CallDialog(WantEatDetailActivity.this, R.style.ActionSheetDialogStyle, info.getMerTeleNumber()).show();
                break;
            case R.id.iv_commit:
                ShopErrorInfoIntent infoIntent = new ShopErrorInfoIntent();
                infoIntent.setShopid(info.getMerchantId());
                infoIntent.setName(poi.getTitle());
                infoIntent.setPhone(info.getMerTeleNumber());
                infoIntent.setLat(info.getPoi().getLatitude());
                infoIntent.setLng(info.getPoi().getLongitude());
                infoIntent.setAddress(poi.getAddress());
                startActivity(ShopErrorTypeActivity.class, infoIntent);
                break;
        }

    }
}
