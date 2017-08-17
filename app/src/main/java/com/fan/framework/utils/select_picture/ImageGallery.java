//package com.fan.framework.utils.select_picture;
//
//import android.content.Context;
//import android.graphics.Matrix;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.GestureDetector.SimpleOnGestureListener;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Gallery;
//
//import com.fan.framework.select_picture.FullscreenActivity;
//import com.fan.framework.utils.FFUtils;
//import com.fan.framework.widgets.ZoomImageView;
//
//@SuppressWarnings("deprecation")
//public class ImageGallery extends Gallery {
//
//    private static final String TAG = "ImageGallery";
//
//    private GestureDetector gestureScanner;
//    private ZoomImageView imageView;
//
//    public ImageGallery(Context context) {
//        super(context);
//    }
//
//    public ImageGallery(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public ImageGallery(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        gestureScanner = new GestureDetector(context, new MySimpleGesture());
//        this.setOnTouchListener(new OnTouchListener() {
//
//            float baseValue;
//            float originalScale;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                View view = ImageGallery.this.getSelectedView();
//                if (view instanceof ZoomImageView) {
//                    imageView = (ZoomImageView) view;
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        baseValue = 0;
//                        originalScale = imageView.getScale();
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        Log.i("manager",
//                                "event.getPointerCount()=="
//                                        + event.getPointerCount());
//                        if (event.getPointerCount() == 2) {
//                            float x = event.getX(0) - event.getX(1);
//                            float y = event.getY(0) - event.getY(1);
//                            float value = (float) Math.sqrt(x * x + y * y);
//                            // System.out.println("value:" + value);
//                            if (baseValue == 0) {
//                                baseValue = value;
//                            } else {
//                                float scale = value / baseValue;
//                                // scale the image
//                                imageView.zoomTo(originalScale * scale, x
//                                        + event.getX(1), y + event.getY(1));
//
//                            }
//                        }
//                    }
//                }
//                return false;
//            }
//
//        });
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//                            float distanceY) {
//        View view = ImageGallery.this.getSelectedView();
//        if (view instanceof ZoomImageView) {
//            imageView = (ZoomImageView) view;
//
//            float v[] = new float[9];
//            Matrix m = imageView.getImageMatrix();
//            m.getValues(v);
//            float left, right;
//            float width, height;
//            width = imageView.getScale() * imageView.getImageWidth();
//            height = imageView.getScale() * imageView.getImageHeight();
//            Log.i("manager", "onScroll--width:" + width + ",height:" + height);
//            Log.i(TAG, "distanceX-distanceY-" + distanceX + "*" + distanceY);
//            if ((int) width <= FFUtils.getDisWidth()
//                    && (int) height <= FFUtils.getDisHight()) {
//                super.onScroll(e1, e2, distanceX, distanceY);
//            } else {
//                left = v[Matrix.MTRANS_X];
//                right = left + width;
//                Rect r = new Rect();
//                imageView.getGlobalVisibleRect(r);
//                Log.i(TAG, "distanceX--" + distanceX);
//                if (distanceX > 0) {
//                    if (r.left > 0) {
//                        super.onScroll(e1, e2, distanceX, distanceY);
//                    } else if (right < FFUtils.getDisWidth()) {
//                        super.onScroll(e1, e2, distanceX, distanceY);
//                    } else {
////						imageView.postTranslate(-distanceX, -distanceY);
//                        if ((int) width > FFUtils.getDisWidth()
//                                && (int) height <= FFUtils.getDisHight()) {
//                            imageView.postTranslate(-distanceX, 0);
//                        } else if ((int) width > FFUtils.getDisWidth()
//                                && (int) height > FFUtils.getDisHight()) {
//                            imageView.postTranslate(-distanceX, -distanceY);
//                        }
//                    }
//                } else if (distanceX < 0) {
//                    if (r.right < FFUtils.getDisWidth()) {
//                        super.onScroll(e1, e2, distanceX, distanceY);
//                    } else if (left > 0) {
//                        super.onScroll(e1, e2, distanceX, distanceY);
//                    } else {
////						imageView.postTranslate(-distanceX, -distanceY);
//                        if ((int) width > FFUtils.getDisWidth()
//                                && (int) height <= FFUtils.getDisHight()) {
//                            imageView.postTranslate(-distanceX, 0);
//                        } else if ((int) width > FFUtils.getDisWidth()
//                                && (int) height > FFUtils.getDisHight()) {
//                            imageView.postTranslate(-distanceX, -distanceY);
//                        }
//                    }
//                }
//            }
//
//        } else {
//            super.onScroll(e1, e2, distanceX, distanceY);
//        }
//        return false;
//    }
//
//    static final int speed = (int) (FFUtils.getPx(400));
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                           float velocityY) {
//        if (Math.abs(velocityX) > Math.abs(velocityY * 3)) {
//            if (velocityX < speed) {
//                if (getSelectedItemPosition() == getCount() - 1) {
//                    return false;
//                }
//                // setSelection(getSelectedItemPosition() +1, true);
//                int kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
//                onKeyDown(kEvent, null);
//            } else if (velocityX > speed) {
//                if (getSelectedItemPosition() == 0) {
//                    return false;
//                }
//                // setSelection(getSelectedItemPosition() - 1, true);
//                int kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
//                onKeyDown(kEvent, null);
//
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        gestureScanner.onTouchEvent(event);
//        View view = ImageGallery.this.getSelectedView();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (view instanceof ZoomImageView) {
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (view instanceof ZoomImageView) {
//                    imageView = (ZoomImageView) view;
//                    float height = imageView.getScale()
//                            * imageView.getImageHeight();
//                    if ((int) height <= FFUtils.getDisHight()) {
//                        break;
//                    }
//                    float v[] = new float[9];
//                    Matrix m = imageView.getImageMatrix();
//                    m.getValues(v);
//                    float top = v[Matrix.MTRANS_Y];
//                    float bottom = top + height;
//                    if (top > 0 && height >= FFUtils.getDisHight()) {
//                        imageView.postTranslateDur(-top, 200f);
//                    }
//                    Log.i("manga", "bottom:" + bottom);
//                    if (bottom < FFUtils.getDisHight()
//                            && height >= FFUtils.getDisHight()) {
//                        imageView.postTranslateDur(
//                                FFUtils.getDisHight() - bottom, 200f);
//                    }
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private class MySimpleGesture extends SimpleOnGestureListener {
//        public boolean onDoubleTap(MotionEvent e) {
//            View view = ImageGallery.this.getSelectedView();
//            if (view instanceof ZoomImageView) {
//                imageView = (ZoomImageView) view;
//                if (imageView.getScale() > imageView.getScaleRate()) {
//                    imageView.zoomTo(imageView.getScaleRate(),
//                            FFUtils.getDisWidth() / 2,
//                            FFUtils.getDisHight() / 2, 200f);
//                    // imageView.layoutToCenter();
//                } else {
//                    float scale = imageView.getScale();
//                    scale = scale + 2;
//                    if (scale >= imageView.getMaxScale()) {
//                        scale = imageView.getMaxScale();
//                    }
//                    imageView.zoomTo(scale,
//                            FFUtils.getDisWidth() / 2,
//                            FFUtils.getDisHight() / 2, 200f);
//                }
//
//            } else {
//
//            }
//            return true;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Context context = getContext();
//            if (context instanceof FullscreenActivity) {
//                ((FullscreenActivity) context).toggleTitle();
//            }
//            return true;
//        }
//    }
//}
