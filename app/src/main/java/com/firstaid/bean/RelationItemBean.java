package com.firstaid.bean;

/**
 * Created by lenovo on 2016/1/6.
 */
public class RelationItemBean {

    public String id;

    public String custId;

    public String mobileNo;

    public String relationId;

    public String relationMobile;

    public String relationName;

    /**
     *状态0-未通过1-已通过
     * */
    public String relationStatus;

    /**
     * 允许关系人访问我的足迹标识(Y-允许，N-不允许)，针对老人端特设。
     * */
    public String allowFlag;


    /**
     *我被允许访问关系人的足迹标识(Y-允许，N-不允许)，针对亲属端特设。
     * */
    public String allowedFlag;

    /**
     * 01-老人端02-亲属端03-急救车04-120中心09-医院
     */
    public String relationType;

    public String relativesRelation;

    public String smallImageUrl;

//    public String bigImageUrl;

}
