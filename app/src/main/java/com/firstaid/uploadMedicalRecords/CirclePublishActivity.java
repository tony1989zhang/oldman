package com.firstaid.uploadMedicalRecords;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firstaid.bean.BaseBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class CirclePublishActivity extends CommonBaseActivity implements OnClickListener, CustomDialog.OnOperationListener {//OnUploadCallBack

    private ScrollGridView gridView;
    private CustomDialog mDia, deleteVoiceDia;
    // private String path = "";
    private static final int TAKE_PICTURE = 0x000000;
    private TextView writeSomeTv;
    private EditText publishContent;
    private List<PublishItem> pubItemList = new ArrayList<PublishItem>();
    private PublishGridAdapter adapter;
    //	private Button recordBtn;
    private ImageView recordImge;
//	private ImageView imgEmoticon;

    private static final String PATH = Constant.CACHE_DIR + "/";
    private MediaPlayer mMediaPlayer;
    private List<String> bigImgList;
    private int uploadFileCount = 0;
    private int uploadFileCountTmp = 0; // 上传成功次数
    private int uploadFailedCountTmp = 0; // 上传失败次数
    private int authType = 0;
    //	private CircleTags recive;
    private String[] imageMore;
    private static String IMAGE_FILE_LOCATION;
    private Button rightBtn;
    InputMethodManager inputMethodManager;
    private View mLinemoticon;
    //	private RelativeLayout wsLayout;
//	private SquareLayout squareLayout;
//	private boolean isVieo;
    private int syncFlag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_publish);

        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.TITLE_TEXT)).setText("上传病例");
        findViewById(R.id.LAYOUT_TOP_BACK).setVisibility(View.VISIBLE);
        findViewById(R.id.TOP_BACK_BUTTON).setOnClickListener(this);
        findViewById(R.id.TOP_BACK_BUTTON).setVisibility(View.VISIBLE);
        writeSomeTv = (TextView) findViewById(R.id.write_someTv);
        writeSomeTv.setOnClickListener(this);
        publishContent = (EditText) findViewById(R.id.publish_content);
//		recordBtn = (Button) findViewById(R.id.publish_presstalk_btn);
        mLinemoticon = findViewById(R.id.lin_emoticon);


        gridView = (ScrollGridView) findViewById(R.id.noScrollgridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // adapter = new GridAdapter();
        // gridView.setAdapter(adapter);

        mDia = new CustomDialog(this);
        mDia.setOperationListener(new RegistOnOperationListener());
        mDia.setTitle("选择头像");
        mDia.setMessage("拍照或者从相册选择?");
        mDia.setButtonsText("相册", "拍照");


        adapter = new PublishGridAdapter(this, pubItemList);

        gridView.setAdapter(adapter);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (pubItemList.get(position).isAddPhoto()) {
                    mDia.show();
                } else if (!pubItemList.get(position).isVoice()) {
                    Intent intent = new Intent(CirclePublishActivity.this, PhotoEditActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                } else {

                }
            }
        });


        rightBtn = (Button) findViewById(R.id.TOP_RIGHT_BUTTON);
        rightBtn.setText("发布");
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);
        publishContent.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 强制显示软键盘
                boolean bool = inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                if (bool) {
                    publishContent.requestFocus();
                }
                return false;
            }
        });

    }


    private void initData() {
        pubItemList.clear();
        // imglist = new ArrayList<String>();
        for (int i = 0; i < Bimp.drr.size(); i++) {
            // String path = Bimp.drr.get(i);
            //
            // String newStr = path.substring(path.lastIndexOf("/") + 1,
            // path.lastIndexOf("."));
            // Bimp.getThumbImage(path, newStr + ".jpg",
            // Constant.EXPORT_IMAGE_DIR);
            // imglist.add(Constant.EXPORT_IMAGE_DIR + newStr + ".jpg");

            PublishItem item = new PublishItem();
            item.setPicDrr(Bimp.drr.get(i));
            item.setAddPhoto(false);
            item.setVoice(false);
            pubItemList.add(item);
        }

        PublishItem pItem = new PublishItem();
        pItem.setAddPhoto(true);
        pItem.setVoice(false);


        if (pubItemList.size() < 9) {
            pubItemList.add(pItem);
        }
        adapter.notifyDataSetChanged();
    }

    class RegistOnOperationListener implements CustomDialog.OnOperationListener {

        @Override
        public void onLeftClick() {
            // isFromTakePhoto = false;
            Intent intent = new Intent(CirclePublishActivity.this, PhotoAlbumActivity.class);
            startActivity(intent);
            mDia.cancel();
        }

        @Override
        public void onRightClick() {
            photo();
            mDia.cancel();
        }

    }


    public void photo() {
        IMAGE_FILE_LOCATION = Constant.CACHE_DIR + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showToask("请先插入内存卡！");
            return;
        }
        File file = new File(IMAGE_FILE_LOCATION);

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                String path = IMAGE_FILE_LOCATION;
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
            case 88:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TOP_BACK_BUTTON:
                finish();
                break;
            case R.id.TOP_RIGHT_BUTTON:
                showProgressDialog("正在发布中...");
                rightBtn.setClickable(false);
                apiParams.put("authType", authType);


                bigImgList = new ArrayList<String>();
                String images = "";
                if (Bimp.drr.size() > 0) {
                    uploadFileCount = Bimp.drr.size();
                    for (int i = 0; i < Bimp.drr.size(); i++) {
                        String iconKey = StringUtils.getUUID();
                        images += iconKey + ",";
                    }
                    images = images.substring(0, images.length() - 1);
                }
                if (StringUtils.isEmpty(images)) {
                    showToask("请选择图片!");
                    rightBtn.setClickable(true);
                    hideProgressDialog();
                    return;
                }
                // else if
                // (StringUtils.isEmpty(publishContent.getText().toString()) &&
                // voiceItem == null) {
                // showToask("至少有语音或文字内容哦!");
                // rightBtn.setClickable(true);
                // hideProgressDialog();
                // return;
                // }
                if (null != images && images.length() > 0) {
                    imageMore = images.split(",");
                }
                if (Bimp.drr.size() > 0) {
                    for (int i = 0; i < Bimp.drr.size(); i++) {
                        try {
                            String path = Bimp.drr.get(i);
                            // 大图压缩
                            String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                            BitmapUtil.saveBitmapFronPath(path, newStr + "_800.jpg", Constant.EXPORT_IMAGE_DIR);
                            bigImgList.add(Constant.EXPORT_IMAGE_DIR + "" + newStr + "_800" + ".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (images.length() > 0) {
                    apiParams.put("images", images);
                    if (bigImgList.size() == 1) {
                        String size = "";
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(bigImgList.get(0), options);
                        int height = options.outHeight;
                        int width = options.outWidth;
                        size = "{" + "\"" + "tw" + "\"" + ":" + "\"" + width + "\"" + "," + "\"" + "th" + "\"" + ":" + "\"" + height + "\"" + "}";
                        apiParams.put("size", size);
                    }
                    uploadSmallImage();
                }
                if (StringUtils.isNotEmpty(publishContent.getText().toString())) {
                    apiParams.put("article", publishContent.getText().toString());
                }
                break;
            case R.id.write_someTv:
                writeSomeTv.setSelected(true);
                publishContent.setVisibility(View.VISIBLE);
                mLinemoticon.setVisibility(View.VISIBLE);
//			recordBtn.setVisibility(View.INVISIBLE);
                break;

        }
    }

    /**
     * 上传小图
     */
    private void uploadSmallImage() {
        for (int i = 0; i < imageMore.length; i++) {
            String path = Bimp.drr.get(i);
            String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
            Bimp.getThumbImage(path, newStr + ".jpg", Constant.EXPORT_IMAGE_DIR);
            LogUtil.d("图片", "图片" + Constant.EXPORT_IMAGE_DIR + newStr + ".jpg");
            File file = new File(Constant.EXPORT_IMAGE_DIR + newStr + ".jpg");
//			FileUploader.asyncUpload(imageMore[i], file, this);
            final int z = i;
            //上传图片
            LogUtil.d("上传 图片", "上传 上传" + Constant.EXPORT_IMAGE_DIR + newStr + ".jpg");
            String uploadId = null;
            String phone = null;
            if (!BuildConfig.hasProduct) {
                uploadId = "" + App.getInstance().getCustId(this);
                phone = "" + App.getInstance().getMoblieNo(this);
            } else {
                String relationBean_id = (String) SPUtil.getInstant(this).get("relationBean_id", "");
                String relationBean_phone = (String) SPUtil.getInstant(this).get("relationBean_phone", "");
                uploadId = relationBean_id;
                phone = relationBean_phone;
                SPUtil.getInstant(this).save("relationBean_id","");
                SPUtil.getInstant(this).save("relationBean_phone","");
            }
            ConnectionManager.getInstance().uploadMedicalRecords(uploadId, phone, file, false, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d(TAG, "uploadSmallImage:" + ret);
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(CirclePublishActivity.this, "" + baseBean.msg, Toast.LENGTH_SHORT).show();
                    if (z > imageMore.length - 2) {
                        hideProgressDialog();
                        CirclePublishActivity.this.finish();
                    }
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//		if (!isVieo) {
        gridView.setVisibility(View.VISIBLE);
//			recordBtn.setVisibility(View.VISIBLE);
//			wsLayout.setVisibility(View.VISIBLE);
//			squareLayout.setVisibility(View.GONE);
        initData();
//		} else {
//			
//			wsLayout.setVisibility(View.GONE);
//			gridView.setVisibility(View.GONE);
//			recordBtn.setVisibility(View.GONE);
//			publishContent.setVisibility(View.VISIBLE);
//		}
    }

//    private void RecursionDeleteFile(File file) {
//        if (file.isFile()) {
//            file.delete();
//            return;
//        }
//        if (file.isDirectory()) {
//            File[] childFile = file.listFiles();
//            if (childFile == null || childFile.length == 0) {
//                return;
//            }
//            for (File f : childFile) {
//                RecursionDeleteFile(f);
//            }
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        File f = new File(Constant.EXPORT_IMAGE_DIR);
//        RecursionDeleteFile(f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLeftClick() {
    }

    @Override
    public void onRightClick() {
        deleteVoiceDia.cancel();
    }
}
