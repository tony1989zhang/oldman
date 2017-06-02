package com.firstaid.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.OpenDeviceBean;
import com.firstaid.bean.OpenDeviceDataBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class OpenDeviceListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.open_device_list_title);
        mTitleView.setTitleRightOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json ==== "+json);
        OpenDeviceBean listBean =  App.getInstance().getBeanFromJson(json,OpenDeviceBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(OpenDeviceBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        if(listBean != null && listBean.data != null && listBean.data.size() > 0 ){
            return;
        }
        listBean.data = new ArrayList<OpenDeviceDataBean>();
        for(int i = 0 ; i < 10 ; i++){
            OpenDeviceDataBean item = new OpenDeviceDataBean();
            item.device_no ="No."+i;
            item.alarm_flag= "Y";
            listBean.data.add(item);
        }

    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new OpenDeviceListAdapter();
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
        return ConnectionManager.getInstance().queryOpenDeviceList(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class OpenDeviceListAdapter extends BasePageAdapter{

        class OpenDeviceItemViewHolder extends RecyclerView.ViewHolder{

            private ImageView mState;
            private TextView mName;
            private TextView mStateTv;
            private View mRoot;
            public OpenDeviceItemViewHolder(View itemView) {
                super(itemView);
                mState = (ImageView) itemView.findViewById(R.id.item_state);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mStateTv = (TextView) itemView.findViewById(R.id.item_state_tv);
                mRoot = itemView.findViewById(R.id.item_root);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_open_device_list, null);
            return new OpenDeviceItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof OpenDeviceItemViewHolder){
                OpenDeviceItemViewHolder productItemViewHolder = (OpenDeviceItemViewHolder)viewHoder;
                OpenDeviceDataBean bean = (OpenDeviceDataBean)mItems.get(position);
                LogUtil.d("OpenDeviceDataBean","OpenDeviceDataBean:" + bean.nikename);
                if(null == bean.nikename || bean.nikename.equals("")){
                     productItemViewHolder.mName.setText("未设置");
                }else {
                    productItemViewHolder.mName.setText(bean.nikename);
                }
                if("Y".equals(bean.alarm_flag)){
                    productItemViewHolder.mStateTv.setText(R.string.state_alarmed);
                    productItemViewHolder.mState.setImageResource(R.mipmap.state_3);
                } else if("Y".equals(bean.insert_flag)){
                    productItemViewHolder.mStateTv.setText(R.string.state_pluged);
                    productItemViewHolder.mState.setImageResource(R.mipmap.state_1);
                } else {
                    if("Y".equals(bean.open_flag)){
                        productItemViewHolder.mStateTv.setText(R.string.state_not_pluged);
                        productItemViewHolder.mState.setImageResource(R.mipmap.state_2);
                    } else {
                        productItemViewHolder.mStateTv.setText(R.string.state_not_open);
                        productItemViewHolder.mState.setImageResource(R.mipmap.state_2);
                    }
                }
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_root:{

                break;
            }
            case R.id.title_back:{
                getActivity().finish();
                break;
            }
        }
    }
}
