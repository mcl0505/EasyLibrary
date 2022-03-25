package com.easy.lib_util.ext

import android.os.CountDownTimer
import android.view.View

//倒计时 逻辑处理
fun View.countDownTimer(tick: (time: Long) -> Unit, finish: () -> Unit,allTime:Long = 59,intervalTime:Long = 1) {
    val timer = object : CountDownTimer(allTime*1000, intervalTime*1000) {
        //开始倒计时
        override fun onTick(millisUntilFinished: Long) {
            this@countDownTimer.isEnabled = false
            tick(millisUntilFinished)
        }

        //倒计时结束
        override fun onFinish() {
            this@countDownTimer.isEnabled = true
            finish()
        }
    }.start()
}