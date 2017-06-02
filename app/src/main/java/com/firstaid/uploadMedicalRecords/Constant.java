package com.firstaid.uploadMedicalRecords;

import java.io.File;

import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.naming.MD5FileNameGenerator;
import com.nostra13.universalimageloader.core.MD5;

/**
 * 
 * @version 1.0
 */
public class Constant {
	/**
	 * 开发1
	 */
	// public static String CIM_SERVER_HOST = "192.168.100.33:8333";
	// public static String CIM_SERVER_HOST2 = "192.168.100.33:8333";
	/**
	 * 开发2
	 */
	// public static String CIM_SERVER_HOST = "192.168.100.148";
	// public static String CIM_SERVER_HOST2 = "192.168.100.253";
	public static String CIM_SERVER_HOST = "192.168.100.251";
	public static String CIM_SERVER_HOST2 = "192.168.100.253";
	// public static String CIM_SERVER_HOST = "218.66.216.142";
	// public static String CIM_SERVER_HOST2 = "192.168.100.251";
	// public static String CIM_SERVER_HOST = "192.168.100.252";
	// public static String CIM_SERVER_HOST2 = "192.168.100.253";
	/**
	 * 正式
	 */
	// public static String CIM_SERVER_HOST = "121.40.57.227";
	// public static String CIM_SERVER_HOST2 = "www.wbaiju.com";
	/**
	 * 外网测试
	 */
	// public static String CIM_SERVER_HOST = "121.40.57.144";
	// public static String CIM_SERVER_HOST2 = "121.40.57.225";
	/**
	 * 外网测试2
	 */
	// public static String CIM_SERVER_HOST = "121.40.57.144";
	// public static String CIM_SERVER_HOST2 = "121.40.57.225";
	/**
	 * 本地内测
	 */
	// public static final String DEFAULT_PATH = "http://" + CIM_SERVER_HOST +
	// ":8833/wbaiju/default/server_dispatch.api";
	// public static final String DEFAULT_PATH2 = "http://" + CIM_SERVER_HOST2 +
	// ":8833/wbaiju/default/server_dispatch.api";
	// public static final String UPDATE_URL = "http://" + CIM_SERVER_HOST +
	// ":8833/wbaiju/cgi/config_appVersion.api?";
	// public static String SERVER_URL = "http://" + CIM_SERVER_HOST +
	// ":8833/wbaiju";
	// public static void reSetSERVER_URL() {
	// SERVER_URL = "http://" + CIM_SERVER_HOST + ":8833/wbaiju";
	// }
	public static final String DEFAULT_PATH = "http://" + CIM_SERVER_HOST + ":8033/wbaiju/default/server_dispatch.api";
	public static final String DEFAULT_PATH2 = "http://" + CIM_SERVER_HOST2
			+ ":8033/wbaiju/default/server_dispatch.api";
	public static final String UPDATE_URL = "http://" + CIM_SERVER_HOST + ":8033/wbaiju/cgi/config_appVersion.api?";
	public static String SERVER_URL = "http://" + CIM_SERVER_HOST + ":8033/wbaiju";

	public static void reSetSERVER_URL() {
		SERVER_URL = "http://" + CIM_SERVER_HOST + ":8033/wbaiju";
	}

	// public static final String DEFAULT_PATH = "http://" + CIM_SERVER_HOST +
	// ":8933/wbaiju/default/server_dispatch.api";
	// public static final String DEFAULT_PATH2 = "http://" + CIM_SERVER_HOST2 +
	// ":8933/wbaiju/default/server_dispatch.api";
	// public static final String UPDATE_URL = "http://" + CIM_SERVER_HOST +
	// ":8933/wbaiju/cgi/config_appVersion.api?";
	// public static String SERVER_URL = "http://" + CIM_SERVER_HOST +
	// ":8933/wbaiju";
	//
	// public static void reSetSERVER_URL() {
	// SERVER_URL = "http://" + CIM_SERVER_HOST + ":8933/wbaiju";
	// }

	/**
	 * 阿里云测试
	 */
	// public static final String DEFAULT_PATH = "http://" + CIM_SERVER_HOST +
	// "/wbaiju/default/server_dispatch.api";
	// public static final String DEFAULT_PATH2 = "http://" + CIM_SERVER_HOST2 +
	// "/wbaiju/default/server_dispatch.api";
	// public static final String UPDATE_URL = "http://" + CIM_SERVER_HOST +
	// "/wbaiju/cgi/config_appVersion.api?";
	// public static String SERVER_URL = "http://" + CIM_SERVER_HOST +
	// "/wbaiju";
	//
	// public static void reSetSERVER_URL() {
	// SERVER_URL = "http://" + CIM_SERVER_HOST + "/wbaiju";
	// }

	/** 开发模式 */
	public static final boolean DEVELOPER_MODE = false;
	// 注意，这里的端口不是tomcat的端口，CIM端口在服务端spring-cim.xml中配置的，没改动就使用默认的28888
	public static int CIM_SERVER_PORT = 28888;
	public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/com.wbaiju.ichat/";
	/** 缓存文件夹地址 */
	public static final String CACHE_DIR = ROOT_DIR + "cache";
	public static final String LOG_DIR = ROOT_DIR + "log";
	public static final String VIDEO_DIR = ROOT_DIR + "video";
	public static final String EXPORT_DIR = Environment.getExternalStorageDirectory().getPath() + "/";//"/Wbaiju/";
	public static final String EXPORT_IMAGE_DIR = EXPORT_DIR ;//+ "image/";
	public static final String EXPORT_VIDEO_DIR = EXPORT_DIR + "video/";
	/** 存放下载文件夹地址 */
	public static final String DOWNLOAD_DIR = ROOT_DIR + "download2";
	// /**
	// * 存放语音签名文件地址
	// */
	// public static final String VOICE_FILE = ROOT_DIR+"voicefile/";

	/** 每页显示消息数目 */
	public static int MESSAGE_PAGE_SIZE = 20;
	/** 每页显示动态消息数目 */
	public static int ARTICLE_PAGE_SIZE = 10;

	public static final String SYSTEM = "1";
	public static final String WBAIJU_TEAM = "0";
	public static final String WBAIJU_DB_NAME = "ichat.db";
	// 百度地图默认经纬度 （公司地址）
	/**
	 * 阿里云OSS 配置
	 * 
	 * 请使用自己的阿里云OSS配置
	 */
	public static final String OSS_BUCKET_NAME = "wbaiju";
	public static final String OSS_ACCESS_ID = "Z80wHnnQEtj4wmKH";
	public static final String OSS_ACCESS_KEY = "5w4LOSSjugHmkcqkMwrlNj5VRazHHo";
	public static final String OSS_FILE_URL = "http://wbaiju.oss-cn-hangzhou.aliyuncs.com/";
	/**
	 * 腾讯Appid
	 */
	public static final String QQ_APPID = "1102115237";
	/**
	 * 腾讯APPKEY
	 */
	public static final String QQ_APPKEY = "goUSf54Z2Qnlc5v3";

	/**
	 * 微信Appid
	 */
	public static final String WX_APPID = "wxdf01117c60c0d795";
	/**
	 * 微信Appsecret
	 */
	public static final String WX_APPSECRET = "d1cbe3a0b9ca8e6373fb7b94f09aadc5";

	/**
	 * 微信获取token地址
	 */
	public static final String WX_ACCESS_TOKEN_PATH = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/**
	 * 微信获取用户信息地址
	 */
	public static final String WX_USERINFO_PATH = "https://api.weixin.qq.com/sns/userinfo";

	public static final String CHAT_OTHRES_ID = "othersId";

	public static final String CHAT_OTHRES_NAME = "othersName";

	public static final String GI = "GI_";

	public static final String FORCE_COLSE = "FORCE_COLSE";

	public static final String UNEXPECTED_FINISH = "UNEXPECTED_FINISH";
	public static final int VOICE_MAX_LENGTH = 60; // 最长语音60秒

}
