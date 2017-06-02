package com.firstaid.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/1.
 */
public class BaseBean implements Serializable {

    /**交易是否成功，true：是，false：否*/
    public boolean success = false;

    /**交易结果响应消息，如果交易失败，则返回交易失败的原因。*/
    public String msg = "未知错误";

}
