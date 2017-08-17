//package com.fengnian.smallyellowo.foodie.bigpicture;
//
//import android.os.Bundle;
//
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//
//public abstract class FullscreenActivity extends BaseActivity<BitPictureIntent> {
////	private FrameLayout controlsView;
////	private View titleView;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		setActionBarOverlap();
//		super.onCreate(savedInstanceState);
////		controlsView = getBottomBar();
////		titleView = getActionBarView();
////		((FrameLayout.LayoutParams) titleView.getLayoutParams()).setMargins(0,
////				FFUtils.getStatusbarHight(this), 0, 0);
////		getWindow().setFlags(
////				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
////						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
////				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
////						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////
////		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
////		}
//	}
//
////	public void setBottomBar(int layoutId) {
////		getBottomBar().addView(getLayoutInflater().inflate(layoutId, null));
////	}
//
////	private boolean isFullScreen = false;
////	private int mControlsHeight = 0;
////	private int mTitleHeight = 0;
////	private int mShortAnimTime = 0;
//
////	public void toggleTitle() {
////		if (isFullScreen) {
////			this.getWindow().clearFlags(
////					WindowManager.LayoutParams.FLAG_FULLSCREEN);
////			isFullScreen = false;
////
////			if (mControlsHeight == 0) {
////				mControlsHeight = controlsView.getHeight();
////				mTitleHeight = titleView.getHeight();
////			}
////			if (mShortAnimTime == 0) {
////				mShortAnimTime = getResources().getInteger(
////						android.R.integer.config_shortAnimTime);
////			}
////			toggleActionBar(true);
////		} else {
////			if (mControlsHeight == 0) {
////				mControlsHeight = controlsView.getHeight();
////				mTitleHeight = titleView.getHeight();
////			}
////			if (mShortAnimTime == 0) {
////				mShortAnimTime = getResources().getInteger(
////						android.R.integer.config_shortAnimTime);
////			}
////			toggleActionBar(false);
////		}
////	}
//
////	public void toggleActionBar(boolean toShow) {
////		if (toShow) {
////			{
////				controlsView.setVisibility(View.VISIBLE);
////				TranslateAnimation anim = new TranslateAnimation(0, 0,
////						mControlsHeight, 0);
////				anim.setDuration(mShortAnimTime);
////				controlsView.startAnimation(anim);
////			}
////
////			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
////				titleView.setVisibility(View.VISIBLE);
////				TranslateAnimation anim = new TranslateAnimation(0, 0,
////						-mTitleHeight, 0);
////				anim.setDuration(mShortAnimTime);
////				titleView.startAnimation(anim);
////			}
////		} else {
////			{
////				TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
////						mControlsHeight);
////				anim.setDuration(mShortAnimTime);
////				anim.setStartOffset(mControlsHeight);
////				controlsView.startAnimation(anim);
////			}
////			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
////				TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
////						-mTitleHeight);
////				anim.setDuration(mShortAnimTime);
////				anim.setStartOffset(mTitleHeight);
////				anim.setAnimationListener(new AnimationListener() {
////
////					@Override
////					public void onAnimationStart(Animation animation) {
////					}
////
////					@Override
////					public void onAnimationRepeat(Animation animation) {
////					}
////
////					@Override
////					public void onAnimationEnd(Animation animation) {
////						isFullScreen = true;
////						getWindow().setFlags(
////								WindowManager.LayoutParams.FLAG_FULLSCREEN,
////								WindowManager.LayoutParams.FLAG_FULLSCREEN);
////						controlsView.setVisibility(View.GONE);
////						titleView.setVisibility(View.GONE);
////					}
////				});
////				titleView.startAnimation(anim);
////
////			}
////		}
////	}
//}
