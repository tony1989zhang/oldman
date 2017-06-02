package com.firstaid.util.frompost;

import java.io.UnsupportedEncodingException;

/**
 * Created by moon.zhong on 2015/2/28.
 */
public class TextForm {

    private String value ;
    private String name ;

    public TextForm(String name,String value) {
        this.name = name;
        this.value = value;
    }

    public byte[] getValue() {
        try {
            byte[] bytes = value.getBytes("utf-8") ;
            return bytes ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getName() {
        return name;
    }


    public String getMineType() {
        return "text/plain";
    }

}
