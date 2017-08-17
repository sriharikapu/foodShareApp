package com.fengnian.smallyellowo.foodie.feeddetail;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.base.XData;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.CommentActivity;
import com.fengnian.smallyellowo.foodie.FastEditActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestLocationActivity;
import com.fengnian.smallyellowo.foodie.RichTextEditActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.results.AddCommentResult;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.DynamicDetailResult;
import com.fengnian.smallyellowo.foodie.bigpicture.BitPictureIntent;
import com.fengnian.smallyellowo.foodie.bigpicture.ChatBigPictureActivity;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.emoji.EmojiPanelLayout;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapBitmapCreatedListener;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentStandardSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichCoverAdapter;
import com.fengnian.smallyellowo.foodie.intentdatas.CommentIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.RestLocationIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.CommentUtil;
import com.fengnian.smallyellowo.foodie.utils.ContextUtils;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;
import com.fengnian.smallyellowo.foodie.utils.ProductImgUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fan.framework.config.FFConfig.IMAGE_QUALITY;
import static com.fengnian.smallyellowo.foodie.R.id.iv2;
import static com.fengnian.smallyellowo.foodie.R.id.iv3;
import static com.fengnian.smallyellowo.foodie.R.id.rb_level;
import static com.fengnian.smallyellowo.foodie.R.id.tv_level;
import static com.fengnian.smallyellowo.foodie.R.id.v_line_level;

public class FastDetailActivity extends BaseActivity<FastDetailIntent> {
    protected SYFeed data;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.ll_edit)
    LinearLayout llEdit;
    @Bind(R.id.ll_transform)
    LinearLayout llTransform;
    @Bind(R.id.ll_publish)
    LinearLayout llPublish;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;

    View headView;
    View footView;

    private HeaderHolder headHolder;
    private FootHolder footHolder;
    private BaseAdapter adapter;
    private SYComment comment;

    private EmojiPanelLayout emoji_panel;
    private View fl_emoji_btn_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_fast_detail);
        ButterKnife.bind(this);
        setTitle("详情");

        if (getIntentData().isMineMode()) {
            llBottom.setVisibility(View.VISIBLE);
        } else {
            llBottom.setVisibility(View.GONE);
        }

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                if (data == null) {
                    return 0;
                }
                int count = 2;
                count += data.getFood().getRichTextLists().size() % 3 == 0 ? 0 : 1;
                count += data.getFood().getRichTextLists().size() / 3;
                count += FFUtils.isListEmpty(data.getSecondLevelcomments()) ? 0 : 1;
                return count;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public int getItemViewType(int position) {
                if (position == 0) {
                    return 0;
                }
                if (FFUtils.isListEmpty(data.getSecondLevelcomments())) {
                    if (position == getCount() - 1) {
                        return 2;
                    }
                } else {
                    if (position == getCount() - 2) {
                        return 2;
                    }
                    if (position == getCount() - 1) {
                        return 3;
                    }
                }
                return 1;
            }

            @Override
            public int getViewTypeCount() {
                return 4;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                switch (getItemViewType(i)) {
                    case 0: {
                        headHolder.refresh();
                        return headView;
                    }
                    case 2: {
                        footHolder.refresh();
                        return footView;
                    }
                    case 3: {
                        if (view == null) {
                            view = getLayoutInflater().inflate(R.layout.item_comment_detail, viewGroup, false);
                        }
                        final Runnable runn = new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        };

                        CommentUtil.dooComment(data, (LinearLayout) view, runn, context(), null);

                        return view;
                    }
                    case 1: {
                        return initView(i - 1, view, viewGroup);
                    }

                }
                return view;
            }

            int width = (FFUtils.getDisWidth() - (FFUtils.getPx(16 + (14 * 3)))) / 3;

            private View initView(int i, View view, ViewGroup viewGroup) {
                ImageView[] holder;
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.item_fast_imgs_detail, viewGroup, false);
                    holder = new ImageView[3];
                    view.setTag(holder);

                    holder[0] = (ImageView) view.findViewById(R.id.iv1);
                    holder[1] = (ImageView) view.findViewById(iv2);
                    holder[2] = (ImageView) view.findViewById(iv3);

                    for (int ii = 0; ii < 3; ii++) {
                        holder[ii].getLayoutParams().width = width;
                        holder[ii].getLayoutParams().height = width;
                    }
                } else {
                    holder = (ImageView[]) view.getTag();
                }

                int j = 0;

                int offset = i * 3;
                int index = j + offset;
                for (; j < 3; j++, index++) {
                    if (index < data.getFood().getRichTextLists().size()) {
                        FFImageLoader.loadMiddleImage(context(), data.getFood().getRichTextLists().get(index).getPhoto().getImageAsset().pullProcessedImageUrl(), holder[j]);
                        holder[j].setVisibility(View.VISIBLE);
                        final int inde = index;
                        holder[j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BitPictureIntent intent = new BitPictureIntent();
                                ArrayList<BitPictureIntent.ImageMap> list = new ArrayList<BitPictureIntent.ImageMap>();
                                for (int i = 0; i < data.getFood().getRichTextLists().size(); i++) {
                                    if (data.getFood().getRichTextLists().get(i).getPhoto() != null) {
                                        BitPictureIntent.ImageMap map = new BitPictureIntent.ImageMap();
                                        map.setPath(data.getFood().getRichTextLists().get(i).getPhoto().getImageAsset().pullProcessedImageUrl());
                                        map.setDishName(data.getFood().getRichTextLists().get(i).getDishesName());
                                        list.add(map);
                                    }
                                }
                                intent.setImages(list);
                                intent.setIndex(inde);
                                startActivity(ChatBigPictureActivity.class, intent);
                            }
                        });
                    } else {
                        holder[j].setVisibility(View.INVISIBLE);
                        holder[j].setOnClickListener(null);
                    }
                }
                return view;
            }

        };
        listView.setAdapter(adapter);

        addMenu(R.mipmap.ic_three_point_black, new View.OnClickListener() {

            private String getText() {
                String title = "";
                if (data.getFood().getFrontCoverModel() != null && data.getFood().getFrontCoverModel().getFrontCoverContent() != null) {
                    title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
                }
                return title;
            }

            private String getUrl1() {
                if (getIntentData().isMineMode()) {
                    return ShareUrlTools.getFastNoteUrl(data);
                } else {
                    return ShareUrlTools.getDynamicFastUrl(data);
                }
            }

            private String getTitle() {
                return data.getUser().getNickName() + "在小黄圈分享美食";
            }


            @Override
            public void onClick(View view) {

                if (data == null) {
                    return;
                }
                View contentView = LayoutInflater.from(context()).inflate(
                        R.layout.pop_share, null);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                // 设置按钮的点击事件
                TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
                TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
                TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);
                TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                //封面快照
                TextView tv_cover = (TextView) contentView.findViewById(R.id.tv_cover);
                //内容快照
                TextView product_tv_content = (TextView) contentView.findViewById(R.id.tv_content);


                contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                tv_pyq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getFood() instanceof NativeRichTextFood) {
                            showToast("未保存成功的数据不能分享哦!");
                            return;
                        }

                        platform = "朋友圈";
                        registerShareReceiver();

                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(FastDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                WeixinOpen.getInstance().share2weixin_pyq(getUrl1(), getText(), getTitle(), bitmap);
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
                        if (data.getFood() instanceof NativeRichTextFood) {
                            showToast("未保存成功的数据不能分享哦!");
                            return;
                        }

                        platform = "微信好友";
                        registerShareReceiver();

                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(FastDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                WeixinOpen.getInstance().share2weixin(getUrl1(), getText(), getTitle(), bitmap);
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
                        if (data.getFood() instanceof NativeRichTextFood) {
                            showToast("未保存成功的数据不能分享哦!");
                            return;
                        }

                        platform = "新浪微博";
                        registerShareReceiver();

                        final int id = showProgressDialog("");
                        FFImageLoader.load_base(FastDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                dismissProgressDialog(id);
                                popupWindow.dismiss();
                                if (bitmap == null) {
                                    return;
                                }
                                SinaOpen.share(context(), bitmap, getTitle(), getText(), getUrl1());
                            }

                            @Override
                            public void onDownLoadProgress(int downloaded, int contentLength) {
                            }
                        });
                    }
                });
                if (data.getUser().getId().equals(SP.getUid())) {
                    tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_del, 0, 0);
                    tv_del.setText("删除");
                    tv_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            if (PublishModelManager.isNative(data)) {
                                if (!PublishModelManager.isPublishFail(data)) {
                                    showToast("发布中动态不可删除");
                                    return;
                                }
                                SYDataManager.shareDataManager().removeTask(((NativeRichTextFood) data.getFood()).getTask());
                                finish();
                                return;
                            }
                            EnsureDialog.showEnsureDialog(context(), true, "确定删除这条记录?", "删除", null, "取消", new EnsureDialog.EnsureDialogListener() {
                                @Override
                                public void onOk(DialogInterface dialog) {
                                    dialog.dismiss();
                                    del();
                                }

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }

                        private void del() {
                            if (!getIntentData().isMineMode()) {
//                                post(Constants.shareConstants().getNetHeaderAdress() + "/food/removeFoodRecordByType.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                                post(IUrlUtils.Search.removeFoodRecordByType, "", null, new FFNetWorkCallBack<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                                        BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public boolean onFail(FFExtraParams extra) {
                                        return false;
                                    }
                                }, "recordId", data.getId(), "type", "2");
                            } else {
//                                post(Constants.shareConstants().getNetHeaderAdress() + "/notes/removeFoodNote.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                                post(IUrlUtils.Search.removeFoodNote, "", null, new FFNetWorkCallBack<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                                        BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public boolean onFail(FFExtraParams extra) {
                                        return false;
                                    }
                                }, "cusId", data.getFood().getId(), "recordId", data.getFoodNoteId());
                            }
                        }
                    });
                } else {
                    tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_jubao, 0, 0);
                    tv_del.setText("举报");
                    tv_del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            setDialog();
                        }
                    });
                }
                //封面快照
                tv_cover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getFood() instanceof NativeRichTextFood) {
                            showToast("未保存成功的数据不能分享哦!");
                            return;
                        }
                        platform = "封面快照";
                        registerShareReceiver();

                        final int id = showProgressDialog("");
                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.startSnap(new RichCoverAdapter(data, FastDetailActivity.this, getUrl1()), new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String bitmap) {
                                        dismissProgressDialog(id);
                                        if (bitmap != null) {
                                            WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                        } else {
                                            showToast("快照生成失败");
                                        }
                                    }
                                });
                            }
                        }.start();


                    }
                });
                product_tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int id = showProgressDialog("");

                        platform = "内容快照";
                        registerShareReceiver();

                        new Thread() {
                            @Override
                            public void run() {
                                ProductImgUtils.startSnap(new RichContentStandardSnapAdapter(FastDetailActivity.this, data, getUrl1()), new OnSnapBitmapCreatedListener() {
                                    @Override
                                    public void OnSnapBitmapCreatedListener(final String bitmap) {
                                        dismissProgressDialog(id);
                                        if (bitmap != null) {
                                            WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                        } else {
                                            showToast("快照生成失败");
                                        }
                                    }
                                });
                            }
                        }.start();

                    }
                });

                popupWindow.setTouchable(true);

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        Log.i("mengdd", "onTouch : ");

                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });

                // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                // 我觉得这里是API的一个bug
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
                popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
            }
        });
    }

    public class FootHolder {

        @Bind(R.id.tv_rest_name)
        TextView tvRestName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_time2)
        TextView tvTime2;
        @Bind(R.id.tv_prise)
        TextView tv_prise;
        @Bind(R.id.tv_eaten)
        TextView tv_eaten;
        @Bind(R.id.tv_comment)
        TextView tv_comment;
        @Bind(R.id.line)
        View line;
        @Bind(R.id.tv_dynamic_commentContent)
        LinearLayout tvDynamicCommentContent;
        @Bind(R.id.ll_dynamic_comment_container)
        LinearLayout llDynamicCommentContainer;

        public FootHolder(View footView) {
            ButterKnife.bind(this, footView);

            tvRestName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getFood().getPoi().getIsCustom() == 1) {
                        showToast("该商户为自定义创建，暂无地址信息");
                        return;
                    }
                    startActivity(RestLocationActivity.class, new RestLocationIntent().
                            setLat(data.getFood().getPoi().getLatitude()).
                            setLng(data.getFood().getPoi().getLongitude()).
                            setName(data.getFood().getPoi().getTitle()).
                            setAddress(data.getFood().getPoi().getAddress()));
                }
            });

            if (getIntentData().isMineMode()) {
                tvTime.setVisibility(View.VISIBLE);
                tvTime2.setVisibility(View.GONE);
            } else {
                tvTime.setVisibility(View.GONE);
                tvTime2.setVisibility(View.VISIBLE);
                tvTime = tvTime2;
            }

            if (getIntentData().isMineMode()) {
                ((View) tv_comment.getParent()).setVisibility(View.GONE);
                llDynamicCommentContainer.setVisibility(View.GONE);
                return;
            }


            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {
                if (data.getEatState() == 1 || data.getUser().getId().equals(SP.getUid())) {
                    tv_eaten.setText("吃过");
                    tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_gray));
                    tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_eaten_normal, 0, 0, 0);
                    tv_eaten.setOnClickListener(null);
                } else if (data.getEatState() == 0) {
                    tv_eaten.setText("想吃");
                    tv_eaten.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat_selected, 0, 0, 0);
                } else if (data.getEatState() == 2) {
                    tv_eaten.setText("想吃");
                    tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_black));
                    tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat, 0, 0, 0);
                }

                tv_eaten.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (data.getEatState() == 0) {
                            data.setEatState(2);
                        } else if (data.getEatState() == 1) {
                            return;
                        } else if (data.getEatState() == 2) {
                            data.setEatState(0);
                        }
                        if (data.getEatState() == 0) {
                            showToast("已将该商户加入您的想吃清单中");
                            tv_eaten.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat_selected, 0, 0, 0);
//                            post(Constants.shareConstants().getNetHeaderAdress() + "/eat/addFoodEat.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                            post(IUrlUtils.Search.addFoodEat, null, null, new FFNetWorkCallBack<BaseResult>() {
                                        @Override
                                        public void onSuccess(BaseResult response, FFExtraParams extra) {

                                        }

                                        @Override
                                        public boolean onFail(FFExtraParams extra) {
                                            return true;
                                        }
                                    }, "recordId", data.getId(),
                                    "id", data.getFood().getPoi().getId(),
                                    "isResource", "2",
                                    "shopImage", data.getFood().pullCoverImage(),
                                    "shopType", data.getFood().getPoi().getType());
                        } else {
                            tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_black));
                            tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat, 0, 0, 0);
                            showToast("已将该商户从想吃清单移除");
//                            post(Constants.shareConstants().getNetHeaderAdress() + "/eat/delFoodEatV200.do.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                            post(IUrlUtils.Search.delFoodEatV200, null, null, new FFNetWorkCallBack<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response, FFExtraParams extra) {

                                }

                                @Override
                                public boolean onFail(FFExtraParams extra) {
                                    return true;
                                }
                            }, "recordId", data.getId(), "merchantId", data.getFood().getPoi().getId(), "eatStatus", 0);
                        }
                    }
                });
            } else {
                tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_eaten_normal, 0, 0, 0);
                tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_gray));
                tv_eaten.setText("想吃");
                tv_eaten.setOnClickListener(null);
            }
            if (data.getThumbsUpCount() == 0) {
                tv_prise.setText("赞");
            } else {
                tv_prise.setText(data.getThumbsUpCount() + "");
            }
            tv_prise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.setbThumbsUp(!data.isbThumbsUp());
                    //是否赞过
                    if (data.isbThumbsUp()) {
                        tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise, 0, 0, 0);
                        data.addThumbsUp(SP.getUser());
                        data.setThumbsUpCount(data.getThumbsUpCount() + 1);
                    } else {
                        tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise_no, 0, 0, 0);
                        data.removeThumbsUp(SP.getUser());
                        data.setThumbsUpCount(data.getThumbsUpCount() - 1);
                    }
                    if (data.getThumbsUpCount() == 0) {
                        tv_prise.setText("赞");
                    } else {
                        tv_prise.setText(data.getThumbsUpCount() + "");
                    }
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/praise/praiseOrUnpraise.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                    post(IUrlUtils.Search.praiseOrUnpraise, null, null, new FFNetWorkCallBack<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response, FFExtraParams extra) {

                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, "foodRecordId", data.getId(), "flag", data.isbThumbsUp() ? 1 : 2);
                }
            });

            //是否赞过
            if (data.isbThumbsUp()) {
                tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise, 0, 0, 0);
            } else {
                tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise_no, 0, 0, 0);
            }

            tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        startActivity(LoginOneActivity.class, loginIntentData);
                        return;
                    }
                    if (data == null) {
                        return;
                    }
                    startActivity(CommentActivity.class, (CommentIntent) new CommentIntent().setFeed(data).setRequestCode(517));
                }
            });

            //评论内容
            initComments(data);
        }

        private void initComments(final SYFeed data) {
            if (data.getSecondLevelcomments() == null || data.getSecondLevelcomments().size() == 0) {
                llDynamicCommentContainer.setVisibility(View.GONE);
                return;
            } else {
                llDynamicCommentContainer.setVisibility(View.VISIBLE);
            }

            LinearLayout tv = tvDynamicCommentContent;
            final Runnable runn = new Runnable() {
                @Override
                public void run() {
                    initComments(data);
                }
            };
            CommentUtil.dooComment(data, tv, runn, context(), null);
        }

        public void refresh() {
            tvTime.setText(ContextUtils.getFriendlyTime(data.getTimeStamp(), false));
        }
    }


    public class HeaderHolder {
        @Bind(R.id.iv_add_crown)
        ImageView iv_add_crown;
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(tv_level)
        TextView tvLevel;
        @Bind(rb_level)
        RatingBar rbLevel;
        @Bind(v_line_level)
        View vLineLevel;
        @Bind(R.id.tv_content)
        TextView tvContent;

        public HeaderHolder(View view) {
            ButterKnife.bind(this, view);
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!data.getUser().getId().equals(SP.getUid())) {
                        UserInfoIntent intent = new UserInfoIntent();
                        intent.setUser(data.getUser());
                        intent.setRequestCode(DynamicDetailActivity.INTENT_USER);
//                        startActivity(UserInfoActivity.class, intent);
                        IsAddCrownUtils.ActivtyStartAct(data.getUser(), intent, FastDetailActivity.this);
                    } else {//
                        finishAllActivitysByTag(ActivityTags.main);
                        MainActivity.toUser();
                    }
                }
            });
        }

        public void refresh() {
            if (data != null) {
                if (data.getUser() != null && data.getUser().getHeadImage() != null) {
                    IsAddCrownUtils.checkIsAddCrow(data.getUser(), iv_add_crown);
                    FFImageLoader.loadAvatar(context(), data.getUser().getHeadImage().getUrl(), ivAvatar);
                } else {
                    return;
                }
            } else {
                return;
            }

            tvName.setText(data.getUser().getNickName());


            if (data.getStarLevel() == 0 || getIntentData().isMineMode()) {//个人中心页面没有评分
                tvLevel.setVisibility(View.INVISIBLE);
                rbLevel.setVisibility(View.INVISIBLE);
                vLineLevel.setVisibility(View.INVISIBLE);
            } else {
                rbLevel.setRating(data.getStarLevel());
                tvLevel.setText(data.pullStartLevelString());
                tvLevel.setVisibility(View.VISIBLE);
                rbLevel.setVisibility(View.VISIBLE);
                vLineLevel.setVisibility(View.VISIBLE);
            }

            tvContent.setText(data.getFood().getContent());
        }
    }


    private void initHeadView() {
        headView = getLayoutInflater().inflate(R.layout.header_fast_detail, listView, false);
        headHolder = new HeaderHolder(headView);
    }

    private void initFootView() {
        footView = getLayoutInflater().inflate(R.layout.footer_fast_detail, listView, false);
        footHolder = new FootHolder(footView);
    }

    private void setDialog() {
        new AlertDialog.Builder(FastDetailActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("确定要举报该内容为不良信息吗!")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/report.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                        post(IUrlUtils.Search.report, null, null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {
//                                    setResult(RESULT_OK);
//                                    finish();
                                showToast("举报成功！");
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "recordId", data.getId(), "defendant", data.getUser().getId());
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        }).show();//在按键响应事件中显示此对话框
    }

    @OnClick({R.id.ll_edit, R.id.ll_transform, R.id.ll_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_edit:
                edit();
                break;
            case R.id.ll_transform:
                if (data == null) {
                    return;
                }
                if (DraftModelManager.hasDraft()) {
                    new EnsureDialog.Builder(context()).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                        }
                    }).setNeutralButton("创建新编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editToRich();
                        }
                    }).create().show();
                } else {
                    editToRich();
                }
                break;
            case R.id.ll_publish:

                if (data == null) {
                    return;
                }
                if (data.getFood().getPoi() == null || FFUtils.isStringEmpty(data.getFood().getPoi().getTitle())) {
                    showToast("请编辑选择餐厅后再发布。");
                    return;
                }
                if (data.getFood() instanceof NativeRichTextFood) {
//                        if (PublishModelManager.isPublishFail(data)) {
//                            showToast("已发布");
//                            PublishModelManager.publish(data);
//                        }
                    showToast("本地数据不支持分享到动态，请先保存数据。");
                } else {
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/notes/publishToDynamic.do", "", null, new FFNetWorkCallBack<DynamicDetailActivity.PublishResult>() {
                    post(IUrlUtils.Search.publishToDynamic, "", null, new FFNetWorkCallBack<DynamicDetailActivity.PublishResult>() {
                        @Override
                        public void onSuccess(DynamicDetailActivity.PublishResult response, FFExtraParams extra) {
                            if (response.judge()) {
                                BrodcastActions.netDataPublishSuccess(response.getFeed());
                                finishAllActivitysByTag(ActivityTags.main);
                                MainActivity.toDynamic();
                            } else {
                                showToast("发布到动态失败.");
                            }
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            showToast("发布到动态失败.");
                            return false;
                        }
                    }, "cusId", XData.uuid(), "recordId", data.getFoodNoteId());
                }
                break;
        }
    }

    private boolean isNativeFail() {
        return data != null && data.getFood() instanceof NativeRichTextFood && ((NativeRichTextFood) data.getFood()).getTask() != null && ((NativeRichTextFood) data.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateFail;
    }

    /**
     * 编辑成为富文本
     */
    private void editToRich() {
        final int id = showProgressDialog("", false);
        new Thread() {
            @Override
            public void run() {
                if (getDestroyed()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (id > 0)
                                dismissProgressDialog(id);
                        }
                    });
                    return;
                }
                if (PublishModelManager.isNative(data)) {//本地
                    if (!data.getFood().wasMeishiBianji()) {//速记
                        if (isNativeFail()) {//本地失败的速记
                            //copy一份
                            PublishModel oldTask = ((NativeRichTextFood) data.getFood()).getTask();
                            String oldCustomId = oldTask.getFirstCustomId();
                            final DraftModel feed = DraftModelManager.edit(data);
                            feed.setFirstComtomId(oldCustomId);
                            //copy资源
                            SYDataManager.shareDataManager().removeAllDrafts();
                            SYDataManager.shareDataManager().addDraft(feed);
                            SYDataManager.shareDataManager().removeTask(oldTask);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                    startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                                    finish();
                                }
                            });
                        } else {//本地正在上传的
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                }
                            });
                        }
                    } else {//本地富文本
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                } else {//网络
                    if (!data.getFood().wasMeishiBianji()) {//速记
                        final DraftModel feed = DraftModelManager.edit(data);
                        SYDataManager.shareDataManager().removeAllDrafts();
                        SYDataManager.shareDataManager().addDraft(feed);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                                startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                            }
                        });
                    } else {//网络富文本
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                }
            }
        }.start();
    }

    /**
     * 编辑成为速记
     */
    private void edit() {
        final int id = showProgressDialog("", false);
        new Thread() {
            @Override
            public void run() {
                if (getDestroyed()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (id > 0)
                                dismissProgressDialog(id);
                        }
                    });
                    return;
                }
                if (PublishModelManager.isNative(data)) {//本地
                    if (!data.getFood().wasMeishiBianji()) {//速记
                        if (isNativeFail()) {//本地失败的速记
                            //copy一份
                            PublishModel oldTask = ((NativeRichTextFood) data.getFood()).getTask();
                            String oldCustomId = oldTask.getFirstCustomId();
                            final DraftModel feed = DraftModelManager.edit(data);
                            feed.setFirstComtomId(oldCustomId);
                            //copy资源
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                    FastEditIntent data = new FastEditIntent();
                                    data.setFeed(feed);
                                    startActivity(FastEditActivity.class, data);
                                    finish();
                                }
                            });
                        } else {//本地正在上传的
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                }
                            });
                        }
                    } else {//本地富文本
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                } else {//网络
                    if (!data.getFood().wasMeishiBianji()) {//速记
                        final DraftModel feed = DraftModelManager.edit(data, true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                                FastEditIntent data = new FastEditIntent();
                                data.setFeed(feed);
                                startActivity(FastEditActivity.class, data);
                            }
                        });
                    } else {//网络富文本
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                }
            }
        }.start();
    }

    public void comment(SYComment comment) {
        if (FFUtils.isStringEmpty(SP.getUid())) {
            LoginIntentData loginIntentData = new LoginIntentData();
            loginIntentData.setCode("");
            startActivity(LoginOneActivity.class, loginIntentData);
            return;
        }
        this.comment = comment;
        CommentIntent intent = (CommentIntent) new CommentIntent();
        intent.setFeed(data);
        intent.setComment(comment);
        startActivity(CommentActivity.class, (CommentIntent) intent.setRequestCode(517));
    }

    private void initData() {
        if (getIntentData().getId() != null) {//只有ID的网络数据
            data = getIntentData().getFeed();
            if (data != null && headView == null) {
                initHeadView();
                initFootView();
            }
//            post(Constants.shareConstants().getNetHeaderAdress() + (getIntentData().isMineMode() ? "/notes/getFoodNoteInfoV250.do" : "/dynamic/queryDynamicDetailV250.do"), "", null, new FFNetWorkCallBack<DynamicDetailResult>() {
            post(getIntentData().isMineMode() ? IUrlUtils.Search.getFoodNoteInfoV250 : IUrlUtils.Search.queryDynamicDetailV250, "", null, new FFNetWorkCallBack<DynamicDetailResult>() {
                @Override
                public void onSuccess(DynamicDetailResult response, FFExtraParams extra) {
                    data = response.getFeed();
                    if (data != null && headView == null) {
                        initHeadView();
                        initFootView();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, getIntentData().isMineMode() ? new Object[]{"foodNoteId", getIntentData().getId()} : new Object[]{"recordId", getIntentData().getId()});

        } else if (!(getIntentData().getFeed().getFood() instanceof NativeRichTextFood)) {//网络数据
            data = getIntentData().getFeed();
            if (data != null && headView == null) {
                initHeadView();
                initFootView();
            }
//            post(Constants.shareConstants().getNetHeaderAdress() + (getIntentData().isMineMode() ? "/notes/getFoodNoteInfoV250.do" : "/dynamic/queryDynamicDetailV250.do"), null, null, new FFNetWorkCallBack<DynamicDetailResult>() {
            post(getIntentData().isMineMode() ? IUrlUtils.Search.getFoodNoteInfoV250 : IUrlUtils.Search.queryDynamicDetailV250, null, null, new FFNetWorkCallBack<DynamicDetailResult>() {
                @Override
                public void onSuccess(DynamicDetailResult response, FFExtraParams extra) {
                    data = response.getFeed();
                    if (data != null && headView == null) {
                        initHeadView();
                        initFootView();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, getIntentData().isMineMode() ? new Object[]{"foodNoteId", getIntentData().getFeed().getFoodNoteId()} : new Object[]{"recordId", getIntentData().getFeed().getId()});
        } else {//本地数据
            data = getIntentData().getFeed();
            if (data != null && headView == null) {
                initHeadView();
                initFootView();
            }
            for (PublishModel task : SYDataManager.shareDataManager().allTasks()) {
                if (task.getFeed().getFood().getId().equals(data.getFood().getId())) {
                    data = task.getFeed();
                    if (data != null && headView == null) {
                        initHeadView();
                        initFootView();
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 517 && resultCode == RESULT_OK) {
            if (this.data.getUser() != null)
//                post(Constants.shareConstants().getNetHeaderAdress() + "/comment/addCommentV250.do", null, null, new FFNetWorkCallBack<AddCommentResult>() {
                post(IUrlUtils.Search.addCommentV250, null, null, new FFNetWorkCallBack<AddCommentResult>() {
                    @Override
                    public void onSuccess(AddCommentResult response, FFExtraParams extra) {
                        FastDetailActivity.this.data.setSecondLevelcomments(response.getSySecondLevelComments());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ugcId", this.data.getId(), "content", data.getStringExtra("comment"), "toUserId", comment == null ? this.data.getUser().getId() : comment.getCommentUser().getId(), "toCommentId", comment == null ? 0 : comment.getId());
            comment = null;
        } else if (requestCode == DynamicDetailActivity.INTENT_USER && resultCode == RESULT_OK) {
            this.data.getUser().setByFollowMe(data.getBooleanExtra("is", false));
            adapter.notifyDataSetChanged();
        }
    }

    private String platform;
    private ShareReceiver shareReceiver;

    public void registerShareReceiver() {
        if (shareReceiver != null){
            return;
        }
        shareReceiver = new ShareReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(IUrlUtils.Constans.SHARE_MESSAGE_WECHAT);
        registerReceiver(shareReceiver, filter);
    }

    public class ShareReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (IUrlUtils.Constans.SHARE_MESSAGE_WECHAT.equals(intent.getAction())) { // 微信登录回调

                HashMap<String, String> event = new HashMap<String, String>();
                event.put("account", SP.getUid());
                event.put("platform", platform);
                if (data != null) {
                    event.put("ugcid", data.getId());
                }
                if (data != null && data.getUser() != null) {
                    event.put("author_name", data.getUser().getNickName());
                    event.put("author_id", data.getUser().getId());
                }

                if (data != null && data.getFood() != null && data.getFood().getPoi() != null) {
                    event.put("shop_name", data.getFood().getPoi().getTitle());
                    event.put("shop_id", data.getFood().getPoi().getId());
                }
                pushEventAction("Yellow_071", event);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shareReceiver != null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(shareReceiver);
        }
    }
}
