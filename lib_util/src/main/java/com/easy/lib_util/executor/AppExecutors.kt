package com.easy.library.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import java.util.concurrent.*

/**
 * 公司：     
 * 作者：Android 孟从伦
 * 创建时间：2021/5/6
 * 功能描述：
 *
 *  UI  线程
 *  AppExecutors.mainThread().execute(new Runnable() {
    @Override
    public void run() {
    //do something
        }
    });
 *
 *
 * 磁盘IO线程池
    AppExecutors.getInstance().diskIO().execute(new Runnable() {
    @Override
    public void run() {
    //do something
    }
    });
 *
 *网络IO线程池
    AppExecutors.getInstance().networkIO().execute(new Runnable() {
    @Override
    public void run() {
    //do something
    }
    });
 *
 *定时(延时)任务线程池
 * 1：延时3秒后执行：
    AppExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {
    @Override
    public void run() {
    // do something
    }
    },3,TimeUnit.SECONDS);
 *
 * 2：5秒后启动第一次,每3秒执行一次(第一次开始执行和第二次开始执行之间间隔3秒)
    AppExecutors.getInstance().scheduledExecutor().scheduleAtFixedRate(new Runnable() {
    @Override
    public void run() {
    // do something
    }
    }, 5, 3, TimeUnit.MILLISECONDS);
 *
 * 3:5秒后启动第一次,每3秒执行一次(第一次执行完成和第二次开始之间间隔3秒)
 *
 * 上面3个方法都会有一个如下返回值:  ScheduledFuture<?> scheduledFuture;
 *取消定时器(等待当前任务结束后，取消定时器)
 * scheduledFuture.cancel(false);
 * 取消定时器(不等待当前任务结束，取消定时器)
 * scheduledFuture.cancel(true);

 *
 *
 */
object AppExecutors {
    private val mDiskIO: Executor
    private val mNetworkIO: Executor
    private val mMainThread: Executor
    private val schedule: ScheduledExecutorService


    fun diskIO(): Executor {
        return mDiskIO
    }

    fun schedule(): ScheduledExecutorService {
        return schedule
    }

    fun networkIO(): Executor {
        return mNetworkIO
    }

    fun mainThread(): Executor {
        return mMainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
        override fun execute(@NonNull command: Runnable?) {
            mainThreadHandler.post(command!!)
        }
    }

    init {
        mDiskIO = Executors.newSingleThreadExecutor(MyThreadFactory("single"))
        mNetworkIO = Executors.newFixedThreadPool(3, MyThreadFactory("fixed"))
        mMainThread = MainThreadExecutor()
        schedule = ScheduledThreadPoolExecutor(
            5, MyThreadFactory("sc"),
            ThreadPoolExecutor.AbortPolicy()
        )
    }
}

class MyThreadFactory(private val name: String) : ThreadFactory {
    private var count = 0
    override fun newThread(@NonNull r: Runnable?): Thread {
        count++
        return Thread(r, "$name-$count-Thread")
    }
}
