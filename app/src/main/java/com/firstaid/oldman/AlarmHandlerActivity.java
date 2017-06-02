package com.firstaid.oldman;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmHandlerActivity extends Activity {
	private int blueColor, whiteColor, transparent;
	private Vibrator vibrator;
	private SoundPool soundPool;
	private int soundID;
	private MyCountDownTimer mc;
	public static boolean ISCALL = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_handler);
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		// 自己的代码
		initUI();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn()) {
			PowerManager.WakeLock wl = pm
					.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
			wl.acquire();
			wl.release();
		}
	}

	private BaoJingThread baoJingThread;
	private Button wbBtn;

	private void initUI() {

		/**
		 * 使用扬声器进行外放
		 */
		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMicrophoneMute(true);
		audioManager.setSpeakerphoneOn(true);//使用扬声器外放，即使已经插入耳机
		setVolumeControlStream(AudioManager.STREAM_MUSIC);//控制声音的大小
		audioManager.setMode(AudioManager.STREAM_MUSIC);


		mc = new MyCountDownTimer(10000, 1000);
		blueColor = getResources().getColor(R.color.bluetext);
		whiteColor = getResources().getColor(android.R.color.white);
		transparent = getResources().getColor(android.R.color.transparent);
		wbBtn = (Button) findViewById(R.id.btn_wb);
		wbBtn.setOnClickListener(new WBOnClickListener());
		if (baoJingThread == null)
			baoJingThread = new BaoJingThread();

		if (!baoJingThread.isAlive())
			baoJingThread.start();

		mc.start();
	}

	class WBOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (soundPool != null) {
				soundPool.pause(soundID);
				soundPool.stop(soundID);
			}
			if (vibrator != null) {
				vibrator.cancel();
			}

			if (mc != null) {
				mc.cancel();
			}
			if (baoJingThread != null && baoJingThread.isAlive()) {
				baoJingThread.interrupt();
			}
			wbBtn.setEnabled(false);
			ISCALL = false;
			finish();
		}

	}

	class BaoJingThread extends Thread {

		@Override
		public void run() {
			super.run();

			vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
			soundID = soundPool.load(AlarmHandlerActivity.this, R.raw.alertmp3, 0);
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
					Log.e("onLoadComplete", "onLoadComplete:");
					soundPool.play(soundID, 5, 5, 0, 20, (float) 1);
				}
			});
			vibrator.vibrate(new long[] { 100, 5000, 100, 5000 }, 1);// [静止时长，震动时长，静止时长，震动时长
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (soundPool != null) {
			soundPool.pause(soundID);
			soundPool.stop(soundID);
		}
		if (vibrator != null) {
			vibrator.cancel();
		}

		mc.cancel();
		if (baoJingThread != null && baoJingThread.isAlive()) {
			baoJingThread.interrupt();
		}

	}

	/**
	 * 继承 CountDownTimer 防范
	 * 
	 * 重写 父类的方法 onTick() 、 onFinish()
	 */

	class MyCountDownTimer extends CountDownTimer {
		/**
		 * 
		 * @param millisInFuture
		 *            表示以毫秒为单位 倒计时的总数
		 * 
		 *            例如 millisInFuture=1000 表示1秒
		 * 
		 * @param countDownInterval
		 *            表示 间隔 多少微秒 调用一次 onTick 方法
		 * 
		 *            例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
		 * 
		 */
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			wbBtn.setText("已报警，关闭该页面");
			wbBtn.setEnabled(true);
			ISCALL = true;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			Log.i("MainActivity", millisUntilFinished + "");
			wbBtn.setEnabled(true);
			wbBtn.setText("取消报警(" + millisUntilFinished / 1000 + ")");
			ISCALL = false;
		}
	}
}
