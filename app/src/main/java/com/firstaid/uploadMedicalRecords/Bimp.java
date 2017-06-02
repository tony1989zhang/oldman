package com.firstaid.uploadMedicalRecords;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
//	public static List<Bitmap> bmp = new ArrayList<Bitmap>();
	public static List<Bitmap> bigBmp = new ArrayList<Bitmap>();
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();
	
	public static List<String> videoIds = new ArrayList<String>();
	

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	
	//  缩略图按比例大小压缩方法
		public static void getThumbImage(String srcPath,String picName,String filePath) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
			newOpts.inJustDecodeBounds = false;
			int oldWidth = newOpts.outWidth;
			int oldHeight = newOpts.outHeight;
			int newWidth = 0, newHeight = 0;
			int hh = 200;// 这里设置高度为200f
			int ww = 200;// 这里设置宽度为200f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可，最大的一边为200,另一边按比例缩放
			int be = 1;// be=1表示不缩放
			if (oldWidth > oldHeight && oldWidth > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = oldWidth / ww;
				newWidth = ww;
				newHeight = oldHeight / be;
			} else if (oldWidth < oldHeight && oldHeight > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = oldHeight / hh;
				newHeight = hh;
				newWidth = oldWidth / be;
			}else{
				newWidth = oldWidth;
				newHeight = oldHeight;
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
					newHeight, true);
			resizedBitmap = BitmapUtil.getRotationBitmap(srcPath, resizedBitmap);
			saveBitmap(resizedBitmap, new File(filePath,picName));
			if(null != bitmap){
				bitmap.recycle();
			}
			if(null != resizedBitmap){
				resizedBitmap.recycle();
			}
			System.gc();
//			return resizedBitmap;// 压缩好比例大小后再进行质量压缩
		}
		
		public static boolean saveBitmap(Bitmap bitmap, File file) {
			return saveBitmap(bitmap, file, 50);
		}

		public static boolean saveBitmap(Bitmap bitmap, File file, int quality) {
			try {
				bitmap.compress(CompressFormat.JPEG, quality, new FileOutputStream(
						file));
				return true;
			} catch (Exception e) {
				return false;
			} finally {
				System.gc();
			}
		}
}
