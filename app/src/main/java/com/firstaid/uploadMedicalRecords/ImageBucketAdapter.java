package com.firstaid.uploadMedicalRecords;

import java.util.LinkedList;
import java.util.List;

import com.firstaid.oldman.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageBucketAdapter extends BaseAdapter {

	final String TAG = getClass().getSimpleName();
	Context act;
//	Map<String, String> map = new HashMap<String, String>();
	LayoutInflater mInflater;
	/**
	 * 图片集列表
	 */
	List<PhotoItem> dataList;
	List<String> imageList;
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	public ImageBucketAdapter(Context act, List<PhotoItem> list) {
		this.act = act;
		this.dataList = list;
		mInflater = LayoutInflater.from(act);
		// this.imageList = choosedImage;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater
					.inflate(R.layout.photoalbum_list_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.myimage_view);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final PhotoItem item = dataList.get(position);

		ImageLoader.getInstance().displayImage(
				"file://" + dataList.get(position).getPath(), holder.iv);
		// holder.iv.setTag(item.getPath());
		// if(imageList.size()>0){
		// for(int i=0;i<imageList.size();i++){
		// if(item.getPath().equals(imageList.get(i))){
		// item.setSelect(true);
		// }
		// }
		// }
		// if (item.select) {
		// holder.selected.setImageResource(R.drawable.friends_sends_pictures_select_icon_selected);
		// } else {
		// holder.selected.setImageResource(R.drawable.friends_sends_pictures_select_icon_unselected);
		// }

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (Bimp.drr.contains(item.getPath())) {
			holder.selected
					.setImageResource(R.drawable.friends_sends_pictures_select_icon_selected);
		} else {
			holder.selected
					.setImageResource(R.drawable.friends_sends_pictures_select_icon_unselected);
		}

		convertView.setOnClickListener(new MyOnClickListener(holder, position));

		return convertView;
	}

	class MyOnClickListener implements OnClickListener {

		Holder holder;
		int position;

		MyOnClickListener(Holder holder, int position) {
			this.holder = holder;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 已经选择过该图片
			if (Bimp.drr.contains(dataList.get(position).getPath())) {
				Bimp.drr.remove(dataList.get(position).getPath());
				holder.selected
						.setImageResource(R.drawable.friends_sends_pictures_select_icon_unselected);
			} else {// 未选择该图片
				if (Bimp.drr.size() >= 9) {
					Toast.makeText(act, "你最多选九张", Toast.LENGTH_SHORT).show();
					return;
				}
				Bimp.drr.add(dataList.get(position).getPath());
				holder.selected
						.setImageResource(R.drawable.friends_sends_pictures_select_icon_selected);
			}
			onPhotoSelectedListener.photoClick(Bimp.drr);
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	class Holder {
		private ImageView iv, selected;
	}

	public OnPhotoSelectedListener onPhotoSelectedListener;

	public void setOnPhotoSelectedListener(
			OnPhotoSelectedListener onPhotoSelectedListener) {
		this.onPhotoSelectedListener = onPhotoSelectedListener;
	}

	public interface OnPhotoSelectedListener {
		public void photoClick(List<String> number);
	}
}
