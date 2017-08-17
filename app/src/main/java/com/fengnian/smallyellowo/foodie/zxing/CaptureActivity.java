package com.fengnian.smallyellowo.foodie.zxing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.IsHuitYuanReult;
import com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.zxing.camera.CameraManager;
import com.fengnian.smallyellowo.foodie.zxing.decoding.CaptureActivityHandler;
import com.fengnian.smallyellowo.foodie.zxing.decoding.InactivityTimer;
import com.fengnian.smallyellowo.foodie.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

public class CaptureActivity extends BaseActivity<IntentData> implements Callback
{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageView iv_left_back;
	private  CameraManager cameraManager;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setNotitle(true);
		setContentView(R.layout.activity_capture_carmer);
		CameraManager.init(getApplication());
		iv_left_back= (ImageView) findViewById(R.id.iv_left_back);
		iv_left_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inactivityTimer.shutdown();
               finish();
			}
		});
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		if(android.os.Build.VERSION.SDK_INT>=23 && !FFUtils.isPermissions(Manifest.permission.CAMERA)) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
		}
	}

	/**
	 * 动态申请  权限后用用的选择
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				} else {
					showToast("未获得访问权限，不能操作！！");
					CaptureActivity.this.finish();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		 initsaoyisao();
	}
	void     initsaoyisao(){
		cameraManager= new CameraManager(this);

		// 关闭摄像头
		cameraManager.closeDriver();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();

		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

//		onrestartanything();
	}


	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}


	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

    void onrestartanything(){
		cameraManager.closeDriver();
		closeCamera();
		restartCamera();

		if (handler == null)
		{
			handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}


	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}
     String tempstr= Constants.shareConstants().getShareUrlProfix()+"/person/info.html?uid";
	public void handleDecode(final Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String  text=obj.getText();
		String[] str=text.split("=");
		if(str.length>1){
        if(str.length==2&&tempstr.equals(str[0])&&str[1].length()>0){
					checkIshuiyuan(str[1]);
		 }else if(obj.getText().length()>0){
//			UIUtil.showToast(CaptureActivity.this,"抱歉不能识别小黄圈以外的二维码");
			//重新扫描
            showErrorDialog();

		}
		}else{
		 if(obj.getText().length()>0){
//				UIUtil.showToast(CaptureActivity.this,"抱歉不能识别小黄圈以外的二维码");
				//重新扫描
			 showErrorDialog();
		}
		}
	}

	private void showErrorDialog() {

		new EnsureDialog.Builder(CaptureActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

				.setMessage("抱歉不能识别小黄圈以外的二维码，请重新扫描二维码!")//设置显示的内容

				.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

					@Override

					public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
						 onrestartanything();
					}

				})
//				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					//添加返回按钮
//			@Override
//
//			public void onClick(DialogInterface dialog, int which) {//响应事件
//
//			}
//
//		}
//				)
				.show();//在按键响应事件中显示此对话框
	}


//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		if (barcode == null)
//		{
//			dialog.setIcon(null);
//		}
//		else
//		{
//
//			Drawable drawable = new BitmapDrawable(barcode);
//			dialog.setIcon(drawable);
//		}
//		dialog.setTitle("扫描结果");
//		dialog.setMessage(obj.getText());
//		dialog.setNegativeButton("确定", new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				//用默认浏览器打开扫描得到的地址
////				Intent intent = new Intent();
////				intent.setAction("android.intent.action.VIEW");
////				Uri content_url = Uri.parse(obj.getText());
////				intent.setData(content_url);
////				startActivity(intent);
////				finish();
//
//				UIUtil.showToast(CaptureActivity.this,obj.getText()+"获取的信息");
//			}
//		});
//		dialog.setPositiveButton("取消", new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				finish();
//			}
//		});
//		dialog.create().show();


	void restartCamera(){

		viewfinderView.setVisibility(View.VISIBLE);

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		initCamera(surfaceHolder);

		// 恢复活动监控器
		inactivityTimer.onActivity();
	}

	void closeCamera(){
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.cancel();

		// 关闭摄像头
		cameraManager.closeDriver();
	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};


	private void   checkIshuiyuan(final String uid){
		FFExtraParams extra = new FFExtraParams();
		extra.setDoCache(true);
		extra.setSynchronized(false);
		extra.setKeepWhenActivityFinished(false);
		extra.setProgressDialogcancelAble(true);
//		post(Constants.shareConstants().getNetHeaderAdress() + "/user/judgeUserType.do", "", extra, new FFNetWorkCallBack<IsHuitYuanReult>() {
		post(IUrlUtils.Search.judgeUserType, "", extra, new FFNetWorkCallBack<IsHuitYuanReult>() {
			@Override
			public void onSuccess(IsHuitYuanReult response, FFExtraParams extra) {
				if (response.judge()) {
					// TODO: 2017-2-23
					UserInfoIntent info=new UserInfoIntent();
					info.setId(uid);
					if(response.getUserType()==0){
						//普通用户
						startActivity(UserInfoActivity.class,info);
					}else{
						//会员用户进入 会员个人  页面
						startActivity(ClubUserInfoActivity.class,info);
					}



				} else {
					showToast(response.getServerMsg());
				}
			}

			@Override
			public boolean onFail(FFExtraParams extra) {
				return false;
			}
		},"targetAccount",uid);
	}

















}