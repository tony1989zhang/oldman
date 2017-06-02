package com.firstaid.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.dapter.AutoViewHolder;
import com.firstaid.dapter.QuickAdapter;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.H5Activity;
import com.firstaid.oldman.R;
import com.firstaid.util.LogUtil;
import com.firstaid.view.TitleView;
import com.squareup.picasso.Picasso;
import com.youzan.sdk.http.engine.OnQuery;
import com.youzan.sdk.http.engine.QueryError;
import com.youzan.sdk.http.query.GoodsOnSaleQuery;
import com.youzan.sdk.model.goods.GoodsDetailModel;
import com.youzan.sdk.model.goods.GoodsListModel;

/**
 * Created by lenovo on 2016/1/6.
 */
public class ProductListFragment extends BaseFragment {
    private QuickAdapter<GoodsDetailModel> adapter;
    View mInflate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mInflate != null) {
            return mInflate;
        }
        mInflate = inflater.inflate(R.layout.fragment_product, container, false);
//        if(BuildConfig.hasProduct) {
            LogUtil.d("onCreateView","onCreateView");
            queryData();
            initAdapter();
            initViews(mInflate);
//        }
        return mInflate ;
    }



    private void queryData() {
        showLoading();
        /**
         * 参数说明请参考文档:  http://open.koudaitong.com/doc/api?method=kdt.items.onsale.get
         */
        new GoodsOnSaleQuery()
                .put("page_no", 1)//起始页码
                .put("page_size", 100)//单页数量
                .post(new OnQuery<GoodsListModel>() {
                    @Override
                    public void onFailed(QueryError error) {
                        /**
                         * 如报"校验App信息错误", 请检查AppID和AppSecret是否正确
                         */
                        Toast.makeText(getActivity(), error.getMsg(), Toast.LENGTH_SHORT).show();
                        dismissLoading();
                    }

                    @Override
                    public void onSuccess(GoodsListModel data) {
                        if (data.getTotalResults() != 0) {
                            adapter.setData(data.getItems());
                        } else {
                            Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT).show();
                        }
                        dismissLoading();
                    }
                });
    }

    /**
     * 初始化视图
     */
    private void initViews(View inflate) {
        TitleView titleView = (TitleView) inflate.findViewById(R.id.title_view);
        titleView.setTitle("商城");
        ListView listView = (ListView)inflate.findViewById(R.id.product_listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent != null) {
                    Object obj = parent.getAdapter().getItem(position);
                    if (obj != null && obj instanceof GoodsDetailModel) {
                        GoodsDetailModel item = (GoodsDetailModel) obj;
                        Intent intent = new Intent(getActivity(), H5Activity.class);
                        intent.putExtra(H5Activity.SIGN_URL, item.getDetailUrl());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 构建Adapter
     * 这里使用{@link QuickAdapter}只是为了简写
     */
    private void initAdapter() {
        adapter = new QuickAdapter<GoodsDetailModel>(R.layout.item_product_list) {
            @Override
            public void bindView(Context context, AutoViewHolder holder, int position, GoodsDetailModel model) {
                //商品缩略图
                Picasso.with(getActivity()).load(model.getPicThumbUrl()).into(holder.getImageView(R.id.goods_item_thumb_img));
                //商品的标题
                holder.getTextView(R.id.goods_item_title).setText(model.getTitle());
                //商品的价格
                holder.getTextView(R.id.goods_item_now_price).setText(String.format("¥%s", model.getPrice()));
                //商品的淘价
                if (!TextUtils.isEmpty(model.getOriginPrice())) {
                    TextView goodOriginPrice = holder.getTextView(R.id.goods_item_origin_price);
                    goodOriginPrice.setText(String.format("¥%s", model.getOriginPrice()));
                    goodOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 将layout从父组件中移除
        ViewGroup parent = (ViewGroup) mInflate.getParent();
        parent.removeView(mInflate);
    }

}
