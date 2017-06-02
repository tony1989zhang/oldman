package com.firstaid.bean;

import com.firstaid.bean.BaseBean;

/**
 * Created by lenovo on 2016/1/19.
 */
public class AddressDataBean extends BaseBean {

    public String id;

    public String cus_id;

    public String receiver;

    public String phone;

    public String zip_code;

    public String address;

    //是否默认收货地址，Y是，N否
    public String default_flag;

    public String create_time;
}
