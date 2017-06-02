package com.firstaid.bean;

/**
 * Created by lenovo on 2016/1/6.
 */
public class BeRelationItemBean extends BaseBean {

    public String id;

    public String custId;

    public String mobileNo;

    public String relationId;

    public String relationMobile;

    public String relationName;

    /**状态0-未通过1-已通过*/
    public String relationStatus;

    public String allowFlag;//是否允许查看足迹 老人端设置
    public String allowedFlag;//针对亲属端特设。亲属端设置
    public String relativesRelation;//亲属名称
    public String  smallImageUrl;//亲属头像
    public String bigImageUrl;//亲属头像，大图
}
