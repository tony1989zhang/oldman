package com.firstaid.dapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.firstaid.view.PageAdapterLoadingView;

import java.util.List;

/**
 * RecyclerView 分页的BaseAdapter
 * */
public abstract class BasePageAdapter<TYPE> extends Adapter {

	public interface Pagingable {
		void onLoadMoreItems();
	}

	
	private boolean isLoading;
	private boolean hasMoreItems;
	private Pagingable pagingableListener;
	private boolean mIsPageEnabled;
	protected PageAdapterLoadingView loadingView;

	private LinearLayoutManager mLayoutManger;
    public boolean isLoading() {
		return this.isLoading;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
		if(isLoading){
			loadingView.loading();
		} else {
			loadingView.hide();	
		}
	}
	
	public void setPagingableListener(Pagingable pagingableListener) {
		this.pagingableListener = pagingableListener;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
		if(hasMoreItems){
			loadingView.loadMore();
		} else {
			loadingView.loadFinished();
		}
	}

	public boolean hasMoreItems() {
		return this.hasMoreItems;
	}


	public void onFinishLoading(boolean hasMoreItems) {
		setIsLoading(false);
		setHasMoreItems(hasMoreItems);
		
	}
	
	public void init(RecyclerView recyclerView,boolean isPageEnabled) {
		mIsPageEnabled = isPageEnabled;
		loadingView = new PageAdapterLoadingView(recyclerView.getContext(),isPageEnabled);
		isLoading = false;
		if (recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager){
			mLayoutManger = (LinearLayoutManager)recyclerView.getLayoutManager();
		}

		if(mIsPageEnabled){
			PagingableScrollListener scrollListener = new PagingableScrollListener();
			recyclerView.setOnScrollListener(scrollListener);
		}

	}
	
	public class PagingableScrollListener extends RecyclerView.OnScrollListener{
		
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			// TODO Auto-generated method stub
			super.onScrolled(recyclerView, dx, dy);
			if(mLayoutManger == null){
				return;
			}
			final int visibleItemCount = mLayoutManger.getChildCount();
			final int totalItemCount = mLayoutManger.getItemCount();
			final int pastVisiblesItems = mLayoutManger.findFirstVisibleItemPosition();
            if (!isLoading && hasMoreItems && ((visibleItemCount+pastVisiblesItems) >= totalItemCount)) {
                if (pagingableListener != null) {
                	setIsLoading(true);
                    pagingableListener.onLoadMoreItems();
                }

            }
		}
	}
	
	class FooterViewHolder extends
	ViewHolder {
		private View loadingView;
		public FooterViewHolder(View root) {
			super(root);
			// TODO Auto-generated constructor stub
			loadingView = root;
		}
	}

	class HeaderViewHolder extends
			ViewHolder {
		public HeaderViewHolder(View root) {
			super(root);
			// TODO Auto-generated constructor stub
		}
	}
	
	
	protected List<TYPE> mItems;
	public void setItems(List<TYPE> items){
		mItems = items;
	}
	
	public List<TYPE> getItems(){
		return mItems;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(mItems != null && position == mItems.size()){
			return RecyclerView.INVALID_TYPE;
		}
		return super.getItemViewType(position);
	}
	
	public void clearData() {
		// TODO Auto-generated method stub
		if(mItems != null){
			loadingView.hide();
			mItems.clear();
			this.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mItems == null ? 0  : mItems.size() + 1 ;
		//1 is for loadingView
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		// TODO Auto-generated method stub
		if(viewType == RecyclerView.INVALID_TYPE){
			return new FooterViewHolder(loadingView);
		}
		return initViewHolder(viewGroup,viewType);
	}
	@Override
	public void onBindViewHolder(
			ViewHolder viewHoder, int position) {
		doBindViewHolder(viewHoder, position);
	}
	protected abstract ViewHolder initViewHolder(ViewGroup viewGroup,int viewType);
	public abstract void doBindViewHolder(ViewHolder viewHoder, int position);
	
}
