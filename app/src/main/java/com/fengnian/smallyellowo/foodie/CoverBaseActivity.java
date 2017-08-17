package com.fengnian.smallyellowo.foodie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYBaseImageAsset;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFrontCoverModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.CoverIntent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public abstract class CoverBaseActivity extends BaseActivity<CoverIntent> implements OnTouchListener {
    Matrix matrix;
    Matrix savedMatrix;
    //    public Bitmap mBitmap;
    float minScaleR = 1;
    static float MAX_SCALE = 4f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode;

    PointF prev;
    PointF mid;
    float dist;

    float maxMove;
    @Bind(R.id.iv_img)
    protected ImageView ivImg;
    @Bind(R.id.tv_title)
    protected TextView tvTitle;

    DraftModel draft = SYDataManager.shareDataManager().draftsOfFirst();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mBitmap != null)
//            mBitmap.recycle();
//        mBitmap = null;
    }

    String assetId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maxMove = FFUtils.getPx(5);
        setContentView(R.layout.activity_cover);
        int disHight = FFUtils.getDisHight();
        int statusbarHight = FFUtils.getStatusbarHight(this);
//        int bottomStatusHeight = FFUtils.getBottomStatusHeight();
        int px = FFUtils.getPx(48);
        findViewById(R.id.rl_container).getLayoutParams().height = disHight - statusbarHight - px;
        setTitle("封面设置");
        findViews();
        String title = draft.getFeed().getFood().getTitle();

        tvTitle.setText(title);
        SYFrontCoverModel coverModel = draft.getFeed().getFood().getFrontCoverModel();
        coverModel.getFrontCoverContent().setContent(title);
        String path = null;
        if (coverModel != null && coverModel.pullImage() != null) {
            path = coverModel.pullImage();
            assetId = coverModel.getFrontCoverAssetID();
        } else {
            List<SYRichTextPhotoModel> allImageContent = draft.getFeed().getFood().allImageContent();
            if (!FFUtils.isListEmpty(allImageContent)) {
                SYBaseImageAsset firstAsset = allImageContent.get(0).getPhoto().getImageAsset();
                path = firstAsset.pullProcessedImageUrl();
                assetId = firstAsset.getId();
            }
        }

        refreshImg(path);
        initView();
        addMenu("完成", new OnClickListener() {

            @Override
            public void onClick(View v) {
                CoverBaseActivity.this.onClick(v);
            }
        });
    }

    private void refreshImg(String path) {
        if (path == null) {
            return;
        }
        FFImageLoader.load_base(this, path, ivImg, true, -1, -1, R.drawable.alpha, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                if (bitmap == null) {
                    return;
                }
                FFImageLoader.delCache(bitmap);
                init();
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        EnsureDialog.showEnsureDialog(this, true, "确定放弃更改?", "确定",null, "取消", new EnsureDialog.EnsureDialogListener() {
            @Override
            public void onOk(DialogInterface dialog) {
                dialog.dismiss();
                CoverBaseActivity.super.onBackPressed();
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    protected abstract void findViews();

    abstract void initView();

    private int getRecHeight() {
        return viewHight;
    }

    private int getRecWidth() {
        return viewWidth;
    }

    public void onClick(View v) {
//        if (assetId == null || mBitmap == null) {
//            return;
//        }
        if (assetId == null) {
            return;
        }
        ivImg.setDrawingCacheEnabled(true);
        ivImg.buildDrawingCache();
        final Bitmap bitmap = ivImg.getDrawingCache();
        final int dialogId = showProgressDialog("");
        final float p[] = new float[9];
        matrix.getValues(p);
        new Thread() {
            @Override
            public void run() {
                Bitmap bitmap1;
                if (p[0] > 1) {
                    bitmap1 = Bitmap.createScaledBitmap(bitmap,
                            (int) ((1 / p[0]) * getRecWidth()),
                            (int) ((1 / p[0]) * getRecHeight()), false);
                } else {
                    bitmap1 = bitmap;
                }


                int height = ivImg.getHeight();
                int width = ivImg.getWidth();

                draft.getFeed().getFood().getFrontCoverModel().init(assetId, bitmap1, draft.getId());
                runOnUiThread(new Runnable() {
                    public void run() {
                        dismissProgressDialog(dialogId);
                        finish();
                    }
                });
            }
        }.start();
    }

    private int getRect_Left() {
        return ivImg.getLeft();
    }

    private int getRect_Right() {
        return ivImg.getRight();
    }

    private int getRect_Top() {
        View parent = (View) ivImg.getParent();
        View parent1 = (View) parent.getParent();
        View parent2 = (View) parent1.getParent();
        return ivImg.getTop();
    }

    private int getRect_Botton() {
        View parent = (View) ivImg.getParent();
        View parent1 = (View) parent.getParent();
        View parent2 = (View) parent1.getParent();
        return ivImg.getBottom();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        if (getDestroyed()) {
            return;
        }

        if (ivImg.getWidth() <= 0) {
            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            }, 50);
            return;
        }
        matrix = new Matrix();
        savedMatrix = new Matrix();
        mode = NONE;
        minScaleR = 1;
        prev = new PointF();
        mid = new PointF();
        dist = 1f;
        maxMove = 0;

        viewHight = ivImg.getHeight();
        viewWidth = ivImg.getWidth();

        LayoutParams lp = ivImg.getLayoutParams();

        ViewGroup vg = (ViewGroup) ivImg.getParent();

        vg.removeView(ivImg);
        Drawable drawable = ivImg.getDrawable();
        ivImg = new ImageView(this);
        ivImg.setLayoutParams(lp);
        ivImg.setScaleType(ImageView.ScaleType.MATRIX);
        vg.addView(ivImg, 0);

        ivImg.setImageDrawable(drawable);
        ivImg.setOnTouchListener(this);
        minZoom();
        center();
        ivImg.setImageMatrix(matrix);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                prev.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                if (spacing(event) > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                CheckView();
                // isBeyound = false;
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // getMoveX(event);
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - prev.x, event.getY()
                            - prev.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = newDist / dist;
                        matrix.postScale(tScale, tScale, mid.x, mid.y);
                    }
                }
                break;
        }
        ivImg.setImageMatrix(matrix);
        return true;
    }

    private void CheckView() {
        int view_bottom = getRect_Botton();
        int view_top = getRect_Top();
        int view_right = getRect_Right();
        int ivew_left = getRect_Left();
        if (mode == ZOOM) {
            float p[] = new float[9];
            matrix.getValues(p);
            if (p[0] < minScaleR) {
                float py = ((view_bottom - view_top) / 2)
                        + view_top;
                float px = ((view_right - ivew_left) / 2)
                        + ivew_left;
                matrix.postScale(minScaleR / p[0], minScaleR / p[0], px, py);
                ivImg.setImageMatrix(matrix);
            } else if (p[0] > MAX_SCALE) {
                float py = ((view_bottom - view_top) / 2)
                        + view_top;
                float px = ((view_right - ivew_left) / 2)
                        + ivew_left;
                matrix.postScale(MAX_SCALE / p[0], MAX_SCALE / p[0], px, py);
                ivImg.setImageMatrix(matrix);
            }

        }
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getWidth(), ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getHeight());
        m.mapRect(rect);
        float deltaX = 0, deltaY = 0;
        if (rect.top > view_top) {
            deltaY = -rect.top + view_top;
        } else if (rect.bottom < view_bottom) {
            deltaY = view_bottom - rect.bottom;
        }
        if (rect.left > ivew_left) {
            deltaX = -rect.left + ivew_left;
        } else if (rect.right < view_right) {
            deltaX = view_right - rect.right;
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    private void minZoom() {
        int vH = getRecHeight();
        int vW = getRecWidth();
        int width = ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getWidth();
        int height = ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getHeight();
        if (height == 0 || width == 0) {
            return;
        }
        if (vH * 1f / height > vW * 1f / width) {//图片更扁
            minScaleR = ((float) vH) / ((float) height);
        } else {
            minScaleR = ((float) vW) / ((float) width);
        }
        MAX_SCALE = Math.max(minScaleR, MAX_SCALE);
        matrix.postScale(minScaleR, minScaleR);
    }

    private void center() {
        center(true, true);
    }

    protected void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getWidth(), ((BitmapDrawable) ivImg.getDrawable()).getBitmap().getHeight());
        m.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            int screenHeight = viewHight;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = viewHight - rect.bottom;
            }
        }
        if (horizontal) {
            int screenWidth = viewWidth;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    int viewHight;
    int viewWidth;

    @SuppressLint("FloatMath")
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


    public class GalleryAdapter extends
            RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<SYRichTextPhotoModel> mDatas;

        public GalleryAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            if (draft != null) {
                mDatas = draft.getFeed().getFood().allImageContent();
            }
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
                imageView = (ImageView) arg0.findViewById(R.id.imageView);
                ll = (LinearLayout) arg0.findViewById(R.id.ll);
            }

            ImageView imageView;
            LinearLayout ll;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.item_cover_imgs, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            FFImageLoader.loadSmallImage(context(), mDatas.get(i).getPhoto().getImageAsset().pullProcessedImageUrl(), viewHolder.imageView);

            viewHolder.imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    assetId = mDatas.get(i).getPhoto().getImageAsset().getId();
                    String path = mDatas.get(i).getPhoto().getImageAsset().pullProcessedImageUrl();
                    refreshImg(path);
                    notifyDataSetChanged();
                }
            });

            if (assetId != null && assetId.equals(mDatas.get(i).getPhoto().getImageAsset().getId())) {
                viewHolder.ll.setBackgroundResource(R.drawable.cover_scale_img_bg);
            } else {
                viewHolder.ll.setBackgroundColor(0);
            }

        }

    }
}