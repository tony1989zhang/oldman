package com.firstaid.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.firstaid.oldman.App;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.ImageFileCache;
import com.firstaid.util.LogUtil;

/**
 * Created by lenovo on 2016/1/5.
 */
public class SmartIndicator extends HorizontalScrollView implements View.OnClickListener{

    public LinearLayout mLinearLayout;
    private int mSizeInPage;
    public SmartIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(App.getInstance().mScreenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(Build.VERSION.SDK_INT > 15){
            this.setScrollBarSize(0);
        }
        this.setHorizontalScrollBarEnabled(false);
        this.addView(mLinearLayout, params);
    }

    private ViewPager mViewPager;
    int mDataSize;
    public void setDatas(int sizeInPage,String[] titles,String iconUrls[],ViewPager viewPager){
        mViewPager = viewPager;
        int size = titles.length;
        mDataSize = size;
        int viewSize = size + 2;
        if(size == 1){
            sizeInPage = 1;
            viewSize = 1;
        }
        mSizeInPage = sizeInPage;
        mLinearLayout.setWeightSum(sizeInPage);

        LayoutInflater inflater = (LayoutInflater)mLinearLayout.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i = 0 ; i < viewSize ; i++){
            if(size == 1){
                addItem(sizeInPage,titles[i],iconUrls[i],inflater);
            } else {
                if(i == 0 || i == viewSize - 1){
                    addItem(sizeInPage,null,null,inflater);
                } else {
                    addItem(sizeInPage,titles[i-1],iconUrls[i - 1],inflater);
                }
            }
        }
        this.scrollTo(0, 0);

    }

    private void addItem(int sizeInPage,String title,String iconUrl,LayoutInflater inflater){
        View itemRoot = inflater.inflate(R.layout.smart_indicator,null);
        if(mDataSize == 1){
            itemRoot.setTag(mLinearLayout.getChildCount());
        } else {
            itemRoot.setTag(mLinearLayout.getChildCount() - 1);
        }

        TextView itemTv = (TextView)itemRoot.findViewById(R.id.item_tv);
        NetworkImageView itemIcon = (NetworkImageView)itemRoot.findViewById(R.id.item_icon);
        if(title == null){
            itemTv.setText("");
            itemIcon.setVisibility(View.GONE);
        } else {
            itemTv.setText(title);
            itemRoot.setOnClickListener(this);
            loadImgae(itemIcon, iconUrl);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(App.getInstance().mScreenWidth / sizeInPage,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        mLinearLayout.addView(itemRoot, params);


    }

    private  void loadImgae(NetworkImageView icon,String url){
        ImageLoader imageLoader = new ImageLoader(ConnectionUtil.getInstance().getRequestQueue(icon.getContext()), ImageFileCache.getInstance());
        icon.setImageUrl(url, imageLoader);
    }

    private int mCurrentIndex;
    public void setCurrentItem(int index){
        mCurrentIndex = index;
        if(mLinearLayout.getChildCount() == 1){
            this.scrollTo(0,0);
        } else {
            int itemWidth = App.getInstance().mScreenWidth / mSizeInPage;
            this.scrollTo(itemWidth * (index ),0);
        }
    }

    public void onPageScrolled(float scrollPercent,int index){
        int itemWidth = App.getInstance().mScreenWidth / mSizeInPage;
        this.scrollTo((int)(itemWidth * scrollPercent)+itemWidth * (index ),0);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return true;

    }

    @Override
    public void onClick(View v) {
        /*if(v.getTag() != null){
            int index = (Integer)v.getTag();
            setCurrentItem(index);
            mViewPager.setCurrentItem(index,false);
        }*/
    }
}
