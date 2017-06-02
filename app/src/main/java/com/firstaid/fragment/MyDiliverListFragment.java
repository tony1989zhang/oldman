package com.firstaid.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.DiliverBean;
import com.firstaid.bean.DiliverItemBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/13.
 */
public class MyDiliverListFragment extends BaseListFragment implements View.OnClickListener{

    private boolean mIsDataGot = false;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitle(R.string.my_deliver_title);
    }

    @Override
    protected List convertToBeanList(String json) {
        DiliverBean bean = App.getInstance().getBeanFromJson(json,DiliverBean.class);
        if(bean.success){
            mIsDataGot = true;
        }
        testData(bean);
        return bean.data;
    }
    private void testData(DiliverBean bean){
        if(!BuildConfig.addTestData){
            return;
        }
        if(bean.data == null || bean.data.size() == 0){
            bean.data = new ArrayList<DiliverItemBean>();
            for(int i = 0 ; i < 10 ; i++){
                DiliverItemBean item = new DiliverItemBean();
                item.userName = "用户 "+i;
                item.orderNumber = "2131213"+i;
                item.expressNo = "21313123123131"+i;
                bean.data.add(item);
            }
        }
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new MyDiliverListAdapter();
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
        return ConnectionManager.getInstance().queryDeliverList(mTitleView.getContext(),""+App.getInstance().getCustId(mTitleView.getContext()),this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    private class MyDiliverListAdapter extends BasePageAdapter{


        class MyDiliverViewHolder  extends RecyclerView.ViewHolder{
            public TextView mTitle;
            public TextView mContent;
            public TextView mTime;
            public MyDiliverViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.item_title);
                mContent = (TextView) itemView.findViewById(R.id.item_content);
                mTime = (TextView) itemView.findViewById(R.id.item_time);
            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_my_diliver_list, null);
            return new MyDiliverViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof MyDiliverViewHolder){
                MyDiliverViewHolder myDiliverViewHolder = (MyDiliverViewHolder)viewHoder;
                DiliverItemBean bean = (DiliverItemBean)mItems.get(position);
                myDiliverViewHolder.mTitle.setText(bean.userName);
                myDiliverViewHolder.mContent.setText("货号："+bean.orderNumber);
                myDiliverViewHolder.mTime.setText("运单号："+bean.expressNo);
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
