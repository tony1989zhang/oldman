package com.firstaid.uploadMedicalRecords;

import com.firstaid.oldman.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * @ClassName: CornerImageView
 * @Description: TODO
 * @author 4evercai
 * @date 2014年9月2日 上午10:50:20
 * 
 */

public class CornerImageView extends WebImageView {
	private static final int DEFAULT_CORNER_SIZE = 5;
	private  float density = getContext().getResources().getDisplayMetrics().density;
	private int cornerSize = (int) (DEFAULT_CORNER_SIZE * density);
	Bitmap composedBitmap;
	Bitmap originalBitmap;
	Canvas composedCanvas;
	Canvas originalCanvas;

	public CornerImageView(Context context) {
		super(context);
		init();
	}

	public CornerImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}

	public CornerImageView(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView,
				defStyle, 0);
		cornerSize = a.getDimensionPixelSize(R.styleable.CornerImageView_corner_size,
				(int) (DEFAULT_CORNER_SIZE * density));
		a.recycle();
		init();
	}

	@Override
	public void init() {
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher)
				.displayer(new RoundedBitmapDisplayer(cornerSize)).showImageOnFail(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true).showImageOnLoading(R.drawable.ic_launcher)
				.considerExifParams(false).build();
	}

	@Override
	public void setinitDefaultImg(int DefaultR) {
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(DefaultR)
				.displayer(new RoundedBitmapDisplayer(cornerSize)).showImageOnFail(DefaultR)
				.cacheInMemory(true).cacheOnDisc(true).showImageOnLoading(DefaultR)
				.considerExifParams(false).build();
	}

	public float getCornerSize() {
		return this.cornerSize / this.density;
	}

	public void setCornerSize(float cornerSize) {
		this.cornerSize = (int) (cornerSize * this.density);
		init();
	}
}
