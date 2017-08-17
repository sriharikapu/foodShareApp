package com.fengnian.smallyellowo.foodie;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.foodClub.ClubItem;
import com.fengnian.smallyellowo.foodie.bean.foodClub.ClubItemsAD;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.SYFoodClubResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;
import com.fengnian.smallyellowo.foodie.widgets.MyRecyclerViewClub;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ClubActivity extends BaseActivity<IntentData> {
    public final int ClubADModelType_Banner = 1;  //banner
    public final int ClubADModelType_MillionFindMerchant = 2;  //百万探店
    public final int ClubADModelType_MillionFindMerchantImage = 3;  //百万探店图片
    public final int ClubADModelType_MillionFindMerchantFindJiJin = 4;  //百万探店探店基金
    public final int ClubADModelType_EatAllOver = 5;  //吃遍全世界
    public final int ClubADModelType_EatAllOverImage = 6;  //吃遍全世界图片
    public final int ClubADModelType_EatAllOver_YiDiMeiShi = 7;  //吃遍全世界活动 用户列表
    public final int ClubADModelType_ExclusivePrivilege = 8;  //专属特权
    public final int ClubADModelType_other = 9;  //其他

    @Bind(R.id.content_View)
    MyRecyclerViewClub contentView;

    final ArrayList<ClubItem> data = new ArrayList<>();
    private RecyclerView.Adapter<MyBaseHolder> adapter;
    private ClubItem sz;

    private class HolderClub1 extends MyBaseHolder {
        private LinearLayout ll_point_container;
        private ViewPager viewPager;
        private final ArrayList<View> viewList = new ArrayList<>();
        private final ArrayList<View> points = new ArrayList<>();

        public HolderClub1(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_1, recyclerView, false));
            ll_point_container = findViewById(R.id.ll_point_container);
            viewPager = findViewById(R.id.viewPager);

            viewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    if (item == null) {
                        return 0;
                    }
                    return item.getItemsAD().size();
                }

                private int mChildCount = 0;

                @Override
                public void notifyDataSetChanged() {
                    mChildCount = getCount();
                    super.notifyDataSetChanged();
                }

                @Override
                public int getItemPosition(Object object) {
                    if (mChildCount > 0) {
                        mChildCount--;
                        return POSITION_NONE;
                    }
                    return super.getItemPosition(object);
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    container.addView(viewList.get(position));
                    return viewList.get(position);
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView(viewList.get(position));
                }
            });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < points.size(); i++) {
                        if (position == i) {
                            points.get(i).setBackgroundResource(R.drawable.home_ugc_pgc_point_yellow);
                        } else {
                            points.get(i).setBackgroundResource(R.drawable.home_ugc_pgc_point_white);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        ClubItem item;

        @Override
        public void onBind(int position) {
            item = data.get(position);
            ll_point_container.removeAllViews();
            points.clear();
            viewList.clear();
            for (int i = 0; i < item.getItemsAD().size(); i++) {
                View view = context().getLayoutInflater().inflate(R.layout.item_home_ugc_pgcs, viewPager, false);
                ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                final ClubItemsAD model = item.getItemsAD().get(i);
                FFImageLoader.loadBigImage(context(), model.getImage(), iv_img);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FFUtils.checkNet()) {
                            showToast("网络连接失败，请检查网络设置");
                            return;
                        }
                        WebInfo data = new WebInfo();
                        data.setUrl(model.getTargetUrl());
                        startActivity(CommonWebviewUtilActivity.class, data);
                    }
                });

                viewList.add(view);

                View point = new View(context());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FFUtils.getPx(4), FFUtils.getPx(4));
                point.setLayoutParams(params);
                ll_point_container.addView(point);
                if (i != 0) {
                    params.setMargins(FFUtils.getPx(13), 0, 0, 0);
                }
                points.add(point);
            }
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }


    private class HolderClub2 extends MyBaseHolder {
        private TextView tv_item_title;
        private TextView tv_bwtd;
        private TextView tv_g;
        private TextView tv_y;
        private TextView tv_g_end;
        private TextView tv_y_end;
        private TextView tv_tdjj;

        public HolderClub2(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_2, recyclerView, false));
            tv_item_title = findViewById(R.id.tv_item_title);
            tv_bwtd = findViewById(R.id.tv_bwtd);
            tv_g = findViewById(R.id.tv_g);
            tv_y = findViewById(R.id.tv_y);
            tv_g_end = findViewById(R.id.tv_g_end);
            tv_y_end = findViewById(R.id.tv_y_end);
            tv_tdjj = findViewById(R.id.tv_tdjj);
            tv_bwtd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_bwtd.getPaint().setAntiAlias(true);//抗锯齿
            tv_tdjj.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_tdjj.getPaint().setAntiAlias(true);//抗锯齿
        }

        public void onBind(int position) {
            final ClubItem item = data.get(position);
//            tv_item_title.setText(item_image_gallery.getTemplateStr());
            String[] title_g = getTitle(item.getItemsAD().get(1).getTitle());
            String[] title_y = getTitle(item.getItemsAD().get(2).getTitle());
            tv_g.setText(title_g[0]);
            tv_y.setText(title_y[0]);
            tv_g_end.setText(title_g[1] + "名");
            tv_y_end.setText(title_y[1] + "元");
            tv_bwtd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebInfo data = new WebInfo();
                    data.setUrl(item.getItemsAD().get(0).getTargetUrl());
                    startActivity(CommonWebviewUtilActivity.class, data);
                }
            });
            tv_tdjj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnsureDialog.showClubDialog(context(), "什么是探店基金", "会员探店小黄圈报销的费用。");
                }
            });
        }
    }

    private class HolderClub3 extends MyBaseHolder {
        private com.fengnian.smallyellowo.foodie.widgets.CircleImageView iv_img;

        public HolderClub3(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_3, recyclerView, false));
            iv_img = findViewById(R.id.iv_img);
        }

        @Override
        public void onBind(int position) {
            ClubItem item = data.get(position);
            FFImageLoader.loadBigImage(context(), item.getItemsAD().get(0).getImage(), iv_img);
            iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(BwtdActivity.class, new IntentData());
                }
            });
        }
    }

    private class HolderClub4 extends MyBaseHolder {
        private TextView tv_congratulations;
        private RecyclerView rv_zpu;
        private RecyclerView.Adapter<HolderBwtdUser> adapter;
        private ClubItem item;

        private class HolderBwtdUser extends MyBaseHolder {
            private ImageView iv_header;
            private TextView tv_title;
            private TextView tv_poi_name;
            private TextView tv_zpjf;
            private TextView tv_bx;

            public HolderBwtdUser(BaseActivity context, RecyclerView recyclerView) {
                super(context.getLayoutInflater().inflate(R.layout.item_bwtd_user, recyclerView, false));
                iv_header = findViewById(R.id.iv_header);
                tv_title = findViewById(R.id.tv_title);
                tv_poi_name = findViewById(R.id.tv_poi_name);
                tv_zpjf = findViewById(R.id.tv_zpjf);
                tv_bx = findViewById(R.id.tv_bx);
            }

            public void onBind(int position) {
                final ClubItemsAD _item = item.getItemsAD().get(position);
                FFImageLoader.loadAvatar(context(), _item.getImage(), iv_header);
                iv_header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoIntent data = new UserInfoIntent();
                        data.setId(_item.getExtra3());
                        startActivity(ClubUserInfoActivity.class, data);
                    }
                });
                tv_title.setText(_item.getTitle());
                tv_poi_name.setText(_item.getValueDesc());
                tv_zpjf.setText(_item.getTrackParam().get(0).getKeyStr() + _item.getTrackParam().get(0).getValue());
                tv_bx.setText(_item.getTrackParam().get(1).getKeyStr() + _item.getTrackParam().get(1).getValue());
            }
        }

        public HolderClub4(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_4, recyclerView, false));
            tv_congratulations = findViewById(R.id.tv_congratulations);
            rv_zpu = findViewById(R.id.rv_zpu);
            rv_zpu.setLayoutManager(new LinearLayoutManager(context()));
            contentView.setFView(rv_zpu);
            adapter = new RecyclerView.Adapter<HolderBwtdUser>() {
                @Override
                public HolderBwtdUser onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new HolderBwtdUser(context(), rv_zpu);
                }

                @Override
                public void onBindViewHolder(HolderBwtdUser holder, int position) {
                    holder.onBind(position % item.getItemsAD().size());
                }

                @Override
                public int getItemCount() {
                    if (item == null) {
                        return 0;
                    }

                    if (item.getItemsAD().size() < 4) {
                        return item.getItemsAD().size();
                    }

                    int i = Integer.MAX_VALUE >>> 1;
                    return i;
                }
            };
            rv_zpu.setAdapter(adapter);
            rv_zpu.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        @Override
        public void onBind(int position) {
            item = data.get(position);
            tv_congratulations.setText(item.getSubtemplate());
            adapter.notifyDataSetChanged();
            if (item.getItemsAD().size() > 3) {
                contentView.setFView(rv_zpu);
                rv_zpu.getLayoutManager().scrollToPosition(Integer.MAX_VALUE >>> 2);
                if (!isScrolling) {
                    scroll();
                }
            } else {
                contentView.setFView(null);
                rv_zpu.getLayoutParams().height = adapter.getItemCount() * FFUtils.getPx(54) - 1;
            }
        }

        final float speed = FFUtils.getPx(50) * 1f / 1000;//每秒50dp

        long startTime = 0;

        boolean isScrolling = false;

        private void scroll() {
            isScrolling = true;
            startTime = System.currentTimeMillis();
            FFUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (rv_zpu.getChildCount() != 0 && item.getItemsAD().size() > 3) {
                        int y = (int) ((System.currentTimeMillis() - startTime) * speed);
                        rv_zpu.scrollBy(0, y);
                    }
                    if (getDestroyed()) {
                        return;
                    }
                    scroll();
                }
            });
        }
    }

    private class HolderClub5 extends MyBaseHolder {
        private TextView tv_item_title;
        private TextView tv_ydms;
        private TextView tv_sum;
        private TextView tv_numbers;

        public HolderClub5(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_5, recyclerView, false));
            tv_item_title = findViewById(R.id.tv_item_title);
            tv_ydms = findViewById(R.id.tv_ydms);
            tv_sum = findViewById(R.id.tv_sum);
            tv_numbers = findViewById(R.id.tv_numbers);
            tv_ydms.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_ydms.getPaint().setAntiAlias(true);//抗锯齿
        }

        public void onBind(int position) {
            final ClubItem item = data.get(position);
            tv_ydms.setText(item.getItemsAD().get(0).getTitle());
            tv_numbers.setText(item.getItemsAD().get(1).getTitle() + "\r\n" + item.getItemsAD().get(1).getValueDesc());
            tv_sum.setText(item.getItemsAD().get(2).getTitle());

            tv_ydms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebInfo data = new WebInfo();
                    data.setUrl(item.getItemsAD().get(0).getTargetUrl());
                    startActivity(CommonWebviewUtilActivity.class, data);
                }
            });
        }
    }

    private class HolderClub6 extends MyBaseHolder {
        ImageView iv_img;

        public HolderClub6(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_6, recyclerView, false));
            iv_img = findViewById(R.id.iv_img);
        }

        @Override
        public void onBind(int position) {
            final ClubItem item = data.get(position);
            FFImageLoader.loadBigImage(context(), item.getItemsAD().get(0).getImage(), iv_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebInfo data = new WebInfo();
                    data.setUrl(item.getItemsAD().get(0).getTargetUrl());
                    startActivity(CommonWebviewUtilActivity.class, data);
                }
            });
        }
    }

    private class HolderClub7 extends MyBaseHolder {
        private TextView tv_action_name;
        private TextView tv_congratulations;
        private RecyclerView rv_users;
        private ImageView iv_history;
        RecyclerView.Adapter<HolderEatWordUser> adapter;
        private ClubItem item;

        private class HolderEatWordUser extends MyBaseHolder {
            private ImageView iv_avatar;
            private TextView tv_name;
            private TextView tv_zpjf;

            public HolderEatWordUser(BaseActivity context, RecyclerView recyclerView) {
                super(context.getLayoutInflater().inflate(R.layout.item_eat_word_user, recyclerView, false));
                iv_avatar = findViewById(R.id.iv_avatar);
                tv_name = findViewById(R.id.tv_name);
                tv_zpjf = findViewById(R.id.tv_zpjf);
            }

            public void onBind(int position) {
                final ClubItemsAD _item = item.getItemsAD().get(position);
                FFImageLoader.loadAvatar(context(), _item.getImage(), iv_avatar);
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoIntent data = new UserInfoIntent();
                        data.setId(_item.getExtra3());
                        startActivity(ClubUserInfoActivity.class, data);
                    }
                });

                tv_name.setText(_item.getTitle());
                tv_zpjf.setText(_item.getTrackParam().get(0).getKeyStr() + _item.getTrackParam().get(0).getValue());
            }
        }


        public HolderClub7(final BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_7, recyclerView, false));
            tv_action_name = findViewById(R.id.tv_action_name);
            tv_congratulations = findViewById(R.id.tv_congratulations);
            rv_users = findViewById(R.id.rv_users);
            iv_history = findViewById(R.id.iv_history);
            LinearLayoutManager layout = new LinearLayoutManager(context);
            layout.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_users.setLayoutManager(layout);
            iv_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(EatWordActivity.class, new IntentData());
                }
            });
            adapter = new RecyclerView.Adapter<HolderEatWordUser>() {
                @Override
                public HolderEatWordUser onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new HolderEatWordUser(context(), rv_users);
                }

                @Override
                public void onBindViewHolder(HolderEatWordUser holder, int position) {
                    holder.onBind(position);
                }

                @Override
                public int getItemCount() {
                    return item == null ? 0 : item.getItemsAD().size();
                }
            };
            rv_users.setAdapter(adapter);
        }

        @Override
        public void onBind(int position) {
            item = data.get(position);
            tv_action_name.setText(item.getTemplateStr());
            tv_congratulations.setText(item.getSubtemplate());
        }
    }

    private class HolderClub8 extends MyBaseHolder {
        private TextView tv_item_title;
        private CircleImageView iv_img;

        public HolderClub8(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_8, recyclerView, false));
            tv_item_title = findViewById(R.id.tv_item_title);
            iv_img = findViewById(R.id.iv_img);
        }

        public void onBind(int position) {
            final ClubItem item = data.get(position);
            tv_item_title.setText(item.getTemplateStr());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebInfo data = new WebInfo();
                    data.setUrl(item.getItemsAD().get(0).getTargetUrl());
                    startActivity(CommonWebviewUtilActivity.class, data);
                }
            });
            FFImageLoader.loadBigImage(context(), item.getItemsAD().get(0).getImage(), iv_img);
        }
    }

    private class HolderClubSz extends MyBaseHolder {
        private TextView tv_zftd;
        private TextView tv_wsm;

        private TextView tv_rg;
        private TextView tv_gt;
        private TextView tv_sc;

        private TextView tv_rg_p;
        private TextView tv_gt_p;
        private TextView tv_sc_p;

        public HolderClubSz(BaseActivity context, RecyclerView recyclerView) {
            super(context.getLayoutInflater().inflate(R.layout.item_club_sz, recyclerView, false));
            tv_zftd = findViewById(R.id.tv_zftd);
            tv_wsm = findViewById(R.id.tv_wsm);

            tv_rg = findViewById(R.id.tv_rg);
            tv_gt = findViewById(R.id.tv_gt);
            tv_sc = findViewById(R.id.tv_sc);

            tv_rg_p = findViewById(R.id.tv_rg_p);
            tv_gt_p = findViewById(R.id.tv_gt_p);
            tv_sc_p = findViewById(R.id.tv_sc_p);
            tv_zftd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_zftd.getPaint().setAntiAlias(true);//抗锯齿
            tv_wsm.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_wsm.getPaint().setAntiAlias(true);//抗锯齿
        }


        @Override
        public void onBind(int position) {
            ClubItem item = data.get(position);
            tv_zftd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnsureDialog.showClubDialog(context(), "什么是自费探店?", "自己匿名去餐厅就餐，正常消费的就餐行为。");
                }
            });
            tv_wsm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EnsureDialog.showClubDialog(context(), "为什么小黄圈坚持自费探店?", "只有自费探店,写下的评论在具备中立性。");
                }
            });
            String[] title_gt = getTitle(item.getItemsAD().get(0).getTitle());
            String[] title_sc = getTitle(item.getItemsAD().get(1).getTitle());
            String[] title_rg = getTitle(item.getItemsAD().get(2).getTitle());

            tv_gt.setText(title_gt[0]);
            tv_sc.setText(title_sc[0]);
            tv_rg.setText(title_rg[0]);

            tv_gt_p.setText(title_gt[1] + "家");
            tv_sc_p.setText(title_sc[1] + "篇");
            tv_rg_p.setText(title_rg[1] + "次");
        }
    }

    public static class SYFoodSzResult extends BaseResult {

        public ClubItem getData() {
            return data;
        }

        public void setData(ClubItem data) {
            this.data = data;
        }

        private ClubItem data;
    }

    private String[] getTitle(String title) {
        try {
            int num = Integer.parseInt(title);
            if (num > 9999) {
                float numF = num * 1f / 10000;
                return new String[]{FFUtils.getSubFloat(numF, 2, false), "万  "};
            }
            return new String[]{num + "", "  "};
        } catch (Exception e) {
            return new String[]{"", "  "};
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        setTitle("俱乐部专栏");
        post(IUrlUtils.Search.getMemberPage, "", null, new FFNetWorkCallBack<SYFoodClubResult>() {
            @Override
            public void onSuccess(SYFoodClubResult response, FFExtraParams extra) {
                if (response.getData() != null) {
                    data.addAll(response.getData());
                    if (sz != null) {
                        data.add(1, sz);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
        post(IUrlUtils.Search.getShopRecordCount, "", null, new FFNetWorkCallBack<SYFoodSzResult>() {
            @Override
            public void onSuccess(SYFoodSzResult response, FFExtraParams extra) {
                if (data.isEmpty()) {
                    sz = response.getData();
                } else {
                    data.add(1, response.getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        });
        ButterKnife.bind(this);
        contentView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerView.Adapter<MyBaseHolder>() {
            @Override
            public MyBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                switch (viewType) {
                    case ClubADModelType_Banner:
                        return new HolderClub1(context(), contentView);
                    case ClubADModelType_MillionFindMerchant:
                        return new HolderClub2(context(), contentView);
                    case ClubADModelType_MillionFindMerchantImage:
                        return new HolderClub3(context(), contentView);
                    case ClubADModelType_MillionFindMerchantFindJiJin:
                        return new HolderClub4(context(), contentView);
                    case ClubADModelType_EatAllOver:
                        return new HolderClub5(context(), contentView);
                    case ClubADModelType_EatAllOverImage:
                        return new HolderClub6(context(), contentView);
                    case ClubADModelType_EatAllOver_YiDiMeiShi:
                        return new HolderClub7(context(), contentView);
                    case ClubADModelType_ExclusivePrivilege:
                        return new HolderClub8(context(), contentView);
                    case ClubADModelType_other:
                        return new HolderClubSz(context(), contentView);
                }
                return new MyBaseHolder(new TextView(context())) {
                    @Override
                    public void onBind(int position) {
                    }
                };
            }

            @Override
            public void onBindViewHolder(MyBaseHolder holder, int position) {
                holder.onBind(position);
            }

            @Override
            public int getItemViewType(int position) {
                return data.get(position).getType();
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };
        contentView.setAdapter(adapter);
    }
}
