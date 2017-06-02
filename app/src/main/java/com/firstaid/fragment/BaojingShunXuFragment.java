package com.firstaid.fragment;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.bean.RelationListBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.dapter.ItemMovableBasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.recyclerMovable.ItemTouchHelperViewHolder;
import com.firstaid.view.recyclerMovable.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class BaojingShunXuFragment extends BaseListFragment implements View.OnClickListener {

    private ItemTouchHelper mItemTouchHelper;
    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.baojing_shunxu_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        initItemMovable();
    }

    private void initItemMovable(){
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemMovableBasePageAdapter)mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mViewList);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        RelationListBean listBean =  App.getInstance().getBeanFromJson(json,RelationListBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(RelationListBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        listBean.data = new ArrayList<RelationItemBean>();
        for(int i = 0 ; i < 10 ; i++){
            RelationItemBean item = new RelationItemBean();
            item.id = "2"+i;
            item.relationName = "猪老"+i;
            item.relationMobile = "1376589910"+i;
            item.relationId = "1"+i;
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
        return ConnectionManager.getInstance().queryRelation(mTitleView.getContext(), SPUtil.getInstant(mTitleView.getContext()).get("mobileNo","").toString(),this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class RelationListAdapter extends ItemMovableBasePageAdapter implements View.OnClickListener{

        class RelationItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
            public TextView mName;
            public TextView mRelation;
            public TextView mPhone;
            public ImageView mMap;
            public ImageView mShareBingLi;
            public View mRoot;
            public TextView mPosition;

            public RelationItemViewHolder(View itemView) {
                super(itemView);
                mName = (TextView)itemView.findViewById(R.id.item_name);
                mRelation = (TextView)itemView.findViewById(R.id.item_relation);
                mPhone = (TextView)itemView.findViewById(R.id.item_phone);
                mMap = (ImageView)itemView.findViewById(R.id.item_map);
                mShareBingLi = (ImageView)itemView.findViewById(R.id.share_bingli);
                mRoot = itemView.findViewById(R.id.item_root);
                mShareBingLi.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mItemTouchHelper.startDrag(RelationItemViewHolder.this);
                        return false;
                    }
                });
                mPosition = (TextView) itemView.findViewById(R.id.item_position);
            }

            @Override
            public void onItemSelected() {
                mRoot.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onItemClear() {
                mRoot.setBackgroundColor(Color.TRANSPARENT);
                StringBuilder sb = new StringBuilder("【");
                for(int i = 0 ; i < mItems.size() ; i++){
                    RelationItemBean bean = (RelationItemBean)mItems.get(i);
                    sb.append(bean.id).append(":").append(i);
                    if(i == mItems.size() - 1){

                    } else {
                        sb.append(",");
                    }
                }
                sb.append("】");
                showLoading();
                ConnectionManager.getInstance().modifyRelationOrder(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), sb.toString(), new ConnectionUtil.OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                        dismissLoading();
                        BaseBean bean = App.getInstance().getBeanFromJson(ret,BaseBean.class);
                        Toast.makeText(mTitleView.getContext(), bean.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_baojingshunxu_list, null);
            return new RelationItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof RelationItemViewHolder){
                RelationItemViewHolder relationItemViewHolder = (RelationItemViewHolder)viewHoder;
                RelationItemBean bean = (RelationItemBean)mItems.get(position);
                relationItemViewHolder.mName.setText(bean.relationName);
                relationItemViewHolder.mRelation.setText("(关系字段)");
                relationItemViewHolder.mPhone.setText(bean.relationMobile);
                relationItemViewHolder.mRoot.setTag(bean);
                relationItemViewHolder.mMap.setTag(bean);
                relationItemViewHolder.mShareBingLi.setTag(bean);
                if(position < 10){
                    relationItemViewHolder.mPosition.setText("0"+position);
                } else {
                    relationItemViewHolder.mPosition.setText(""+position);
                }
            }
        }


        @Override
        public void onClick(final View v) {
            RelationItemBean bean = (RelationItemBean)v.getTag();
            switch(v.getId()){
                case R.id.item_root:{
                    break;
                }
                case R.id.item_map:{
                    //goto map
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
}
