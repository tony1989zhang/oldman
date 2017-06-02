package com.firstaid.oldman;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 只加载一个Fragment的Activity，只需继承ActivityForFragmentNormal，并重写initFragment()方法即可
 * */
public class ActivityForFragmentNormal extends BaseFragmentActivity{

	Fragment mFragment;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_for_fragment_normal);
		initViews();
	}

	private void initViews(){
		mFragment = initFragment();
		if(this.getIntent() != null && this.getIntent().getExtras() != null){
			mFragment.setArguments(this.getIntent().getExtras());
		}
		this.getSupportFragmentManager().beginTransaction().add(R.id.main_container, mFragment).commit();
		
	}
	
	public Fragment initFragment(){
		return null;
	}
}
