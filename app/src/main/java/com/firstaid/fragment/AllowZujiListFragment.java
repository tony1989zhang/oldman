package com.firstaid.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.CustInfoBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.bean.RelationListBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.NearActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class AllowZujiListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.title_allow_zuji);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;

    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx", "json " + json);
        RelationListBean listBean = App.getInstance().getBeanFromJson(json, RelationListBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(RelationListBean listBean) {
        if (!BuildConfig.addTestData) {
            return;
        }
        listBean.data = new ArrayList<RelationItemBean>();
        for (int i = 0; i < 10; i++) {
            RelationItemBean item = new RelationItemBean();
            item.id = "2" + i;
            item.relationName = "傻老" + i;
            item.relationMobile = "1376589910" + i;
            item.relationId = "1" + i;
            if (i % 2 == 0) {
                item.allowFlag = "Y";
            } else {
                item.allowFlag = "N";
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
        return ConnectionManager.getInstance().queryRelation(mTitleView.getContext(), App.getInstance().getMoblieNo(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class RelationListAdapter extends BasePageAdapter implements View.OnClickListener {

        class RelationItemViewHolder extends RecyclerView.ViewHolder {
            public TextView mName;
            public TextView mRelation;
            public TextView mPhone;
            public ImageView mIvStatus;
            public TextView mStatus;
            public View rootItem;

            public RelationItemViewHolder(View itemView) {
                super(itemView);
                rootItem = itemView.findViewById(R.id.item_root);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mRelation = (TextView) itemView.findViewById(R.id.item_relation);
                mPhone = (TextView) itemView.findViewById(R.id.item_phone);
                Button mBtnStatus = (Button) itemView.findViewById(R.id.btn_status);
                mBtnStatus.setVisibility(View.GONE);
                mIvStatus = (ImageView) itemView.findViewById(R.id.iv_status);
                mIvStatus.setVisibility(View.VISIBLE);

                mStatus = (TextView) itemView.findViewById(R.id.item_status);
                mStatus.setVisibility(View.GONE);
                mIvStatus.setOnClickListener(RelationListAdapter.this);
                rootItem.setOnClickListener(RelationListAdapter.this);

            }
        }


        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_berelation_list, null);
            return new RelationItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof RelationItemViewHolder) {
                RelationItemViewHolder relationItemViewHolder = (RelationItemViewHolder) viewHoder;
                RelationItemBean bean = (RelationItemBean) mItems.get(position);
                relationItemViewHolder.mName.setText(bean.relationName);
                relationItemViewHolder.mRelation.setText("(关系字段)");
                relationItemViewHolder.mPhone.setText(bean.relationMobile);
                relationItemViewHolder.mIvStatus.setTag(bean);
                relationItemViewHolder.rootItem.setTag(bean);
                if ("N".equals(bean.allowFlag)) {
                    relationItemViewHolder.mIvStatus.setImageResource(R.mipmap.check_off);
                } else {
                    relationItemViewHolder.mIvStatus.setImageResource(R.mipmap.check_on);
                }
            }
        }

        @Override
        public void onClick(final View v) {
            RelationItemBean bean = (RelationItemBean) v.getTag();
            switch (v.getId()) {
                case R.id.iv_status: {
                    if ("N".equals(bean.allowFlag)) {
                        changeAllowZujiFlag(bean.relationId);
                    }

                    break;
                }
                case R.id.item_root:

                    if (null == bean) {
                        Toast.makeText(mTitleView.getContext(), "点击效果", Toast.LENGTH_SHORT).show();
                    } else {
                        if (null == bean.relationMobile) {
                            Toast.makeText(mTitleView.getContext(), "点击效果2", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mTitleView.getContext(), "点击效果" + bean.relationMobile, Toast.LENGTH_SHORT).show();
                            custInfo(bean.relationMobile);
                        }
                    }
                    break;
            }
        }

        private void custInfo(String mobile) {
            ConnectionManager.getInstance().queryCustInfo(mTitleView.getContext(), "" + mobile, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d("RET", "RET:" + ret);
                    CustInfoBean custInfoBean = App.getInstance().getBeanFromJson(ret, CustInfoBean.class);
                    if (custInfoBean.success) {
                        LogUtil.d("TAG", "TAG:经纬度" + custInfoBean.data.latitude + custInfoBean.data.longitude);
                        Toast.makeText(App.getInstance().getApplicationContext(), "TAG:经纬度" + custInfoBean.data.latitude + custInfoBean.data.longitude, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), NearActivity.class);
                        intent.putExtra("latitude", custInfoBean.data.latitude);
                        intent.putExtra("longitude", custInfoBean.data.longitude);
                        intent.putExtra("name", custInfoBean.data.nickname);
                        intent.putExtra("phone", custInfoBean.data.phone);
                        startActivity(intent);
                    } else {

                    }
                }
            });
        }

        private void changeAllowZujiFlag(String relationId) {
            showLoading();
            ConnectionManager.getInstance().allowVisiteMe(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), relationId, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d("cx", "ret " + ret);
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if (baseBean.success) {
                        if (mAdapter != null) {
                            mAdapter.clearData();
                        }
                        requestData(0, true);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back: {
                this.getActivity().finish();
                break;
            }
        }
    }
}
