package com.fengnian.smallyellowo.foodie;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.base.XData;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.imageloader.PercentImageView;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bigpicture.ImageGallery;
import com.fengnian.smallyellowo.foodie.bigpicture.ReviewImageGalleryAdapter;
import com.fengnian.smallyellowo.foodie.fragments.MyCameraFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.ReviewImagesIntent;

import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * 速记图片预览页
 */
public class ReviewImagesActivity extends BaseActivity<ReviewImagesIntent> {
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.iv_filter)
    ImageView ivFilter;
    @Bind(R.id.iv_none)
    ImageView ivNone;
    @Bind(R.id.iv_none_cover)
    View ivNoneCover;
    @Bind(R.id.iv_xian)
    ImageView ivXian;
    @Bind(R.id.iv_xian_cover)
    View ivXianCover;
    @Bind(R.id.iv_su)
    ImageView ivSu;
    @Bind(R.id.iv_su_cover)
    View ivSuCover;
    @Bind(R.id.iv_nuan)
    ImageView ivNuan;
    @Bind(R.id.iv_nuan_cover)
    View ivNuanCover;
    @Bind(R.id.iv_liang)
    ImageView ivLiang;
    @Bind(R.id.iv_liang_cover)
    View ivLiangCover;
    @Bind(R.id.ll_bottomBar)
    View ll_bottomBar;
    @Bind(R.id.iv_ruan)
    ImageView ivRuan;
    @Bind(R.id.iv_ruan_cover)
    View ivRuanCover;
    @Bind(R.id.iv_close_filters)
    ImageView ivCloseFilters;
    @Bind(R.id.ll_filters_container)
    LinearLayout llFiltersContainer;
    @Bind(R.id.iv_img)
    ImageGallery gallery;
    @Bind(R.id.tv_filter_name)
    TextView tvFilterName;

    boolean has = false;
    @Bind(R.id.tv_ok)
    public TextView tvOk;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.iv_del)
    ImageView ivDel;
    @Bind(R.id.activity_review_images)
    RelativeLayout activityReviewImages;

    public EditText et_dish1;
    private ImageView iv_clear_dish1;
    public RecyclerView rv_hot_dish;
    public RelativeLayout rl_dish_container1;

//    @Override
//    public void onBackPressed(View v) {
//        if (v.getId() == R.id.rl_dish_container1) {
//            onBackPressed();
//            return;
//        }
//        ok();
//    }


    private void onSwitchFilter(String name) {
        if (!has) {
            has = true;
            return;
        }
        tvFilterName.setText(name);
        tvFilterName.setVisibility(View.VISIBLE);
        tvFilterName.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFilterName.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_images);
        ButterKnife.bind(this);
        et_dish1 = (EditText) findViewById(R.id.et_dish1);

        et_dish1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    FFUtils.setSoftInputInvis(textView.getWindowToken());
                    return true;
                }
                return false;
            }
        });
        iv_clear_dish1 = (ImageView) findViewById(R.id.iv_clear_dish1);
        rv_hot_dish = (RecyclerView) findViewById(R.id.rv_hot_dish);
        rl_dish_container1 = (RelativeLayout) findViewById(R.id.rl_dish_container1);
        iv_clear_dish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_dish1.setText("");
            }
        });
        setNotitle(true);
        afterCreate();
    }


    private void initView() {
        if (getDestroyed()) {
            return;
        }
        if (gallery.getWidth() == 0) {
            FFUtils.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            });
            return;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(gallery.getWidth(), gallery.getHeight());
        layoutParams.addRule(RelativeLayout.BELOW, R.id.relativeLayout3);
        gallery.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ll_bottomBar.getWidth(), ll_bottomBar.getHeight());
        layoutParams1.addRule(RelativeLayout.BELOW, gallery.getId());
        ll_bottomBar.setLayoutParams(layoutParams1);

    }

    private SYRichTextPhotoModel currentModel;
    private int width;
    private ReviewImageGalleryAdapter adapter;
    //    int sum = 0;
    private final ArrayList<MyCameraFragment.TempPic> images = new ArrayList<>();
//    private List<SYRichTextPhotoModel> richTexts;

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public Holder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            ((ViewGroup.MarginLayoutParams) tv_name.getLayoutParams()).setMargins(0, FFUtils.getPx(12), 0, FFUtils.getPx(12));
        }
    }

    @SuppressLint("NewApi")
    public void afterCreate() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            findViewById(R.id.ll_actionBar).setAlpha(0.75f);
        }
        this.width = FFUtils.getDisWidth() - FFUtils.getPx(160);
        if (isFrom(FastEditActivity.class) && null != FastEditActivity.draft) {
            List<SYRichTextPhotoModel> richTexts = FastEditActivity.draft.getFeed().getFood().getRichTextLists();
            for (SYRichTextPhotoModel model : richTexts) {
                images.add(new MyCameraFragment.TempPic(model));
            }
//            sum = images.size();
        } else if (isFrom(TuFragmentActivity.class)) {
            images.addAll(MyCameraFragment.tokenPicturePath);
            List<SYRichTextPhotoModel> richTexts = FastEditActivity.draft.getFeed().getFood().getRichTextLists();
            for (int i = richTexts.size(); i > 0; i--) {
                SYRichTextPhotoModel model = richTexts.get(i - 1);
                images.add(new MyCameraFragment.TempPic(model));
            }
//            sum = images.size();
        } else {
            finish();
            return;
        }
        int intExtra = getIntentData().getIndex();
//		setTitle("餐厅详情");
        tvNum.setText((intExtra + 1) + "/" + images.size());
        gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
        gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
        adapter = new ReviewImageGalleryAdapter(this) {
            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public MyCameraFragment.TempPic getItem(int position) {
                return images.get(position);
            }
        };
        gallery.setAdapter(adapter);
        gallery.setSelection(intExtra);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currentModel = images.get(position).getModel();
                tvNum.setText((position + 1) + "/" + images.size());
                if (images.get(position).hasFilter()) {
                    ivFilter.setImageResource(R.mipmap.ic_camera_filters_selected);
                } else {
                    ivFilter.setImageResource(R.mipmap.ic_camera_filters);
                }

                has = false;

                setFilterCode(images.get(position).getFilterCode(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        initView();

        rv_hot_dish.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_hot_dish.setAdapter(new RecyclerView.Adapter<Holder>() {

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new Holder(getLayoutInflater().inflate(R.layout.item_select_dish, parent, false));
            }

            @Override
            public void onBindViewHolder(final Holder holder1, final int position1) {
                final String content = currentModel.getPhoto().getDishesNameList().get(position1).getContent();
                holder1.tv_name.setText(content);
                if (content.equals(et_dish1.getText().toString().trim())) {
                    holder1.tv_name.setTextColor(context().getResources().getColor(R.color.colorPrimary));
                    holder1.tv_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.restrestult_checked1, 0, R.mipmap.restrestult_checked, 0);
                } else {
                    holder1.tv_name.setTextColor(0xffffffff);
                    holder1.tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentModel.setDishesName(content);
                        et_dish1.setText(content);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return currentModel == null ? 0 : currentModel.getPhoto().getDishesNameList().size();
            }
        });
        et_dish1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentModel.setDishesName(s.toString().trim());
                rv_hot_dish.getAdapter().notifyDataSetChanged();
                if (s.length() == 0) {
                    iv_clear_dish1.setVisibility(View.GONE);
                } else {
                    iv_clear_dish1.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setFilterCode(String filterCode, boolean doFilter) {
        View iv;
        if (filterCode == null || filterCode.length() == 0) {
            iv = ivNoneCover;
            filterCode = "";
            onSwitchFilter("无");
        } else if (filterCode.equals("Lightup")) {//iv_xian
            iv = ivXianCover;
            onSwitchFilter("鲜");
        } else if (filterCode.equals("Olympus")) {//su
            iv = ivSuCover;
            onSwitchFilter("素");
        } else if (filterCode.equals("Nan")) {//nuan
            iv = ivNuanCover;
            onSwitchFilter("暖");
        } else if (filterCode.equals("Yajing")) {//liang
            iv = ivLiangCover;
            onSwitchFilter("凉");
        } else if (filterCode.equals("Summer")) {//ruan
            iv = ivRuanCover;
            onSwitchFilter("软");
        } else {
            iv = ivNoneCover;
            filterCode = "";
            onSwitchFilter("无");
        }

        final String filterName = filterCode;

        ivNoneCover.setVisibility(View.GONE);
        ivXianCover.setVisibility(View.GONE);
        ivSuCover.setVisibility(View.GONE);
        ivNuanCover.setVisibility(View.GONE);
        ivLiangCover.setVisibility(View.GONE);
        ivRuanCover.setVisibility(View.GONE);

        iv.setVisibility(View.VISIBLE);
        if (doFilter) {
            final PercentImageView imageView = adapter.allItems.get(gallery.getSelectedItemPosition());

            if (imageView != null) {

//                final int id = showProgressDialog("");
                FFImageLoader.load_base((FFContext) context, images.get(gallery.getSelectedItemPosition()).getPaths()[0], null, true, Constants.BigImage,
                        Constants.BigImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                            @Override
                            public void imageLoaded(final Bitmap bitmap, String imageUrl) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        final Bitmap bmp = FilterManager.shared().process(bitmap.copy(Bitmap.Config.ARGB_8888, true), filterName);
                                        final String pa = FileUitl.getCacheFileDir() + "/" + XData.uuid() + ".fast";
                                        FFImageUtil.saveBitmap(pa, bmp);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                                dismissProgressDialog(id);
                                                images.get(gallery.getSelectedItemPosition()).setFilterCode(filterName);
                                                images.get(gallery.getSelectedItemPosition()).getPaths()[1] = pa;
                                                if (images.get(gallery.getSelectedItemPosition()).getModel() != null) {
                                                    images.get(gallery.getSelectedItemPosition()).getModel().getPhoto().
                                                            getImageAsset().setSYImage(pa, false);
                                                    images.get(gallery.getSelectedItemPosition()).getModel().getPhoto().setImageFilterName(filterName);
                                                    images.get(gallery.getSelectedItemPosition()).getPaths()[1] = images.get(gallery.getSelectedItemPosition()).getModel().getPhoto().
                                                            getImageAsset().getProcessedImage().getImage().getUrl();
                                                }
                                                if (images.get(gallery.getSelectedItemPosition()).hasFilter()) {
                                                    ivFilter.setImageResource(R.mipmap.ic_camera_filters_selected);
                                                } else {
                                                    ivFilter.setImageResource(R.mipmap.ic_camera_filters);
                                                }
                                                FFImageLoader.map.trimToSize(0);
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }.start();
                            }

                            @Override
                            public void onDownLoadProgress(int downloaded, int contentLength) {

                            }
                        });
            }
        }
    }

    @OnClick({R.id.tv_ok, R.id.iv_del, R.id.iv_filter, R.id.iv_none, R.id.iv_none_cover, R.id.iv_xian, R.id.iv_xian_cover, R.id.iv_su, R.id.iv_su_cover, R.id.iv_nuan, R.id.iv_nuan_cover, R.id.iv_liang, R.id.iv_liang_cover, R.id.iv_ruan, R.id.iv_ruan_cover, R.id.iv_close_filters})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (rl_dish_container1.getVisibility() == View.VISIBLE) {
                    onBackPressed();
                    return;
                }
                ok();
                break;
            case R.id.iv_del:
                if (images.size() <= 1) {
                    showToast("请至少保留一张图片");
                    return;
                }
                MyCameraFragment.TempPic img = images.remove(gallery.getSelectedItemPosition());

                MyCameraFragment.tokenPicturePath.remove(img);

                if (img.getModel() != null) {
                    if (FastEditActivity.draft != null) {
                        FastEditActivity.draft.getFeed().getFood().getRichTextLists().remove(img.getModel());
                    }
                }
//                sum = images.size();
                tvNum.setText((gallery.getSelectedItemPosition() + 1) + "/" + images.size());
                adapter.notifyDataSetChanged();
                if (images.size() == 0) {
                    ok();
                }
                break;
            case R.id.iv_filter:
                llFiltersContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_none:
            case R.id.iv_none_cover:
                setFilterCode("", true);
                break;
            case R.id.iv_xian:
            case R.id.iv_xian_cover:
                setFilterCode("Lightup", true);
                break;
            case R.id.iv_su:
            case R.id.iv_su_cover:
                setFilterCode("Olympus", true);
                break;
            case R.id.iv_nuan:
            case R.id.iv_nuan_cover:
                setFilterCode("Nan", true);
                break;
            case R.id.iv_liang:
            case R.id.iv_liang_cover:
                setFilterCode("Yajing", true);
                break;
            case R.id.iv_ruan:
            case R.id.iv_ruan_cover:
                setFilterCode("Summer", true);
                break;
            case R.id.iv_close_filters:
                llFiltersContainer.setVisibility(View.GONE);
                break;
        }
    }

    private void ok() {
        if (isFrom(TuFragmentActivity.class)) {
            setResult(RESULT_OK);
        } else if (isFrom(FastEditActivity.class)) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (rl_dish_container1.getVisibility() == View.VISIBLE) {
            rl_dish_container1.setVisibility(View.GONE);
            tvOk.setText("完成");
            adapter.notifyDataSetChanged();
            gallery.getSelectedView().findViewById(R.id.rl_dish_container).setVisibility(View.VISIBLE);
            FFUtils.setSoftInputInvis(getContainer().getWindowToken());
        } else {
            ok();
        }
    }
}
