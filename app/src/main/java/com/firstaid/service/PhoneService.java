package com.firstaid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.firstaid.util.LogUtil;

public class PhoneService extends Service {

    private MyPhoneStateListener mMyPhoneStateListener;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        listenPhoneState();

        return super.onStartCommand(intent, flags, startId);
    }


    private void listenPhoneState(){
        if(mMyPhoneStateListener == null){
            mMyPhoneStateListener = new MyPhoneStateListener();
            TelephonyManager tm = (TelephonyManager)this.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(mMyPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }


    }

    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    android.util.Log.d("cx","-----------------CALL_STATE_IDLE " + incomingNumber);
                    if(mHandler.hasMessages(MSG_SET_SPEAKER_ON)){
                        mHandler.removeMessages(MSG_SET_SPEAKER_ON);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    android.util.Log.d("cx","----------------CALL_STATE_OFFHOOK " + incomingNumber);
                    if("10086".equals(incomingNumber)){
                        startAutoAlarmAudio();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //incoming call maybe a follow up patient call back.
                    android.util.Log.d("cx","-----------------CALL_STATE_RINGING " + incomingNumber);
                    // 输出来电号码
                    break;
            }
        }
    }

    boolean isSamePhoneNu(String phone1,String phone2){
        return phone1.equals(phone2);
    }

    private void startAutoAlarmAudio(){
        AudioManager audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//        adjustVolume();
        setSpeekModle(audioMgr,true);
    }

    private void adjustVolume(AudioManager audioMgr){
        int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        audioMgr.setStreamVolume(AudioManager.STREAM_VOICE_CALL,maxVolume,AudioManager.FLAG_VIBRATE);
        maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,maxVolume,AudioManager.FLAG_VIBRATE);
    }
    void setSpeekModle(AudioManager audioManager,boolean open){
        audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        if(!audioManager.isSpeakerphoneOn()&&true==open) {
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(true);
            LogUtil.d("cx", "--------------------------setSpeakerphoneOn-------" + audioManager.isSpeakerphoneOn());
            if(!audioManager.isSpeakerphoneOn()){
                mHandler.sendEmptyMessageDelayed(MSG_SET_SPEAKER_ON,500);
            }
        }else if(audioManager.isSpeakerphoneOn()&&false==open){
            LogUtil.d("cx","--------------------------setSpeakerphoneOFF==========------");
            audioManager.setSpeakerphoneOn(false);
        }
    }

    private static final int MSG_SET_SPEAKER_ON = 1;
    private Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SET_SPEAKER_ON:{
                    AudioManager audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    if(!audioMgr.isSpeakerphoneOn()) {
                        audioMgr.setSpeakerphoneOn(true);
                        LogUtil.d("cx", "-=======--setSpeakerphoneOn-------" + audioMgr.isSpeakerphoneOn());
                        if(!audioMgr.isSpeakerphoneOn()){
                            mHandler.sendEmptyMessageDelayed(MSG_SET_SPEAKER_ON,500);
                        }
                    }else
                        break;
                }
            }
        }
    };
}


