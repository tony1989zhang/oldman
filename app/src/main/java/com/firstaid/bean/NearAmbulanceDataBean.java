package com.firstaid.bean;

/**
 * Created by Administrator on 2016/1/9.
 */
public class NearAmbulanceDataBean extends BaseBean {

    public String cusId; //急救车ID
//    public String cus_id; //急救车ID

    //车牌号
    public String idCardNo;

    //评分
    public int evaluateStar;

    //单位名称
    public String unitName;

    //绑定手机号
    public String bindingPhone;

    //固定电话
    public String fixTelephone;

    //状态：00待接，01已接，02已完成，03未完成
    public String status;

    public double latitude;

    public double longitude;


    @Override
    public String toString() {
        return "cus_id:" + cusId + "bindingPhone:" + bindingPhone + "latitude:" + latitude + "longitude:" + longitude;
    }
}
