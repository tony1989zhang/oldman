package com.firstaid.oldman;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firstaid.view.TitleView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;


public class BaseFragmentActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		if (App.getInstance().mScreenHeight <= 0) {
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			App.getInstance().mScreenHeight = metrics.heightPixels;
			App.getInstance().mScreenWidth = metrics.widthPixels;
		}
//
//		final PushAgent pushAgent = PushAgent.getInstance(this);
//		pushAgent.enable();
//		pushAgent.setResourcePackageName("com.firstaid.oldman");
//		//开启推送并设置注册的回调处理
//		pushAgent.setPushCheck(true);
//		Log.e("mPushAgent.isEnabled()", "mPushAgent.isEnabled():" + pushAgent.isEnabled());
//		String registrationId = UmengRegistrar.getRegistrationId(this);
//		pushAgent.onAppStart();


//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				int x = 0;
//				while (x <= 10) {
//					x++;
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					String deviceToken = pushAgent.getRegistrationId();
//					Log.e("TAG", " == " + x + " ==deviceToken info :" + deviceToken);
//				}
//			}
//		}).start();
		//服务端控制代码
//		pushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);
//		pushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);
//		pushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);
		//客户端控制代码
//		pushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//		pushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//		pushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
	}

	public void initSystemBarTint(View rootView,int color){
		if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
			setTranslucentStatus(this, true);
		} else {
			return;
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintColor(color);
		if(rootView != null){
			if(rootView instanceof TitleView){
				((TitleView)rootView).setStatusBarTopInsert(tintManager.getConfig().getPixelInsetTop(false));
			} else if(rootView.getLayoutParams() instanceof LinearLayout.LayoutParams){
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rootView.getLayoutParams() ;
				params.topMargin += tintManager.getConfig().getPixelInsetTop(false);
				rootView.setLayoutParams(params);
			} else if(rootView.getLayoutParams() instanceof RelativeLayout.LayoutParams){
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)rootView.getLayoutParams() ;
				params.topMargin = +tintManager.getConfig().getPixelInsetTop(false);
				rootView.setLayoutParams(params);
			}
		}


		if(App.getInstance().mTintInsertTop <= 0){
			App.getInstance().mTintInsertTop = tintManager.getConfig().getPixelInsetTop(false);
		}
		if(App.getInstance().mTintInsertTopWithActionBar <= 0){
			App.getInstance().mTintInsertTopWithActionBar = tintManager.getConfig().getPixelInsetTop(true);
		}
	}

	public void setStatusBarTintColor(int color){
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintColor(color);
	}
	private void setTranslucentStatus(Activity activity, boolean on) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS/* | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*/;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}


	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
