package com.firstaid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.baidu.mapapi.model.LatLng;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.NearAmbulanceDataBean;
import com.firstaid.fragment.FragmentNear;
import com.firstaid.oldman.App;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.LruImageCache;
import com.firstaid.util.SPUtil;
import com.firstaid.util.Util;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NearHospitalDetailView extends LinearLayout {

    private Context context;
    private NetworkImageView mNetworkImageView;
    private TextView mName;
    private TextView mAddr;
    private TextView mPhone;
    private View mBtnCall;
    private NearAmbulanceDataBean item;
    public NearHospitalDetailView(Context context){
        super(context);
        View root = LayoutInflater.from(context).inflate(R.layout.layout_near_hospital, this,false);
        LinearLayout.LayoutParams params = new LayoutParams(App.getInstance().mScreenWidth * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView(root);
        this.addView(root, params);

    }
    public NearHospitalDetailView(Context context,NearAmbulanceDataBean item) {
        super(context);
        this.item = item;
        this.context = context;
        View root = LayoutInflater.from(context).inflate(R.layout.layout_near_hospital, this,false);
        LinearLayout.LayoutParams params = new LayoutParams(App.getInstance().mScreenWidth * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView(root);
        this.addView(root,params);
    }
    protected void initView(View root) {
        mNetworkImageView = (NetworkImageView)root.findViewById(R.id.item_icon);
        mName = (TextView) root.findViewById(R.id.item_name);
        mAddr = (TextView) root.findViewById(R.id.item_addr);
        mPhone = (TextView) root.findViewById(R.id.item_phone);
        mBtnCall = root.findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNo = mPhone.getText() == null ? "" : mPhone.getText().toString();
                if(TextUtils.isEmpty(phoneNo)){
                    Toast.makeText(NearHospitalDetailView.this.getContext(), R.string.no_phone_no, Toast.LENGTH_SHORT).show();
                } else {

                    if(context !=  null && item != null){
                        ConnectionManager.getInstance().onKeyAlarm(context, "" + item.cusId, "" + App.getInstance().getMoblieNo(context), new ConnectionUtil.OnDataLoadEndListener() {
                            @Override
                            public void OnLoadEnd(String ret) {
                                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                                Toast.makeText(context, baseBean.msg, Toast.LENGTH_SHORT).show();
                                //调用NearFragment 线路规划
                                FragmentNear.ISROUTEPLAN = true;
                                FragmentNear.startLat = new LatLng(item.latitude, item.longitude);
                                Util.call(NearHospitalDetailView.this.getContext(), phoneNo);
                                LogUtil.d("TAG", "TAG 打电话");
                            }
                        });


                        ConnectionManager.getInstance().shareBingli(v.getContext(), "" + App.getInstance().getMoblieNo(context),""  +  item.cusId, new ConnectionUtil.OnDataLoadEndListener() {
                            @Override
                            public void OnLoadEnd(String ret) {
                                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            }
                        });
                    }
                }
            }
        });
    }

    public void setIconUrl(String url){
        url = "http://img2.imgtn.bdimg.com/it/u=3947386643,2401800583&fm=21&gp=0.jpg";
        int size = this.getResources().getDimensionPixelSize(R.dimen.map_hospital_detail_img_size);
        final Bitmap bitmap = LruImageCache.instance().getBitmap(getCacheKey(url,size,size,ImageView.ScaleType.FIT_CENTER,null));
        ConnectionUtil.getInstance().loadImgae(mNetworkImageView, url);
        if(bitmap != null){
            mNetworkImageView.setImageBitmap(bitmap);
        }


    }

    public void setName(String name){
        mName.setText(name);
    }

    public void setAddr(String addr){
        mAddr.setText(addr);
    }

    public void setPhone(String phone){
        mPhone.setText(phone);
    }
    private static String getCacheKey(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, ImageRequest.Transformation transformation) {
        final StringBuilder key = new StringBuilder();
        key.append(url);
        key.append("#W").append(maxWidth);
        key.append("#H").append(maxHeight);
        key.append("#S").append(scaleType.toString());

        if (transformation != null)
            key.append("#T").append(transformation.key());

        return key.toString();
    }
}
