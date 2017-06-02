package com.firstaid.receiver;

import java.util.List;

import com.firstaid.oldman.App;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String ActionName = intent.getAction();
		if(Intent.ACTION_BOOT_COMPLETED.equals(ActionName)){
			App.getInstance().registHeadSet(context);
		}
	}
	

		
}
