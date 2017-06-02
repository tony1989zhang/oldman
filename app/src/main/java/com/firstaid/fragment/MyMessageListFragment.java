package com.firstaid.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.MyMessageBean;
import com.firstaid.bean.MyMessageItemBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.LogUtil;

import java.util.List;

/**
 * Created by lenovo on 2016/1/13.
 */
public class MyMessageListFragment extends BaseListFragment implements View.OnClickListener{

    private boolean mIsDataGot = false;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitle(R.string.message_center_title);
    }

    @Override
    protected List convertToBeanList(String json) {
        MyMessageBean bean = App.getInstance().getBeanFromJson(json,MyMessageBean.class);
        LogUtil.d("cx","json "+json);
        if(bean.success){
            mIsDataGot = true;
        }
        return bean.data;
    }


    @Override
    protected BasePageAdapter initAdapter() {
        return new MyMessageListAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return false;
    }

    @Override
    protected int getSizeInPage() {
        return 1000;
    }

    @Override
    protected Request initRequest(int start) {
        return ConnectionManager.getInstance().queryMessageList(mTitleView.getContext(),""+App.getInstance().getCustId(mTitleView.getContext()),this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    private class MyMessageListAdapter extends BasePageAdapter{


        class MyMessageViewHolder  extends RecyclerView.ViewHolder{
            public TextView mTitle;
            public TextView mContent;
            public TextView mTime;
            public MyMessageViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.item_title);
                mContent = (TextView) itemView.findViewById(R.id.item_content);
                mTime = (TextView) itemView.findViewById(R.id.item_time);
            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_my_message_list, null);
            return new MyMessageViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof MyMessageViewHolder){
                MyMessageViewHolder myMessageViewHolder = (MyMessageViewHolder)viewHoder;
                MyMessageItemBean bean = (MyMessageItemBean)mItems.get(position);
                myMessageViewHolder.mTitle.setText(bean.title);
                myMessageViewHolder.mContent.setText(bean.content);
                myMessageViewHolder.mTime.setText(bean.createDate);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_back:{
                this.getActivity().finish();
                break;
            }
        }
    }
}
