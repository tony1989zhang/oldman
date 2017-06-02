package com.firstaid.bean;

/**
 * Created by Administrator on 2016/1/2.
 */
public class NewsItemBean extends BaseBean{

    public String id;

    public String title;

    /**分类*/
    public String classify;

    public String publish_time;

    /**浏览数*/
    public String views;

    public String content;

    /**备注*/
    public String remark;

    public String[] img_urls;
}
