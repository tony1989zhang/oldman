package com.firstaid.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.firstaid.bean.NewsItemBean;
import com.firstaid.bean.NewsListBean;
import com.firstaid.bean.OpenDeviceDataBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.App;
import com.firstaid.oldman.NewsDetailActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.LogUtil;

import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class NewsListFragment extends BaseListFragment implements View.OnClickListener {

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.news_list_title);
        mTitleView.setTitleRightOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx","json ==== "+json);
        NewsListBean listBean =  App.getInstance().getBeanFromJson(json,NewsListBean.class);
        mIsDataGot = listBean.success;
        return listBean.data;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new NewsListAdapter();
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
        return ConnectionManager.getInstance().queryNews(mTitleView.getContext(), "1000", this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class NewsListAdapter extends BasePageAdapter{

        class NewsItemViewHolder extends RecyclerView.ViewHolder{

            private TextView mNewsTitle;
            private View mRoot;
            public NewsItemViewHolder(View itemView) {
                super(itemView);
                mNewsTitle = (TextView) itemView.findViewById(R.id.home_news);
                mRoot = itemView.findViewById(R.id.item_root);
                mRoot.setOnClickListener(NewsListFragment.this);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_homenews, null);
            return new NewsItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof NewsItemViewHolder){
                NewsItemViewHolder productItemViewHolder = (NewsItemViewHolder)viewHoder;
                NewsItemBean bean = (NewsItemBean)mItems.get(position);
                productItemViewHolder.mNewsTitle.setText(bean.title);
                productItemViewHolder.mRoot.setTag(bean);
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_root:{
                NewsItemBean item = (NewsItemBean)v.getTag();
                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewsDetailActivity.class);
                intent.putExtra("ext_item",item);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.title_back:{
                getActivity().finish();
                break;
            }
        }
    }
}
