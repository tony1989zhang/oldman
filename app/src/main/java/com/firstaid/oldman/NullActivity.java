package com.firstaid.oldman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.firstaid.service.HeadSetService;

public class NullActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null);
        findViewById(R.id.nullclick).setOnClickListener(this);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        // 自己的代码
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nullclick:
                finish();
                break;
        }
    }


}
