package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.firstaid.alipay.PayResult;
import com.firstaid.alipay.PayUtils;
import com.firstaid.bean.ProductDataBean;
import com.firstaid.fragment.BaseFragment;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.view.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/19.
 */
public class ProductDetailActivity extends BaseFragmentActivity implements View.OnClickListener{


    private TitleView mTitleView;
    private ProductDataBean mProductBean;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initViews();
    }

    private void initViews(){
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitle(R.string.product_list_title);
        if(this.getIntent() == null || this.getIntent().getSerializableExtra("ext_item") == null){
            return;
        }
        mProductBean = (ProductDataBean) this.getIntent().getSerializableExtra("ext_item");
        mViewPager = (ViewPager) findViewById(R.id.product_detail_vp);
        fillViewPager();
        ((TextView)findViewById(R.id.item_name)).setText(mProductBean.introduction);
        ((TextView)findViewById(R.id.item_price2)).setText(this.getString(R.string.price_format, mProductBean.discountPrice));
        ((TextView)findViewById(R.id.item_price1)).setText(this.getString(R.string.price_format, mProductBean.marketPrice));
        ((TextView)findViewById(R.id.item_price1)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        ((TextView)findViewById(R.id.item_yunfei)).setText(this.getString(R.string.freight_format, mProductBean.freight));
        ((TextView)findViewById(R.id.item_brand)).setText(this.getString(R.string.brand_format, mProductBean.brandName));
        ((TextView)findViewById(R.id.item_xinghao)).setText(this.getString(R.string.xinghao_format, mProductBean.brandNo));
        ((TextView)findViewById(R.id.item_duixiang)).setText(this.getString(R.string.duixiang_format, "无字段"));
        ((TextView)findViewById(R.id.item_fenlei)).setText(this.getString(R.string.fenlei_format, "无字段"));
        fillImages();
        findViewById(R.id.buy_way1).setOnClickListener(this);
        findViewById(R.id.buy_way2).setOnClickListener(this);
    }

    private void fillViewPager(){
        if(mProductBean.productImgs == null || mProductBean.productImgs.length == 0){
            mViewPager.setVisibility(View.GONE);
            return;
        }
        final List<BaseFragment> fragments = new ArrayList<BaseFragment>();
        for(int i = 0 ; i < mProductBean.productImgs.length ; i++){
            ImageFragment fragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ext_url",mProductBean.productImgs[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        mViewPager.setAdapter(adapter);
    }

    private void fillImages(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.img_list);
        if(mProductBean.productImgs == null || mProductBean.productImgs.length == 0){
            return;
        }
        for(int i = 0 ; i < mProductBean.productImgs.length ; i++){
            NetworkImageView imageView = new NetworkImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(App.getInstance().mScreenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(imageView,params);
            ConnectionUtil.getInstance().loadImgae(imageView,mProductBean.productImgs[i]);
        }
    }
    public static class ImageFragment extends BaseFragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.product_detail_vp_item, container, false);;
            NetworkImageView image = (NetworkImageView)root.findViewById(R.id.item_icon);
            ConnectionUtil.getInstance().loadImgae(image,this.getArguments().getString("ext_url"));
            return root;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buy_way1:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://7.77-7.com/bz/tb.htm"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ProductDetailActivity.this.startActivity(intent);
                break;
            }
            case R.id.buy_way2:{
                mProductBean.discountPrice = "0.01";
                PayUtils.aliPay(this,mHandler,"名称:" + mProductBean.brandName,"描述" + mProductBean.isInvoice,mProductBean.discountPrice);
                Toast.makeText(ProductDetailActivity.this,"商品详情:" + mProductBean.brandName +"商品描述:" + mProductBean.introduction,Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayUtils.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ProductDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ProductDetailActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ProductDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case PayUtils.SDK_CHECK_FLAG: {
                    Toast.makeText(ProductDetailActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };
}
