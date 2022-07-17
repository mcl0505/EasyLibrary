package com.easy.lib_pay

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

object EasyPayConstants {
    //微信获取信息接口
    const val WX_HOST = "https://api.weixin.qq.com"
    const val WX_SCOPE = "snsapi_userinfo"
    const val WX_STATE = "sharecommunity_weixin_login"
    const val WX_SESSION = SendMessageToWX.Req.WXSceneSession //好友
    const val WX_TIMELINE = SendMessageToWX.Req.WXSceneTimeline //朋友圈
    const val WX_FAVORITE = SendMessageToWX.Req.WXSceneFavorite //收藏

}