package com.firstaid.bean;

/**
 * Created by lenovo on 2016/1/20.
 */
public class HelpRecordDataBean extends BaseBean {

    //求救记录id，评价是需上传此id
    public String helpId;
    //营救方id，比如医院id，120中心id
    public String rescueId;
    public String rescueName;
    public String ambulanceId;
    //急救车车牌号
    public String ambulanceNo;
    /**营救方评价状态，Y已评价，N未评价*/
    public String rescueState;
    /**急救车评价状态，Y已评价，N未评价*/
    public String ambulanceState;
    /**营救方评价时间，格式yyyy-MM-dd HH:mm:ss*/
    public String rEvaluateTime;
    /**营救方评价星级*/
    public String rEvaluateStar;
    /**营救方评价内容*/
    public String rEvaluateContent;
    /**急救车评价时间，格式yyyy-MM-dd HH:mm:ss*/
    public String aEvaluateTime;
    /**急救车评价星级*/
    public String aEvaluateStar;
    /**急救车评价内容*/
    public String aEvaluateContent;
}
