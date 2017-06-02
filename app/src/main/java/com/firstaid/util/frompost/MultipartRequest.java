package com.firstaid.util.frompost;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.Util;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/1/20.
 */
public class MultipartRequest extends Request<JSONObject> {

    private ConnectionUtil.OnDataLoadEndListener listener = null;
    private MultipartRequestParams params = null;
    private HttpEntity httpEntity = null;

    public MultipartRequest(String url, MultipartRequestParams params,ConnectionUtil.OnDataLoadEndListener listener) {
        this(Method.POST,params, url, null,listener);
    }

    public MultipartRequest(int method,MultipartRequestParams params, String url,
                            Context context,ConnectionUtil.OnDataLoadEndListener listener) {
        super(method, url, null);
        // TODO Auto-generated constructor stub
        this.params = params;
        this.listener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        // TODO Auto-generated method stub
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LogUtil.d("cx", "IOException writing to ByteArrayOutputStream");
            }
            LogUtil.d("cx","bodyString is bbbbbbbbbbbbbb:" );
            Util.saveToSd(new String(baos.toByteArray()));
        }
        return baos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // TODO Auto-generated method stub
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            listener.OnLoadEnd(null);
            return Response.error(new ParseError(response));
        } catch (JSONException je) {
            listener.OnLoadEnd(null);
            return Response.error(new ParseError(response));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // TODO Auto-generated method stub
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        //MainApplication.getInstance().addSessionCookie(headers);
        return headers;
    }

    @Override
    public String getBodyContentType() {
        // TODO Auto-generated method stub
        String str = httpEntity.getContentType().getValue();
        return httpEntity.getContentType().getValue();
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        if(listener != null) {
            listener.OnLoadEnd(response.toString());
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        // TODO Auto-generated method stub
        if(listener != null) {
            listener.OnLoadEnd(null);
        }
    }}