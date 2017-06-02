package com.firstaid.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.RelativeItemBean;
import com.firstaid.bean.RelationListBean;
import com.firstaid.bean.RelativeListBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.AddRelationActivity;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class RelativesListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.relatives_list_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleRightImg(R.mipmap.title_icon_add);
        mTitleView.setTitleRightOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json "+json);
        RelativeListBean listBean =  App.getInstance().getBeanFromJson(json,RelativeListBean.class);
        mIsDataGot = listBean.success;
        return listBean.data;
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
        return ConnectionManager.getInstance().queryRelativeList(mTitleView.getContext(), ""+App.getInstance().getCustId(mTitleView.getContext()), this);
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
            public ImageView mMap;
            public ImageView mShareBingLi;
            public View mRoot;

            public RelationItemViewHolder(View itemView) {
                super(itemView);
                mName = (TextView)itemView.findViewById(R.id.item_name);
                mRelation = (TextView)itemView.findViewById(R.id.item_relation);
                mPhone = (TextView)itemView.findViewById(R.id.item_phone);
                mMap = (ImageView)itemView.findViewById(R.id.item_map);
                mMap.setVisibility(View.GONE);
                mShareBingLi = (ImageView)itemView.findViewById(R.id.share_bingli);
                mRoot = itemView.findViewById(R.id.item_root);
                mRoot.setOnClickListener(RelationListAdapter.this);
                mMap.setOnClickListener(RelationListAdapter.this);
                mShareBingLi.setVisibility(View.GONE);
                mShareBingLi.setOnClickListener(RelationListAdapter.this);
                /*mRoot.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                                .setItems(R.array.relation_item_long_click_options,new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RelativeItemBean bean = (RelativeItemBean)v.getTag();
                                        switch(which){
                                            case 0:{
                                                showModifyRelatonDialog(bean);
                                                //edit
                                                break;
                                            }
                                            case 1:{
                                                deleteRelation(bean.id);
                                                //delete
                                                break;
                                            }
                                        }
                                    }
                                });
                        builder.show();
                        return true;
                    }
                });*/

            }
        }

       
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_relation_list, null);
            return new RelationItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof RelationItemViewHolder){
                RelationItemViewHolder relationItemViewHolder = (RelationItemViewHolder)viewHoder;
                RelativeItemBean bean = (RelativeItemBean)mItems.get(position);
                relationItemViewHolder.mName.setText(bean.relativesName);
                relationItemViewHolder.mRelation.setText(bean.relativesRelation);
                relationItemViewHolder.mPhone.setText(bean.phone);
                relationItemViewHolder.mRoot.setTag(bean);
                relationItemViewHolder.mMap.setTag(bean);
                relationItemViewHolder.mShareBingLi.setTag(bean);
            }
        }

        @Override
        public void onClick(final View v) {
            RelativeItemBean bean = (RelativeItemBean)v.getTag();
            switch(v.getId()){
                case R.id.item_root:{
                    Util.call(v.getContext(),bean.phone);
                    break;
                }
                case R.id.item_map:{
                    //goto map
                    break;
                }
                case R.id.share_bingli:{
                    RelativesListFragment.this.showLoading();;
                    LogUtil.d("cx", " share binglie------- ");
                    /*ConnectionManager.getInstance().shareBingli(v.getContext(), SPUtil.getInstant(mTitleView.getContext()).get("mobileNo", "").toString(), bean.relationId, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            RelativesListFragment.this.dismissLoading();
                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            Toast.makeText(v.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                            LogUtil.d("cx", " share binglie " + ret);
                        }
                    });*/
                    break;
                }

            }
        }
    }

    private static final int REQUEST_ADD_RELATIVES= 103;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:{
                this.getActivity().finish();
                break;
            }
            case R.id.title_right_img:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(),AddRelationActivity.class);
                intent.putExtra(AddRelationActivity.EXT_ADD_RELATIVES, true);
                startActivityForResult(intent,REQUEST_ADD_RELATIVES);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_RELATIVES && resultCode == Activity.RESULT_OK){
            mAdapter.clearData();
            requestData(0,true);
            getActivity().setResult(Activity.RESULT_OK);
        }
    }
}
