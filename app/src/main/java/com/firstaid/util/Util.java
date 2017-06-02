package com.firstaid.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.firstaid.impl.TelRingIngImpl;
import com.firstaid.service.HeadSetService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import java.net.HttpURLConnection;





/**
 * Created by lenovo on 2016/1/5.
 */
public class Util {

    private static final int REQUEST_CODE_SCAN = 0x0000; // 扫描二维码返回值
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int IMAGE_HALFWIDTH = 40;// 二维码 宽度值，影响中间图片大小


    public static boolean OFFHOOK = false;

    //    show,HeadSetService.this,phones,this
    public static void calls(Activity activity, Context context, String[] phones, TelRingIngImpl telRingIngImple) {

    }

    public static void call(Context context, String phone) {
        call(null, context, phone, null);
    }

    /**
     * 传单个电话号码
     */
    public static void call(Activity activity, Context context, String phone, TelRingIngImpl telRingIngImple) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            if (null != telRingIngImple && null != activity) {

                Util.getTelState(telRingIngImple, activity);
            }
        } catch (ActivityNotFoundException e) {

        }
    }

    public static void call120(Context context) {
        call(context, "120");
    }

    public static void saveToSd(String text) {
        try {
            File file = new File("/sdcard/" + File.separator + "121.log");

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            pw.write(text);
            pw.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSMS(Context context, String phoneNumber, String message) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        String SENT_SMS_ACTION = "com.firstaid.oldman.ACTION_SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,
                0);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, null);
        }
    }


    public static void getTelState(final TelRingIngImpl tr, final Activity activity) {
        // 取得电话服务
        TelephonyManager telManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话的状态
        PhoneStateListener listener = new PhoneStateListener() {
            int callState = 0;

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                Log.e("去电状态", "去电状态:" + state);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
                        Log.e("拨打电话", "电话状态:无任何状态");
//                        if(HeadSetService.ISFROMHSS) {
                        tr.stopRingIngDoing();
                        if (callState > 0) {
                            getTime(activity, callState);
                        }
                        OFFHOOK = false;
//                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                        callState++;
                        Log.e("拨打电话", "电话状态:接起电话");
                        if (HeadSetService.ISFROMHSS) {
                            tr.startRingIngDoing(null);
                            OFFHOOK = true;
                        }
                        break;
//                    ReadLog.CallViewState.FORE_GROUND_CALL_S
                    case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
                        Log.e("拨打电话", "电话状态:电话进来时");
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }

        };
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    public static void getTime(Activity activity, int callState) {
        if (callState >= 1) {
            ContentResolver resolver = activity.getContentResolver();
            try {
                Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
                        new String[]{CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE, CallLog.Calls.DATE, CallLog.Calls.DURATION},
                        null, null, CallLog.Calls.DEFAULT_SORT_ORDER);


                Log.e("moveToPosition:","moveToPosition:" +  cursor.moveToPosition(0));
//                long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));
                if (cursor.moveToPosition(0)) {
                    long duration = cursor.getLong(4);
                    Log.e("duration", "duration:" + duration);
                    if (duration > 0) {
                        Log.e("duration", "duration:" + "有人接听电话");
                        if (null != HeadSetService.phonesState && HeadSetService.phonesState.length != 0) {
                            for (int i = 0; i < HeadSetService.phonesState.length; i++) {
                                if (i <= HeadSetService.position) {
                                } else {
                                    HeadSetService.phonesState[i] = true;
                                }
                            }
                        }
                    } else {
                        Log.e("电话无人接听", "电话无人接听");
                        Toast.makeText(activity, "无人接听电话", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (SecurityException e) {
            }
        }

    }

    //接听电话
    public static void ReceivingCalls(final TelRingIngImpl tr, final Activity activity) {
        // 取得电话服务
        TelephonyManager telManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话的状态
        PhoneStateListener listener = new PhoneStateListener() {
            int callState = 0;

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
                        Log.e("拨打电话", "电话状态:无任何状态");
                        answerRingingCall(activity);
//                        setSpeekModle(true,activity);
//                        tr.stopRingIngDoing();
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                        Log.e("拨打电话", "电话状态:接起电话"); //判断来电是否对应手机号9————播放录音
//                        tr.startRingIngDoing(incomingNumber);
                        answerRingingCall(activity);
//                        setSpeekModle(true, activity);
                        break;
//                    ReadLog.CallViewState.FORE_GROUND_CALL_S
                    case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
                        Log.e("拨打电话", "电话状态:电话进来时");

                        answerRingingCall(activity);
//                        setSpeekModle(tru be,activity);
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }

        };
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

    }
    // 生成二维码 带logo 形式的
    public static Bitmap createQRCode(String string, Bitmap mBitmap, BarcodeFormat format) throws WriterException {
        if(format == null)
            format = BarcodeFormat.QR_CODE;
        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);// 设置缩放信息
        // 将logo图片按martix设置的信息缩放
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hst = new Hashtable();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");// 设置字符编码
        BitMatrix matrix = writer.encode(string, format, 400, 400, hst);// 生成二维码矩阵信息
        int width = matrix.getWidth();// 矩阵高度
        int height = matrix.getHeight();// 矩阵宽度
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];// 定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
        for (int y = 0; y < height; y++) {// 从行开始迭代矩阵
            for (int x = 0; x < width; x++) {// 迭代列
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {// 该位置用于存放图片信息
                    // 记录图片每个像素信息
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {// 如果有黑块点，记录信息
                        pixels[y * width + x] = 0xff000000;// 记录黑块信息
                    }
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    // 生成二维码
    public static Bitmap createQRCode(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = CodeCreator.createQRCode(url);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }
    //接听电话
    public static void ReceivingCalls2( final Activity activity) {
        // 取得电话服务
        TelephonyManager telManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话的状态
        PhoneStateListener listener = new PhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
                        Log.e("拨打电话", "电话状态:无任何状态");
                        answerRingingCall(activity);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                        Log.e("拨打电话", "电话状态:接起电话"); //判断来电是否对应手机号9————播放录音
                        answerRingingCall(activity);
                        break;
//                    ReadLog.CallViewState.FORE_GROUND_CALL_S
                    case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
                        Log.e("拨打电话", "电话状态:电话进来时");
                        answerRingingCall(activity);
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }

        };
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }





   public static void setSpeekModle(boolean open,Context context){
        //audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);

        if(!audioManager.isSpeakerphoneOn()&&true==open) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }else if(audioManager.isSpeakerphoneOn()&&false==open){
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,currVolume,
                    AudioManager.STREAM_VOICE_CALL);
        }
    }

    public synchronized static  void answerRingingCall(Activity activity){
            //据说该方法只能用于Android2.3及2.3以上的版本上，但本人在2.2上测试可以使用
            try {
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent1.putExtra("state", 1);
                localIntent1.putExtra("microphone", 1);
                localIntent1.putExtra("name", "Headset");
                activity.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");
                Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
                activity.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
                Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
                activity.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
                Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                localIntent4.putExtra("state", 0);
                localIntent4.putExtra("microphone", 1);
                localIntent4.putExtra("name", "Headset");
                activity.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
            }
            catch (Exception e){
                e.printStackTrace();
                Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
                meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
                activity.sendOrderedBroadcast(meidaButtonIntent, null);
            }
    }
}
