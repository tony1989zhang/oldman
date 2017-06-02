package com.firstaid.uploadMedicalRecords;

import com.firstaid.oldman.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class WebImageView extends ImageView {

	private String url;

	Context _context;

	protected DisplayImageOptions options;

	public WebImageView(Context context) {
		super(context);
		_context = context;
		init();
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		_context = context;
		init();
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

	public void init() {

		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).showImageOnLoading(R.drawable.ic_launcher).bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的质量
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT) // 设置图片的缩放类型，
				.considerExifParams(false).build();
	}

	public void setinitDefaultImg(int DefaultR) {
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(DefaultR).showImageOnFail(DefaultR).showImageOnLoading(DefaultR).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(DefaultR).considerExifParams(false).build();
	}

	/**
	 * @return download url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置download url，开始下载
	 * 
	 * @param url
	 */
	public void load(String url, int rid) {
		setinitDefaultImg(rid);
		options.showImageForEmptyUri(rid);
		options.showImageOnFail(rid);
		if (url == null || url.endsWith("null") || url.equals("")) {
			this.setImageResource(rid);
			return;
		}
		this.url = url;
		 ImageLoader.getInstance().displayImage(url, this, options, null);
	}

	public void load(String url, int rid, ImageLoadingListener listener) {
		init();
		options.showImageForEmptyUri(rid);
		options.showImageOnFail(rid);
		if (url == null || url.endsWith("null") || url.equals("")) {
			this.setImageResource(rid);
			return;
		}
		this.url = url;
		ImageLoader.getInstance().displayImage(url, this, options, listener);
	}

	/**
	 * 设置download url，开始下载
	 * 
	 * @param url
	 */
	public void loadLocale(String key) {
		init();
		ImageLoader.getInstance().displayImage(StringUtils.getOSSFileURI(key), this, options);
	}

	public void loadHeadPhoto(final String key) {
		init();
		setImageResource(R.drawable.ic_launcher);
		if (StringUtils.isEmpty(key)) {
			return;
		}
		ImageLoader.getInstance().displayImage(key, this, options, null);
	}

}
