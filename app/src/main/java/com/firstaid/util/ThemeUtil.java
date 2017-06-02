package com.firstaid.util;

import android.content.Context;


public class ThemeUtil {	
	public static final String RESOURCES_TYPE_COLOR = "color";
	public static final String RESOURCES_TYPE_DRAWABLE = "drawable";
	public static final String RESOURCES_TYPE_ID = "id";
	public static final String RESOURCES_TYPE_LAYOUT = "layout";
	public static final String RESOURCES_TYPE_STRING = "string";
	public static final String RESOURCES_TYPE_STYLE = "style";
	public static final String RESOURCES_TYPE_STYLEABLE = "styleable";
	public static final String RESOURCES_TYPE_RAW = "raw";
	public static final String RESOURCES_TYPE_DIMEN = "dimen";
	
	public static int getRemoteId(Context context, String name , String idType,int defaultID){
		StringBuilder custrom_name = new StringBuilder(context.getPackageName());
		custrom_name.append(":").append(idType).append("/").append(name);
		int remoteId = context.getResources().getIdentifier(custrom_name.toString(), null, null);
		return remoteId <= 0 ? defaultID : remoteId;
	}

}
