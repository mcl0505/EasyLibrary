package com.easy.lib_util

import com.easy.lib_util.ext.getString

object CheckUtil {

    fun checkStartAndFinishEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(
                R.string.start_activity_finish_tips.getString()
            )
        }
    }

    fun checkStartForResultEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(
                R.string.start_activity_for_result_tips.getString()
            )
        }
    }

    fun checkLoadSirEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(R.string.load_sir_tips.getString())
        }
    }

    fun checkLoadingDialogEvent(event: Any?) {
        if (event == null) {
            throw RuntimeException(R.string.loadingDialogTips.getString())
        }
    }

    fun checkCusDialogFragmentEvent(event: Any?){
        if(event == null){
            throw RuntimeException("CusDialogFragment错误！")
        }
    }

}