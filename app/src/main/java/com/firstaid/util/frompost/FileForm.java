package com.firstaid.util.frompost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by moon.zhong on 2015/2/28.
 */
public class FileForm {

    private String value ;
    private String name ;
    private int id ;
    private Context mContext ;

    public FileForm(Context context ,String name, String value, int id) {
        this.name = name;
        this.value = value;
        this.id = id ;
        mContext = context ;
    }

    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources() ,id) ;
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bos) ;
        return bos.toByteArray() ;
    }


    public String getName() {
        return name;
    }

    public String getFileName() {
//        String name = fileName.substring(fileName.lastIndexOf("/"),fileName.length()) ;
        return "logo.png";
    }

    public String getMineType() {
        return "image/png";
    }

}
