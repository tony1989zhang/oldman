package com.firstaid.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.BeRelationBean;
import com.firstaid.bean.BeRelationItemBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class BeRelationListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.berelation_list_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json "+json);
        BeRelationBean listBean =  App.getInstance().getBeanFromJson(json,BeRelationBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(BeRelationBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        listBean.data = new ArrayList<BeRelationItemBean>();
        for(int i = 0 ; i < 10 ; i++){
            BeRelationItemBean item = new BeRelationItemBean();
            item.id = "2"+i;
            item.relationName = "李老"+i;
            item.relationMobile = "1376589910"+i;
            item.relationId = "1"+i;
            if(i % 2 == 0){
                item.relationStatus = "0";
            } else {
                item.relationStatus = "1";
            }
            listBean.data.add(item);
        }

    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new RelationListAdapter();
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
        return ConnectionManager.getInstance().queryBeRelation(mTitleView.getContext(), App.getInstance().getMoblieNo(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class RelationListAdapter extends BasePageAdapter implements View.OnClickListener{

        class RelationItemViewHolder extends RecyclerView.ViewHolder{
            public TextView mName;
            public TextView mRelation;
            public TextView mPhone;
            public Button mBtnStatus;
            public TextView mStatus;
            public View mRoot;

            public RelationItemViewHolder(View itemView) {
                super(itemView);
                mName = (TextView)itemView.findViewById(R.id.item_name);
                mRelation = (TextView)itemView.findViewById(R.id.item_relation);
                mPhone = (TextView)itemView.findViewById(R.id.item_phone);
                mBtnStatus = (Button) itemView.findViewById(R.id.btn_status);
                mStatus = (TextView) itemView.findViewById(R.id.item_status);
                mBtnStatus.setOnClickListener(RelationListAdapter.this);

            }
        }


        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_berelation_list, null);
            return new RelationItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof RelationItemViewHolder){
                RelationItemViewHolder relationItemViewHolder = (RelationItemViewHolder)viewHoder;
                BeRelationItemBean bean = (BeRelationItemBean)mItems.get(position);
                relationItemViewHolder.mName.setText(bean.relativesRelation);
//                relationItemViewHolder.mName.setText(bean.relationName);
                relationItemViewHolder.mRelation.setText("(关系字段)");
                relationItemViewHolder.mPhone.setText(bean.mobileNo);
                relationItemViewHolder.mBtnStatus.setTag(bean);
                if("0".equals(bean.relationStatus)){
                    relationItemViewHolder.mBtnStatus.setText(R.string.not_allowed);
//                    relationItemViewHolder.mStatus.setTextColor(relationItemViewHolder.mStatus.getResources().getColor(R.color.text_color_off));
//                    relationItemViewHolder.mBtnStatus.setImageResource(R.mipmap.check_off);
                    relationItemViewHolder.mBtnStatus.setBackgroundColor(getResources().getColor(R.color.home_state_green));
                    relationItemViewHolder.mBtnStatus.setEnabled(true);
                } else {
                    relationItemViewHolder.mBtnStatus.setText(R.string.allowed);
                    relationItemViewHolder.mBtnStatus.setTextColor(relationItemViewHolder.mStatus.getResources().getColor(R.color.title_bg_color));
//                    relationItemViewHolder.mBtnStatus.setImageResource(R.mipmap.check_on);
//                    relationItemViewHolder.mBtnStatus.setVisibility(View.INVISIBLE);
                    relationItemViewHolder.mBtnStatus.setBackgroundColor(getResources().getColor(R.color.transparent));
                    relationItemViewHolder.mBtnStatus.setEnabled(false);

                }
            }
        }

        @Override
        public void onClick(final View v) {
            BeRelationItemBean bean = (BeRelationItemBean)v.getTag();
            switch(v.getId()){
                case R.id.btn_status:{
                    agreeRelationApply(bean.relationId,bean.custId);
                    break;
                }
            }
        }

        private void agreeRelationApply(String custId,String relationId){
            showLoading();
            // App.getInstance().getCustId(mTitleView.getContext())
            ConnectionManager.getInstance().agreenRelationApplay(mTitleView.getContext() ,custId, relationId, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if(baseBean.success){
                        if(mAdapter != null){
                            mAdapter.clearData();
                        }
                        requestData(0,true);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:{
                this.getActivity().finish();
                break;
            }
        }
    }
}
