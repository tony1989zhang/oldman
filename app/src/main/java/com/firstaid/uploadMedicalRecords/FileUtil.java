package com.firstaid.uploadMedicalRecords;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.firstaid.oldman.App;

public class FileUtil {

	public static String SDPATH = Environment.getExternalStorageDirectory() + "/formats/";
	public static int SELECTED_IMAGE_SIZE_IN_MB = 20;
	public static int SELECTED_VIDEO_SIZE_IN_MB = 20;

	public static void initSDDir() {
		FileUtil.creatSDDir(Constant.CACHE_DIR);
		FileUtil.creatSDDir(Constant.DOWNLOAD_DIR);
		FileUtil.creatSDDir(Constant.LOG_DIR);
		FileUtil.creatSDDir(Constant.EXPORT_IMAGE_DIR);
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatSDFile(String fileName) {
		File file = new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName) {

		try {
			File file = new File(fileName);
			return file.exists();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static File saveFile(File file, Bitmap photo) {

		FileOutputStream fOut = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fOut = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		photo.compress(Bitmap.CompressFormat.PNG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	/**
	 * 文件大小格式转换 B转KB或者M
	 */
	public static String getSizeName(long size) {
		DecimalFormat format = new DecimalFormat("0.00");

		if (size < 1024) {
			return size + "B";
		} else if (size >= 1024 && size < 1024 * 1024) {
			return format.format((double) size / 1024l) + "K";
		} else {
			return format.format((double) size / 1024l / 1024l) + "M";

		}
	}

	/**
	 * 使用文件通道的方式复制文件
	 */
	public static boolean fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * assest文件MD5值
	 */
	public static String getAssestFileMD5(String fileName) {
		AssetManager assManager = App.getInstance().getAssets();
		InputStream is = null;
		MessageDigest digest = null;
		try {
			is = assManager.open(fileName);
			byte buffer[] = new byte[1024];
			int len;

			digest = MessageDigest.getInstance("MD5");
			while ((len = is.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	/**
	 * 文件MD5值
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static void copyDB(String assestFileName, String dbFileName) {
		try {
			File file = new File("/data/data/" + App.getInstance().getPackageName() + "/databases/" + dbFileName);
			if (file.exists() && (getFileMD5(file).equals(getAssestFileMD5(assestFileName)))) {
				return;
			}
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			parentFile = null;
			FileOutputStream fos;
			fos = new FileOutputStream(file);
			InputStream is = App.getInstance().getResources().getAssets().open(assestFileName);
			byte[] buffer = new byte[1024 * 500];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static File getFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(filePath + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}

	public static String imageUri2FilePath(Activity acivity, Uri uri) {
		File file = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = acivity.managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String path = actualimagecursor.getString(actual_image_column_index);
		// actualimagecursor.close();
		return path;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	public static void distrory() {
		File file = new File("/data/data/" + "com.wbaiju.ichat/databases/");
		FileUtil.delete(file);
	}

	public static void saveBitmap(Bitmap bm, String picName, String filePath) {
		try {
			File f = new File(filePath, picName + ".jpg");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	public static void isGifImage(String filePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true; // 确保图片不加载到内存
		BitmapFactory.decodeFile(filePath, opts);
		Log.e("isGifImage","图片类型:" + opts.outMimeType);
	}

	public static String getFileHeader(String filePath) {
		FileInputStream is = null;
		String value = null;
		try {
			is = new FileInputStream(filePath);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}

	public static String getPath(String uri) {
		if (TextUtils.isEmpty(uri))
			return null;
		if (uri.startsWith("file://") && uri.length() > 7)
			return Uri.decode(uri.substring(7));
		return Uri.decode(uri);
	}

	public static String getFileType(String filePath) {
		return mFileTypes.get(getFileHeader(filePath));
	}

	public static boolean isImage(String type) {
		if (type != null
				&& (type.equals("jpg") || type.equals("gif") || type.equals("png") || type.equals("jpeg") || type.equals("bmp") || type.equals("wbmp") || type.equals("ico") || type
						.equals("jpe"))) {
			return true;
		}
		return false;
	}

	public static long ChekcMediaFileSize(File mediaFile, boolean isVideo) {

		/** Get length of file in bytes */
		long fileSizeInBytes = mediaFile.length();

		/** Convert the bytes to Kilobytes (1 KB = 1024 Bytes) */
		long fileSizeInKB = fileSizeInBytes / 1024;

		/** Convert the KB to MegaBytes (1 MB = 1024 KBytes) */
		long fileSizeInMB = fileSizeInKB / 1024;

		int requireSize = 0;
		if (isVideo) {
			requireSize = SELECTED_VIDEO_SIZE_IN_MB;
		} else {
			requireSize = SELECTED_IMAGE_SIZE_IN_MB;
		}
		if (fileSizeInMB <= requireSize) {
			return 0;
		}
		return fileSizeInMB;
	}

	public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
	static {
		// images
		mFileTypes.put("FFD8FF", "jpg");
		mFileTypes.put("89504E", "png");
		mFileTypes.put("474946", "gif");
		mFileTypes.put("49492A", "tif");
		mFileTypes.put("424D", "bmp");
		//
		mFileTypes.put("41433130", "dwg"); // CAD
		mFileTypes.put("38425053", "psd");
		mFileTypes.put("7B5C727466", "rtf"); // 日记本
		mFileTypes.put("3C3F786D6C", "xml");
		mFileTypes.put("68746D6C3E", "html");
		mFileTypes.put("44656C69766572792D646174653A", "eml"); // 邮件
		mFileTypes.put("D0CF11E0", "doc");
		mFileTypes.put("5374616E64617264204A", "mdb");
		mFileTypes.put("252150532D41646F6265", "ps");
		mFileTypes.put("255044462D312E", "pdf");
		mFileTypes.put("504B0304", "zip");
		mFileTypes.put("52617221", "rar");
		mFileTypes.put("57415645", "wav");
		mFileTypes.put("41564920", "avi");
		mFileTypes.put("2E524D46", "rm");
		mFileTypes.put("000001BA", "mpg");
		mFileTypes.put("000001B3", "mpg");
		mFileTypes.put("6D6F6F76", "mov");
		mFileTypes.put("3026B2758E66CF11", "asf");
		mFileTypes.put("4D546864", "mid");
		mFileTypes.put("1F8B08", "gz");
		mFileTypes.put("", "");
		mFileTypes.put("", "");
	}

}