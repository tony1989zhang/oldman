package com.firstaid.oldman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;

import com.firstaid.impl.TelRingIngImpl;
import com.firstaid.service.HeadSetService;
import com.firstaid.util.BdSpeehUtils;

/**
 * Created by lenovo on 2016/1/14.
 */
public class Dialog2Activity extends BaseActivity{
    private TelRingIngImpl telRingIngImple;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = this.getIntent().getStringExtra("ext_title");
        String content = this.getIntent().getStringExtra("ext_content");
        mDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();;
                    }
                })
                .setPositiveButton("取消报警", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BdSpeehUtils.getInstant(Dialog2Activity.this).stopSpeech();
                        if(null != HeadSetService.systemService){
                            HeadSetService.systemService.cancel();
                        }
                    }
                })
                .create();
        mDialog.show();
    }

}
