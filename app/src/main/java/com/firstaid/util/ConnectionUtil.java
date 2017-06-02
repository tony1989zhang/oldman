package com.firstaid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firstaid.oldman.R;

import java.util.Map;

/**请求网络Util*/
public class ConnectionUtil {

	private static final String IP = "7.77-7.com"; //"121.42.167.141";
	private static final String PORT = "";//":80";

	private static ConnectionUtil connectionUtil;
	public RequestQueue mQueue;

	protected ConnectionUtil() {
	}

	
	public static ConnectionUtil getInstance(){
		if(connectionUtil == null){
			connectionUtil = new ConnectionUtil();
		}

		return connectionUtil;
	}
	

	public  boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}


	private void initRequestQueue(Context context){
		if(mQueue == null){
			mQueue = Volley.newRequestQueue(context);;
		}
	}

	public RequestQueue getRequestQueue(Context context){
		initRequestQueue(context);
		return mQueue;
	}
	public void loadImgae(NetworkImageView imageView,String url){
		ImageLoader imageLoader = new ImageLoader(ConnectionUtil.getInstance().getRequestQueue(imageView.getContext()), ImageFileCache.getInstance());
		imageView.setImageUrl(TextUtils.isEmpty(url) ? "" : url, imageLoader);
	}
	public String getUrl(String path){
		LogUtil.d("cx","url "+"http://"+IP+PORT+path);
		return "http://"+IP+PORT+path;
	}
	public Request doGet(Context context,String path,final OnDataLoadEndListener listener){
		initRequestQueue(context);
		final StringRequest request = new StringRequest(getUrl(path), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(listener != null){
					listener.OnLoadEnd(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(listener != null){
					listener.OnLoadEnd(null);
				}
			}
		});
		return	mQueue.add(request);
	}

	public Request doPost(Context context,String path,final Map<String, String> params,final OnDataLoadEndListener listener){
		initRequestQueue(context);
		final StringRequest request = new StringRequest(Request.Method.POST,getUrl(path), new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(listener != null){
					listener.OnLoadEnd(response);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if(listener != null){
					listener.OnLoadEnd(null);
				}
			}
		}
		){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};

		return	mQueue.add(request);
	}

	public interface OnDataLoadEndListener{
		public void OnLoadEnd(String ret);
	}


	public String getCurrentNetType(Context context) {
		String type = context.getString(R.string.not_connected);
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			type = context.getString(R.string.not_connected);
		} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			type = "Wifi";
		} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
			int subType = info.getSubtype();
			if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
					|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
				type = "2G";
			} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
					|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
				type = "3G";
			} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
				type = "4G";
			}
		}
		return type;
	}

}

