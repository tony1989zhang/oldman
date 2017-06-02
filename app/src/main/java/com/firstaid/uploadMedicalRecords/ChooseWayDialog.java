package com.firstaid.uploadMedicalRecords;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.firstaid.oldman.R;


/**
 * @author Administrator操作选择
 */
public class ChooseWayDialog extends Dialog implements View.OnClickListener {

	private ChooseBack mWayBack;
	private TextView mTv1;
	private TextView mTv2;
	private TextView mTv3;
	private TextView mtip;

	public ChooseWayDialog(Context context) {
		super(context, R.style.custom_dialog);
		this.setContentView(R.layout.chooseway_dialog);
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth() / 6 * 5); // 设置宽度
		getWindow().setAttributes(lp);
		findViewById(R.id.tv_cancel).setOnClickListener(this);
		mtip = (TextView) findViewById(R.id.tv_tip);
		mTv1 = (TextView) findViewById(R.id.tv_1);
		mTv1.setOnClickListener(this);
		mTv2 = (TextView) findViewById(R.id.tv_2);
		mTv2.setOnClickListener(this);
		mTv3 = (TextView) findViewById(R.id.tv_3);
		mTv3.setOnClickListener(this);

	}

	public void settitle(String title)
	{
		if (null != title) {
			mtip.setText(title);
		}
	}
	public void setData(String Str1, String Str2, String Str3) {
		if (null != Str1) {
			mTv1.setText(Str1);
		}else{
			mTv1.setVisibility(View.GONE);
		}
		if (null != Str2) {
			mTv2.setText(Str2);
		}else{
			mTv2.setVisibility(View.GONE);
		}
		if (null != Str3) {
			mTv3.setVisibility(View.VISIBLE);
			findViewById(R.id.divider_3).setVisibility(View.VISIBLE);
			mTv3.setText(Str3);
		}else{
			mTv3.setVisibility(View.GONE);
		}

	}

	public void setWayBack(ChooseBack mWayBack) {
		this.mWayBack = mWayBack;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			this.cancel();
			break;
		case R.id.tv_1:
			if (null != mWayBack) {
				mWayBack.wayback(0);
			}
			this.cancel();
			break;
		case R.id.tv_2:
			if (null != mWayBack) {
				mWayBack.wayback(1);
			}
			this.cancel();
			break;
		case R.id.tv_3:
			if (null != mWayBack) {
				mWayBack.wayback(2);
			}
			this.cancel();
			break;

		default:
			break;
		}

	}

	public interface ChooseBack {
		void wayback(int i);
	}

}
