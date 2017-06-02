package com.firstaid.util;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by lenovo on 2016/1/5.
 */
public class ImageFileCache implements ImageLoader.ImageCache {


    private static ImageFileCache mImageFileCache;
    private ImageFileCache(){

    }

    public static ImageFileCache getInstance(){
        if(mImageFileCache == null){
            mImageFileCache = new ImageFileCache();
        }
        return mImageFileCache;
    }

    @Override
    public Bitmap getBitmap(String url) {
        final Bitmap bt = LruImageCache.instance().getBitmap(url);
        if(bt != null){
            return bt;
        }
        return ImageFileCacheUtils.getInstance().getImage(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        LruImageCache.instance().putBitmap(url,bitmap);
        ImageFileCacheUtils.getInstance().saveBitmap(bitmap, url);
    }

}
