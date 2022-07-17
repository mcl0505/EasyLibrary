# 初始化
````
     initEasyPay {
            initWX("","")
            initAli()
            initSina("", mSinaRedirectUrl, mSinaCope)
            initQQ("","com.tencent.easy.fileprovider")
        }
````

## 微信
### 配置
````
        ////在应用的AndroidManifest.xml增加配置的<application>节点下增加以下配置  需要在主项目下增加这两个文件  一个是登录回调 一个支付回调
        <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:launchMode="singleTop" />
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop" />
````

### 登录
````
        WXUtils.wxLogin()
````
### 支付
````
        WXUtils.wxPay("支付需要的 json 参数")
````
### 分享
````
        //需要传入的图片
        val bitmap = mBinding.root.getBitmap()
        //分享文本
        WXUtils.shareText("分享的内容")
        //分享图片
        WXUtils.shareImage(bitmap)
        //分享音乐
        WXUtils.shareMusic("音乐地址","音乐标题","音乐描述",bitmap)
        //分享视频
        WXUtils.shareVideo("视频地址","视频标题","视频描述",bitmap)
        //分享网址
        WXUtils.shareWeb("网页地址","网页标题","网页描述",bitmap)
        //分享网址
        WXUtils.shareApplet("小程序标题","小程序描述","小程序原始Id","小程序打开页面路劲",bitmap)
        
        //DSL 方式调用
         shareWX { 
            //分享图片
            shareImage {}
            //分享文字
            shareText {  }
            //分享音乐
            shareMusic {  }
            //分享视频
            shareVideo {  }
            //分享网址
            shareWeb {  }
            //分享小程序
            shareApplet {  }
        }
        
````


## 支付宝
### 登录
````
    AiliPayUtil.aliLogin(th is,"登录参数  后端返回")
````
### 支付
````
     AiliPayUtil.aliPay(this,"支付需要的参数  后端返回")
````
### 分享
    当前未使用到该需求，使用到的时候在添加

## 新浪微博
### 登录
````
    //需要在当前Activity 中设置回调  不设置将接收不到回调  mWBAPI.authorizeCallback(requestCode, resultCode, data);
    SinaUtils.sinaLogin(this,{"授权成功回调"})
````
### 分享  
````
    //还没写  待完成
````

## QQ
### 配置
````
    //在应用的AndroidManifest.xml增加配置的<application>节点下增加以下配置（注：不配置将会导致无法调用API）
    <activity
       android:name="com.tencent.tauth.AuthActivity"
       android:noHistory="true"
       android:launchMode="singleTask" >
        <intent-filter>
               <action android:name="android.intent.action.VIEW" />
               <category android:name="android.intent.category.DEFAULT" />
               <category android:name="android.intent.category.BROWSABLE" />
               <data android:scheme="tencent你的AppId" />
        </intent-filter>
     </activity>
     <activity
         android:name="com.tencent.connect.common.AssistActivity"
         android:configChanges="orientation|keyboardHidden"
         android:screenOrientation="behind" 
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />
````
### 登录
````
    TencentUtils.tencentLogin(this,{"授权成功回调"})
````
### 分享  
````
        //需要在Activity 环境下调用
        shareTencent { 
                    shareQQ(
                        params = {},
                        onSuccess = {},
                        other = {}
                    )
                    shareQQZone(
                        params = {},
                        onSuccess = {},
                        other = {}
                    )
        }
````