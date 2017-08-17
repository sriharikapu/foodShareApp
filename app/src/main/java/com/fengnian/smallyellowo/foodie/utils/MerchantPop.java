//package com.fengnian.smallyellowo.foodie.utils;
//
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.fan.framework.base.FFContext;
//import com.fan.framework.http.FFExtraParams;
//import com.fan.framework.http.FFNetWorkCallBack;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appconfig.Constants;
//import com.fengnian.smallyellowo.foodie.bean.DynamiDrawer;
//import com.fengnian.smallyellowo.foodie.dialogs.AddrDialog;
//import com.fengnian.smallyellowo.foodie.dialogs.AddrDialog2;
//import com.fengnian.smallyellowo.foodie.fragments.HomeChildDynamicFrag;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2017/1/5.
// */
//
//public class MerchantPop {
//
//    private String merchantName;
//    private String merchantPhone;
//    private ArrayList<String> strings;
//
//    public MerchantPop() {
//    }
//    public FFContext context;
//    public String  id;
//    public HomeChildDynamicFrag fragment;
//    public Context context1;
//    public Activity activity;
//
//
//    public MerchantPop(FFContext context, String id, Context context1, Activity ctivity) {
//        this.context=context;
//        this.id=id;
//        this.context1=context1;
//        this.activity=ctivity;
//    }
//
//    private static final ArrayList<String> titles = new ArrayList<String>();
//
//    private String merchantAddress;
//    public void netDrawer(){
//
//        context.post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfo.do", null, null, new FFNetWorkCallBack<DynamiDrawer>() {
//
//
//
//            @Override
//            public void onSuccess(DynamiDrawer response, FFExtraParams extra) {
//                DynamiDrawer.BuinessDetailBean buinessDetail = response.getBuinessDetail();
//                merchantAddress = buinessDetail.getMerchantAddress();
//                merchantName = buinessDetail.getMerchantName();
//                merchantPhone = (String) buinessDetail.getMerchantPhone();
//
//                strings = new ArrayList<String>();
//                if(strings !=null){
//                    strings.clear();
//                }
//
//                //String sourceStr = "1,2,3,4,5,6,7,8,9,0";
//                if(merchantPhone !=null&& merchantPhone.contains(",")){
//
//                    String[] sourceStrArray = merchantPhone.split(",");
//                    for (int i = 0; i < sourceStrArray.length; i++) {
//                        String s = sourceStrArray[i];
//                        strings.add(s);
//                    }
//                }else if(merchantPhone !=null&& !merchantPhone.contains(",")==true){
//                    strings.add(merchantPhone);
//                }
//                // lvStyleAdapter1.notifyDataSetChanged();
//
//                if (titles != null) {
//                    titles.clear();
//                }
//
////                                       if (merchantPhone!=null) {
////                                       titles.add(merchantPhone);
////                                       showToast(merchantPhone+merchantName+"");
////                                    }
//
//
//                if (merchantPhone !=null&& merchantPhone.contains(",")) {
//                    titles.add(strings.get(0));
//                }else if(merchantPhone !=null&& !merchantPhone.contains(",")==true){
//                    titles.add(merchantPhone);
//                   // showToast(merchantPhone+merchantName+"");
//                }
//
//                if (merchantAddress != null) {
//                    titles.add(merchantAddress);
//                }
//                styleAdapter.notifyDataSetChanged();
//                firstdialog();
//
//
//
//            }
//
//            @Override
//            public boolean onFail(FFExtraParams extra) {
//                return false;
//            }
//
//        }, "merchantId",id);
//    }
//
//
//
//
//    private StyleAdapter styleAdapter;
//
//    {
//        styleAdapter = new StyleAdapter();
//    }
//
//    public void firstdialog() {
//        final AddrDialog addrDialog = new AddrDialog(context1);
//        addrDialog.show();
//        addrDialog.getDialog2().setText(merchantName);
//
//        ListView lvStyle = addrDialog.getLvStyle();
//        ProgressBar pb_bar = addrDialog.getPb_bar();
//        pb_bar.setVisibility(View.VISIBLE);
//        lvStyle.setAdapter(styleAdapter);
//
//        Window window = addrDialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        Display d = window.getWindowManager().getDefaultDisplay();
//        wlp.width = (int) (d.getWidth());
//        wlp.gravity = Gravity.BOTTOM;
//        if (wlp.gravity == Gravity.BOTTOM)
//            wlp.y = 0;
//        window.setAttributes(wlp);
//        pb_bar.setVisibility(View.INVISIBLE);
//        lvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //SpUtils.putParam(getContext(), Constants.ADDR_STYLE_BG, mBgs[position]);
//                addrDialog.dismiss();
//
//
////                                    if(getItem(position).){
//                TextView tv_ias_name = (TextView) view.findViewById(R.id.tv_ias_name);
//                String s1 = tv_ias_name.getText().toString();
//
//                if (s1.matches("[0-9]{1,}")||s1.substring(0,2).matches("[0-9]{1,}")) {
//                    //ivBg.setImageResource(mBgs[1]);
//                    //telmethod();
//
//                  dialte2();
//
//
//                }
//                else {
//                    Toast.makeText(context1,"那你",Toast.LENGTH_SHORT).show();
//                    //ivBg.setImageResource(mBgs[0]);
//
//                    map.gotomap();
//
////                        fragment.startActivity(RestLocationActivity.class, new RestLocationIntent().
////                                setLat(feed.getFood().getPoi().getLatitude()).
////                                setLng(feed.getFood().getPoi().getLongitude()).
////                                setName(feed.getFood().getPoi().getTitle()).
////                                setAddress(feed.getFood().getPoi().getAddress()));
//                    }
//
//                }
//
//
//
//
//        });
//    }
//
//    public Map map;
//
//    public void setmap(Map map){
//        this.map=map;
//    };
//
//
//    public interface Map{
//       void gotomap();
//    };
//
//
//
//    public class StyleAdapter extends BaseAdapter {
//        //在同一类中就没必要传了这里犯了错误,不要在这里进行集合的应用
//
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return titles.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = View.inflate(context1, R.layout.item_addr_style, null);
//            }
//            ImageView ivBg = (ImageView) convertView.findViewById(R.id.iv_ias_bg);
//            TextView tvName = (TextView) convertView.findViewById(R.id.tv_ias_name);
//            ImageView ivSelect = (ImageView) convertView.findViewById(R.id.iv_ias_select);
//
//            tvName.setText(titles.get(position));
//
//            String s = titles.get(position);
//            String b=s.substring(0,2);
//            if (s.matches("[0-9]{1,}")||b.matches("[0-9]{1,}")) {
//                ivBg.setImageResource(mBgs[1]);
//
//            } else {
//                ivBg.setImageResource(mBgs[0]);
//            }
//
//
//
////			// 取出选中的背景
////			int bg = SpUtils.getIntParam(getContext(), Constants.ADDR_STYLE_BG,
////					R.drawable.addr_toast_bg);
////			if (mBgs[position] == bg) {
////				// 被选中的 显示右侧的对号图标
////				ivSelect.setVisibility(View.VISIBLE);
////			} else {
////				ivSelect.setVisibility(View.INVISIBLE);
////			}
//            return convertView;
//        }
//
//    }
//
//
//
//    private static final int[] mBgs = new int[]{R.mipmap.accuracy_adress,
//            R.mipmap.add_tel};
//
//
//    LvStyleAdapter lvStyleAdapter1;
//
//    public void dialte2() {
//
//
////               strings.add("123");
//        //              strings.add("456");
//        final AddrDialog2 addrDialog2=new AddrDialog2(context1,R.style.filletDialog);
//        addrDialog2.show();
//        TextView dialog3 = addrDialog2.getDialog3();
//        dialog3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addrDialog2.dismiss();
//            }
//        });
//
//        //addrDialog.getDialog2().setText(feed.getFood().getPoi().getTitle());
//        ListView lvStyle2 = addrDialog2.getLvStyle1();
//        lvStyleAdapter1 = new LvStyleAdapter();
//        lvStyle2.setAdapter(lvStyleAdapter1);
//        lvStyleAdapter1.notifyDataSetChanged();
//
//        lvStyle2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                addrDialog2.dismiss();
//                TextView viewById = (TextView) view.findViewById(R.id.vt_tel2);
//
//                dialtel(viewById.getText().toString());
//            }
//        });
//
//
//    }
//
//
//    class LvStyleAdapter extends BaseAdapter {
//
//
//        @Override
//        public int getCount() {
//            return strings.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return strings.get(position );
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = View.inflate(context1, R.layout.item_tel, null);
//            TextView viewById = (TextView) view.findViewById(R.id.vt_tel2);
//            viewById.setText(strings.get(position));
//            return view;
//        }
//    }
//
//
//
//    public void dialtel(final String s) {
//        final Dialog dialog = new Dialog(context1, R.style.filletDialog);
//        View view = View.inflate(context1, R.layout.diatel, null);
//        dialog.setContentView(view);
//        Window dialogWindow = dialog.getWindow();
//        TextView bottom_item_text = (TextView) dialogWindow.findViewById(R.id.bottom_item_text);
//        bottom_item_text.setText(s);
//        Button btn_ok2 = (Button) dialogWindow.findViewById(R.id.btn_ok2);
//        btn_ok2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//        Button btn_cancle2 = (Button) dialogWindow.findViewById(R.id.btn_cancle2);
//        btn_cancle2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:"+s));//给意图添加要传递的数据
//                activity.startActivity(intent);//打开打电话
//
//            }
//        });
//        dialog.show();
//
////                            rl_commend_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                                @Override
////                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                                    UserInfoIntent intent = new UserInfoIntent();
////                                    intent.setUser(data.get(position));
////                                    context2.startActivity(UserInfoActivity.class, intent);
////                                }
////                            });
//    }
//
//
//}
