package com.easy.lib_util.notifition

import com.easy.lib_util.LogUtil.d
import com.easy.lib_util.toast.ToastUtil.toast
import com.easy.lib_util.app.AppUtil.Companion.getAppLogoIcon
import android.os.Build
import com.easy.lib_util.notifition.NotificationUtils
import android.app.NotificationManager
import android.annotation.TargetApi
import android.app.NotificationChannel
import androidx.annotation.RequiresApi
import android.app.NotificationChannelGroup
import android.app.PendingIntent
import android.widget.RemoteViews
import android.annotation.SuppressLint
import com.easy.lib_util.toast.ToastUtil
import com.easy.lib_util.app.AppUtil
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import android.app.AppOpsManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.easy.lib_util.LogUtil
import com.easy.lib_util.R
import java.lang.Exception

object NotificationUtils {
    const val NEW_MESSAGE = "chat"
    const val NEW_GROUP = "chat_group"
    const val OTHER_MESSAGE = "other"
    const val Ticker = "您有一条新的消息"
    const val CHECK_OP_NO_THROW = "checkOpNoThrow"
    const val OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION"
    var notifyId = 0

    /**
     * TODO 适配 Android8.0  创建通知渠道
     * tips：可以写在MainActivity中，也可以写在Application中，实际上可以写在程序的任何位置，
     * 只需要保证在通知弹出之前调用就可以了。并且创建通知渠道的代码只在第一次执行的时候才会创建，
     * 以后每次执行创建代码系统会检测到该通知渠道已经存在了，因此不会重复创建，也并不会影响任何效率。
     */
    fun setNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channelId = NEW_MESSAGE
            var channelName = "新消息通知"
            createNotificationChannel(context, channelId, channelName, NotificationManager.IMPORTANCE_MAX)
            channelId = OTHER_MESSAGE
            channelName = "其他通知"
            createNotificationChannel(context, channelId, channelName, NotificationManager.IMPORTANCE_MAX)
        }
    }

    /**
     * 创建配置通知渠道
     * @param channelId   渠道id
     * @param channelName 渠道nanme
     * @param importance  优先级
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, importance: Int) {
//        createNotifycationGroup(context,channelId,channelName);
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.setShowBadge(false) //禁止该渠道使用角标
        //        channel.setGroup(channelId); //设置渠道组id
        // 配置通知渠道的属性
//        channel .setDescription("渠道的描述");
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true)
        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.enableVibration(true)
        //如上设置使手机：静止1秒，震动2秒，静止1秒，震动3秒
        channel.vibrationPattern = longArrayOf(1000, 2000, 1000, 3000)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC //设置锁屏是否显示通知
        channel.lightColor = Color.BLUE
        channel.setBypassDnd(true) //设置是否可以绕过请勿打扰模式
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * 创建渠道组(若通知渠道比较多的情况下可以划分渠道组)
     * @param groupId
     * @param groupName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotifycationGroup(context: Context, groupId: String?, groupName: String?) {
        val group = NotificationChannelGroup(groupId, groupName)
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(group)
    }

    /**
     * TODO: 发送通知（刷新前面的通知）
     *
     * @param context
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun show(context: Context, contentTitle: String?, contentText: String?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, null, 0, NEW_MESSAGE, pendingIntent)
    }

    /**
     * TODO: 发送自定义通知（刷新前面的通知）
     *
     * @param context
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun show(context: Context, contentTitle: String?, contentText: String?, views: RemoteViews?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, views, 0, NEW_MESSAGE, pendingIntent)
    }

    /**
     * 发送通知（刷新前面的通知，指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun show(context: Context, contentTitle: String?, contentText: String?, channelId: String?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, null, 0, channelId, pendingIntent)
    }

    /**
     * 发送自定义通知（刷新前面的通知，指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun show(context: Context, contentTitle: String?, contentText: String?, views: RemoteViews?, channelId: String?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, views, 0, channelId, pendingIntent)
    }

    /**
     * 发送多条通知（默认通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun showMuch(context: Context, contentTitle: String?, contentText: String?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, null, ++notifyId, NEW_MESSAGE, pendingIntent)
    }

    /**
     * 发送多条自定义通知（默认通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     */
    fun showMuch(context: Context, contentTitle: String?, contentText: String?, views: RemoteViews?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, views, ++notifyId, NEW_MESSAGE, pendingIntent)
    }

    /**
     * 发送多条通知（指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun showMuch(context: Context, contentTitle: String?, contentText: String?, channelId: String?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, null, ++notifyId, channelId, pendingIntent)
    }

    /**
     * 发送多条自定义通知（指定通知渠道）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param channelId    渠道id
     */
    fun showMuch(context: Context, contentTitle: String?, contentText: String?, channelId: String?, views: RemoteViews?, pendingIntent: PendingIntent?) {
        show(context, contentTitle, contentText, views, ++notifyId, channelId, pendingIntent)
    }

    /**
     * 发送通知（设置默认：大图标/小图标/小标题/副标题/优先级/首次弹出文本）
     *
     * @param contentTitle 标题
     * @param contentText  内容
     * @param notifyId     通知栏id
     * @param channelId    设置渠道id
     * @param pendingIntent          意图类
     */
    fun show(
        context: Context,
        contentTitle: String?,
        contentText: String?,
        views: RemoteViews?,
        notifyId: Int,
        channelId: String?,
        pendingIntent: PendingIntent?,
    ) {
        show(context, 0, 0, contentTitle, null, contentText, NotificationCompat.PRIORITY_HIGH, null, views, notifyId, channelId, pendingIntent)
    }

    /**
     * 发送通知
     *
     * @param largeIcon    大图标
     * @param smallIcon    小图标
     * @param contentTitle 标题
     * @param subText      小标题/副标题
     * @param contentText  内容
     * @param priority     优先级
     * @param ticker       通知首次弹出时，状态栏上显示的文本
     * @param notifyId     定义是否显示多条通知栏
     * @param pendingIntent          意图类
     */
    @SuppressLint("WrongConstant")
    fun show(
        context: Context, largeIcon: Int,
        smallIcon: Int, contentTitle: String?,
        subText: String?, contentText: String?,
        priority: Int, ticker: String?, view: RemoteViews?,
        notifyId: Int, channelId: String?, pendingIntent: PendingIntent?,
    ) {
        //flags
        // FLAG_ONE_SHOT:表示此PendingIntent只能使用一次的标志
        // FLAG_IMMUTABLE:指示创建的PendingIntent应该是不可变的标志
        // FLAG_NO_CREATE : 指示如果描述的PendingIntent尚不存在，则只返回null而不是创建它。
        // FLAG_CANCEL_CURRENT :指示如果所描述的PendingIntent已存在，则应在生成新的PendingIntent,取消之前PendingIntent
        // FLAG_UPDATE_CURRENT : 指示如果所描述的PendingIntent已存在，则保留它，但将其额外数据替换为此新Intent中的内容
//

        //获取通知服务管理器
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //判断应用通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!openNotificationChannel(context, manager, channelId)) {
                toast("缺少通知权限")
                return
            }
        }

        //创建 NEW_MESSAGE 渠道通知栏  在API级别26.1.0中推荐使用此构造函数 Builder(context, 渠道名)
        val builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationCompat.Builder(context, channelId!!) else NotificationCompat.Builder(context)

        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources,(if (smallIcon == 0) R.drawable.logo else smallIcon))) //设置自动收报机和通知中显示的大图标。
            .setSmallIcon(if (smallIcon == 0) R.drawable.logo else smallIcon)
            .setContentText(if (TextUtils.isEmpty(contentText)) null else contentText) // 内容
            .setContentTitle(if (TextUtils.isEmpty(contentTitle)) null else contentTitle) // 标题
            .setSubText(if (TextUtils.isEmpty(subText)) null else subText) // APP名称的副标题
            .setPriority(priority) //设置优先级 PRIORITY_DEFAULT
            .setTicker(if (TextUtils.isEmpty(ticker)) Ticker else ticker) // 设置通知首次弹出时，状态栏上显示的文本
            .setContent(view)
            .setVibrate(longArrayOf(1000, 2000, 1000, 3000))
            .setDefaults(Notification.DEFAULT_ALL)//Notification.DEFAULT_ALL  使用默认的声音、振动、闪光    DEFAULT_SOUND:声音  DEFAULT_VIBRATE:震动  DEFAULT_LIGHTS:闪光
            .setWhen(System.currentTimeMillis()) // 设置通知发送的时间戳
            .setShowWhen(true) //设置是否显示时间戳
            .setAutoCancel(true) // 点击通知后通知在通知栏上消失
//            .setDefaults(Notification.PRIORITY_HIGH) // 设置默认的提示音、振动方式、灯光等 使用的默认通知选项
            .setContentIntent(pendingIntent) // 设置通知的点击事件
            //锁屏状态下显示通知图标及标题 1、VISIBILITY_PUBLIC 在所有锁定屏幕上完整显示此通知/2、VISIBILITY_PRIVATE 隐藏安全锁屏上的敏感或私人信息/3、VISIBILITY_SECRET 不显示任何部分
            .setVisibility(Notification.VISIBILITY_PUBLIC) //部分手机没效果
            .setFullScreenIntent(pendingIntent, true) //悬挂式通知8.0需手动打开
        //                .setColorized(true)
        //                .setGroupSummary(true)//将此通知设置为一组通知的组摘要
        //                .setGroup(NEW_GROUP)//使用组密钥
        //                .setDeleteIntent(pendingIntent)//当用户直接从通知面板清除通知时 发送意图
        //                .setFullScreenIntent(pendingIntent,true)
        //                .setContentInfo("大文本")//在通知的右侧设置大文本。
        //                .setContent(RemoteViews RemoteView)//设置自定义通知栏
        //                .setColor(Color.parseColor("#ff0000"))
        //                .setLights()//希望设备上的LED闪烁的argb值以及速率
        //                .setTimeoutAfter(3000)//指定取消此（如果尚未取消）。

        // 通知栏id
        manager.notify(notifyId, builder.build()) // build()方法需要的最低API为16 ,
    }

    /**
     * 判断应用渠道通知是否打开（适配8.0）
     * @return true 打开
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun openNotificationChannel(context: Context, manager: NotificationManager, channelId: String?): Boolean {
        //判断通知是否有打开
        if (!isNotificationEnabled(context)) {
            toNotifySetting(context, null)
            return false
        }
        //判断渠道通知是否打开
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(channelId)
            channel.vibrationPattern
            if (channel.importance == NotificationManager.IMPORTANCE_NONE) {
                //没打开调往设置界面
                toNotifySetting(context, channel.id)
                return false
            }
        }
        return true
    }

    /**
     * 判断应用通知是否打开
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun isNotificationEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            return notificationManagerCompat.areNotificationsEnabled()
        }
        val mAppOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        var appOpsClass: Class<*>? = null
        /* Context.APP_OPS_MANAGER */try {
            appOpsClass = Class.forName(AppOpsManager::class.java.name)
            val checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                String::class.java)
            val opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION)
            val value = opPostNotificationValue[Int::class.java] as Int
            return checkOpNoThrowMethod.invoke(mAppOps, value, context.applicationInfo.uid, context.packageName) as Int == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 手动打开应用通知
     */
    fun toNotifySetting(context: Context, channelId: String?) {
        val intent = Intent()
        try {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS

            //8.0及以后版本使用这两个extra.  >=API 26
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, AppUtil.getPackageName())
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)

            //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
            intent.putExtra("app_package", AppUtil.getPackageName())
            intent.putExtra("app_uid", channelId)

            startActivity(context, intent, null)
        } catch (e: Exception) {
            e.printStackTrace()

            //其他低版本或者异常情况，走该节点。进入APP设置界面
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.putExtra("package", AppUtil.getPackageName())

            //val uri = Uri.fromParts("package", packageName, null)
            //intent.data = uri
            startActivity(context, intent, null)
        }
    }
}