package com.firstaid.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstaid.bean.NewsItemBean;
import com.firstaid.bean.NewsListBean;
import com.firstaid.oldman.NewsDetailActivity;
import com.firstaid.oldman.R;

/**
 * Created by lenovo on 2016/1/5.
 */
public class HomeNewsListView extends LinearLayout implements View.OnClickListener {
    public HomeNewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(Color.WHITE);
    }
    public void setDatas(NewsListBean list){
        this.removeAllViews();
        if(list == null && list.data == null){
            return;
        }
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(NewsItemBean item : list.data){
            addItem(item,inflater);
        }

    }

    private void addItem(NewsItemBean item,LayoutInflater inflater){
        View itemRoot = inflater.inflate(R.layout.item_homenews,null);
        TextView tv = (TextView)itemRoot.findViewById(R.id.home_news);
        tv.setText(item.title);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(itemRoot,params);
        itemRoot.setTag(item);
        itemRoot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() != null){
            NewsItemBean item = (NewsItemBean)v.getTag();
            Intent intent = new Intent();
            intent.setClass(v.getContext(), NewsDetailActivity.class);
            intent.putExtra("ext_item",item);

            v.getContext().startActivity(intent);

        }
    }
}
