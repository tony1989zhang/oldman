package com.firstaid.fragment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
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
public class XianJinListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setVisibility(View.GONE);
        mViewList.setBackgroundColor(Color.WHITE);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json ==== "+json);
        BalanceRecordBean listBean =  App.getInstance().getBeanFromJson(json,BalanceRecordBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(BalanceRecordBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        if(listBean != null && listBean.data != null && listBean.data.size() > 0 ){
            return;
        }
        listBean.data = new ArrayList<BalanceRecordDataBean>();
        for(int i = 0 ; i < 10 ; i++){
            BalanceRecordDataBean item = new BalanceRecordDataBean();

            if(i % 2 == 0){
                item.debit_credit = "1";
            } else {
                item.debit_credit = "0";
            }
            item.change_amount = "121"+i;
            item.change_time = "2016.1.2"+i;
            listBean.data.add(item);
        }

    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new BalanceRecordListAdapter();
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
        return ConnectionManager.getInstance().queryBalanceRecord(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class BalanceRecordListAdapter extends BasePageAdapter{

        class BalanceRecordItemViewHolder extends RecyclerView.ViewHolder{

            private TextView mBalance;
            private TextView mName;
            private TextView mTime;
            private View mRoot;
            public BalanceRecordItemViewHolder(View itemView) {
                super(itemView);
                mBalance = (TextView) itemView.findViewById(R.id.item_balance);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mTime = (TextView) itemView.findViewById(R.id.item_time);
                mRoot = itemView.findViewById(R.id.item_root);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_balance_record_list, null);
            return new BalanceRecordItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof BalanceRecordItemViewHolder){
                BalanceRecordItemViewHolder viewHolder = (BalanceRecordItemViewHolder)viewHoder;
                BalanceRecordDataBean bean = (BalanceRecordDataBean)mItems.get(position);
                if("0".equals(bean.debit_credit)){
                    viewHolder.mName.setText(R.string.shouru);
                } else {
                    viewHolder.mName.setText(R.string.zhichu);
                }
                viewHolder.mTime.setText(bean.change_time);
                ((BalanceRecordItemViewHolder) viewHoder).mBalance.setText(mTitleView.getResources().getString(R.string.xianjin_format,bean.change_amount));

            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_root:{

                break;
            }

        }
    }


    private class BalanceRecordBean extends BaseBean{
        public List<BalanceRecordDataBean> data;
    }
    private class BalanceRecordDataBean extends BaseBean{

        public String cus_id;

        /**收支标识，0收入，1支出*/
        public String debit_credit;
        public String change_amount;
        public String change_time;
        public String change_des;

    }
}
