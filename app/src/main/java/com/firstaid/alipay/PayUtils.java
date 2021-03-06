package com.firstaid.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by tony on 2016/2/6.
 */
public class PayUtils {
    // 商户PID
    public static final String PARTNER = "2088801299241271";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALa0exV5VEXwGhPo" +
            "WN88hlF+jQhr+dlvKDeTv1c3rYRXz8/RlavqoYP3+4lcWuIRq6xWdKVZToHKhKML" +
            "MTsTADGVj2xVF0+Mj0AbcPyKBQBVDcJz5I9cLSSNf3OfRRDWY34pR4T8Zl0dfSnD" +
            "vUU3w8W+MhKRQLSDLJj6J+E6MTxrAgMBAAECgYA9TR8DHHtnH+FQDqRK+n7CtyhQ" +
            "zBG/n6kpxJvtgpPolKH4Q4TpcscQhVLTSrZ/pZgpMbTfibBqoe2Q2v+Ap6Zna2ta" +
            "SqLTkijam6Ik+CIZw1ifjGsXg9ghKlfLP0Obkxj82PtYBBcTDuPmUq7aIBcoknlM" +
            "8ge7nVlJjVfLB+Hl+QJBAN6w95sVT0PCVsfgRFQJ2XZ9effMOB4ItAUVoUr1NNqs" +
            "H6RjSz+0pIKn6oSonCLfiWzHkC1JFcEwzDaYHuatnV8CQQDSCGf3hhRMJgAI3zc/" +
            "RU1Xc5kYQYIiH62FUSgVwptHiQixepWZu1ADB/I/9qYu8YX1iZPTnpuu6jKyDzCp" +
            "ubB1AkEA3Jkf3x/f8naccsmlxj4vjuJDpYZG/PGQKw0/Rzrajdg+TCEUlNoEsW1X" +
            "suw4J4+a2ghiuYmRpFwLn/VXfxnzLwJAA91vvFhVS0lvuroQ1DM8Rx2zdYTPFj2q" +
            "/ccOi8f2/4wAAvQUiK2vvT6KMK8jQ73iDzCm7b//cWqR4EDzTkxCeQJAem/qTZOB" +
            "WKYjyBkffy8B9eDoU4ySrRcaSCT/g03/8otIwyQLMzHI6lDQJwjWrRKRLbSKz5nR" +
            "zskV7aypovnn2A==";
    // 商户收款账号
    public static final String SELLER = "784020411@qq.com";

    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_CHECK_FLAG = 2;

    public static void aliPay(final Activity activity, final Handler mHandler, String subject, String body, String price) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            activity.finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(subject, body, price);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /*
    * 创建订单消息
    * */
    private static String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
