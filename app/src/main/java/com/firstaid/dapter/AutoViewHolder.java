package com.firstaid.dapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder模式
 */
public class AutoViewHolder {
    private final SparseArray<View> viewHolder;
    private final View view;

    private AutoViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }

    public static AutoViewHolder getViewHolder(View view) {
        AutoViewHolder viewHolder = (AutoViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new AutoViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

}
