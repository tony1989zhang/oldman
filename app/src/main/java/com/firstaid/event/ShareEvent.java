package com.firstaid.event;

import android.app.AlertDialog;

import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.bridge.IBridgeEnv;
import com.youzan.sdk.web.event.ShareDataEvent;

/**
 * 功能    : 获取当前页面的分享信息
 *
 * 使用场景: 将当前商品页面分享到各大社交平台增加曝光率
 * 触发条件: 在当前页面加载完成后调用{@code YouzanBridge.sharePage()}
 * 说明    : 回调中只会提供分享信息, 之后的分享动作需要开发者自行集成
 * 参数说明:
 *
 * {@link GoodsShareModel}
 *
 *      title:  页面标题
 *      link:   当前页面链接
 *      desc:   商品详细的描述
 *      imgUrl: 商品图片链接
 *      imgWidth:   图片宽度
 *      imgHeight:  图片高度
 *
 */
public final class ShareEvent extends ShareDataEvent {

    /**
     * 回传分享数据, 再调用组件进行分享
     *
     * @param env 一些上下文环境
     * @param data 分享数据
     */
    @Override
    public void call(IBridgeEnv env, GoodsShareModel data) {
        new AlertDialog
                .Builder(env.getActivity())
                .setTitle(data.getTitle())
                .setMessage("分享链接:\n" + data.getLink() + "\n\n图片链接:\n" + data.getImgUrl() + "\n\n描述:\n" + data.getDesc())
                .create()
                .show();
    }
}
