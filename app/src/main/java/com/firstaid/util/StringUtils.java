package com.firstaid.util;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


public class StringUtils {

	/**
	 * 判断是否为空 ，或者全部空格
	 */
	public static boolean isEmpty(Object obj) {

		return null == obj || "".equals(obj.toString().trim());
	}

	/**
	 * 判断是否空白
	 */
	public static boolean isBlank(Object obj) {

		return null == obj || "".equals(obj.toString());
	}

	/**
	 * 判断是否非空白
	 */
	public static boolean isNotEmpty(Object obj) {

		return !isEmpty(obj);
	}

	/**
	 * 获取当前时间戳
	 */
	public static String getSequenceId() {
		String mark = String.valueOf(System.currentTimeMillis());
		return mark;
	}

	/**
	 * 获取当前时间 格式：yyyyMMddHHmmss
	 */
	public static String getCurrentlyDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	/**
	 * 时间戳转时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String transformDateTime(long t) {
		Date date = new Date(t);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static String transformDateTime(long t, String fmt) {
		Date date = new Date(t);
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		return dateFormat.format(date);
	}

	public static String transformDateTime(String t, String fmt) {
		Date date = new Date(Long.valueOf(t));
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		return dateFormat.format(date);
	}

	/**
	 * 获取当前日期 格式：yyyyMMdd
	 */
	public static String getCurrentlyDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(new Date());
	}

	/**
	 * 生成UUID通用唯一识别码 (去掉-)
	 */
	public static String getUUID() {

		return UUID.randomUUID().toString().replaceAll("-", "");
	}



	/**
	 * 将相册uri转为String
	 * 
	 * @param uri
	 * @param context
	 * @return
	 */
	public static String getPath(Uri uri, Activity context) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static String timeRange(Date d1, Date d2) {
		String time = "";
		try {
			// DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long diff = Math.abs(d1.getTime() - d2.getTime());// 这样得到的差值是微秒级别
			long days = Math.abs(diff / (1000 * 60 * 60 * 24));
			long hours = Math.abs((diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
			long minutes = Math.abs((diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60));
			// if (days > 356) {
			// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// }
			// else if (days > ) {
			//
			// }
			if (days > 30) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				time = df.format(d1);
			} else if (days > 0) {
				System.out.println(days + "天" + hours + "小时");
				time = days + "天";
			} else if (days == 0 && hours > 0) {
				System.out.println(hours + "小时");
				time = hours + "小时";
			} else if (days == 0 && hours == 0 && minutes >= 0) {
				System.out.println(minutes + "分钟");
				time = minutes + "分钟";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String NumFormat(String num) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(num)) {
			return "";
		}
		String s = null;
		try {
			float f = Float.parseFloat(num);
			boolean m = false;
			if (f >= 10000) {
				f = f / 10000.0f;
				m = true;
			}
			s = f + "";
			// s = decimalFormat.format(f);
			if (s.contains(".")) {
				String ss;
				if (s.length() > s.indexOf(".") + 2) {
					ss = s.substring(0, s.indexOf(".") + 3);
					if (ss.endsWith("00")) {
						ss = s.substring(0, s.indexOf("."));
					} else if (ss.endsWith("0")) {
						ss = s.substring(0, s.indexOf(".") + 2);
					}
				} else if (s.length() > s.indexOf(".") + 1) {
					ss = s.substring(0, s.indexOf(".") + 2);
					if (ss.endsWith("0")) {
						ss = s.substring(0, s.indexOf("."));
					}
				} else {
					ss = s.substring(0, s.indexOf("."));
				}
				s = ss;
			}

			if (m) {
				s = s + "万";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return s;
	}

	/**
	 * 将第一个字转为*
	 * 
	 * @param name
	 * @return
	 */
	public static String nameFormat(String name) {
		if (StringUtils.isNotEmpty(name)) {
			char c = name.charAt(0);
			return name.replaceFirst(c + "", "*");
		} else {
			return "";
		}
	}

	public static String accountFormat(String account) {
		if (StringUtils.isNotEmpty(account)) {
			String s = null;
			if (account.length() <= 0) {
				return "***";
			} else if (account.length() < 3) {
				s = account.charAt(0) + "***";
			} else if (account.length() <= 6) {
				String substring = account.substring(0, 3);
				s = substring + "***";
			} else {
				String start = account.substring(0, 3);
				String end = account.substring(account.length() - 3, account.length());
				s = start + "***" + end;
			}
			return s;
		} else {
			return "";
		}

	}

	public static String dayBefore(String time) {
		if (StringUtils.isEmpty(time)) {
			return "";
		}
		long current = System.currentTimeMillis();
		long before = Long.parseLong(time);
		long interval = current - before;
		if (interval > 0 && interval < 60 * 1000) {
			return interval / 1000 + "秒前";
		} else if (interval >= 60 * 1000 && interval < 3600 * 1000) {
			return interval / (60 * 1000) + "分钟前";
		} else if (interval >= 3600 * 1000 && interval < 24 * 3600 * 1000) {
			return interval / (3600 * 1000) + "小时前";
		} else if (interval >= 24 * 3600 * 1000 && interval < 48 * 3600 * 1000) {
			return "昨天";
		} else if (interval >= 48 * 3600 * 1000 && interval < 72 * 3600 * 1000) {
			return "前天";
		} else if (interval >= 72 * 3600 * 1000) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
			String df = dateFormat.format(new Date(before));
			return df;
		}
		return "";
	}
	//最多保留两位小数
	public static String max2bit(String tex){
		DecimalFormat format=new DecimalFormat("#.##");
		try {
			Double texValues = Double.valueOf(tex);
			return format.format(texValues);
		} catch (Exception e) {
			Log.e("", "" + "转化为最多两位小数出错");
			return "";
		}
		
	}
	public static String extractDigital(String tex){
		String regEx="[0-9]\\d*\\.?\\d*";
		Pattern p = Pattern.compile(regEx);
		Matcher m=p.matcher(tex);
		while (m.find()) {
			String hasFind = m.group(0);
			String numFormat="0";
			try {
				numFormat = NumFormat(hasFind);
			} catch (Exception e) {
				Log.e("", "" + "正则拆分数字后值化失败");
			}
			String last = m.replaceAll(numFormat);
			return last;
		}
		return "0";
	}
}
