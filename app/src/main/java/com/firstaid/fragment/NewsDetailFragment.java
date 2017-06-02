package com.firstaid.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.firstaid.bean.NewsItemBean;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BaseFragmentActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.view.TitleView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2016/1/6.
 */
public class NewsDetailFragment extends BaseFragment implements View.OnClickListener{


    private TitleView mTitleView;
    private NewsItemBean newsItemBean;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root  = inflater.inflate(R.layout.fragment_news_detail, container, false);

        initViews(root);
        if(this.getActivity() instanceof BaseFragmentActivity){
            ((BaseFragmentActivity)this.getActivity()).initSystemBarTint(mTitleView, Color.TRANSPARENT);
        }
        return root;
    }
    private void initViews(View root){
        mTitleView = (TitleView)root.findViewById(R.id.title_view);
        mTitleView.setTitle(R.string.news_detail_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);

        TextView newsTitle = (TextView) root.findViewById(R.id.news_detail_title);
        TextView newsTime = (TextView)root.findViewById(R.id.news_time);
        LinearLayout newsImages = (LinearLayout) root.findViewById(R.id.news_images);
        TextView newsContent = (TextView)root.findViewById(R.id.news_content);

        if(this.getArguments() == null || this.getArguments().getSerializable("ext_item") == null){
            this.getActivity().finish();
            return;
        }
        mTitleView.setTitleRightText("分享");
        mTitleView.setTitleRightOnClickListener(this);
        newsItemBean = (NewsItemBean) this.getArguments().getSerializable("ext_item");
        newsTitle.setText(newsItemBean.title);
        newsTime.setText(newsItemBean.publish_time);
        if(newsItemBean.img_urls == null || newsItemBean.img_urls.length == 0){
            newsImages.setVisibility(View.GONE);
        } else {
            newsImages.setVisibility(View.VISIBLE);
            for(int i = 0 ; i < newsItemBean.img_urls.length ; i++){
                NetworkImageView imageView = new NetworkImageView(newsImages.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                params.topMargin = 10;
                params.bottomMargin = 10;
                newsImages.addView(imageView,params);
                ConnectionUtil.getInstance().loadImgae(imageView,newsItemBean.img_urls[i]);
            }
        }
        if(newsItemBean.content != null){
            newsContent.setText(Html.fromHtml(newsItemBean.content));
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_back:{
                this.getActivity().finish();
                break;
            }
            case R.id.title_right:{
                App.getInstance().doShare(getActivity(),newsItemBean.title,newsItemBean.title,""+ newsItemBean.img_urls,"http://7.77-7.com");
                break;
            }
        }
    }


}
