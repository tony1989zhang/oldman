package com.firstaid.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.ReservePlanBean;
import com.firstaid.bean.ReservePlanDataBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReservePlanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReservePlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservePlanFragment extends BaseListFragment implements View.OnClickListener{
    boolean mIsDataGot = false;
    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle("急救手册");
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
//        mTitleView.setTitleRightImg(R.mipmap.title_icon_add);
        mTitleView.setTitleRightOnClickListener(this);
    }

    @Override
    protected List convertToBeanList(String json) {
        ReservePlanBean listBean = App.getInstance().getBeanFromJson(json,ReservePlanBean.class);;
//        listBean.success
        mIsDataGot = listBean.success;
        return listBean.data;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new ReservePlanListAdapter();
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
        return ConnectionManager.getInstance().queryReservePlan(getContext(),null,this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    @Override
    public void onClick(View v) {

    }

    class ReservePlanListAdapter extends BasePageAdapter implements View.OnClickListener{

        class ReserveItemViewHolder extends RecyclerView.ViewHolder{

            public TextView mTitle;
            public TextView mTime;
            public TextView mContent;
            public ReserveItemViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.item_title);
                 mTime = (TextView) itemView.findViewById(R.id.item_time);
                mContent = (TextView) itemView.findViewById(R.id.item_content);
            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View view = View.inflate(viewGroup.getContext(), R.layout.activity_reserve_plan, null);
            return new ReserveItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof  ReserveItemViewHolder){
                ReserveItemViewHolder reserveItemViewHolder = (ReserveItemViewHolder) viewHoder;
                ReservePlanDataBean bean = (ReservePlanDataBean) mItems.get(position);
                reserveItemViewHolder.mTitle.setText(bean.title);
                reserveItemViewHolder.mTime.setText(getString(R.string.reserve_plan_time_format, bean.editor,bean.editTime));
                reserveItemViewHolder.mContent.setText(bean.content);
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}
