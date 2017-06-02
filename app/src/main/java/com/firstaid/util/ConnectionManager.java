package com.firstaid.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.util.frompost.MultipartRequest;
import com.firstaid.util.frompost.MultipartRequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/2.
 */
public class ConnectionManager {

    private static final String PATH_CHECK_USER = "/medicalmanager/app/common/checkUser.do";
    private static final String PATH_SEND_SMS = "/medicalmanager/app/common/sendSms.do";
    private static final String PATH_USER_LOGIN = "/medicalmanager/app/common/userLogin.do";
    private static final String PATH_QUERY_NEWS = "/medicalmanager/app/elder/queryLatelyNumNews.do";
    private static final String PATH_RELATIVE_INDEX = "/medicalmanager/app/relatives/relativesIndex.do";
    private static final String PATH_QUERY_RELATIVE_LIST = "/medicalmanager/app/relatives/queryRelativesList.do";//亲属列表
    private static final String PATH_QUERY_RELATION = "/medicalmanager/app/center/queryRelation.do";//关系人列表
    private static final String PATH_SHARE_BINGLI = "/medicalmanager/app/elder/shareMedicalRecords.do";
    private static final String PATH_DELETE_RELATION = "/medicalmanager/app/center/deleteRelation.do";
    private static final String PATH_MODIFY_RELATION = "/medicalmanager/app/center/modifyRelation.do";
    private static final String PATH_ADD_RELATION = "/medicalmanager/app/center/addRelation.do";
    private static final String PATH_QUERY_BE_RELATION = "/medicalmanager/app/center/queryBeRelation.do";
    private static final String PATH_AGREE_RELATION_APPLAY = "/medicalmanager/app/elder/agreeRelationApply.do";
    private static final String PATH_QUERY_MY_INDEX_INFO = "/medicalmanager/app/ambulance/queryMyIndexInfo.do";
    private static final String PATH_QUERY_HELP_RECORD = "/medicalmanager/app/relatives/queryHelpRecord.do";
    private static final String PATH_CHANGE_PHONE_NUMBER = "/medicalmanager/app/ambulance/changePhoneNumber.do";
    private static final String PATH_OPEN_DEVICE = "/medicalmanager/app/elder/openDevice.do";
    private static final String PATH_QUERY_NEAR_AMBULANCE = "/medicalmanager/app/ambulance/queryNearAmbulance.do";
    private static final String PATH_QUERY_DELIVERLIST = "/medicalmanager/app/relatives/queryDeliverList.do";
    private static final String PATH_QUERY_MESSAGELIST = "/medicalmanager/app/ambulance/queryMessageList.do";
    private static final String PATH_DELTE_ACCOUNT = "/medicalmanager/app/common/deleteAccount.do";
    private static final String PATH_QUERY_RESERVE_PLAN = "/medicalmanager/app/ambulance/queryReservePlan.do";
    private static final String PATH_UPLOAD_LOCATION = "/medicalmanager/app/common/uploadLongitudeLatitude.do";
    private static final String PATH_MODIFY_MY_INFO = "/medicalmanager/app/ambulance/modifyCusBasicInfo.do";
    private static final String PATH_ALLOW_VISITME = "/medicalmanager/app/elder/allowVisitMe.do";
    private static final String PATH_QUERY_NEAR_120 = "/medicalmanager/app/center/queryNear120.do";
    private static final String PATH_ADD_ALARM = "/medicalmanager/app/center/receiveAlarm.do";
    private static final String PATH_QUERY_PRODUCT_LIST = "/medicalmanager/app/relatives/queryProductList.do";
    private static final String PATH_INSERT_ADDRESS = "/medicalmanager/app/relatives/insertAddress.do";
    private static final String PATH_QUERY_ADDRESS_LIST = "/medicalmanager/app/relatives/queryAddressList.do";
    private static final String PATH_UPDATE_ADDRESS_LIST = "/medicalmanager/app/relatives/updateAddress.do";
    private static final String PATH_DELETE_ADDRESS = "/medicalmanager/app/relatives/deleteAddress.do";
    private static final String PATH_QUERY_OPEN_DEVICE_LIST = "/medicalmanager/app/elder/queryOpenDeviceList.do";
    private static final String PATH_EVALUATION_HELP = "/medicalmanager/app/relatives/evaluationHelp.do";
    private static final String PATH_QUERY_BALANCE = "/medicalmanager/app/common/queryBalanceByCusId.do";
    private static final String PATH_BALANCE_RECORD = "/medicalmanager/app/common/queryBalanceSeqByCusId.do";
    private static final String PATH_QUERY_DRAW_ACC_LIST = "/medicalmanager/app/common/queryWithdrawAccList.do";
    private static final String PATH_QUERY_INVITATION_INFO = "/medicalmanager/app/relatives/queryInvitationInfo.do";
    private static final String PATH_ADD_RELATIVES = "/medicalmanager/app/elder/addRelatives.do";
    private static final String PATH_QUERY_120_CENTER = "/medicalmanager/app/center/queryCenter.do";
    private static final String PATH_QUERY_SITE_MSG = "/medicalmanager/app/common/querySiteMsg.do";
    private static final String PATH_MODIFY_RELATION_ORDER = "/medicalmanager/app/center/modifyRelationOrder.do";
    private static final String PATH_TIXIAN_WITHDRAW = "/medicalmanager/app/common/withdraw.do";
    private static final String PATH_UPDATEWITHDRAWACC = "/medicalmanager/app/common/updateWithdrawAcc.do";
    private static final String PATH_INSERTWITHDRAWACC = "/medicalmanager/app/common/insertWithdrawAcc.do";
    private static final String PATH_QUERYWITHDRAWACCLIST = "/medicalmanager/app/common/queryWithdrawAccList.do";
    private static final String PATH_SENDVOICE = "/medicalmanager/app/common/sendVoice.do";//播放语音
    private static final String MOBILE_NO = "mobileNo";
    private static final String PATH_ADDRECORDS = "/medicalmanager/app/common/addRecords.do";
    private static final String PATH_QUERYRECORDS = "/medicalmanager/app/common/queryRecords.do";
    private static final String PATH_DELETERECORDS = "/medicalmanager/app/common/deleteRecords.do";
    private static final String PATH_UPLOADMEDICALRECORDS = "/medicalmanager/app/elder/uploadMedicalRecords.do";
    private static final String PATH_ONEKEYALARM = "/medicalmanager/app/center/oneKeyAlarm.do";
    private static final String PATH_QUERYNEARHOSPITAL = "/medicalmanager/app/ambulance/queryNearHospital.do";
    private static final String PATH_QUERYMEDICALRECORDS = "/medicalmanager/app/elder/queryMedicalRecords.do"; //查找病例
    private static final String PATH_QUERYCUSTINFO = "/medicalmanager/app/common/queryCustInfo.do";
    private static final String PATH_BINDDEVICETOKEN = "/medicalmanager/app/common/bindDeviceToken.do";

    private static ConnectionManager mConnectionManager;

    private ConnectionManager() {

    }

    public static final ConnectionManager getInstance() {
        if (mConnectionManager == null) {
            mConnectionManager = new ConnectionManager();
        }
        return mConnectionManager;
    }

    public Request sendSms(Context context, String mobileNo, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, mobileNo);
        return ConnectionUtil.getInstance().doPost(context, PATH_SEND_SMS, params, listener);
    }

    public Request checkUser(Context context, String mobileNo, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, mobileNo);
        return ConnectionUtil.getInstance().doPost(context, PATH_CHECK_USER, params, listener);
    }

    public Request userlogin(Context context, String mobileNo, String smsCode, String invitationCode, String orgSmsCode, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, mobileNo);
        params.put("smsCode", smsCode);
        if (!TextUtils.isEmpty(invitationCode)) {
            params.put("invitationCode", invitationCode);
        }
        params.put("orgSmsCode", orgSmsCode);
        params.put("custType", "0" + BuildConfig.custType);
        return ConnectionUtil.getInstance().doPost(context, PATH_USER_LOGIN, params, listener);
    }

    public Request queryNews(Context context, String number, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("number", number);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_NEWS, params, listener);
    }

    public Request getRelativeIndex(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_RELATIVE_INDEX, params, listener);
    }

    public Request queryRelativeList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_RELATIVE_LIST, params, listener);
    }

    public Request queryRelation(Context context, String moblieNo, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, moblieNo);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_RELATION, params, listener);
    }

    public Request shareBingli(Context context, String moblieNo, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, moblieNo);
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_SHARE_BINGLI, params, listener);
    }

    public Request deleteRelation(Context context, String id, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return ConnectionUtil.getInstance().doPost(context, PATH_DELETE_RELATION, params, listener);
    }

    public Request modifyRelation(Context context, String id, String mobileNo, String relationMobile, String relationName, ConnectionUtil.OnDataLoadEndListener listener){
       return modifyRelation(context, id, mobileNo, relationMobile, relationName, false, listener);
    }
    public Request modifyRelation(Context context, String id, String mobileNo, String relationMobile, String relationName,boolean isCheck, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        if (!TextUtils.isEmpty(mobileNo) && null != mobileNo) {
            params.put(MOBILE_NO, mobileNo);
        }
        if (!TextUtils.isEmpty(relationMobile) && null != relationMobile) {
            params.put("relationMobile", relationMobile);
        }
        if (!TextUtils.isEmpty(relationName) && null != relationName) {
            params.put("relationName", relationName);
        }
        if (isCheck)
            params.put("isAlarmOrder","1");
        else
            params.put("isAlarmOrder","0");
        LogUtil.d("cx", id + "   " + mobileNo + "   " + relationName + "  " + relationMobile);
        return ConnectionUtil.getInstance().doPost(context, PATH_MODIFY_RELATION, params, listener);
    }

    public Request addRelation(Context context, String custId, String mobileNo, String relationMobile, ConnectionUtil.OnDataLoadEndListener listener){

        return  addRelation(context, custId, mobileNo, relationMobile, false, listener);
    }
    public Request addRelation(Context context, String custId, String mobileNo, String relationMobile,boolean isCheck, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("custId", custId);
        params.put(MOBILE_NO, mobileNo);
        params.put("relationMobile", relationMobile);
        params.put("custName", App.getInstance().getCustName(context));
        if (isCheck)
        params.put("isAlarmOrder","1");
        else
        params.put("isAlarmOrder","0");
        LogUtil.d("cx", custId + "   " + mobileNo + "   " + relationMobile + "  " + App.getInstance().getCustName(context));

        return ConnectionUtil.getInstance().doPost(context, PATH_ADD_RELATION, params, listener);
    }

    public Request queryBeRelation(Context context, String moblieNo, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put(MOBILE_NO, moblieNo);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_BE_RELATION, params, listener);
    }

    public Request agreenRelationApplay(Context context, String cusId, String relationId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("relationId", relationId);
        return ConnectionUtil.getInstance().doPost(context, PATH_AGREE_RELATION_APPLAY, params, listener);
    }

    public Request queryMyIndexInfo(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_MY_INDEX_INFO, params, listener);
    }

    public Request queryHelpRecord(Context context, String cusId, String phone, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("phone", phone);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_HELP_RECORD, params, listener);
    }

    public Request changePhoneNuber(Context context, String cusId, String newphone, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        LogUtil.d("cx", "--userId  " + cusId);
        params.put("userId", cusId);
        params.put("newPhone", newphone);
        params.put("smsCode", "1");
        params.put("orgSmsCode", "1");
        return ConnectionUtil.getInstance().doPost(context, PATH_CHANGE_PHONE_NUMBER, params, listener);
    }

    public Request openDevice(Context context, String cusId, String deviceNo, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("useId", cusId);
        params.put("openId", cusId);
        params.put("machineCode", deviceNo);
        params.put("deviceNo", deviceNo);
        // 操作失败！机器码不存在或已被使用，请重新输入！
        return ConnectionUtil.getInstance().doPost(context, PATH_OPEN_DEVICE, params, listener);
    }

    public Request queryNearAmbulance(Context context, String latitude, String longitude, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("latitude", latitude);//横坐标，经度
        params.put("longitude", longitude);//纵坐标,纬度
        LogUtil.d("cx", "latitude " + latitude + "   " + longitude);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_NEAR_AMBULANCE, params, listener);
    }


    public Request queryDeliverList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_DELIVERLIST, params, listener);
    }

    public Request sendVoice(Context context, String cusId, String msg, String phone, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("custId", cusId);
        params.put("msg", msg);
        params.put("phone", phone);
        LogUtil.d("sendVoice", "sendVoice:" + params);
        return ConnectionUtil.getInstance().doPost(context, PATH_SENDVOICE, params, listener);
    }

    public Request queryMessageList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        LogUtil.d("cx", "cusId " + cusId);
        params.put("custId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_SITE_MSG, params, listener);
    }

    public Request deleteAccount(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_DELTE_ACCOUNT, params, listener);
    }

    public Request queryReservePlan(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        if (null != cusId) {
            params.put("cusId", cusId);
        }
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_RESERVE_PLAN, params, listener);
    }

    public Request uploadLocation(Context context, String cusId, String latitude, String longitude, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        LogUtil.d("cx", BuildConfig.custType + " == cusId " + cusId + "  latitude " + latitude + "   " + longitude);
        LogUtil.d("uploadLocation", "uploadLocation:" + " == cusId " + cusId + "  latitude " + latitude + "  Longitude" + longitude + "时间间隔:" + System.currentTimeMillis());
        params.put("custId", cusId);
        params.put("latitude", latitude);//横坐标，经度
        params.put("longitude", longitude);//纵坐标,纬度
        return ConnectionUtil.getInstance().doPost(context, PATH_UPLOAD_LOCATION, params, listener);
    }

    /*public Request modifyMyInfo(Context context,String cusId,String headImgFile,String nickname,ConnectionUtil.OnDataLoadEndListener listener){
        final Map<String,String> params = new HashMap<String,String>();
        params.put("cusId", cusId);
        if(!TextUtils.isEmpty(headImgFile)){
            params.put("headImgFile",headImgFile);
        }
        if(!TextUtils.isEmpty(nickname)){
            params.put("nickname",nickname);
        }
        LogUtil.d("cx", "cusId " + cusId + "  ----headImgFile--- " + headImgFile + "   " + nickname);
        return ConnectionUtil.getInstance().doPost(context,PATH_MODIFY_MY_INFO,params,listener);
    }*/
    public Request modifyMyInfo(Context context, String cusId, String headImgFile, String nickname, String age, String medicalHistory, ConnectionUtil.OnDataLoadEndListener listener) {
        MultipartRequestParams params = new MultipartRequestParams();
        params.put("id", cusId);
        LogUtil.d("cx", cusId + " headImgFile " + headImgFile);
        if (!TextUtils.isEmpty(headImgFile)) {

            params.put("headImgFile", new File(headImgFile));
        } else {
            params.put("headImgFile", new File(""));
        }
        if (!TextUtils.isEmpty(nickname)) {
            params.put("nickname", nickname);
        }
        if (!TextUtils.isEmpty(age)) {
            params.put("age", age);
        }
        if (!TextUtils.isEmpty(medicalHistory)) {
            params.put("medicalHistory", medicalHistory);
        }
        LogUtil.d("ret", "ret:" + params);
        Request request = new MultipartRequest(ConnectionUtil.getInstance().getUrl(PATH_MODIFY_MY_INFO), params, listener);
        ConnectionUtil.getInstance().mQueue.add(request);
        return request;
    }

    public Request allowVisiteMe(Context context, String cusId, String relationId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("relationId", relationId);
        return ConnectionUtil.getInstance().doPost(context, PATH_ALLOW_VISITME, params, listener);
    }

    public Request queryNear120(Context context, String latitude, String longitude, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("latitude", latitude);//横坐标，经度
        params.put("longitude", longitude);//纵坐标,纬度
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_NEAR_120, params, listener);
    }


    public Request queryNearHospital(Context context, String latitude, String longitude, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("latitude", latitude);//横坐标，经度
        params.put("longitude", longitude);//纵坐标,纬度
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_NEAR_120, params, listener);
    }

    public Request addAlarm(Context context, String mobileNo, String alarmTime, String linkInfo, String alarmTitle,
                            String province, String city, String alarmAddress, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNo", mobileNo);
        params.put("alarmTime", alarmTime);
        params.put("linkInfo", linkInfo);
        params.put("alarmTitle", alarmTitle);
        params.put("province", province);
        params.put("city", city);
        params.put("alarmAddress", alarmAddress);

        return ConnectionUtil.getInstance().doPost(context, PATH_ADD_ALARM, params, listener);
    }

    public Request queryProductList(Context context, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_PRODUCT_LIST, params, listener);
    }

    public Request insertAddress(Context context, String cus_id, String receiver, String phone, String zipCode, String address, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cus_id);
        params.put("receiver", receiver);
        params.put("phone", phone);
        params.put("zipCode", zipCode);
        params.put("address", address);
        // params.put("defaultFlag","Y");
        return ConnectionUtil.getInstance().doPost(context, PATH_INSERT_ADDRESS, params, listener);
    }


    public Request updateAddress(Context context, String addresId, String receiver, String phone, String zipCode, String address, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("addresId", addresId);
        params.put("receiver", receiver);
        params.put("phone", phone);
        params.put("zipCode", zipCode);
        params.put("address", address);
        // params.put("defaultFlag","Y");
        return ConnectionUtil.getInstance().doPost(context, PATH_UPDATE_ADDRESS_LIST, params, listener);
    }

    public Request queryAddressList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_ADDRESS_LIST, params, listener);
    }

    public Request deleteAddress(Context context, String addresId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("addresId", addresId);
        return ConnectionUtil.getInstance().doPost(context, PATH_DELETE_ADDRESS, params, listener);
    }

    public Request queryOpenDeviceList(Context context, String useId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("useId", useId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_OPEN_DEVICE_LIST, params, listener);
    }

    public Request evaluationHelp(Context context, String cusId, String helpId, String evaluationType, String evaluateContent, String evaluateStar, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("helpId", helpId);
        //评价类型，0医院或120中心，1急救车
        params.put("evaluationType", evaluationType);
        params.put("evaluateContent", evaluateContent);
        params.put("evaluateStar", evaluateStar);
        return ConnectionUtil.getInstance().doPost(context, PATH_EVALUATION_HELP, params, listener);
    }

    public Request queryBalance(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_BALANCE, params, listener);
    }

    public Request queryBalanceRecord(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_BALANCE_RECORD, params, listener);
    }

    public Request queryAcountList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_DRAW_ACC_LIST, params, listener);
    }

    public Request queryInvitationInfo(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_INVITATION_INFO, params, listener);
    }

    public Request addRelatives(Context context, String cusId, String relativePhone, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("phone", relativePhone);
        return ConnectionUtil.getInstance().doPost(context, PATH_ADD_RELATIVES, params, listener);
    }

    public Request query120Center(Context context, String province, String city, String county, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("province", province);
        params.put("city", city);
        params.put("county", county);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERY_120_CENTER, params, listener);
    }

    public Request modifyRelationOrder(Context context, String cusId, String relations, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("relations", relations);

        return ConnectionUtil.getInstance().doPost(context, PATH_MODIFY_RELATION_ORDER, params, listener);
    }

    //添加录音的信息
    public Request addRecords(Context context, String mobileNo, String callMobileNo, String objectKey, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNo", mobileNo);
        params.put("callMobileNo", callMobileNo);
        params.put("objectKey", objectKey);
        return ConnectionUtil.getInstance().doPost(context, PATH_ADDRECORDS, params, listener);
    }

    //查询录音信息
    public Request queryRecords(Context context, String mobileNo, String callMobileNo, String objectKey, String recordTime, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        if (!TextUtils.isEmpty(mobileNo) && null != mobileNo)
            params.put("mobileNo", mobileNo);
        if (!TextUtils.isEmpty(callMobileNo) && null != callMobileNo)
            params.put("callMobileNo", callMobileNo);
        if (!TextUtils.isEmpty(objectKey) && null != objectKey)
            params.put("objectKey", objectKey);
        if (!TextUtils.isEmpty(recordTime) && null != recordTime)
            params.put("recordTime", recordTime);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERYRECORDS, params, listener);
    }

    //删除录音信息
    public Request deleteRecords(Context context, String mobileNo, String callMobileNo, String objectKey, String recordTime, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        if (!TextUtils.isEmpty(mobileNo) && null != mobileNo)
            params.put("mobileNo", mobileNo);
        if (!TextUtils.isEmpty(callMobileNo) && null != callMobileNo)
            params.put("callMobileNo", callMobileNo);
        if (!TextUtils.isEmpty(objectKey) && null != objectKey)
            params.put("objectKey", objectKey);
        if (!TextUtils.isEmpty(recordTime) && null != recordTime)
            params.put("recordTime", recordTime);
        return ConnectionUtil.getInstance().doPost(context, PATH_DELETERECORDS, params, listener);
    }


    /**
     * 查询提现账号密码
     */
    public Request queryWithdrawAccList(Context context, String cusId, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERYWITHDRAWACCLIST, params, listener);
    }

    /**
     * 提现
     */
    public Request withDraw(Context context, String cusId, String withdrawAccId, String withdrawAmount, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("withdrawAccId", withdrawAccId);
        params.put("withdrawAmount", withdrawAmount);
        return ConnectionUtil.getInstance().doPost(context, PATH_TIXIAN_WITHDRAW, params, listener);
    }

    /**
     * 设置提现账号
     */
    public Request insertWithDrawacc(Context context, String cusId, String accType, String openBank, String accName, String accNo, String defaultFlag, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("cusId", cusId);
        params.put("accType", accType);
        if ((!TextUtils.isEmpty(openBank)) && null != openBank) {
            params.put("openBank", openBank);
        }
        if ((!TextUtils.isEmpty(accName)) && null != accName) {
            params.put("accName", accName);
        }
        params.put("accNo", accNo);
        params.put("defaultFlag", defaultFlag);
        LogUtil.d("insertWithDrawacc", "insertWith:" + params.toString());
        return ConnectionUtil.getInstance().doPost(context, PATH_INSERTWITHDRAWACC, params, listener);
    }

    /**
     * 修改提现账号
     */
    public Request updateWithdrawAcc(Context context, String accType, String withdrawAccId, String openBank, String accName, String accNo, String defaultFlag, ConnectionUtil.OnDataLoadEndListener listener) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("withdrawAccId", withdrawAccId);
        if (!TextUtils.isEmpty(accType) && null != accType) {
            params.put("accType", accType);
        }
        if ((!TextUtils.isEmpty(openBank)) && null != openBank) {
            params.put("openBank", openBank);
        }
        if ((!TextUtils.isEmpty(accName)) && null != accName) {
            params.put("accName", accName);
        }
        if (!TextUtils.isEmpty(accNo) && null != accNo) {
            params.put("accNo", accNo);
        }
        if (!TextUtils.isEmpty(defaultFlag) && null != defaultFlag) {
            params.put("defaultFlag", defaultFlag);
        }
        LogUtil.d("insertWithDrawacc", "insertWith:" + params.toString());
        return ConnectionUtil.getInstance().doPost(context, PATH_UPDATEWITHDRAWACC, params, listener);
    }


    //    public static byte[] callPhone(String urlPath) throws Exception {
//        // TODO Auto-generated method stub
//        URL url=new URL(urlPath);//实例化URL对象url
//        HttpURLConnection connection=(HttpURLConnection) url.openConnection();//实例化HttpURLConnection对象connection
//        connection.setConnectTimeout(5000);//设置连接超时时间为5秒
//        connection.setRequestProperty("Accept-Charset", "gb2312");
//        connection.setRequestProperty("Content-type", "application/json");
//        // 设定请求的方法为"POST"，默认是GET
//        connection.setRequestMethod("POST");
//        int code=connection.getResponseCode();//获取状态码
//        //如果状态码请求成功的话，即code等于HttpURLConnection.HTTP_OK，也可以写成code==200
//        if(code==HttpURLConnection.HTTP_OK){
//            InputStream inputStream=connection.getInputStream();//获得输入流，返回一个InputStream对象
//            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();//实例化一个字节数组输出输入流对象
//            byte[] buffer=new byte[1024];//实例化一个字节数组对象
//            int len=0;//定义一个变量，初始值为0
//            //当获取到的输入流有数据时，循环写入数据
//            while((len=inputStream.read(buffer))!=-1){
//                outputStream.write(buffer, 0, len);//写入数据
//            }
//            inputStream.close();//关闭输入流
//            return outputStream.toByteArray();//返回数据字节数组
//        }
//        return null;
//    }
    //图片上传
    public Request uploadMedicalRecords( String cusId, String mobileNo, File recordsFile, ConnectionUtil.OnDataLoadEndListener listener) {
        return uploadMedicalRecords(cusId, mobileNo, recordsFile,true, listener);
    }

    @NonNull
    public Request uploadMedicalRecords(String cusId, String mobileNo, File recordsFile, boolean upload,ConnectionUtil.OnDataLoadEndListener listener) {
        MultipartRequestParams params = new MultipartRequestParams();
        params.put("cusId", cusId);
        LogUtil.d("cx", cusId + " recordsFile " + recordsFile + "mobileNo:" + mobileNo + "cusId:" + cusId);
        if (!TextUtils.isEmpty(mobileNo)) {
            params.put("recordsFile", recordsFile,upload);
        } else {
            params.put("recordsFile", new File(""));
        }
        if (!TextUtils.isEmpty(mobileNo)) {
            params.put("mobileNo", mobileNo);
        }
        LogUtil.d("params","params:" + params);
        Request request = new MultipartRequest(ConnectionUtil.getInstance().getUrl(PATH_UPLOADMEDICALRECORDS), params, listener);
        ConnectionUtil.getInstance().mQueue.add(request);
        return request;
    }

    public Request onKeyAlarm(Context context, String receiveCustId, String mobileNo, ConnectionUtil.OnDataLoadEndListener listener) {
        Request request = oneKeyAlarm(context, receiveCustId, mobileNo, null
                , null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, listener);
        return request;
    }

    public Request oneKeyAlarm(Context context, String receiveCustId, String mobileNo, String alarmTime, String linkInfo, String alarmTitle, String alarmInfo, String alarmAddress, String saveNum, String needAnbulanceNum, String isAttack,
                               String isSpecifyHos, String hospital, String incidentTime, String waitingAdd, String belongArea, String patientName, String patientSex, String patientAge, String patientMobile, String isHarass, String policeId
            , String policeMobile, String policeName, String status, String province, String city, String talkTime, ConnectionUtil.OnDataLoadEndListener listener
    ) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("receiveCustId", receiveCustId);
        params.put("mobileNo", mobileNo);
        if ((!TextUtils.isEmpty(alarmTime)) && null != alarmTime) {
            params.put("alarmTime", alarmTime);
        }
        if ((!TextUtils.isEmpty(linkInfo)) && null != linkInfo) {
            params.put("linkInfo", linkInfo);
        }
        LogUtil.d("OnKeyAlarm", "OnKeyAlarm:" + params);
        return ConnectionUtil.getInstance().doPost(context, PATH_ONEKEYALARM, params, listener);
    }

    public Request queryMedicalRecords(Context context, String mobileNo, ConnectionUtil.OnDataLoadEndListener listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("mobileNo", mobileNo);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERYMEDICALRECORDS, params, listener);
    }

    public Request queryCustInfo(Context context, String mobile, ConnectionUtil.OnDataLoadEndListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        return ConnectionUtil.getInstance().doPost(context, PATH_QUERYCUSTINFO, params, listener);
    }


    public Request bindDeviceToken(Context context,String custId,String deviceToken, ConnectionUtil.OnDataLoadEndListener listener){
        HashMap<String, String> params = new HashMap<>();
        params.put("custId", custId);
        params.put("deviceToken",deviceToken);
        LogUtil.d("bindDeviceToken","bindDeviceToken:" + params);
        return ConnectionUtil.getInstance().doPost(context, PATH_BINDDEVICETOKEN, params, listener);

    }
}
