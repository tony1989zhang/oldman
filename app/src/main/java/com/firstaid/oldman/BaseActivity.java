package com.firstaid.oldman;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firstaid.view.TitleView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (App.getInstance().mScreenHeight <= 0) {
			DisplayMetrics metrics = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			App.getInstance().mScreenHeight = metrics.heightPixels;
			App.getInstance().mScreenWidth = metrics.widthPixels;
            PushAgent.getInstance(this).onAppStart();
		}


	}

	protected void initSystemBarTint(View rootView,int color){
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
                params.topMargin += tintManager.getConfig().getPixelInsetTop(true);
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


    ProgressDialog mLoadingDialog;
    protected void showLoading(){
        if(mLoadingDialog == null){
            mLoadingDialog = new ProgressDialog(this);
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoading(){
        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

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
