package com.firstaid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SPUtil {

	private static SharedPreferences sp;
	
	private static SPUtil spUtil;
	
	private static final String CONFIG="APP_COONFIG";
	
	private static Editor editor;
	
	public static SPUtil getInstant(Context context){
		if(spUtil==null){
			spUtil=new SPUtil();
		}
		
		if(sp==null){
			sp=context.getSharedPreferences(CONFIG,0);
		}
		
		if(editor==null){
			editor=sp.edit();
		}
		
		return spUtil;
	}
	
	public SPUtil save(String key,Object value){
		if(value instanceof String){
			editor.putString(key, value.toString());
		}
		if(value instanceof Boolean){
			editor.putBoolean(key, Boolean.valueOf(value.toString()));
		}
		if (value instanceof Long) {
			editor.putLong(key, Long.valueOf(value.toString()));
		}
		if (value instanceof Float) {
			editor.putFloat(key, Float.valueOf(value.toString()));
		}
		if (value instanceof Integer) {
			editor.putInt(key, Integer.valueOf(value.toString()));
		}
		editor.commit();
		return spUtil;
	}
	
	public Object get(String key,Object value){
		if(value instanceof String){
			return sp.getString(key, value == null ? "" : value.toString());
		}
		if(value instanceof Boolean){
			return sp.getBoolean(key, false);
		}
		if (value instanceof Long) {
			return sp.getLong(key,(Long)value);
		}
		if (value instanceof Float) {
			return sp.getFloat(key, -1.0f);
		}
		if (value instanceof Integer) {
			return sp.getInt(key, (Integer)value);
		}
		return null;
	}
	
}
