package com.firstaid.event;

import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanUser;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.UserInfoEvent;


/**
 * 功能    : 同步注册有赞用户信息(高阶实现)
 *
 * 使用场景: 打开有赞的网页有可能会早于客户端用户登录(比如一些APP不强制用户先登录再使用),
 * 触发条件: 页面自动触发
 * 说明    : 一个用户需要在您的APP中通过有赞的SDK完成整个交易过程, 需要向有赞的后台注册一个用户.
 * 参数说明:
 *
 * {@link YouzanUser}
 *          UserId :    必需字段
 *                  意义: 用户ID,用于标识该用户在APP中是唯一的(推荐使用用户的手机号等),
 *                  类型: 字符串
 *
 *          Avatar :    非必要字段
 *                  意义: 头像图标链接
 *                  类型: 字符串
 *
 *          Gender :    非必要字段
 *                  意义: 性别
 *                  类型: 整型, 1代表男性, 0代表女性
 *
 *          NickName :  非必要字段
 *                  意义: 昵称
 *                  类型: 字符串
 *
 *          Telephone :  非必要字段
 *                  意义: 手机号
 *                  类型: 字符串
 *
 *          UserName :  非必要字段
 *                  意义: 用户名
 *                  类型: 字符串
 *
 *  {@link IBridgeEnv}
 *          意义: 包裹一些上下文环境信息
 *          说明: {@code IBridgeEnv.getWebView()}可以获取WebView对象
 *                {@code IBridgeEnv.getActivity()}可以获取Activity对象
 *
 */
public final class UserEvent extends UserInfoEvent {

    @Override
    public void call(IBridgeEnv env) {
        YouzanUser user = new YouzanUser();
        user.setUserId("12345");
        user.setAvatar("http://..");
        user.setGender(1);
        user.setNickName("小明的昵称");
        user.setTelephone("12345678901");
        user.setUserName("小明");

        //执行同步注册有赞用户操作
        YouzanSDK.syncRegisterUser(env.getWebView(), user);
    }
}