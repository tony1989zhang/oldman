package com.firstaid.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.impl.OnOperationListener;
import com.firstaid.impl.RecyclerItemClickListener;
import com.firstaid.oldman.AddAccActivity;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.view.CustomEditDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/1/6.
 */
public class AcountListFragment extends BaseListFragment implements View.OnClickListener,  RecyclerItemClickListener.OnItemClickListener {
    private final  static int REQUEST_ADD_ACC = 10000;//添加账号
    private final static int REQUEST_UPDATE_ACC = 10001;
    public static final String UPDATE_ACCNO = "update_accno";
    CustomEditDialog  mEditdialog;
    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.tixian_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleRightImg(R.mipmap.title_icon_add);
        mTitleView.setTitleRightOnClickListener(this);
        if(null != mViewList){
        mViewList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),mViewList,this));
        }
    }

    boolean mIsDataGot = false;
    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx", "json ==== " + json);
        AccountBean listBean =  App.getInstance().getBeanFromJson(json,AccountBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(AccountBean listBean){
        if(!BuildConfig.addTestData){
            return;
        }
        LogUtil.d("json_data_size","json_data_size:" + listBean.data.size());
        if(listBean != null && listBean.data != null && listBean.data.size() > 0 ){
            return;
        }
        listBean.data = new ArrayList<AccountDataBean>();
        for(int i = 0 ; i < 10 ; i++){
            AccountDataBean item = new AccountDataBean();

            if(i % 2 == 0){
                item.acc_type = "1";
            } else {
                item.acc_type = "4";
            }
            item.id=""+i;
            item.create_time = "2016.1.2"+i;
            item.acc_name ="用户"+i;
            item.acc_no="12312ik98982"+i;
            item.open_bank ="招商银行";
            listBean.data.add(item);
        }

    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new AccountListAdapter();
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
        return ConnectionManager.getInstance().queryAcountList(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    @Override
    public void onItemClick(View view, int position) {
        showRemarkDialog(position);
        Toast.makeText(getContext(),"短点击:" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {

        //跳转到添加用户
        Intent intent = new Intent(getActivity(), AddAccActivity.class);
        intent.putExtra(AddAccActivity.EXID_ADD_ACC,true);
        AccountDataBean accountDataBean = (AccountDataBean) mAdapter.getItems().get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(UPDATE_ACCNO,accountDataBean);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_UPDATE_ACC);
    }


    class AccountListAdapter extends BasePageAdapter{

        class AccountItemViewHolder extends RecyclerView.ViewHolder{

            private ImageView mIcon;
            private TextView mName;
            private TextView mNo;
            private TextView mDivider;
            private TextView mBankNo;
            private View mRoot;
            public AccountItemViewHolder(View itemView) {
                super(itemView);
                mIcon = (ImageView) itemView.findViewById(R.id.item_icon);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mNo = (TextView) itemView.findViewById(R.id.item_no);
                mRoot = itemView.findViewById(R.id.item_root);
                mDivider = (TextView) itemView.findViewById(R.id.item_divider);
                mBankNo = (TextView) itemView.findViewById(R.id.item_bank_no);
                mRoot.setOnClickListener(AcountListFragment.this);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_account_list, null);
            return new AccountItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if(viewHoder instanceof AccountItemViewHolder){
                AccountItemViewHolder viewHolder = (AccountItemViewHolder)viewHoder;
                AccountDataBean bean = (AccountDataBean)mItems.get(position);
                if("0".equals(bean.acc_type)){
                    viewHolder.mIcon.setBackgroundResource(R.mipmap.ic_wallet_alipay);
                    viewHolder.mName.setText("" + mTitleView.getResources().getString(R.string.zhifubao));//(bean.acc_name+" ("+mTitleView.getResources().getString(R.string.zhifubao)+")");
                    viewHolder.mNo.setText(mTitleView.getResources().getString(R.string.zhanghao)+bean.acc_no);
                    viewHolder.mDivider.setVisibility(View.GONE);
                    viewHolder.mBankNo.setVisibility(View.GONE);
                } else if("1".equals(bean.acc_type)){

                    viewHolder.mIcon.setBackgroundResource(R.mipmap.ic_wallet_tenpay);
                    viewHolder.mName.setText(bean.acc_name+" ("+mTitleView.getResources().getString(R.string.caifutong)+")");
                    viewHolder.mNo.setText(mTitleView.getResources().getString(R.string.zhanghao)+bean.acc_no);
                    viewHolder.mDivider.setVisibility(View.GONE);
                    viewHolder.mBankNo.setVisibility(View.GONE);
                } else if("3".equals(bean.acc_type)){
                    viewHolder.mIcon.setBackgroundResource(R.mipmap.ic_wallet_wechat);
                    viewHolder.mName.setText(bean.acc_name+" ("+mTitleView.getResources().getString(R.string.weixin)+")");
                    viewHolder.mNo.setText(mTitleView.getResources().getString(R.string.zhanghao)+bean.acc_no);
                    viewHolder.mDivider.setVisibility(View.GONE);
                    viewHolder.mBankNo.setVisibility(View.GONE);
                } else if("4".equals(bean.acc_type)){
                    viewHolder.mIcon.setBackgroundResource(R.mipmap.ic_wallet_unionpay);
                    viewHolder.mName.setText(bean.acc_name+" ("+mTitleView.getResources().getString(R.string.yinhangka)+")");
                    viewHolder.mNo.setText(mTitleView.getResources().getString(R.string.kaihuhang)+bean.open_bank);
                    viewHolder.mDivider.setVisibility(View.VISIBLE);
                    viewHolder.mBankNo.setVisibility(View.VISIBLE);
                    viewHolder.mBankNo.setText(mTitleView.getResources().getString(R.string.kahao)+bean.acc_no);
                } else {
                    viewHolder.mName.setText("");
                }
                viewHolder.mRoot.setTag(bean);

            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_root:{
                break;
            }
            case R.id.title_back:{
                getActivity().finish();
                break;
            }

            case R.id.title_right_img:
//                showDeleteMsgDialog();
                Intent intent = new Intent(getActivity(),AddAccActivity.class);
                intent.putExtra(AddAccActivity.EXID_ADD_ACC,false);
                startActivityForResult(intent, REQUEST_ADD_ACC);
                break;
        }
    }


    public class AccountBean extends BaseBean{
        public List<AccountDataBean> data;
    }
    public class AccountDataBean extends BaseBean{

        public String id;
        public String cus_id;

        /**账号类型，0支付宝，1财付通，3微信，4银行卡*/
        public String acc_type;
        /**开户行，账号类型为4银行卡*/
        public String open_bank;
        /**账户名*/
        public String acc_name;
        /**账号*/
        public String acc_no;
        /**是否默认，Y是，N否*/
        public String default_flag;
        /**账号创建时间，格式yyyy-MM-dd HH:mm:ss*/
        public String create_time;

    }


    private void showRemarkDialog(int position) {


        final AccountDataBean accountDataBean = (AccountDataBean) mAdapter.getItems().get(position);
        int accType = Integer.valueOf(accountDataBean.acc_type);
        String accTypeStr = "支付宝";
        if(accType == 0)
            accTypeStr = "提现至"+ "支付宝" + "账号";
            else if(accType == 1)
            accTypeStr = "提现至"+"财付通"+ "账号";
        else if(accType == 3)
            accTypeStr = "提现至"+ "微信支付" + "账号";
        else if(accType == 4)
            accTypeStr ="提现至"+ accountDataBean.open_bank + "\n" + accountDataBean.acc_name + "\n" + "账号";

        mEditdialog = new CustomEditDialog(getContext());
            mEditdialog.setButtonsText("取消", "确定");
        mEditdialog.setOperationListener(new OnOperationListener(){
            @Override
            public void onLeftClick() {
                mEditdialog.dismiss();
            }

            @Override
            public void onRightClick() {
                showLoading();
                ConnectionManager.getInstance().withDraw(getContext(), accountDataBean.cus_id, accountDataBean.id, mEditdialog.getEditText().toString(), new EndLoadListener());
            }

        });
//        mEditdialog.setEditHint("提现金额");
        mEditdialog.setTitle(accTypeStr);
        mEditdialog.setMessage("" + accountDataBean.acc_no);
        mEditdialog.show();

    }

    class EndLoadListener implements ConnectionUtil.OnDataLoadEndListener{

        @Override
        public void OnLoadEnd(String ret) {

            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
            Toast.makeText(getContext(), "" + baseBean.msg, Toast.LENGTH_SHORT).show();
            mEditdialog.dismiss();
            dismissLoading();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == REQUEST_ADD_ACC||requestCode == REQUEST_UPDATE_ACC) && resultCode == Activity.RESULT_OK){
            mAdapter.clearData();
                requestData(0,true);
                getActivity().setResult(Activity.RESULT_OK);
            }
    }
}
