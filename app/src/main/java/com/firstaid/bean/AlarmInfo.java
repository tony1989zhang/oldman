package com.firstaid.bean;

import java.io.Serializable;

/**
 * Created by tony on 2016/4/12.
 */
public class AlarmInfo implements Serializable {
    //    alarmId alarmType msg
    public String alarmId;
    public String alarmType;
    public String msg;

    @Override
    public String toString() {
        return "alarmId" + alarmId + "alarmType:" + alarmType + "msg:" + msg;
    }
}
