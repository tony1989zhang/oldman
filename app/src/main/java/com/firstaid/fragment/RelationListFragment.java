package com.firstaid.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.bean.RelationListBean;
import com.firstaid.dapter.BasePageAdapter;
import com.firstaid.oldman.AddRelationActivity;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BeRelationListActivity;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.MainActivity;
import com.firstaid.oldman.QMRActivity;
import com.firstaid.oldman.R;
import com.firstaid.uploadMedicalRecords.ChooseWayDialog;
import com.firstaid.uploadMedicalRecords.PhotoAlbumActivity;
import com.firstaid.uploadMedicalRecords.ToastManager;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/1/6.
 */
public class RelationListFragment extends BaseListFragment implements View.OnClickListener {
    private ChooseWayDialog mDia;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        mTitleView.setTitle(R.string.relation_list_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleRightImg(R.mipmap.title_icon_add);
        mTitleView.setTitleRightOnClickListener(this);
        root.findViewById(R.id.add_py).setOnClickListener(this);
    }

    boolean mIsDataGot = false;

    @Override
    protected List convertToBeanList(String json) {
        LogUtil.d("cx", "json " + json);
        RelationListBean listBean = App.getInstance().getBeanFromJson(json, RelationListBean.class);
        mIsDataGot = listBean.success;
        testData(listBean);
        return listBean.data;
    }

    private void testData(RelationListBean listBean) {
        if (!BuildConfig.addTestData) {
            return;
        }
        listBean.data = new ArrayList<RelationItemBean>();
        for (int i = 0; i < 10; i++) {
            RelationItemBean item = new RelationItemBean();
            item.id = "2" + i;
            item.relationName = "李老" + i;
            item.relationMobile = "1376589910" + i;
            item.relationId = "1" + i;
            listBean.data.add(item);
        }

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.list_normal2;
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
        return ConnectionManager.getInstance().queryRelation(mTitleView.getContext(), SPUtil.getInstant(mTitleView.getContext()).get("mobileNo", "").toString(), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class RelationListAdapter extends BasePageAdapter implements View.OnClickListener {

        class RelationItemViewHolder extends RecyclerView.ViewHolder {
            public TextView mName;
            public TextView mRelation;
            public TextView mPhone;
            public ImageView mMap;
            public ImageView mShareBingLi;
            public View mRoot;

            public RelationItemViewHolder(View itemView) {
                super(itemView);
                mName = (TextView) itemView.findViewById(R.id.item_name);
                mRelation = (TextView) itemView.findViewById(R.id.item_relation);
                mPhone = (TextView) itemView.findViewById(R.id.item_phone);
                mMap = (ImageView) itemView.findViewById(R.id.item_map);
                mShareBingLi = (ImageView) itemView.findViewById(R.id.share_bingli);
//                mShareBingLi.setVisibility(View.INVISIBLE);
                mRoot = itemView.findViewById(R.id.item_root);
                mRoot.setOnClickListener(RelationListAdapter.this);
                mMap.setOnClickListener(RelationListAdapter.this);
                mShareBingLi.setOnClickListener(RelationListAdapter.this);
                mRoot.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                                .setItems(R.array.relation_item_long_click_options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        RelationItemBean bean = (RelationItemBean) v.getTag();
                                        switch (which) {
                                            case 0: {
                                                showModifyRelatonDialog(bean);
                                                //edit
                                                break;
                                            }
                                            case 1: {
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
                });

            }
        }

        private void deleteRelation(String id) {
            showLoading();
            ConnectionManager.getInstance().deleteRelation(mTitleView.getContext(), id, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if (baseBean.success) {
                        if (mAdapter != null) {
                            mAdapter.clearData();
                        }
                        requestData(0, true);
                    }
                }
            });
        }

        boolean checked;
        private AlertDialog mModifyRelationDialog;

        private void showModifyRelatonDialog(final RelationItemBean itemBean) {

            if (mModifyRelationDialog == null) {
                final View dialogView = ((LayoutInflater) mTitleView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_modify_relation, null);
                final EditText editName = (EditText) dialogView.findViewById(R.id.edit_name);
                final TextView phoneTV = (TextView) dialogView.findViewById(R.id.textView_phone);
                CheckBox isAlarmOrder = (CheckBox) dialogView.findViewById(R.id.isAlarmOrder);
                checked = isAlarmOrder.isChecked();
                isAlarmOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checked = isChecked;
                    }
                });
                phoneTV.setText("手机号:" + itemBean.relationMobile + "<不可更改>");
                EditText editPhone = (EditText) dialogView.findViewById(R.id.edit_phone);
                editPhone.setVisibility(View.GONE);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (TextUtils.isEmpty(editName.getText().toString()) && TextUtils.isEmpty(phoneTV.getText().toString())) {
                            Toast.makeText(mTitleView.getContext(), R.string.name_and_phone_empty, Toast.LENGTH_SHORT).show();
                        } else {
                            modifyRelation(itemBean.id, null, editName.getText().toString(), itemBean.relationMobile, checked);
                        }
                    }
                };
                dialogView.findViewById(R.id.btn_submit).setOnClickListener(listener);
                mModifyRelationDialog = new AlertDialog.Builder(mTitleView.getContext()).setView(dialogView).create();
            }
            mModifyRelationDialog.show();

        }

        private void modifyRelation(String id, String moblieNo, String relationName, String relationNo, boolean isChecked) {
            LogUtil.d("ret", "id" + id + "moblieNo:" + moblieNo + "relationName:" + relationName + "relationNo:" + relationNo);
            showLoading();
            ConnectionManager.getInstance().modifyRelation(mTitleView.getContext(), id, moblieNo, relationNo, relationName, isChecked, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d("ret", "OnLoadend:" + ret);
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if (baseBean.success) {
                        if (mModifyRelationDialog != null && mModifyRelationDialog.isShowing() && RelationListFragment.this.isAdded()) {
                            mModifyRelationDialog.dismiss();
                        }
                        if (mAdapter != null) {
                            mAdapter.clearData();
                        }
                        requestData(0, true);
                    }
                }
            });
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_relation_list, null);
            return new RelationItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof RelationItemViewHolder) {
                RelationItemViewHolder relationItemViewHolder = (RelationItemViewHolder) viewHoder;
                RelationItemBean bean = (RelationItemBean) mItems.get(position);
                relationItemViewHolder.mName.setText(bean.relationName);
                relationItemViewHolder.mRelation.setText("(关系字段)");
                relationItemViewHolder.mPhone.setText(bean.relationMobile);
                relationItemViewHolder.mRoot.setTag(bean);
                relationItemViewHolder.mMap.setTag(bean);
                relationItemViewHolder.mShareBingLi.setTag(bean);
            }
        }

        @Override
        public void onClick(final View v) {
            RelationItemBean bean = (RelationItemBean) v.getTag();
            switch (v.getId()) {
                case R.id.item_root: {
                    if (BuildConfig.hasProduct) {
                        SPUtil.getInstant(getActivity()).save("relationBean_id", bean.relationId);
                        SPUtil.getInstant(getActivity()).save("relationBean_phone", bean.relationMobile);
                        if (null == mDia) {

                            mDia = new ChooseWayDialog(getActivity());
                            mDia.setWayBack(new RegistOnOperationListener(bean));
                            mDia.settitle("添加或查看病例");
                            mDia.setData("添加病例", "查看病例", null);
                        }

                        mDia.show();
                    }
                    break;
                }
                case R.id.item_map: {
                    //goto map
                    Util.call(v.getContext(), bean.relationMobile);
                    break;
                }
                case R.id.share_bingli: {
                    RelationListFragment.this.showLoading();
                    ;
                    LogUtil.d("cx", " share binglie------- ");
                    ConnectionManager.getInstance().shareBingli(v.getContext(), SPUtil.getInstant(mTitleView.getContext()).get("mobileNo", "").toString(), bean.relationId, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            RelationListFragment.this.dismissLoading();
                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            Toast.makeText(v.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                            LogUtil.d("cx", " share binglie " + ret);
                        }
                    });
                    break;
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back: {
                this.getActivity().finish();
                break;
            }
            case R.id.title_right_img: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), AddRelationActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.add_py: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), BeRelationListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
        }
    }


    class RegistOnOperationListener implements ChooseWayDialog.ChooseBack {

        RelationItemBean bean;

        public RegistOnOperationListener(RelationItemBean bean) {
            this.bean = bean;
        }

        @Override
        public void wayback(int i) {
            // TODO Auto-generated method stub
            switch (i) {
                case 0:
//                    if(!BuildConfig.hasProduct) {

                    Intent intent = new Intent(getActivity(), PhotoAlbumActivity.class);
                    intent.putExtra("isFromOtherActivity", true);
                    startActivity(intent);

//                    }else{
//                        ToastManager.getManager().show("亲属端无上传病例权限");
//                    }
//                    mDia.show();
                    mDia.cancel();
                    break;
                case 1:
//				photo();
                    Intent intent2 = new Intent(getActivity(), QMRActivity.class);
                    intent2.putExtra("relationItemBeanPhone", "" + bean.relationMobile);
                    startActivity(intent2);
                    mDia.cancel();
                    break;
                case 2:
                    mDia.cancel();
                    break;

            }
        }
    }
}
