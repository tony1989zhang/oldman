package com.firstaid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.AddressBean;
import com.firstaid.bean.AddressDataBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.InsertAddressActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.LogUtil;

import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class AddressListFragment extends BaseListFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_UPDATE_ADDRESS = 50;
    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.address_list_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json "+json);
        AddressBean listBean =  App.getInstance().getBeanFromJson(json,AddressBean.class);
        mIsDataGot = listBean.success;
        return listBean.data;
    }


    @Override
    protected BasePageAdapter initAdapter() {
        return new AddressListAdapter();
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
        return ConnectionManager.getInstance().queryAddressList(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class AddressListAdapter extends BasePageAdapter implements View.OnClickListener{

        class AddressItemViewHolder extends RecyclerView.ViewHolder{
            public TextView mName;
            public TextView mAddr;
            public TextView mPhone;
            public TextView mYouBian;
            public View mRoot;

            public AddressItemViewHolder(View itemView) {
                super(itemView);
                mName = (TextView)itemView.findViewById(R.id.item_name);
                mAddr = (TextView)itemView.findViewById(R.id.item_addr);
                mPhone = (TextView)itemView.findViewById(R.id.item_phone);
                mYouBian = (TextView)itemView.findViewById(R.id.item_youbian);
                mRoot = itemView.findViewById(R.id.item_root);
                mRoot.setOnClickListener(AddressListAdapter.this);

            }

        }


        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_address_list, null);
            return new AddressItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof AddressItemViewHolder){
                AddressItemViewHolder relationItemViewHolder = (AddressItemViewHolder)viewHoder;
                AddressDataBean bean = (AddressDataBean)mItems.get(position);
                relationItemViewHolder.mName.setText(bean.receiver);
                relationItemViewHolder.mPhone.setText(bean.phone);
                relationItemViewHolder.mRoot.setTag(bean);
                relationItemViewHolder.mAddr.setText(bean.address);
                relationItemViewHolder.mYouBian.setText(mTitleView.getResources().getString(R.string.zip_code_format, bean.zip_code));
            }
        }

        @Override
        public void onClick(final View v) {
            AddressDataBean bean = (AddressDataBean)v.getTag();
            switch(v.getId()){
                case R.id.item_root:{
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), InsertAddressActivity.class);
                    intent.putExtra("ext_item", bean);
                    AddressListFragment.this.startActivityForResult(intent, REQUEST_CODE_UPDATE_ADDRESS);
                    break;
                }

            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_UPDATE_ADDRESS && resultCode == Activity.RESULT_OK){
            reloadData();
        }
    }
}
