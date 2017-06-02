package com.firstaid.uploadMedicalRecords;

import java.util.ArrayList;
import java.util.List;

import com.firstaid.oldman.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PhotoEditActivity extends Activity {

	private ArrayList<View> listViews = null;
	private ViewPager mPager;
	private int count;
	// public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;
	// private PhotoViewAttacher mAttacher;
	// private ProgressBar progressBar;
	private MyPageAdapter adapter;
	RelativeLayout photo_relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_edit);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOnPageChangeListener(pageChangeListener);
		// for (int i = 0; i < Bimp.bmp.size(); i++) {
		// bmp.add(Bimp.bmp.get(i));
		// }
		for (int i = 0; i < Bimp.drr.size(); i++) {
			drr.add(Bimp.drr.get(i));
		}
		max = Bimp.max;
		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (listViews.size() == 1) {
					// Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					FileUtil.deleteDir();
					finish();
				} else {
					String newStr = drr.get(count).substring(
							drr.get(count).lastIndexOf("/") + 1,
							drr.get(count).lastIndexOf("."));
					// bmp.remove(count);
					drr.remove(count);
					del.add(newStr);
					max--;
					mPager.removeAllViews();
					listViews.remove(count);
					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
				}
			}
		});
		Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		photo_bt_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Bimp.bmp = bmp;
				Bimp.drr = drr;
				Bimp.max = max;
				for (int i = 0; i < del.size(); i++) {
					FileUtil.delFile(del.get(i) + ".JPEG");
				}
				finish();
			}
		});

		for (int i = 0; i < drr.size(); i++) {
			initListViews(drr.get(i));//
		}
		adapter = new MyPageAdapter(listViews);// 构造adapter
		mPager.setAdapter(adapter);// 设置适配器
		Intent intent = getIntent();
		int id = intent.getIntExtra("ID", 0);
		mPager.setCurrentItem(id);
	}

	private void initListViews(String string) {
//		try {
			if (listViews == null)
				listViews = new ArrayList<View>();
			// View view =
			// getLayoutInflater().inflate(R.layout.image_detail_fragment,
			// null);
			int degree = BitmapUtil.readPictureDegree(string);
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			ImageView img = new ImageView(this);// 构造textView对象
			// progressBar = (ProgressBar) view.findViewById(R.id.loading);
			// mAttacher = new PhotoViewAttacher(img);
			img.setBackgroundColor(0xff000000);
//			Bitmap bitmap = BitmapUtil.getBitmapFronPath(this, string);
			ImageLoader.getInstance().displayImage("file://" + string, img);
//			img.setImageBitmap(bitmap);
			img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			img.setImageMatrix(matrix);
			listViews.add(img);// 添加view
//		}
//		catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
		// Bimp.bmp = bmp;
			Bimp.drr = drr;
			Bimp.max = max;
			for (int i = 0; i < del.size(); i++) {
				FileUtil.delFile(del.get(i) + ".JPEG");
			}
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;
		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
			// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		@Override
		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(listViews.get(position % size),
						0);

			} catch (Exception e) {
			}
			return listViews.get(position % size);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(listViews.get(position % size));
		}

	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};
}
