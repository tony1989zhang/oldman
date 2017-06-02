package com.firstaid.uploadMedicalRecords;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firstaid.oldman.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PhotoAlbumActivity extends CommonBaseActivity implements
		OnClickListener {

	ImageBucketAdapter adapter;// 自定义的适配器
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private GridView mGridView;
	// 所有的图片
	private List<PhotoItem> mAllImgs = new ArrayList<PhotoItem>();
	private TextView totalChoosed;
	private boolean isFromOtherActivity;
	Map<String, String> map = new HashMap<String, String>();
	private ImageView bottomIcon;
	private LinearLayout photoFloderLayout;
	private PopupWindow popupWindow;
	private View dirview;
	private ListView dirListView;
	private int mScreenHeight;
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();// 扫描拿到所有的图片文件夹
	private File mImgDir = new File("");// 图片数量最多的文件夹
	private int mPicsSize;// 存储文件夹中的图片数量
	private ImageFloderAdapter floderAdapter;
	private HashSet<String> mDirPaths = new HashSet<String>();// 临时的辅助类，用于防止同一个文件夹的多次扫描
	int totalCount = 0;
	private RelativeLayout bottomReLayout;
	private List<String> mImgs = new ArrayList<String>();// 所有的图片

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		isFromOtherActivity = intent.getBooleanExtra("isFromOtherActivity",
				false);
		setContentView(R.layout.activity_photo_album);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		initView();
		getPhotoFolder();
	}

	private void initView() {

		findViewById(R.id.LAYOUT_TOP_BACK).setVisibility(View.VISIBLE);
		Button btnBack = (Button) this.findViewById(R.id.TOP_BACK_BUTTON);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setOnClickListener(this);

		TextView mTitle = (TextView) findViewById(R.id.TITLE_TEXT);
		mTitle.setText("图片选择");

		mGridView = (GridView) findViewById(R.id.group_gridview);
		// mAllImgs = getImages();
		adapter = new ImageBucketAdapter(PhotoAlbumActivity.this, mAllImgs);

		mGridView.setAdapter(adapter);

		// mGridView.setOnItemClickListener(this);

		photoFloderLayout = (LinearLayout) findViewById(R.id.photoFloder_layout);
		bottomIcon = (ImageView) findViewById(R.id.selected_photo_icon);
		bottomIcon.setBackgroundResource(R.drawable.ic_up_flow);
		bottomReLayout = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		photoFloderLayout = (LinearLayout) findViewById(R.id.photoFloder_layout);
		photoFloderLayout.setOnClickListener(this);

		totalChoosed = (TextView) findViewById(R.id.id_total_preview);

		totalChoosed.setOnClickListener(this);

		if (Bimp.drr.size() > 0) {
			for (int i = 0; i < Bimp.drr.size(); i++) {
				map.put(Bimp.drr.get(i), Bimp.drr.get(i));
			}
			totalChoosed.setText("完成" + "(" + Bimp.drr.size() + ")");
		}
	}

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw() {
		if (popupWindow == null) {
			dirview = LayoutInflater.from(PhotoAlbumActivity.this).inflate(
					R.layout.image_select_dirlist, null);
			dirListView = (ListView) dirview.findViewById(R.id.id_list_dirs);
			popupWindow = new PopupWindow(dirview, LayoutParams.MATCH_PARENT,
					mScreenHeight * 3 / 5);
			floderAdapter = new ImageFloderAdapter(PhotoAlbumActivity.this,
					mImageFloders);
			dirListView.setAdapter(floderAdapter);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int[] location = new int[2];
		bottomReLayout.getLocationOnScreen(location);
		popupWindow.showAtLocation(bottomReLayout, Gravity.NO_GRAVITY,
				location[0], location[1] - popupWindow.getHeight());
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		bottomIcon.setBackgroundResource(R.drawable.ic_over_flow);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				bottomIcon.setBackgroundResource(R.drawable.ic_up_flow);
			}
		});
		dirListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mImgDir = new File(mImageFloders.get(position).getDir());
				mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						if (filename.endsWith(".jpg")
								|| filename.endsWith(".png")
								|| filename.endsWith(".jpeg"))
							return true;
						return false;
					}
				}));
				for (int i = 0; i < mImageFloders.size(); i++) {
					mImageFloders.get(i).setSelected(false);
				}
				mImageFloders.get(position).setSelected(true);
				floderAdapter.changeData(mImageFloders);
				setDataView();
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TOP_BACK_BUTTON:
			finish();
			break;
		case R.id.id_total_preview:
			// ArrayList<String> list = new ArrayList<String>();
			// Collection<String> c = map.values();
			// Iterator<String> it = c.iterator();
			// for (; it.hasNext();) {
			// list.add(it.next());
			// }
			// if (Bimp.act_bool) {
			// Bimp.act_bool = false;
			// }
			// for (int i = 0; i < ImageBucketAdapter.mSelectedImage.size();
			// i++) {
			// Bimp.drr.add(ImageBucketAdapter.mSelectedImage.get(i));
			// }
			if (Bimp.drr.size() > 0) {

				if (isFromOtherActivity) {
					Intent intent = new Intent(this,
							CirclePublishActivity.class);
					startActivity(intent);
					finish();
				} else {
					finish();
				}
			} else {
				finish();
			}
			break;
		case R.id.photoFloder_layout:
			initListDirPopupWindw();
			break;
		}
	}

	/**
	 * 方法描述：获取相册所有图片
	 * 
	 * @author: why
	 * @time: 2013-10-18 下午1:35:24
	 */
	// private List<PhotoItem> getImages() {
	// List<PhotoItem> pathList = new ArrayList<PhotoItem>();
	// Uri mImageUri =
	// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	// ContentResolver mContentResolver = PhotoAlbumActivity.this
	// .getContentResolver();
	// // 只查询jpeg和png的图片
	// Cursor mCursor = mContentResolver.query(mImageUri, null,
	// MediaStore.Images.Media.MIME_TYPE + "=? or "
	// + MediaStore.Images.Media.MIME_TYPE + "=?",
	// new String[] { "image/jpeg", "image/png" },
	// MediaStore.Images.Media.DATE_MODIFIED + " DESC");
	// while (mCursor.moveToNext()) {
	// // 获取图片的路径
	// String path = mCursor.getString(mCursor
	// .getColumnIndex(MediaStore.Images.Media.DATA));
	// PhotoItem photoItem = new PhotoItem(path);
	// File f = new File(path);
	// if (f.exists()) {
	// pathList.add(photoItem);
	// f = null;
	// }
	// }
	// mCursor.close();
	// return pathList;
	// }

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getPhotoFolder() {
		String firstImage;
		Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver mContentResolver = PhotoAlbumActivity.this
				.getContentResolver();
		// 只查询jpeg和png的图片
		Cursor mCursor = mContentResolver.query(mImageUri, null,
				MediaStore.Images.Media.MIME_TYPE + "=? or "
						+ MediaStore.Images.Media.MIME_TYPE + "=?",
				new String[] { "image/jpeg", "image/png" },
				MediaStore.Images.Media.DATE_MODIFIED);
		while (mCursor.moveToNext()) {
			// 获取图片的路径
			String path = mCursor.getString(mCursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
			// 拿到第一张图片的路径
			File f = new File(path);
			if (f.exists()) {
				firstImage = path;
				File parentFile = new File(path).getParentFile();
				if (parentFile == null)
					continue;
				String dirPath = parentFile.getAbsolutePath();
				ImageFloder imageFloder = null;
				// 利用一个HashSet防止多次扫描同一个文件夹
				if (mDirPaths.contains(dirPath)) {
					continue;
				} else {
					mDirPaths.add(dirPath);
					// 初始化imageFloder
					imageFloder = new ImageFloder();
					imageFloder.setDir(dirPath);
					imageFloder.setFirstImagePath(path);
				}
				if (parentFile.list() == null)
					continue;
				int picSize = parentFile.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String filename) {
						if (filename.endsWith(".jpg")
								|| filename.endsWith(".png")
								|| filename.endsWith(".jpeg"))
							return true;
						return false;
					}
				}).length;
				totalCount += picSize;
				imageFloder.setCount(picSize);
				mImageFloders.add(imageFloder);
				if (picSize > mPicsSize) {
					mPicsSize = picSize;
					mImgDir = parentFile;
				}
			}
		}
		mCursor.close();
		// 扫描完成，辅助的HashSet也就可以释放内存了
		mDirPaths = null;
		setDataView();
	}

	/**
	 * 为View绑定数据
	 */
	public void setDataView() {
		mAllImgs.clear();
		if (mImgDir.exists()) {
			mImgs = Arrays.asList(mImgDir.list());
		}
		for (int i = 0; i < mImgs.size(); i++) {
			File f = new File(mImgDir.getAbsolutePath() + "/" + mImgs.get(i));
			if (f.length() > 0) {
				if (FileUtil.isImage(getFileType(mImgDir.getAbsolutePath() + "/"
						+ mImgs.get(i)))) {
					PhotoItem photoItem = new PhotoItem(
							mImgDir.getAbsolutePath() + "/" + mImgs.get(i));
					mAllImgs.add(photoItem);
				}
			}
		}
		adapter = new ImageBucketAdapter(PhotoAlbumActivity.this, mAllImgs);
		mGridView.setAdapter(adapter);
		adapter.setOnPhotoSelectedListener(new ImageBucketAdapter.OnPhotoSelectedListener() {

			@Override
			public void photoClick(List<String> number) {
				totalChoosed.setText("完成" + "(" + number.size() + ")");
			}
		});
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return 文件后缀名
	 */
	public static String getFileType(String fileName) {
		if (fileName != null) {
			int typeIndex = fileName.lastIndexOf(".");
			if (typeIndex != -1) {
				String fileType = fileName.substring(typeIndex + 1)
						.toLowerCase();
				return fileType;
			}
		}
		return "";
	}

}
