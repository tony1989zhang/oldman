package com.firstaid.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.HelpRecordBean;
import com.firstaid.bean.HelpRecordDataBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.JiJiuCheRateActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class HelpRecordListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.help_record_list_title);
        mTitleView.setTitleRightOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json ==== "+json);
        HelpRecordBean listBean =  App.getInstance().getBeanFromJson(json,HelpRecordBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(HelpRecordBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        if(listBean != null && listBean.data != null && listBean.data.size() > 0 ){
            return;
        }
        listBean.data = new ArrayList<HelpRecordDataBean>();
        for(int i = 0 ; i < 10 ; i++){
            HelpRecordDataBean item = new HelpRecordDataBean();
            item.rescueId = "1"+i;
            item.rescueName = "***医院"+i;
            item.helpId = "1"+i;
            item.aEvaluateTime = "time"+i;
            item.aEvaluateStar = "4";
            item.aEvaluateContent = "这个真的不错啊，不错不错，真的不骗人";
            item.ambulanceNo="京A23123"+i;
            if(i % 3 == 0){
                item.ambulanceState = "Y";
            } else {
                item.ambulanceState = "N";
            }
            listBean.data.add(item);
        }

    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new HelpRecordListAdapter();
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
        return ConnectionManager.getInstance().queryHelpRecord(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), App.getInstance().getMoblieNo(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class HelpRecordListAdapter extends BasePageAdapter{

        class HelpRecordItemViewHolder extends RecyclerView.ViewHolder{

            private ImageView mIcon;
            private TextView mName;
            private TextView mNo;
            private View mRoot;
            private TextView mTime;
            private RatingBar mRatingBar;
            private TextView mContent;
            private TextView mNotRate;
            public HelpRecordItemViewHolder(View itemView) {
                super(itemView);
                mIcon = (ImageView) itemView.findViewById(R.id.item_icon);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mNo = (TextView) itemView.findViewById(R.id.item_no);
                mRoot = itemView.findViewById(R.id.item_root);
                mTime = (TextView) itemView.findViewById(R.id.item_time);
                mRatingBar = (RatingBar) itemView.findViewById(R.id.item_rate);
                mContent = (TextView) itemView.findViewById(R.id.item_content);
                mRatingBar.setEnabled(false);
                mRatingBar.setMax(5);
                mNotRate = (TextView) itemView.findViewById(R.id.item_not_rate);
                mRoot.setOnClickListener(HelpRecordListFragment.this);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_help_record_list, null);
            return new HelpRecordItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof HelpRecordItemViewHolder){
                HelpRecordItemViewHolder productItemViewHolder = (HelpRecordItemViewHolder)viewHoder;
                HelpRecordDataBean bean = (HelpRecordDataBean)mItems.get(position);
                productItemViewHolder.mName.setText(bean.rescueName);
                productItemViewHolder.mNo.setText(bean.ambulanceNo);
                productItemViewHolder.mTime.setText(bean.aEvaluateTime);


                if("Y".equals(bean.ambulanceState)){
                    productItemViewHolder.mRatingBar.setVisibility(View.VISIBLE);
                    productItemViewHolder.mNotRate.setVisibility(View.INVISIBLE);
                    productItemViewHolder.mRatingBar.setRating(Float.parseFloat(bean.aEvaluateStar));
                    productItemViewHolder.mContent.setText(bean.aEvaluateContent);
                } else {
                    productItemViewHolder.mRatingBar.setVisibility(View.INVISIBLE);
                    productItemViewHolder.mNotRate.setVisibility(View.VISIBLE);
                    productItemViewHolder.mContent.setText("");
                }
                productItemViewHolder.mRoot.setTag(bean);
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_root:{
                HelpRecordDataBean bean = (HelpRecordDataBean)v.getTag();
                if("Y".equals(bean.ambulanceState)){

                } else {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), JiJiuCheRateActivity.class);
                    intent.putExtra("ext_item",bean);
                    v.getContext().startActivity(intent);
                }
                break;
            }
            case R.id.title_back:{
                getActivity().finish();
                break;
            }
        }
    }
}
