package com.firstaid.uploadMedicalRecords;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.firstaid.oldman.R;

public class PublishGridAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private List<PublishItem> dataList;
	private ImageView playControlImage;
	private File voiceFile;

	public PublishGridAdapter(Context context, List<PublishItem> list) {
		this.mContext = context;
		this.dataList = list;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		final PublishItem item = dataList.get(position);

			convertView = inflater.inflate(R.layout.item_published_grida, null);
			viewHolder.icon = (CornerImageView) convertView
					.findViewById(R.id.item_grida_image);

			if (item.isAddPhoto()) {
				viewHolder.icon.setImageResource(R.drawable.icon_add_photo);
			}
			if (!item.isAddPhoto()) {
				viewHolder.icon.load("file://" + item.getPicDrr(),
						R.drawable.icon_img_null);
			}

		return convertView;
	}

	class ViewHolder {
		public CornerImageView icon;
	}

}
