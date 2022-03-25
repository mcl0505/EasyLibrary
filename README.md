# 框架使用文档

> 规范

### Activity

    所有的Activity 都继承与 EasyActivity  
    BaseNoViewModelActivity:页面过于简单,不需要网络交互  可以不使用ViewModel   可使用定义的模板创建,ViewModel不勾选
    BaseActivity : 普通页面,有网络交互的情况 使用ViewModel     可使用定义的模板创建,ViewModel勾选(默认)
    BaseRefreshActivity:里面封装了刷新相关逻辑与Adapter 仅支持单item 可使用定义的模板创建,ViewModel勾选(默认)  勾选刷新

### Fragment

    所有的Fragment 都继承与 EasyFragment 
    BaseNoViewModelFragment:页面过于简单,不需要网络交互  可以不使用Vi  ewModel   可使用定义的模板创建,ViewModel不勾选
    BaseFragment : 普通页面,有网络交互的情况 使用ViewModel     可使用定义的模板创建,ViewModel勾选(默认)
    BaseRefreshFragment:里面封装了刷新相关逻辑与Adapter 仅支持单item 可使用定义的模板创建,ViewModel勾选(默认)  勾选刷新

> 常用工具/功能

## 动态权限请求

```
    PermissionUtils.init(this)
        .permissions(Manifest.permission.CAMERA)
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
            
            
                    //权限已全部同意
            } else {
                  // grantedList  授权的权限  
                  // deniedList   拒绝的权限
            }
        }
```

## 事件传递

抛弃EventBus 使用LiveDataBus

```
发送:
 * TAG:唯一标识
 * A:发送的数据
LiveDataBus.send("TAG","A")
接收:
 * TAG:唯一标识
 * A:接收的数据类型
LiveDataBus.observe<A>(this,"TAG",{})
```

## 图片选择

```
//通过配置文件修改每次选择的内容  
SelectImageDialog(config:SelectImageConfig()){
            mAdapter.addDataList(it)
        }.show(supportFragmentManager)
```

## 常用

```
//常用工具
1:DateUtil  =====================>时间格式化工具
2:LogUtil   =====================>日志打印
3:ToastUtil  ====================> 吐司
4:InfoVerify ===================> 信息验证
5:SignTool  ===================> App签名工具
6:InputMethodUtil  =============> 输入法
7:AppExecutorsHelper  ======> 线程工具
8:AppUtil  ===================> App 基础信息获取与操作
9:BaseAnimationUtil   =============> 基础动画工具  

//常用扩展函数

//资源获取
1:Int.getColor()  获取资源文件的颜色值
2:Int.getDrawable() 获取资源文件的Drawable
3:Int.getString() 获取资源文件的字符串
4:Int.getStringArray()  获取资源文件的数组

//View 操作
1:View.countDownTimer  倒计时封装
2:View.singleClick   防止快速点击
3:View.visibleOrGone  视图显示与隐藏

```