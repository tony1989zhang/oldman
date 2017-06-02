package com.firstaid.util;


import java.io.File;

public class LogUtil {

	private static final int LOG_STATE_UNKOWN = -1;
	private static final int LOG_STATE_ON = 1;
	private static final int LOG_STATE_OFF = 2;
	
	private static int mLogState = LOG_STATE_ON;
	
	public static void d(String tag,String msg){
		if(mLogState == LOG_STATE_UNKOWN){
			final File file = new File("/sdcard/com.firstaid.oldman/log_state");
			if(file.exists()){
				mLogState = LOG_STATE_ON;
			} else {
				mLogState = LOG_STATE_OFF;
			}
		}
		if(mLogState == LOG_STATE_ON){
			android.util.Log.d(tag,msg);
		}
	}
}
