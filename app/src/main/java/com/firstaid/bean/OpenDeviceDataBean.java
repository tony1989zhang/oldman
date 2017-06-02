package com.firstaid.bean;


/**
 * Created by lenovo on 2016/1/19.
 */
public class OpenDeviceDataBean extends BaseBean {

    public String id;

    public String device_no;
    public String machine_code_id;
    //开通标识，Y已开通，N未开通
    public String open_flag;

    public String insert_flag;
    public String alarm_flag;
    public String open_time;
    //使用客户id
    public String use_id;
    //开通客户id
    public String open_id;
    public String remark;

    public String nikename;

}
