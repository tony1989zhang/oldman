package com.firstaid.util.frompost;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.firstaid.util.LogUtil;
import com.firstaid.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by moon.zhong on 2015/2/28.
 */
public class UploadRequest extends PostRequest {
    private static final String BOUNDARY = "---------------------------7d931c5d043e";
    private static final String ENTRY_BOUNDARY = "--" + BOUNDARY;
    private static final String END_BOUNDARY = ENTRY_BOUNDARY + "--\r\n";

    private List<TextForm> mItemForm;

    public UploadRequest(String url, List<TextForm> itemForm, ResponseListener listener) {
        super(url, null, listener);
        this.mItemForm = itemForm;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        LogUtil.d("cx", "=======getBody==start=====" + mItemForm);
        if (mItemForm == null || mItemForm.size() == 0) {
            return null;
        }
        int N = mItemForm.size();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            TextForm textForm;
            for (int i = 0; i < N; i++) {
                textForm = mItemForm.get(i);
                StringBuffer sb = new StringBuffer();
                sb.append(ENTRY_BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"");
                sb.append(textForm.getName());
                sb.append("\"\r\nContent-Type:");
                sb.append(textForm.getMineType());
                sb.append("\r\n\r\n");
                bos.write(sb.toString().getBytes(getParamsEncoding()));
                bos.write(textForm.getValue());
                bos.write("\r\n".getBytes(getParamsEncoding()));

            }
            bos.write(END_BOUNDARY.getBytes(getParamsEncoding()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("cx", "=======end== ---------  end==" );
        Util.saveToSd(new String(bos.toByteArray()));
        return bos.toByteArray();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }
}
