package com.firstaid.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firstaid.oldman.R;

public class TitleView extends LinearLayout {

	ImageView mTitleBack;
	TextView mTitleName;
	TextView mTitleRight;
	ImageView mTitleRightImg;
	RelativeLayout mTitleViewRoot;
	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.activity_title, this);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mTitleViewRoot = (RelativeLayout) findViewById(R.id.title_view_root);
		mTitleBack = (ImageView) findViewById(R.id.title_back);
		mTitleName = (TextView) findViewById(R.id.title_name);
		mTitleRight = (TextView) findViewById(R.id.title_right);
		mTitleRightImg = (ImageView) findViewById(R.id.title_right_img);
		final LayoutParams param = (LayoutParams) mTitleViewRoot.getLayoutParams();
		param.height = this.getResources().getDimensionPixelSize(R.dimen.title_height);
		mTitleViewRoot.setLayoutParams(param);
	}
	
	public void setTitle(int resid){
		setTitle(this.getResources().getString(resid));
	}
	
	public void setTitle(String title){
		if(mTitleName != null){
			mTitleName.setText(title);
		}
	}
	
	public void setTitleBackVisibility(int visibility){
		if(mTitleBack != null){
			mTitleBack.setVisibility(visibility);
		}
	}
	
	public void setTitleRightText(String text){
		mTitleRightImg.setVisibility(View.GONE);
		if(mTitleRight != null){
			if(TextUtils.isEmpty(text)){
				mTitleRight.setVisibility(View.GONE);
			} else {
				mTitleRight.setText(text);
				mTitleRight.setVisibility(View.VISIBLE);
			}
		}
	}

	public void setTitleRightImg(int resid){
		mTitleRight.setVisibility(View.GONE);
		if(mTitleRightImg != null){
			if(resid < 0){
				mTitleRightImg.setVisibility(View.GONE);
			} else {
				mTitleRightImg.setImageResource(resid);
				mTitleRightImg.setVisibility(View.VISIBLE);
			}
		}
	}
	

	public void setTitleBackOnClickListener(OnClickListener listener){
		if(mTitleBack != null){
			mTitleBack.setOnClickListener(listener);
		}
		if(mTitleRightImg != null){
			mTitleRightImg.setOnClickListener(listener);
		}
	}
	
	public void setTitleRightOnClickListener(OnClickListener listener){
		if(mTitleRight != null){
			mTitleRight.setOnClickListener(listener);
		}
		if(mTitleRightImg != null){
			mTitleRightImg.setOnClickListener(listener);
		}
	}
	
	public void setStatusBarTopInsert(int topMargin){
		final LayoutParams param = (LayoutParams) mTitleViewRoot.getLayoutParams();
		param.height = topMargin + this.getResources().getDimensionPixelSize(R.dimen.title_height);
		mTitleViewRoot.setLayoutParams(param);
	}
	

}
