package com.firstaid.dapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Usage:
 * #
 * #      new QuickAdapter<Model>(R.layout.layout_model, ArrayList<Model>){
 * #
 * #          bindView(Context context, AutoViewHolder holder, int position, @NonNull Model model){
 * #              holder.getTextView(R.id.textview_follow_name).setText(model.getName());
 * #          }
 * #
 * #      };
 * #
 * #
 * #      Just Create a instance and override {@link #bindView(Context, AutoViewHolder, int, T)} method.
 * #
 */
public class QuickAdapter<T> extends BaseAdapter {
    private final int layoutId;
    protected List<T> mData;

    public QuickAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public QuickAdapter(int layoutId, List<T> data) {
        this.layoutId = layoutId;
        this.mData = data;
    }


    public void addData(List<T> data) {
        if (data != null) {
            if (this.mData == null) {
                this.mData = new ArrayList<T>(data.size());
            }
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        if (data != null) {
            if (this.mData == null) {
                this.mData = data;
            } else {
                this.mData.clear();
                this.mData.addAll(data);
            }
            notifyDataSetChanged();
        }
    }

    public View getLayoutView(ViewGroup parent, int layoutRes) {
        View view = null;
        if (parent != null) {
            Context context = parent.getContext();
            if (context != null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (li != null) {
                    view = li.inflate(layoutRes, parent, false);
                }
            }
        }
        return view;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData != null && mData.size() > position && position >= 0 ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String toString() {
        return mData == null ? "CommonAdapter:Data Is Null" : "CommonAdapter{" + "mData=" + mData + '}';
    }

    public int getLayoutId() {
        return layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getLayoutView(parent, layoutId);
        }
        bindView(parent.getContext(), AutoViewHolder.getViewHolder(convertView), position, getItem(position));
        return convertView;
    }

    public void bindView(Context context, AutoViewHolder holder, int position, T model) {
    }
}
