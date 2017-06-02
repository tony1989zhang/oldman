package com.firstaid.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tony on 2016/2/16.
 * RecyclerItemClickListener 关于Recycler的点击事件
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    //点击回调
    public interface OnItemClickListener{
        public void onItemClick(View view,int position);
        public void onItemLongClick(View view,int position);
    }

    public RecyclerItemClickListener(Context context,final RecyclerView recyclerView,OnItemClickListener listener){
          mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //轻击触屏后，弹起，必须返回true，否则无法触发单击
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
//                super.onLongPress(e);
                //长按
                //根据findChildViewUnder(float x,float y) 来计算哪个item 被选择了
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(null != childView && null != mListener){
                    mListener.onItemLongClick(childView,recyclerView.getChildPosition(childView));
                }
            }
        });

    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (null != childView && null != mListener && mGestureDetector.onTouchEvent(e)){
              //触发点击事件
             mListener.onItemClick(childView,rv.getChildPosition(childView));
            return true;
        }
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
