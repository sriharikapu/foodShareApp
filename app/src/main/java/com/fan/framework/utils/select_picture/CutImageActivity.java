//package com.fan.framework.utils.select_picture;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ImageView;
//
//import com.fan.framework.select_picture.AllFolderImagesActivity;
//import com.fan.framework.utils.FFImageUtil;
//import com.fan.framework.utils.FFUtils;
//import com.fan.framework.utils.FileUitl;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//
//public class CutImageActivity extends BaseActivity implements OnTouchListener {
//    Matrix matrix = new Matrix();
//    Matrix savedMatrix = new Matrix();
//    ImageView imgView;
//    public Bitmap mBitmap;
//    float minScaleR;
//    static float MAX_SCALE = 4f;
//    static final int NONE = 0;
//    static final int DRAG = 1;
//    static final int ZOOM = 2;
//    public static final String INTENT_PATH = "path";
//    public static final String RESULT_PATH = "path";
//    View rec_view;
//
//    int mode = NONE;
//    PointF prev = new PointF();
//    PointF mid = new PointF();
//    float dist = 1f;
//
//    float maxMove = 0;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mBitmap != null)
//            mBitmap.recycle();
//        mBitmap = null;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        maxMove = FFUtils.getPx(5);
//        setContentView(R.layout.ff_activity_select_pic_fullscreen);
//        rec_view = findViewById(R.id.view2);
//        LayoutParams params = rec_view.getLayoutParams();
//        params.height = getRecHeight();
//        params.width = params.height;
//
//        imgView = (ImageView) findViewById(R.id.image);
//        String path = getIntent().getStringExtra(INTENT_PATH);
//        mBitmap = FFImageUtil.loadBitmap(path, FFImageUtil.bitmapFromPath(
//                path, -1, -1));
//        imgView.setImageBitmap(mBitmap);
//        init();
//        addMenu("完成", new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                CutImageActivity.this.onClick(v);
//            }
//        });
//    }
//
//    private int getRecHeight() {
//        return (int) (FFUtils.getDisWidth() - FFUtils.getPx(30));
//    }
//
//    public void onClick(View v) {
//        final View view1 = findViewById(R.id.content);
//        view1.setDrawingCacheEnabled(true);
//        view1.buildDrawingCache();
//        final Bitmap bitmap = view1.getDrawingCache();
//        final int dialogId = showProgressDialog("正在保存");
//        final float p[] = new float[9];
//        matrix.getValues(p);
//        new Thread() {
//            @Override
//            public void run() {
//                Bitmap bitmap1 = Bitmap.createBitmap(bitmap,
//                        getRect_Left(), getRect_Top() - view1.getTop(),
//                        getRecHeight(), getRecHeight());
//                Bitmap bitmap;
//                if (p[0] > 1) {
//                    bitmap = Bitmap.createScaledBitmap(bitmap1,
//                            (int) ((1 / p[0]) * getRecHeight()),
//                            (int) ((1 / p[0]) * getRecHeight()), false);
//                } else {
//                    bitmap = bitmap1;
//                }
////				int height = bitmap.getHeight();
////				int width = bitmap.getWidth();
////				for (int y = 0; y < height; y++) {
////					for (int x = 0; x < width; x++) {
////						long pix = bitmap.getPixel(x, y);
////						if(pix>>>24 != 255){
////							bitmap.setPixel(x, y, 0xffffff);
////						}
////					}
////				}
//
//                final String path = FileUitl.getCacheFileDir()
//                        + "/_tmp";
//                try {
//                    FileOutputStream fout = new FileOutputStream(path);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            showToast("文件保存失败", null);
//                            dismissProgressDialog(dialogId);
//                            view1.setDrawingCacheEnabled(false);
//                        }
//                    });
//                    return;
//                }
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        dismissProgressDialog(dialogId);
//                        Intent data = new Intent();
//                        data.putExtra(RESULT_PATH, path);
//                        setResult(RESULT_OK, data);
//                        finish();
//                        view1.setDrawingCacheEnabled(false);
//                    }
//                });
//            }
//        }.start();
//    }
//
//    private int getRect_Left() {
//        return rec_view.getLeft() + ((View) rec_view.getParent()).getLeft();
//    }
//
//    private int getRect_Right() {
//        return rec_view.getRight() + ((View) rec_view.getParent()).getLeft();
//    }
//
//    private int getRect_Top() {
//        return rec_view.getTop() + ((View) rec_view.getParent()).getTop();
//    }
//
//    private int getRect_Botton() {
//        return rec_view.getBottom() + ((View) rec_view.getParent()).getTop();
//    }
//
//    private void init() {
//        imgView.setImageBitmap(mBitmap);
//        imgView.setOnTouchListener(this);
//        minZoom();
//        center();
//        imgView.setImageMatrix(matrix);
//    }
//
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                savedMatrix.set(matrix);
//                prev.set(event.getX(), event.getY());
//                mode = DRAG;
//                // isBeyound = false;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                dist = spacing(event);
//                if (spacing(event) > 10f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                // if (mode == DRAG) {
//                // if (!isBeyound) {
//                // finish();
//                // }
//                // }
//            case MotionEvent.ACTION_POINTER_UP:
//                CheckView();
//                // isBeyound = false;
//                mode = NONE;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mode == DRAG) {
//                    // getMoveX(event);
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - prev.x, event.getY()
//                            - prev.y);
//                } else if (mode == ZOOM) {
//                    float newDist = spacing(event);
//                    if (newDist > 10f) {
//                        matrix.set(savedMatrix);
//                        float tScale = newDist / dist;
//                        matrix.postScale(tScale, tScale, mid.x, mid.y);
//                    }
//                }
//                break;
//        }
//        imgView.setImageMatrix(matrix);
//        return true;
//    }
//
//    // private void getMoveX(MotionEvent event) {
//    // // System.out.println(event.getX() + "-" + prev.x +"+" + event.getY() +
//    // // "-" + prev.y + ">" + maxMove);
//    // if (isBeyound) {
//    // System.out.println(true);
//    // return;
//    // }
//    // isBeyound = Math.abs(event.getX() - prev.x)
//    // + Math.abs(event.getY() - prev.y) > maxMove;
//    // }
//
//    private void CheckView() {
//        int view_bottom = getRect_Botton();
//        int view_top = getRect_Top();
//        int view_right = getRect_Right();
//        int ivew_left = getRect_Left();
//        if (mode == ZOOM) {
//            float p[] = new float[9];
//            matrix.getValues(p);
//            if (p[0] < minScaleR) {
//                float py = ((view_bottom - view_top) / 2)
//                        + view_top;
//                float px = ((view_right - ivew_left) / 2)
//                        + ivew_left;
//                matrix.postScale(minScaleR / p[0], minScaleR / p[0], px, py);
//                imgView.setImageMatrix(matrix);
//            } else if (p[0] > MAX_SCALE) {
//                float py = ((view_bottom - view_top) / 2)
//                        + view_top;
//                float px = ((view_right - ivew_left) / 2)
//                        + ivew_left;
//                matrix.postScale(MAX_SCALE / p[0], MAX_SCALE / p[0], px, py);
//                imgView.setImageMatrix(matrix);
//            }
//
//        }
//        Matrix m = new Matrix();
//        m.set(matrix);
//        RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//        m.mapRect(rect);
//        float deltaX = 0, deltaY = 0;
//        if (rect.top > view_top) {
//            deltaY = -rect.top + view_top;
//        } else if (rect.bottom < view_bottom) {
//            deltaY = view_bottom - rect.bottom;
//        }
//        if (rect.left > ivew_left) {
//            deltaX = -rect.left + ivew_left;
//        } else if (rect.right < view_right) {
//            deltaX = view_right - rect.right;
//        }
//        matrix.postTranslate(deltaX, deltaY);
//    }
//
//    private void minZoom() {
//        int vH = getRecHeight();
//        int width = mBitmap.getWidth();
//        int height = mBitmap.getHeight();
//        if (height == 0 || width == 0) {
//            return;
//        }
//        if (width > height) {
//            minScaleR = ((float) vH) / ((float) height);
//        } else {
//            minScaleR = ((float) vH) / ((float) width);
//        }
//        MAX_SCALE = Math.max(minScaleR, MAX_SCALE);
//        matrix.postScale(minScaleR, minScaleR);
//    }
//
//    private void center() {
//        center(true, true);
//    }
//
//    protected void center(boolean horizontal, boolean vertical) {
//        Matrix m = new Matrix();
//        m.set(matrix);
//        RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//        m.mapRect(rect);
//        float height = rect.height();
//        float width = rect.width();
//        float deltaX = 0, deltaY = 0;
//        if (vertical) {
//            int screenHeight = FFUtils.getContentHight(this)
//                    - getResources().getDimensionPixelSize(
//                    R.dimen.ff_actionbarHight);
//            if (height < screenHeight) {
//                deltaY = (screenHeight - height) / 2 - rect.top;
//            } else if (rect.top > 0) {
//                deltaY = -rect.top;
//            } else if (rect.bottom < screenHeight) {
//                deltaY = imgView.getHeight() - rect.bottom;
//            }
//        }
//        if (horizontal) {
//            int screenWidth = FFUtils.getDisWidth();
//            if (width < screenWidth) {
//                deltaX = (screenWidth - width) / 2 - rect.left;
//            } else if (rect.left > 0) {
//                deltaX = -rect.left;
//            } else if (rect.right < screenWidth) {
//                deltaX = screenWidth - rect.right;
//            }
//        }
//        matrix.postTranslate(deltaX, deltaY);
//    }
//
//    @SuppressLint("FloatMath")
//    private float spacing(MotionEvent event) {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    private void midPoint(PointF point, MotionEvent event) {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        // overridePendingTransition(android.R.anim.fade_in,
//        // android.R.anim.fade_out);
//    }
//
//    public static void skipToForResult(Activity activity, String path, boolean isAfterLogin,
//                                       int intentCode) {
//        Intent intent = new Intent(activity, CutImageActivity.class);
//        intent.putExtra(INTENT_PATH, path);
//        intent.putExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, isAfterLogin);
//        activity.startActivityForResult(intent, intentCode);
//    }
////	@Override
////	public boolean isAfterLoginPage() {
////		boolean alp = getIntent().getBooleanExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, false);
////		return alp;
////	}
//}