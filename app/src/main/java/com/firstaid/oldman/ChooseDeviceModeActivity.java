package com.firstaid.oldman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.firstaid.service.HeadSetService;
import com.firstaid.view.TitleView;

/**
 * Created by Administrator on 2016/1/1.
 */
public class ChooseDeviceModeActivity extends BaseActivity implements View.OnClickListener {

    private TitleView mTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_device);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(mTitleView,this.getResources().getColor(R.color.title_bg_color));
        mTitleView.setTitle(R.string.choose_mode_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        this.findViewById(R.id.mode_intelligent).setOnClickListener(this);
        this.findViewById(R.id.mode_headset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_back:{
                this.finish();
                break;
            }
            case R.id.mode_intelligent:{
                //设置听筒外放
//                AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setMode(AudioManager.MODE_IN_CALL);
//                audioManager.setWiredHeadsetOn(false);
//                audioManager.setSpeakerphoneOn(true);
//                setSpeekModle(true);
//                answerRingingCall(this);


                App.getInstance().mIsIntelligent = true;
                Intent intent = new Intent(this,HeadSetService.class);
                intent.putExtra("ext_cmd", "modeSeted");
                this.startService(intent);





//                    MediaRecorder mRecorder = new MediaRecorder(); // Create MediaRecorder

                    // Set audio and video source and encoder
                    // 这两项需要放在setOutputFormat之前
//                    mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                    // Set output file format
//                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                    // 这两项需要放在setOutputFormat之后
//                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);

//                    mRecorder.setVideoSize(320, 240);
//                    mRecorder.setVideoFrameRate(20);
//                    mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());















                    finish();
                break;
            }
            case R.id.mode_headset:{
                App.getInstance().mIsIntelligent = false;
                Intent intent = new Intent(this,HeadSetService.class);
                intent.putExtra("ext_cmd","modeSeted");
                this.startService(intent);
                finish();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerHeadsetPlugReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHeadsetPlugReceiver != null){
            this.unregisterReceiver(mHeadsetPlugReceiver);
        }
    }

    HeadsetPlugReceiver mHeadsetPlugReceiver;
    private void registerHeadsetPlugReceiver(){
        mHeadsetPlugReceiver  = new HeadsetPlugReceiver ();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(HeadSetService.ACTION_HEADSET_MODE_CHOOSE_TIMEOUT);
        registerReceiver(mHeadsetPlugReceiver, filter);
    }

    public class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())){
                if(intent.hasExtra("state")){
                    if(intent.getIntExtra("state", 0)==0){
                        finish();
                    }
                }
            } else if(HeadSetService.ACTION_HEADSET_MODE_CHOOSE_TIMEOUT.equals(intent.getAction())){
                Toast.makeText(context,R.string.mode_seted_by_timeout, Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }


    //解决取消耳机模式
    public synchronized void answerRingingCall(Context context){
        //据说该方法只能用于Android2.3及2.3以上的版本上，但本人在2.2上测试可以使用
        try {
            Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent1.putExtra("state", 1);
            localIntent1.putExtra("microphone", 1);
            localIntent1.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");
            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
            context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
            Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
            context.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
            Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent4.putExtra("state", 0);
            localIntent4.putExtra("microphone", 1);
            localIntent4.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
        }
        catch (Exception e){
            e.printStackTrace();
            Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT,keyEvent);
            context.sendOrderedBroadcast(meidaButtonIntent, null);
        }
    }

    void setSpeekModle(boolean open){
        //audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
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

}
