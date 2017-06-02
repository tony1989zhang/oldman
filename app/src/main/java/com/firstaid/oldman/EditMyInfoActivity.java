package com.firstaid.oldman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.MyIndexBean;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.ImageCompress;
import com.firstaid.util.LogUtil;
import com.firstaid.util.Util;
import com.firstaid.util.frompost.ResponseListener;
import com.firstaid.view.CircleImageView;
import com.firstaid.view.CircleNetworkImageView;
import com.firstaid.view.TitleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2016/1/14.
 */
public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener {

    TitleView mTitleVew;
    CircleNetworkImageView mCircleNetworkImageView;
    TextView mName;
    TextView mPhone;
    MyIndexBean myIndexBean;
    private String mTouPath;
    private TextView myAge;
    private TextView medicaHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_info_fragment);
        mTitleVew = (TitleView) findViewById(R.id.title_view);
        mTitleVew.setTitle(R.string.title_edit_my_info);
        mTitleVew.setTitleBackVisibility(View.VISIBLE);
        mTitleVew.setTitleBackOnClickListener(this);
        mCircleNetworkImageView = (CircleNetworkImageView) findViewById(R.id.item_icon);
        mName = (TextView) findViewById(R.id.item_name);
        mPhone = (TextView) findViewById(R.id.item_phone);
         myAge = (TextView) findViewById(R.id.item_age);
         medicaHistory = (TextView) findViewById(R.id.item_medicalHistory);

        findViewById(R.id.my_touxiang).setOnClickListener(this);
        findViewById(R.id.my_name).setOnClickListener(this);
        findViewById(R.id.my_phone).setOnClickListener(this);
        findViewById(R.id.my_age).setOnClickListener(this);
        findViewById(R.id.my_medicalHistory).setOnClickListener(this);

        findViewById(R.id.btn_ok).setOnClickListener(this);
        myIndexBean = (MyIndexBean) getIntent().getSerializableExtra("ext_data");
        if (myIndexBean == null) {
            Toast.makeText(EditMyInfoActivity.this, R.string.get_my_info_err, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mName.setText(myIndexBean.data.nickName);
        mPhone.setText(App.getInstance().getMoblieNo(this));
        myAge.setText("" + myIndexBean.data.age);
        medicaHistory.setText("" + myIndexBean.data.medicalHistory);
        ConnectionUtil.getInstance().loadImgae(mCircleNetworkImageView, myIndexBean.data.headImg);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back: {
                finish();
                break;
            }
            case R.id.my_touxiang: {
                showPickImageDialog();
                break;
            }
            case R.id.my_name: {
                showEditDialog(0);
                break;
            }
            case R.id.my_phone: {
                // showEditDialog(1);
                break;
            }
            case R.id.my_medicalHistory:
            {
                showEditDialog(3);
                break;
            }
            case R.id.my_age:{
                showEditDialog(2);
                break;
            }
            case R.id.btn_ok: {
                updateMyInfo();
                break;
            }
        }
    }

    private void updateMyInfo() {
        showLoading();

        ConnectionManager.getInstance().modifyMyInfo(this, "" + App.getInstance().getCustId(this), mTouPath, mName.getText().toString(),myAge.getText().toString(),medicaHistory.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                dismissLoading();
                BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                Toast.makeText(EditMyInfoActivity.this, bean.msg, Toast.LENGTH_SHORT).show();
                if (bean != null && bean.success) {
                    setResult(Activity.RESULT_OK);
                    EditMyInfoActivity.this.finish();
                }
            }
        });

    }

    private void showEditDialog(final int typeIndex) {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_text, null, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        if (typeIndex == 0) {
            editText.setHint(mName.getText().toString());
        } else if (typeIndex == 1) {
            editText.setHint(mPhone.getText().toString());
        }else if(typeIndex == 2){
            editText.setHint(myAge.getText().toString());
        }else if(typeIndex == 3){
            editText.setHint(medicaHistory.getText().toString());
        }

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (typeIndex == 0) {
                            mName.setText(editText.getText().toString());
                        } else if (typeIndex == 1) {
                            mPhone.setText(editText.getText().toString());
                        }else  if(typeIndex == 2){
                            myAge.setText(editText.getText().toString());
                        }else if(typeIndex == 3){
                            medicaHistory.setText(editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();

    }

    private static final int REQUEST_PICK_IMAGE_GALLERY = 100004;
    private static final int REQUEST_PICK_IMAGE_CAMERA = 100005;

    private void showPickImageDialog() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, R.string.toast_sd_not_mounted, Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this).setTitle(R.string.pick_image_sources_title)
                .setItems(R.array.pick_iamge_sources, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK);
                            //intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, getString(R.string.pick_image_gallery_title)),
                                    REQUEST_PICK_IMAGE_GALLERY);
                        } else {
                            File dir = new File(App.DATA_DIR);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File file = new File(App.DATA_DIR +
                                    "uploadInfo.jpg");
                            if (file.exists()) {
                                file.delete();
                            }
                            Uri outputFileUri = Uri.fromFile(file);

                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    outputFileUri);
                            startActivityForResult(intent, REQUEST_PICK_IMAGE_CAMERA);
                        }
                    }
                }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("requestCode", "requestCode:" + requestCode + "resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE_GALLERY) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.managedQuery(uri, proj, // Which
                        // columns
                        // to
                        // return
                        null, // WHERE clause; which rows to return (all rows)
                        null, // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
                cursor.moveToFirst();
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);


                mTouPath = cursor.getString(column_index);
                compress();
            } else if (requestCode == REQUEST_PICK_IMAGE_CAMERA) {
                mTouPath = App.DATA_DIR + "uploadInfo.jpg";
                compress();

            }
            android.util.Log.d("cx", "-------- mTouPath" + mTouPath);
            if (!TextUtils.isEmpty(mTouPath)) {
                android.util.Log.d("cx", "-------- setImageDrawable----------------");
                mCircleNetworkImageView.setImageDrawable(new BitmapDrawable(getResources(), mTouPath));
                //mCircleNetworkImageView.setImageBitmap(BitmapFactory.decodeFile(mTouPath));
                mCircleNetworkImageView.invalidate();
            }

        }
    }

    private void compress() {
        ImageCompress compress = new ImageCompress();
        ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
        options.filePath = mTouPath;
        options.maxWidth = 200;
        options.maxHeight = 200;
        Bitmap bitmap = compress.compressFromUri(this, options);
        saveMyBitmap(App.DATA_DIR + "uploadInfoCompressed.png", bitmap);
        mTouPath = App.DATA_DIR + "uploadInfoCompressed.png";
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void saveMyBitmap(String path, Bitmap mBitmap) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
