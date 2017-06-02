package com.firstaid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;

import com.firstaid.bean.AlarmInfo;
import com.firstaid.oldman.App;
import com.firstaid.oldman.MainActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.LogUtil;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;
public class MyPushIntentService extends UmengBaseIntentService{
    private static final String TAG = MyPushIntentService.class.getName();
    public static final String MYPUSHKEY = "KEY_POS";
    public static final String KEY_DATA = "KEY_DATA";

// 如果需要打开Activity，请调用Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)；否则无法打开Activity。

    @Override
    protected void onMessage(Context context, Intent intent) {
        // 需要调用父类的函数，否则无法统计到消息送达
        super.onMessage(context, intent);
        try {
            //可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            UTrack.getInstance(context).trackMsgClick(msg);
            Log.d(TAG, "message="+message);    //消息体
            Log.d(TAG, "custom="+msg.custom);    //自定义消息的内容
            Log.d(TAG, "title="+msg.title);    //通知标题
            Log.d(TAG, "text="+msg.text);    //通知内容
            // code  to handle message here
            // ...
            // 完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if(isClickOrDismissed) {

                // 完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }

            LogUtil.d("msg.text","msg.text:" + msg.text);
            AlarmInfo info = App.getInstance().getBeanFromJson(msg.text, AlarmInfo.class);
            LogUtil.d("info","info:" + info);

            NotificationManager mNotificationManager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int icon = R.mipmap.ic_launcher;

            CharSequence tickerText = info.msg;
            Notification.Builder builder = new Notification.Builder(context).setTicker(tickerText).setSmallIcon(icon);

            Intent notificationIntent = new Intent(this, MainActivity.class).putExtra(MYPUSHKEY, 2).putExtra(KEY_DATA, msg.text);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = builder.setContentIntent(pendingIntent).setContentTitle("救命啊").setContentText(tickerText).getNotification();
            mNotificationManager.notify(10,notification);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}