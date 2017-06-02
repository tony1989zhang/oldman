package com.firstaid.uploadMedicalRecords;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firstaid.oldman.App;
import com.firstaid.oldman.BaseFragmentActivity;
import com.firstaid.oldman.R;
import com.firstaid.view.TitleView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class CommonBaseActivity extends Activity {
	protected final static String LAST_ACTIVITY_NAME = "LAST_ACTIVITY_NAME";
	public String TAG = this.getClass().getSimpleName();
	private CustomProgressDialog progressDialog;
	public HashMap<String, Object> apiParams = new HashMap<String, Object>();
	public Intent intent = new Intent();
	public WeakReference<Activity> wrActivity = new WeakReference<Activity>(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
//			else{
//				 LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) rootView.getLayoutParams();
//				param.height = tintManager.getConfig().getPixelInsetTop(false) + this.getResources().getDimensionPixelSize(R.dimen.title_height);
//				rootView.setLayoutParams(param);
//			}
		}



		if(App.getInstance().mTintInsertTop <= 0){
			App.getInstance().mTintInsertTop = tintManager.getConfig().getPixelInsetTop(false);
		}
		if(App.getInstance().mTintInsertTopWithActionBar <= 0){
			App.getInstance().mTintInsertTopWithActionBar = tintManager.getConfig().getPixelInsetTop(true);
		}
	}
	/**
	 * @Title: showProgressDialog
	 * @Description: 显示进度条对话框
	 * @param content
	 *            文字提示内容
	 * @param cancancelable
	 *            是否可以取消
	 * @throws
	 */

	public void showProgressDialog(String content, boolean cancancelable) {
		if (progressDialog == null) {
			progressDialog = new CustomProgressDialog(this);
		}
		progressDialog.setMessage(content);
		progressDialog.setCancelable(cancancelable);
		progressDialog.show();
	}

	public void setProgressDialogMessage(String content) {
		if (progressDialog == null) {
			progressDialog = new CustomProgressDialog(this);
			progressDialog.setCancelable(false);
		}
		progressDialog.setMessage(content);
	}

	/**
	 * @Title: showProgressDialog
	 * @Description: 显示进度条对话框
	 * @param content
	 *            文字提示内容
	 * @throws
	 */
	public void showProgressDialog() {
		if (progressDialog != null) {
			progressDialog.show();
		}
	}
	public void showProgressDialog(String content) {
		showProgressDialog(content, true);
	}
	public boolean ProgressIsShow(){
		 return progressDialog!=null && progressDialog.isShowing();
	}
	/**
	 * @Title: hideProgressDialog
	 * @Description:隐藏进度条对话框
	 * @param 设定文件
	 * @throws
	 */

	public void hideProgressDialog() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	/**
	 * @Title: showToask
	 * @Description: 显示Toast
	 * @param @param hint 文字提示内容
	 * @throws
	 */

	public void showToask(String hint) {
		ToastManager.getManager().show(hint);
	}

	public void startActivity(Class cls) {
		intent.setClass(this, cls);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideProgressDialog();
		apiParams.clear();
	}

	@Override
	public void startActivity(Intent intent) {
		if (intent != null) {
			intent.putExtra(LAST_ACTIVITY_NAME, this.getClass().getName());
		}
		super.startActivity(intent);
	}
	protected <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

}
