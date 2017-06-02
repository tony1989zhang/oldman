package com.firstaid.dapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstaid.bean.NewsItemBean;
import com.firstaid.bean.RelativeListBean;
import com.firstaid.oldman.R;

/**
 * Created by Administrator on 2016/1/2.
 */
public class HomeAdapter extends BasePageAdapter {

    private static final int TYPE_HEADER = 100;
    private RelativeListBean mRelativeList;

    public void setRelativeList(RelativeListBean relativeList){
        mRelativeList = relativeList;
        notifyDataSetChanged();;
    }
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if(position == 0){
            return TYPE_HEADER;
        } else if(mItems != null && position == mItems.size()){
            return RecyclerView.INVALID_TYPE;
        }
        return super.getItemViewType(position);
    }



    @Override
    protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup,int viewType) {
        if(viewType == TYPE_HEADER){
            final View view = View.inflate(viewGroup.getContext(), R.layout.fragment_home_header, null);
            return new HomeNewsHeaderViewHolder(view);
        } else {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_homenews, null);
            return new HomeNewsViewHolder(view);
        }

    }

    @Override
    public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
        if(viewHoder instanceof HomeNewsViewHolder){
            HomeNewsViewHolder homeNewsViewHolder = (HomeNewsViewHolder)viewHoder;
            final NewsItemBean itemBean = (NewsItemBean)mItems.get(position);
        } else if(viewHoder instanceof HomeNewsHeaderViewHolder){
            HomeNewsHeaderViewHolder homeNewsHeaderViewHolder = (HomeNewsHeaderViewHolder)viewHoder;
            if(mRelativeList == null){
                homeNewsHeaderViewHolder.mQinshuTitle.setText(R.string.getting);
            } else if(!mRelativeList.success){
                homeNewsHeaderViewHolder.mQinshuTitle.setText(R.string.load_failed);
            } else {
                String title = homeNewsHeaderViewHolder.mQinshuTitle.getResources().getString(R.string.home_relative_title, mRelativeList.data.size());
                homeNewsHeaderViewHolder.mQinshuTitle.setText(title);
            }
            homeNewsHeaderViewHolder.mQinshuTitle.setOnClickListener(mHomeHeaderClickListener);
        }
    }


    class HomeNewsViewHolder extends RecyclerView.ViewHolder{
        private TextView mLeftIcon;
        private TextView mNewTitle;
        public HomeNewsViewHolder(View itemView) {
            super(itemView);
            mLeftIcon = (TextView)itemView.findViewById(R.id.dian);
            mNewTitle = (TextView)itemView.findViewById(R.id.home_news);

        }
    }

    class HomeNewsHeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView mQinshuTitle;
        public HomeNewsHeaderViewHolder(View itemView) {
            super(itemView);
            mQinshuTitle = (TextView)itemView.findViewById(R.id.qingshu_title);
        }
    }
    private View.OnClickListener mHomeHeaderClickListener;
    public void setmHomeHeaderClickListener(View.OnClickListener listener){
        mHomeHeaderClickListener = listener;
    }

}
