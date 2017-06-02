package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firstaid.util.Util;
import com.google.zxing.WriterException;

public class CodeCreatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_creator);
        ImageView codeCreatorIV = (ImageView) findViewById(R.id.iv_codeCreator);

        Bitmap qrCode = Util.createQRCode("http://7.77-7.com/x.htm");
        codeCreatorIV.setImageBitmap(qrCode);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        try {
//            Bitmap qrCode = Util.createQRCode("http://7.77-7.com/", bitmap, null);
//            codeCreatorIV.setImageBitmap(qrCode);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }

    }
}
