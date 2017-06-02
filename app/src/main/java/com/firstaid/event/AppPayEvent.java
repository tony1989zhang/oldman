package com.firstaid.event;

import com.youzan.sdk.model.trade.WxPayModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.WXAppPayEvent;


/**
 * 功能   : 返回微信APP支付的数据(高阶实现)
 *
 * 使用场景: 已有公众号并开通微信支付
 * 触发条件: 网页页面触发, 并在有赞后台开启了微信APP支付
 * 说明    : 微信APP支付集成比较麻烦, 推荐使用微信WAP支付(默认开启)
 *           接口只会返回微信支付的订单数据, 之后的跳转支付需要开发者自行集成
 *           参考微信SDK文档 http://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167&token=&lang=zh_CN
 * 参数说明:
 *
 * {@link WxPayModel}类同于{@link com.tencent.mm.sdk.modelpay.PayReq}
 *
 *
 */
public final class AppPayEvent extends WXAppPayEvent {

    @Override
    public void call(IBridgeEnv env, WxPayModel item) {
        if (item != null) {
            //invoke wechat sdk pay for money
        }
    }

}
