package com.firstaid.oldman;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.firstaid.service.HeadSetService;
import com.firstaid.service.PhoneService;
import com.firstaid.uploadMedicalRecords.Constant;
import com.firstaid.util.ImageFileCacheUtils;
import com.firstaid.util.SPUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.MD5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.update.UpdateConfig;
import com.uuxia.crashhander.CrashHandler;
import com.uuxia.crashhander.Email;
import com.youzan.sdk.YouzanSDK;

import java.io.File;

/**
 * Created by Administrator on 2016/1/1.
 */
public class App extends Application{

    public static final String DATA_DIR = Environment.getExternalStorageDirectory() + File.separator + ImageFileCacheUtils.CHCHEDIR + File.separator;

    private static App mApp;


    /**屏幕高度*/
    public int mScreenHeight;
    /**屏幕宽度*/
    public int mScreenWidth;
    /**状态栏高度*/
    public int mTintInsertTop;
    /**状态栏+ActionBar高度*/
    public int mTintInsertTopWithActionBar;

    public boolean mIsIntelligent = false;
    public boolean mIsHeadSetPluged = false;
    public static App getInstance(){
        if(mApp == null){
            mApp = new App();
        }
        return mApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initShares();
        com.umeng.fb.util.Res.setPackageName("com.firstaid.oldman");
//        com.umeng.fb.util.Res.setPackageName("com.firstaid.relative");
        mApp = this;
        initImageLoader();
        /**
         * 初始化SDK
         * @param Context application Context
         * @param userAgent 用户代理 UA
         */
//        if(BuildConfig.hasProduct)
            YouzanSDK.init(this, "89101360d955b5446f1455871103314");

        /**
         * 俘获app异常，发送到我的邮箱
         * */
        CrashHandler crashHandler = CrashHandler.getInstance();
        Email email = Email.create("smtp.126.com", "981601917@qq.com",
                "zhangcanneng@126.com", "19891029zhang");
        crashHandler.setEmail(email);
        crashHandler.init(getApplicationContext());
        UpdateConfig.setDebug(true);
    }

    private void initShares(){
        //微信 appid appsecret 老人端
//        PlatformConfig.setWeixin("wxb3e6cc289951e8ca", "c4653b2f5e8f792009f305b75fc1982f");
        //微信亲属端
        PlatformConfig.setWeixin("wx0d9ef1c9b049bc48", "3fedfd57850c890e73434e05145c916c");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("337590324", "6754ceb10763406e3be0a9a12406698e");

        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    public <T>T getBeanFromJson(String ret,Class<T> c){
        T bean = null;
        try{
            bean = new Gson().fromJson(ret,c);
        } catch (Exception e) {
            //format json error
        }
        if(bean == null){
            try {
                bean = c.newInstance();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bean;
    }


    public void registHeadSet(Context context){
        Intent intent = new Intent();
        intent.setClass(context, HeadSetService.class);
        context.startService(intent);
    }

    public int getCustId(Context context){
        return (Integer)SPUtil.getInstant(context).get("custId",0);
    }

    public String getMoblieNo(Context context){
        return (String)SPUtil.getInstant(context).get("mobileNo","");
    }
    public String getCustName(Context context){
        return (String)SPUtil.getInstant(context).get("custName","");
    }

    public void startPhoneService(Context context){
        Intent intent = new Intent();
        intent.setClass(context, PhoneService.class);
        //context.startService(intent);
    }



    public void doShare(Activity activity,String title,String content,String imgUrl,String targetUrl){
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,SHARE_MEDIA.QQ
                };
        if(TextUtils.isEmpty(imgUrl)||imgUrl.equals("null")){
            imgUrl = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1458369217&di=38ee9d1f514253d77d1ba086f904efa2&src=http://www.chinadaily.com.cn/dfpd/retu/attachement/jpg/site1/20110527/0013729ed1480f49b48c0f.jpg";
            new ShareAction(activity).setDisplayList( displaylist )
                    .withText(content)
                    .withTitle(title)
                    .withTargetUrl(targetUrl)
                             .withMedia(new UMImage(activity, imgUrl))
                            // .setListenerList(umShareListener,umShareListener)
                            // .setShareboardclickCallback(shareBoardlistener)
                    .open();
        } else {
            new ShareAction(activity).setDisplayList( displaylist )
                    .withText(content )
                    .withTitle(title)
                    .withTargetUrl(targetUrl)
                     .withMedia(new UMImage(activity,imgUrl))
                            // .setListenerList(umShareListener,umShareListener)
                            // .setShareboardclickCallback(shareBoardlistener)
                    .open();
        }

    }
    /**
     * 初始化 ImageLoader
     */
    public void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2).memoryCacheExtraOptions(480, 800)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache()).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCache(new UnlimitedDiscCache(new File(Constant.CACHE_DIR), new MD5FileNameGenerator())).denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
                .imageDownloader(new BaseImageDownloader(mApp)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                        // .memoryCache(new WeakMemoryCache())
                .threadPoolSize(3).build();
        ImageLoader.getInstance().init(config);
    }
}
