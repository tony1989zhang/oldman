package com.firstaid.uploadMedicalRecords;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore.Images;

/**
 * 图片工具类
 */
public class BitmapUtil {
	private static int THUMBNAIL_SIZE = 140;
	private static int NOMARL_SIZE = 800;
	/**
	 * 压缩图片
	 */
	public static Bitmap compressionAndSavePhoto(Bitmap bitmap, File file) {

		bitmap = getResizedBitmap(bitmap);
		
		try {
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 获取缩略图(100px*100px)
	 */
	public static Bitmap getThumbnail(Bitmap bitmap, String fileName) {
		return getThumbnail(bitmap, fileName, 80);
	}
	public static Bitmap getThumbnail(Bitmap bitmap, String fileName,int quality) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int tw;
		int th;
		if (w < THUMBNAIL_SIZE || h < THUMBNAIL_SIZE) {
			return bitmap;
		}
		if (w >= h) {
			th = THUMBNAIL_SIZE;
			tw = w * THUMBNAIL_SIZE / h;
		} else {
			th = h * THUMBNAIL_SIZE / w;
			tw = THUMBNAIL_SIZE;
		}
		if (tw > THUMBNAIL_SIZE * 3) {
			tw = THUMBNAIL_SIZE * 3;
		}
		if (th > THUMBNAIL_SIZE * 3) {
			th = THUMBNAIL_SIZE * 3;
		}
		Bitmap tb =ThumbnailUtils.extractThumbnail(bitmap, tw, th);
	//	Bitmap tb = Bitmap.createScaledBitmap(bitmap, tw, th, true);
		File file = new File(Constant.CACHE_DIR + "/" + fileName);
		try {
			tb.compress(CompressFormat.JPEG, quality, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return tb;
	}
	public static Bitmap getThumbnail(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int tw;
		int th;
		if (w < THUMBNAIL_SIZE || h < THUMBNAIL_SIZE) {
			return bitmap;
		}
		if (w >= h) {
			th = THUMBNAIL_SIZE;
			tw = w * THUMBNAIL_SIZE / h;
		} else {
			th = h * THUMBNAIL_SIZE / w;
			tw = THUMBNAIL_SIZE;
		}

		Bitmap tb = Bitmap.createScaledBitmap(bitmap, tw, th, true);
		if (bitmap != null)
			bitmap.recycle();
		System.gc();
		return tb;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		Options opt = new Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static boolean saveBitmap(Bitmap bitmap, File file) {
		return saveBitmap(bitmap, file, 50);
	}
	public static boolean saveBitmap(Bitmap bitmap, File file,int quality){
		try {
			bitmap.compress(CompressFormat.JPEG,quality, new FileOutputStream(file));
			return true;
		} catch (Exception e) {
			return false;
		}finally{
			System.gc();
		}
	}
	public static Bitmap compressQualityBitmap(Bitmap bitmap, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		bitmap.recycle();
		Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		baos.reset();
		System.gc();
		return newBitmap;
	}
	public static Bitmap getBitmapFronPath(Context context,String filePath)
			throws FileNotFoundException {
		if (null == filePath) {
			return null;
		}

		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap temp = BitmapFactory.decodeFile(filePath, options);
		options.inJustDecodeBounds = false;
		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;
		int desiredWidth = actualWidth;
		int desiredHeight = actualHeight;
		if (actualWidth > NOMARL_SIZE && actualHeight > NOMARL_SIZE) {
			if (actualWidth > actualHeight) {
				desiredWidth = (int) (actualWidth * ((NOMARL_SIZE *1.0) / actualHeight));
				desiredHeight = NOMARL_SIZE;
			} else {
				desiredHeight = (int) (actualHeight * ((NOMARL_SIZE *1.0)/ actualWidth));
				desiredWidth = NOMARL_SIZE;
			}
		}
		options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
				desiredWidth, desiredHeight);
		temp = BitmapFactory.decodeFile(filePath, options);
		temp = Bitmap.createScaledBitmap(temp, desiredWidth,
				desiredHeight, true);
		temp = BitmapUtil.getRotationBitmap(filePath, temp);
		return temp;
	}
	
	public static void saveBitmapFronPath(String srcPath,String picName,String filePath)
			throws FileNotFoundException {

		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap temp = BitmapFactory.decodeFile(srcPath, options);
		options.inJustDecodeBounds = false;
		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;
		int desiredWidth = actualWidth;
		int desiredHeight = actualHeight;
		if (actualWidth > NOMARL_SIZE && actualHeight > NOMARL_SIZE) {
			if (actualWidth > actualHeight) {
				desiredWidth = (int) (actualWidth * ((NOMARL_SIZE *1.0) / actualHeight));
				desiredHeight = NOMARL_SIZE;
			} else {
				desiredHeight = (int) (actualHeight * ((NOMARL_SIZE *1.0)/ actualWidth));
				desiredWidth = NOMARL_SIZE;
			}
		}
		options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
				desiredWidth, desiredHeight);
		options.inPurgeable = true;// 同时设置才会有效  
		options.inInputShareable = true;//。当系统内存不够时候图片自动被回收 

		temp = BitmapFactory.decodeFile(srcPath, options);
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(temp, desiredWidth,
				desiredHeight, true);
		resizedBitmap = BitmapUtil.getRotationBitmap(srcPath, resizedBitmap);
		saveBitmap(resizedBitmap, new File(filePath,picName));
		if(null != temp){
			temp.recycle();
		}
		if(null != resizedBitmap){
			resizedBitmap.recycle();
		}
	}
	/**
	 * 从uri读取图片并压缩（最窄边>800px，压缩至800px）
	 */
	public static Bitmap getBitmapFronUri(Context context, Uri uri)
			throws FileNotFoundException {
		/*ContentResolver resolver = context.getContentResolver();
		InputStream is;
		is = resolver.openInputStream(uri);
		return BitmapFactory.decodeStream(is, null, null);*/
		String filePath = getFilePath(context, uri);
		if (null == filePath) {
			return null;
		}
		return getBitmapFronPath(context,filePath);
	}
	/**
	 *  压缩图片 压缩后最窄边为800px
	 */
	public static Bitmap getResizedBitmap(Bitmap bitmap) {
		System.gc();
		int oldWidth = bitmap.getWidth();
		int oldHeight = bitmap.getHeight();
		int newWidth,newHeight;
		if(oldWidth>oldHeight){
			newWidth = (int) (oldWidth*((NOMARL_SIZE *1.0)/oldHeight));
			newHeight = NOMARL_SIZE;
		}else{
			newHeight = (int) (oldHeight*((NOMARL_SIZE *1.0)/oldWidth));
			newWidth = NOMARL_SIZE;
		}
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
		bitmap.recycle();
		System.gc();
		return resizedBitmap;
	}
	public static Bitmap getRotationBitmap(String path, Bitmap bitmap) {
		int degree = readPictureDegree(path);
		System.gc();
		if (degree != 0)
			bitmap = rotaingImageView(degree, bitmap);
		return bitmap;
	}

	public static Bitmap getResizedBitmap(String path, byte[] data, Context context,
			Uri uri) throws Exception {
		Bitmap bitmap = null;
		// 添加一个Options对象
		Options options = null;
		options = new Options();
		// 设置options的属性:inJustDecodeBounds=true的时候读取图片的时候，bitmap为null,将图片宽和高放到options中
		options.inJustDecodeBounds = true;
		// 获得图片(这样会将图片的宽和高放入到options中)
		decode(path, data, context, uri, options);
		// 获得压缩的比例
		int outWidth = options.outWidth;

		// 这样做宽和高就是相等了
		outWidth = Math.max(outWidth, options.outHeight);
		// 计算压缩比例
		int ssize = (int) (outWidth / (NOMARL_SIZE *1.0));
		if (ssize <= 0)
			ssize = 1;
		options.inSampleSize = ssize;
		// 设置inJustDecodeBounds = false,从新构建bitmap
		options.inJustDecodeBounds = false;
		bitmap = decode(path, data, context, uri, options);
		System.gc();
		return bitmap;
	}

	/**
	 * @description:解析Bitmap的公用方法.注意各个方法的参数必须要有options
	 * @author:hui-ye
	 * @param path
	 * @param data
	 * @param context
	 * @param uri
	 * @param options
	 * @return：
	 */

	public static Bitmap decode(String path, byte[] data, Context context, Uri uri,
			Options options) throws Exception {
		Bitmap bitmap = null;
		if (path != null) {
			bitmap = BitmapFactory.decodeFile(path, options);
		} else if (data != null) {
			BitmapFactory.decodeByteArray(data, 0, data.length, options);
		} else if (uri != null) {
			// uri不为空的时候context也不要为空.:ContentResolver;Uri内容解析器
			ContentResolver resolver = context.getContentResolver();
			InputStream is;
			is = resolver.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is, null, options);
		}
		System.gc();
		return bitmap;
	}

	public static Bitmap createQrCode(String contents, int desiredWidth, int desiredHeight) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 2);
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = null;
		try {
			result = writer.encode(contents, BarcodeFormat.QR_CODE, desiredWidth,
					desiredHeight, hints);
		} catch (WriterException e) {
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
//		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		System.gc();
		return bitmap;
	}
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		System.gc();
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		if (StringUtils.isEmpty(path))
			return 0;
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
		}
		return degree;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		System.gc();
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.RGB_565);
//		Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		System.gc();
		return output;
	}

	/**
	  * addFrameBitmap(添加描边 边框)
	  *
	  * @param  srcBitmap 原图片 
	  * @param  borderWidth 边框宽度
	  * @param  color 边框的颜色值 
	  * @return Bitmap    
	  * @throws
	  */
	public static Bitmap addFrameBitmap(Bitmap srcBitmap, int borderWidth, int color) {
		if (srcBitmap == null) {
			return null;
		}
		int srcWidth = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		int newWidth = srcWidth + borderWidth;
		int newHeight = srcHeight + borderWidth;
		System.gc();
		Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.RGB_565);
//		Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		canvas.drawColor(color);
		Paint paint = new Paint();
		canvas.drawBitmap(srcBitmap, Math.abs(newWidth - srcWidth) / 2,
				Math.abs(newHeight - srcHeight) / 2, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		// 存储新合成的图片
		canvas.restore();
		if (srcBitmap != null && !srcBitmap.isRecycled()) {
			srcBitmap.recycle();
			srcBitmap = null;
		}
		System.gc();
		return outBitmap;
	}
	private static int findBestSampleSize(int actualWidth, int actualHeight,
            int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
 
        return (int) n;
    }
	
	/**
	 * 压缩收藏表情，最大边压到150
	 */
	public static Bitmap getFaceBitmapFronPath(Context context,String filePath)
			throws FileNotFoundException {
		if (null == filePath) {
			return null;
		}

		Options options = new Options();
		options.inJustDecodeBounds = true;
		Bitmap temp = BitmapFactory.decodeFile(filePath, options);
		options.inJustDecodeBounds = false;
		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;
		int desiredWidth = actualWidth;
		int desiredHeight = actualHeight;
		if (actualWidth > 150 && actualHeight > 150) {
			if (actualWidth > actualHeight) {
				desiredWidth = (int) (actualWidth * ((150 *1.0) / actualHeight));
				desiredHeight = 150;
			} else {
				desiredHeight = (int) (actualHeight * ((150 *1.0)/ actualWidth));
				desiredWidth = 150;
			}
		}
		options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
				desiredWidth, desiredHeight);
		temp = BitmapFactory.decodeFile(filePath, options);
		temp = Bitmap.createScaledBitmap(temp, desiredWidth,
				desiredHeight, true);
		return temp;
	}
	
	/**
     * 获取文件的路径
     * 
     * @param scheme
     * @return
     */
    private static String getFilePath(Context context, Uri uri) {
 
        String filePath = null;
 
        if ("content".equalsIgnoreCase(uri.getScheme())) {
 
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { Images.Media.DATA }, null, null, null);
 
            if (null == cursor) {
                return null;
            }
 
            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor
                            .getColumnIndex(Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }
 
        // 从文件中选择
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
 
        return filePath;
    }
}
