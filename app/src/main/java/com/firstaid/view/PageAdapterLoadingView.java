package com.firstaid.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firstaid.oldman.App;
import com.firstaid.oldman.R;

/**
 * 分页加载底部控件
 * */
public class PageAdapterLoadingView extends LinearLayout {

	public ProgressBar mProgressBar;
	public TextView mTextView;
	boolean mIsPageEnabled;
	public PageAdapterLoadingView(Context context,boolean isPageEnabled) {
		super(context);
		// TODO Auto-generated constructor stub
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.page_adapter_loading, null);
		mProgressBar = (ProgressBar) root.findViewById(R.id.page_adapter_loading_progress);
		mTextView = (TextView) root.findViewById(R.id.page_adapter_loading_text);
		this.addView(root, new LayoutParams(App.getInstance().mScreenWidth, LayoutParams.WRAP_CONTENT));
		mIsPageEnabled = isPageEnabled;
	}

	public void loading(){
		mProgressBar.setVisibility(View.VISIBLE);
		mTextView.setText(R.string.page_adapter_loading_text);
		checkEnabled();
		this.invalidate();
	}
	
	public void loadMore(){
		mProgressBar.setVisibility(View.GONE);
		mTextView.setText(R.string.page_adapter_loading_text_more);
		checkEnabled();
		this.invalidate();
	}
	
	public void hide(){
		mProgressBar.setVisibility(View.GONE);
		mTextView.setText("");
		checkEnabled();
		this.invalidate();
	}
	
	public void loadFinished(){
		mProgressBar.setVisibility(View.GONE);
		mTextView.setText(R.string.page_adapter_loading_text_no_more);
		checkEnabled();
		this.invalidate();
	}

	private void checkEnabled(){
		if(!mIsPageEnabled){
			mProgressBar.setVisibility(View.GONE);
			mTextView.setText("");
		}
	}

	
}
