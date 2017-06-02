package com.firstaid.oldman;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;

/**
 * Created by lenovo on 2016/1/14.
 */
public class DialogActivity extends BaseActivity {

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
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        mDialog.show();
    }
}
