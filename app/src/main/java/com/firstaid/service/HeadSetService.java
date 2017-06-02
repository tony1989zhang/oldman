package com.firstaid.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.QueryRecords;
import com.firstaid.bean.RecordsBean;
import com.firstaid.impl.TelRingIngImpl;
import com.firstaid.oldman.AlarmHandlerActivity;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.ChooseDeviceModeActivity;
import com.firstaid.oldman.DialogActivity;
import com.firstaid.oldman.LoginActivity;
import com.firstaid.oldman.MainActivity;
import com.firstaid.oldman.MapWeiLanActivity;
import com.firstaid.oldman.R;
import com.firstaid.receiver.MediaButtonReceiver;
import com.firstaid.util.BdSpeehUtils;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.util.Util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/1.
 */
public class HeadSetService extends Service implements TelRingIngImpl {
    public static boolean ISFROMHSS = false;

    public static final String ACTION_HEADSET_MODE_CHOOSE_TIMEOUT = "com.firstaid.oldman.ACTION_HEADSET_MODE_CHOOSE_TIMEOUT";
    private HeadsetPlugReceiver mHeadsetPlugReceiver;
    public static Vibrator systemService;
    private int mCurrentBatterLevel;
    private boolean mWeiLanBaojing = false;
    private boolean mDianLiangAlarmed = false;
    public static boolean[] phonesState;
    public static int position;
    public static MainActivity show;
    private String newPhone = "120";
    private String callPhone = "13350888258";

    static final int frequency = 44100;
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int recBufSize, playBufSize;
    Thread oldManCallPhoneStartSpeeh = null;
    Thread oldManCallPhoneThread = null;
    LatLng tLocatol = null;
//    private ShareUrlSearch mShareUrlSearch = null;


    public static void startsevice(Context context) {
        show = (MainActivity) context;

        Intent in = new Intent(context, MainActivity.class);
        context.startService(in);
    }


    @Override
    public void onCreate() {
        Intent intent = new Intent(getApplicationContext(), HeadSetService.class);
        AlarmManager mAm = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent mPendingIntent = PendingIntent.getService(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        long now = System.currentTimeMillis();
        mAm.setInexactRepeating(AlarmManager.RTC, now, 60000, mPendingIntent);

        super.onCreate();
        registerHeadsetPlugReceiver();
        startLoc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
        }
        if (oldManCallPhoneThread != null && oldManCallPhoneThread.isAlive())
            oldManCallPhoneThread.interrupt();
        if (oldManCallPhoneStartSpeeh != null && oldManCallPhoneStartSpeeh.isAlive())
            oldManCallPhoneStartSpeeh.interrupt();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String cmd = intent.getStringExtra("ext_cmd");
            LogUtil.d("onStartCommand", "onStartCommand:" + cmd);
            if ("modeSeted".equals(cmd)) {
                cancelAlarm();
                handleMode();
            } else if ("lowBatteryChanged".equals(cmd)) {
                int currentDianLiangSeted = (Integer) SPUtil.getInstant(this).get("baojing_diangliang", 15);
                if (mCurrentBatterLevel < currentDianLiangSeted) {
                    lowBatteryAlarm();
                } else {
                    lowBatteryAlarmEnd();
                }
            } else if ("weichan_changed".equals(cmd)) {
                mWeiLanBaojing = false;
            } else if ("mediaBtnClicked".equals(cmd) || "mainActivity".equals(cmd)) {
                handleMediaBtnClicked();
            }
            if (!BuildConfig.hasProduct && null != show) {
                Util.ReceivingCalls(this,show);
            }
        }
        bindNotification();
        App.getInstance().startPhoneService(this);
        return Service.START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    private void bindNotification() {
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification noti = new Notification();
        noti.when = System.currentTimeMillis();
        noti.icon = R.mipmap.ic_launcher;
        noti.tickerText = getString(R.string.app_name) + getString(R.string.running);
        noti.contentIntent = pendingIntent;
        RemoteViews mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.lay_notifycation);
        mRemoteViews.setTextViewText(R.id.notification_title, getString(R.string.app_name) + getString(R.string.running));
        noti.contentView = mRemoteViews;
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        startForeground(12346, noti);
    }

    private void registerHeadsetPlugReceiver() {
        mHeadsetPlugReceiver = new HeadsetPlugReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(ACTION_HEADSET_MODE_CHOOSE_TIMEOUT);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction("com.firstaid.oldman.ACTION_SENT_SMS_ACTION");
        filter.setPriority(2147483647);
        registerReceiver(mHeadsetPlugReceiver, filter);
    }


    public class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        cancelAlarm();
                    } else if (intent.getIntExtra("state", 0) == 1) {
                        Intent chooseModeIntent = new Intent();
                        chooseModeIntent.setClass(context, ChooseDeviceModeActivity.class);
                        chooseModeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(chooseModeIntent);
                        setAlarm();
                    }
                }
            } else if (ACTION_HEADSET_MODE_CHOOSE_TIMEOUT.equals(intent.getAction())) {
                App.getInstance().mIsIntelligent = true;
                handleMode();
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int status = intent.getIntExtra("status", -1);
                int health = intent.getIntExtra("health", -1);
                int level = -1; // percentage, or -1 for unknown
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                if (level == mCurrentBatterLevel) {
                    return;
                }
                mCurrentBatterLevel = level;
                int currentDianLiangSeted = (Integer) SPUtil.getInstant(context).get("baojing_diangliang", 15);
                if (mCurrentBatterLevel < currentDianLiangSeted) {
                    lowBatteryAlarm();
                } else {
                    lowBatteryAlarmEnd();
                }
            } else if ("com.firstaid.oldman.ACTION_SENT_SMS_ACTION".equals(intent.getAction())) {

            }

        }

    }

    ComponentName componentMediaBtn = new ComponentName(App.getInstance().getPackageName(), MediaButtonReceiver.class.getName());

    private void handleMode() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //AudioManager注册一个MediaButton对象
        //只有China_MBReceiver能够接收到了，它是出于栈顶的。
        //不过，在模拟上检测不到这个效果，因为这个广播是我们发送的，流程不是我们在上面介绍的。
        LogUtil.d("cx", "------------handleMode ---------------");
        mAudioManager.setSpeakerphoneOn(true);//话筒外放
        if (App.getInstance().mIsIntelligent) {
            mAudioManager.registerMediaButtonEventReceiver(componentMediaBtn);
        } else {
            try {
                mAudioManager.unregisterMediaButtonEventReceiver(componentMediaBtn);
            } catch (Exception e) {

            }

        }

    }

    /*触发报警电话*/
    private void handleMediaBtnClicked() {
        if (!App.getInstance().mIsIntelligent) {
            return;
        }
        final String phonesStr = SPUtil.getInstant(this).get("relative_phones", "").toString();
        LogUtil.d("cx", "------------" + getString(R.string.baojing_format, mLocaationStr));
        if (TextUtils.isEmpty(phonesStr)) {
            Toast.makeText(HeadSetService.this, R.string.no_relatives, Toast.LENGTH_SHORT).show();
            return;
        }
        if (BuildConfig.hasProduct) {
            return;
        }
//        KeyguardManager km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//        if (!km.inKeyguardRestrictedInputMode()) {
        Intent alarmIntent = new Intent(this, AlarmHandlerActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(alarmIntent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("cx", "cx：" + AlarmHandlerActivity.ISCALL);
                if (AlarmHandlerActivity.ISCALL) {
                    goToSendVoice(phonesStr);
                } else {
                    Toast.makeText(HeadSetService.this, "已经取消报警", Toast.LENGTH_SHORT).show();
                }
            }
        }, 12000);


//        }else{
//            goToSendVoice(phonesStr);
//        }


//        String phoneMsg = "我是" + App.getInstance().getCustName(this) + "手机号码:" + App.getInstance().getMoblieNo(this) + "我现在受伤了需要帮助，在位置" + getString(R.string.baojing_format, mLocaationStr);
//        phoneMsg = phoneMsg + phoneMsg + phoneMsg + phoneMsg;
//        AudioManager audioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);
//        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, audioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL ));

//        systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        systemService.vibrate(new long[]{300, 3000, 300, 3000}, 1);

//        BdSpeehUtils.getInstant(HeadSetService.this).startSpeech(phoneMsg);
//        Intent intent = new Intent();
//        intent.setClass(HeadSetService.this, Dialog2Activity.class);
//        intent.putExtra("ext_title", "紧急事件");
//        intent.putExtra("ext_content","遇见紧急事件，快救我");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        HeadSetService.this.startActivity(intent);
//        Toast.makeText(HeadSetService.this, getString(R.string.baojing_format,mLocaationStr), Toast.LENGTH_SHORT).show();

    }

    //语音报警
    private void goToSendVoice(String phonesStr) {
        if (phonesStr.contains(",")) {
            String[] phones = phonesStr.split(",");
            for (boolean z : phonesState = new boolean[phones.length]) {
                z = false;
            }
            ;
            final String[] phones2 = phones;
            String phoneMsg = "我是" + App.getInstance().getCustName(this) + "手机号码:" + App.getInstance().getMoblieNo(this) + "我现在受伤了需要帮助，在位置" + getString(R.string.baojing_format, mLocaationStr);
            phoneMsg = phoneMsg + phoneMsg + phoneMsg + phoneMsg;
            for (int j = 0; j < phones.length; j++) {
                String sendPhoneStr = "";
//                Util.sendSMS(this, phones[j], getString(R.string.baojing_format, mLocaationStr));//按对应顺序报警 发送短信
                //分享百度地图的位置。
                callPhone = phones[j];
                ShareUrlSearch mShareUrlSearch = ShareUrlSearch.newInstance();
                mShareUrlSearch.setOnGetShareUrlResultListener(new ShareMyLoca());
                mShareUrlSearch
                        .requestLocationShareUrl(new LocationShareURLOption()
                                .location(tLocatol).snippet(mLocaationStr)
                                .name("紧急报警"));

                Toast.makeText(HeadSetService.this, "正在向:" + phones[j] + "报警", Toast.LENGTH_SHORT).show();
                char[] charArray = phones[j].toCharArray();
                if (null != charArray && charArray.length > 0) {
                    for (char tempChar : charArray)
//                        System.out.print(tempChar + ",");
                        sendPhoneStr += tempChar + ",";
                }
                ConnectionManager.getInstance().sendVoice(HeadSetService.this, "" + App.getInstance().getCustId(HeadSetService.this),
                        "我电话号码是," + sendPhoneStr + "," + getString(R.string.baojing_format, mLocaationStr) + getString(R.string.baojing_format, mLocaationStr),
                        phones[j], new ConnectionUtil.OnDataLoadEndListener() {
                            @Override
                            public void OnLoadEnd(String ret) {
                                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                                if (baseBean.success) {
                                    Toast.makeText(HeadSetService.this, "发送语音成功", Toast.LENGTH_SHORT).show();
                                } else {

                                }
                            }
                        });
            }
        } else {
            callPhone = phonesStr;
            ShareUrlSearch mShareUrlSearch = ShareUrlSearch.newInstance();
            mShareUrlSearch.setOnGetShareUrlResultListener(new ShareMyLoca());
            mShareUrlSearch
                    .requestLocationShareUrl(new LocationShareURLOption()
                            .location(tLocatol).snippet(mLocaationStr)
                            .name("紧急报警"));

            String sendPhoneStr = "";
            char[] charArray = phonesStr.toCharArray();
            if (null != charArray && charArray.length > 0) {
                for (char tempChar : charArray)
//                        System.out.print(tempChar + ",");
                    sendPhoneStr += tempChar + ",";
            }
            ConnectionManager.getInstance().sendVoice(HeadSetService.this, "" + App.getInstance().getCustId(HeadSetService.this),
                    "我电话号码是," + sendPhoneStr + "," + getString(R.string.baojing_format, mLocaationStr) + getString(R.string.baojing_format, mLocaationStr),
                    phonesStr, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            if (baseBean.success) {
                                Toast.makeText(HeadSetService.this, "发送短信成功", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                    });
        }
    }


    private void lowBatteryAlarm() {
        // Toast.makeText(HeadSetService.this, "Battery too Low", Toast.LENGTH_SHORT).show();
        if (mDianLiangAlarmed) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, DialogActivity.class);
        intent.putExtra("ext_title", this.getString(R.string.dianliang_alarm_title));
        intent.putExtra("ext_content", this.getString(R.string.dianliang_alarm_content));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        mDianLiangAlarmed = true;
    }

    private void lowBatteryAlarmEnd() {
        mDianLiangAlarmed = false;
        // Toast.makeText(HeadSetService.this, "Battery too HIGH", Toast.LENGTH_SHORT).show();
    }

    PendingIntent pIntent;

    private void setAlarm() {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_HEADSET_MODE_CHOOSE_TIMEOUT);
        pIntent = PendingIntent.getBroadcast(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.ELAPSED_REALTIME, 8 * 1000, pIntent);
    }

    private void cancelAlarm() {
        if (pIntent != null) {
            AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pIntent);
        }
    }


    private LocationClient mLocClient;
    private MyLocationListenner mMyLocationListenner = new MyLocationListenner();

    private void startLoc() {
        mLocClient = new LocationClient(App.getInstance().getApplicationContext());
        mLocClient.registerLocationListener(mMyLocationListenner);
        initLocation();
        mLocClient.start();
    }

    private void initLocation() {
        final LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000 * 30);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setAddrType("all");//返回的定位结果包含地址信息
        ArrayList<Object> arrayList = new ArrayList<>();
        mLocClient.setLocOption(option);
    }

    private String mLocaationStr = "";
    private long mLastLocationUpload;
    private static final long UPLOAD_LOCATION_BETWEEN = 5 * 60 * 1000;

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                //gps定位成功
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                //网络定位成功
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                //离线定位成功，离线定位结果也是有效的
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                return;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //网络不同导致定位失败，请检查网络是否通畅
                return;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                return;
            }

            checkWeiLan(new LatLng(location.getLatitude(), location.getLongitude()));
//            if(!BuildConfig.hasProduct) {
            if (System.currentTimeMillis() - mLastLocationUpload > UPLOAD_LOCATION_BETWEEN) {
                mLastLocationUpload = System.currentTimeMillis();
                if (App.getInstance().getCustId(HeadSetService.this) > 0) {
                    ConnectionManager.getInstance().uploadLocation(HeadSetService.this, "" + App.getInstance().getCustId(HeadSetService.this), "" + location.getLatitude(), "" + location.getLongitude(), new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            LogUtil.d("uploadLocation", "uploadLocation:" + ret);
                        }
                    });
                    tLocatol = new LatLng(location.getLatitude(), location.getLongitude());
                    mLocaationStr = location.getAddrStr();// + "," + getString(R.string.zuobiao) + ":" + location.getLatitude() + "," + location.getLongitude();  //当前定位的位置
                }
            }
//            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }

        private void checkWeiLan(LatLng currentLocation) {
            double latiCenter = Double.parseDouble(SPUtil.getInstant(HeadSetService.this).get("weilan_center_lat", "0").toString());
            double longCenter = Double.parseDouble(SPUtil.getInstant(HeadSetService.this).get("weilan_center_long", "0").toString());
            if (latiCenter == 0 || longCenter == 0) {
                //not seted
                return;
            }
            double distance = DistanceUtil.getDistance(currentLocation, new LatLng(latiCenter, longCenter));
            if (distance > MapWeiLanActivity.RADIUS) {
                if (!mWeiLanBaojing) {
                    mWeiLanBaojing = true;
                    Intent intent = new Intent();
                    intent.setClass(HeadSetService.this, DialogActivity.class);
                    intent.putExtra("ext_title", HeadSetService.this.getString(R.string.weilan_alarm_title));
                    intent.putExtra("ext_content", HeadSetService.this.getString(R.string.weilan_alarm_content));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    HeadSetService.this.startActivity(intent);
                }
            }
        }
    }

    @Override
    public void startRingIngDoing(String callMobileNo) {
        //AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        // audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式
        if (!BuildConfig.hasProduct) {
            if (null != oldManCallPhoneStartSpeeh && oldManCallPhoneStartSpeeh.isAlive()) {
                oldManCallPhoneStartSpeeh.interrupt();
            }
            oldManCallPhoneStartSpeeh = new Thread(new Runnable() {
                @Override
                public void run() {
                    deleteAndAddRecords();
                }
            });
            if (!oldManCallPhoneStartSpeeh.isAlive()) {
                oldManCallPhoneStartSpeeh.start();
            }

            ISFROMHSS = false;
        } else {
            queryAndDeleteRecords(callMobileNo);
        }
    }

    private void queryAndDeleteRecords(String callMobileNo) {
        final String callMobileNo2 = callMobileNo;
        //调用服务端接口
        ConnectionManager.getInstance().queryRecords(HeadSetService.this, "" + App.getInstance().getMoblieNo(HeadSetService.this), callMobileNo, null, null, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                QueryRecords queryRecords = App.getInstance().getBeanFromJson(ret, QueryRecords.class);
                if (queryRecords.success == true && queryRecords.data != null && queryRecords.data.size() > 0) {
                    Log.e("queryRecords", "queryRecords: 可以正常运行" + ret);
                    RecordsBean recordsBean = queryRecords.data.get(queryRecords.data.size() - 1);
                    BdSpeehUtils.getInstant(HeadSetService.this).startSpeech("" + recordsBean.objectKey);
                    ConnectionManager.getInstance().deleteRecords(HeadSetService.this, "" + App.getInstance().getMoblieNo(HeadSetService.this), "" + callMobileNo2, null, null, new ConnectionUtil.OnDataLoadEndListener() {

                        @Override
                        public void OnLoadEnd(String ret) {
                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            if (baseBean.success) {

                            }
                        }
                    });
                }
            }
        });
    }

    private void deleteAndAddRecords() {
        //运行之前删除之前全部录音
        ConnectionManager.getInstance().deleteRecords(this, "" + newPhone, "" + App.getInstance().getMoblieNo(HeadSetService.this), null, null, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                if (baseBean.success) {
                    //"我是"+ App.getInstance().getCustName(this) +"手机号码:" +App.getInstance().getMoblieNo(this)+
                    final String sendVoiceStr = "我现在受伤了需要帮助，在位置" + getString(R.string.baojing_format, mLocaationStr);
                    Log.e("sendVoiceStr", "sendVoiceStr:" + sendVoiceStr);
                    //保存地址
                    ConnectionManager.getInstance().addRecords(HeadSetService.this, "" + newPhone, "" + App.getInstance().getMoblieNo(HeadSetService.this), sendVoiceStr, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            if (bean.success == true) {
                                //成功以后本机不再播放语音
                                BdSpeehUtils.getInstant(HeadSetService.this).startSpeech(sendVoiceStr + sendVoiceStr + sendVoiceStr + sendVoiceStr);
                            } else {
                                Toast.makeText(HeadSetService.this, "" + bean.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                }
            }
        });
    }

    @Override
    public void stopRingIngDoing() {
        BdSpeehUtils.getInstant(this).stopSpeech();
        if (null != oldManCallPhoneStartSpeeh && oldManCallPhoneStartSpeeh.isAlive())
            oldManCallPhoneStartSpeeh.interrupt();
    }


    class ShareMyLoca implements OnGetShareUrlResultListener {

        @Override
        public void onGetPoiDetailShareUrlResult(ShareUrlResult shareUrlResult) {
//            mLocaationStr += "网址:" + shareUrlResult.getUrl();
//            Util.sendSMS(HeadSetService.this, "15859254561", getString(R.string.baojing_format, mLocaationStr) );//只有一个号码拨打该号码 发送短信
            // 分享短串结果
        }

        @Override
        public void onGetLocationShareUrlResult(ShareUrlResult shareUrlResult) {
            String sendStr = mLocaationStr + "网址:" + shareUrlResult.getUrl();
            Util.sendSMS(HeadSetService.this, callPhone, getString(R.string.baojing_format, sendStr));//只有一个号码拨打该号码 发送短信
            // 分享短串结果
//            Intent it = new Intent(Intent.ACTION_SEND);
//            it.putExtra(Intent.EXTRA_TEXT, mLocaationStr
//                    + " -- " + shareUrlResult.getUrl());
//            it.setType("text/plain");
//            startActivity(Intent.createChooser(it, "将短串分享到"));
        }

        @Override
        public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {
//            mLocaationStr += "网址:" + shareUrlResult.getUrl();
//            Util.sendSMS(HeadSetService.this, "15859254561", getString(R.string.baojing_format, mLocaationStr) + " -- " + shareUrlResult.getUrl());//只有一个号码拨打该号码 发送短信
            // 分享短串结果
//            Intent it = new Intent(Intent.ACTION_SEND);
//            it.putExtra(Intent.EXTRA_TEXT, mLocaationStr
//                    + " -- " + shareUrlResult.getUrl());
//            it.setType("text/plain");
//            startActivity(Intent.createChooser(it, "将短串分享到"));
        }
    }

}
