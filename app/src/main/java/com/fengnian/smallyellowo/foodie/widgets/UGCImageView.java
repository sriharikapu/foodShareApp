package com.fengnian.smallyellowo.foodie.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.fan.framework.utils.FFUtils;

public class UGCImageView extends ImageView {

	public UGCImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected Matrix mBaseMatrix = new Matrix();

	protected Matrix mSuppMatrix = new Matrix();

	private final Matrix mDisplayMatrix = new Matrix();

	private final float[] mMatrixValues = new float[9];

	protected Bitmap image = null;

	int mThisWidth = -1, mThisHeight = -1;

	float mMaxZoom = 4.0f;
	final float mMinZoom = 0.9f;

	private int imageWidth;
	private int imageHeight;

	private float scaleRate;

	@Override
	public void setImageResource(int resId) {
		setImageBitmap1(((BitmapDrawable) getResources().getDrawable(resId)).getBitmap());
	}

	public UGCImageView(Context context) {
		super(context);
		init();
	}

	public UGCImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 *
	 */
	private void arithScaleRate() {
		float scaleWidth = FFUtils.getDisWidth() / (float) imageWidth;
		float scaleHeight = FFUtils.getDisHight() / (float) imageHeight;
		scaleRate = Math.min(scaleWidth, scaleHeight);
//		mMinZoom = scaleRate;
		mMaxZoom = Math.max(mMinZoom, mMaxZoom);
	}

	public float getScaleRate() {
		return scaleRate;
	}
	public float getMaxScale() {
		return this.mMaxZoom;
	}
	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
			if (getScale() > 1.0f) {
				// If we're zoomed in, pressing Back jumps out to show the
				// entire image, otherwise Back returns the user to the gallery.
				zoomTo(1.0f);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected Handler mHandler = new Handler();

	/**
	 * 在imageView 显示之前加载图片
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
		image = bitmap;
		if (image == null) {
			return;
		}
		imageHeight = image.getHeight();
		imageWidth = image.getWidth();
		arithScaleRate();
		zoomTo(mMinZoom, FFUtils.getDisWidth() / 200f, FFUtils.getDisHight() / 200f);
		layoutToCenter();
	}

	/**
	 * @param bitmap
	 */
	public void setImageBitmap1(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
		image = bitmap;
		if (image == null) {
			return;
		}
		imageHeight = image.getHeight();
		imageWidth = image.getWidth();
		arithScaleRate();
		zoomTo(mMinZoom, FFUtils.getDisWidth() / 200f, FFUtils.getDisHight() / 200f);
		center(true, true);
	}

	public void clearCache() {
		if (image != null) {
			image.recycle();
			image = null;
		}
	}

	// Center as much as possible in one or both axis. Centering is
	// defined as follows: if the image is scaled down below the
	// view's dimensions then center it (literally). If the image
	// is scaled larger than the view and is translated out of view
	// then translate it back into view (i.e. eliminate black bars).
	protected void center(boolean horizontal, boolean vertical) {
		// if (mBitmapDisplayed.getBitmap() == null) {
		// return;
		// }
		if (image == null) {
			return;
		}

		Matrix m = getImageViewMatrix();

		RectF rect = new RectF(0, 0, image.getWidth(), image.getHeight());
		// RectF rect = new RectF(0, 0, imageWidth*getScale(),
		// imageHeight*getScale());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	private void init() {
		setScaleType(ScaleType.MATRIX);
	}

	/**
	 *
	 */
	public void layoutToCenter() {

		float width = imageWidth;
		float height = imageHeight;

		float fill_width = FFUtils.getDisWidth() - width;
		float fill_height = FFUtils.getDisHight() - height;

		float tran_width = 0f;
		float tran_height = 0f;

		if (fill_width > 0)
			tran_width = fill_width / 2;
		if (fill_height > 0)
			tran_height = fill_height / 2;
		postTranslate(tran_width, tran_height);
		setImageMatrix(getImageViewMatrix());
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		// mMinZoom = (FFUtils.getDisWidth() / 2f) / imageWidth;

		return mMatrixValues[whichValue];
	}

	// Get the scale factor out of the matrix.
	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	public float getScale() {
		return getScale(mSuppMatrix);
	}

	// Combine the base matrix and the supp matrix to make the final matrix.
	protected Matrix getImageViewMatrix() {
		// The final matrix is computed as the concatentation of the base matrix
		// and the supplementary matrix.
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	static final float SCALE_RATE = 1.25F;

	// Sets the maximum zoom, which is a scale relative to the base matrix. It
	// is calculated to show the image at 400% zoom regardless of screen or
	// image orientation. If in the future we decode the full 3 megapixel image,
	// rather than the current 1024x768, this should be changed down to 200%.
	public float maxZoom() {
		if (image == null) {
			return 1F;
		}

		float fw = (float) image.getWidth() / (float) mThisWidth;
		float fh = (float) image.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 6;
		return max;
	}

	public void zoomTo(float scale, float centerX, float centerY) {
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		} else if (scale < mMinZoom) {
			scale = mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void zoomTo(final float scale, final float centerX, final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);
				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	float currentScale = -1;

	public float getCurrentScale() {
		return currentScale;
	}

	public void zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;
		currentScale = scale;
		zoomTo(scale, cx, cy);
	}

	public void zoomToPoint(float scale, float pointX, float pointY) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		panBy(cx - pointX, cy - pointY);
		zoomTo(scale, cx, cy);
	}

	public void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	public void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	public void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		} else if (getScale() <= mMinZoom) {
			return;
		}
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	public void zoomOut(float rate) {
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		// Zoom out to at most 1x.
		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		if (getScale(tmp) < 1F) {
			mSuppMatrix.setScale(1F, 1F, cx, cy);
		} else {
			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	float _dy = 0.0f;

	public void postTranslateDur(final float dy, final float durationMs) {
		_dy = 0.0f;
		final float incrementPerMs = dy / durationMs;
		final long startTime = System.currentTimeMillis();
		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);

				postTranslate(0, incrementPerMs * currentMs - _dy);
				_dy = incrementPerMs * currentMs;
				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	public void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

}
