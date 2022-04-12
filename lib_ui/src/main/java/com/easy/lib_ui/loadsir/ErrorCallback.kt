package com.easy.lib_ui.loadsir

import com.easy.lib_ui.R
import com.kingja.loadsir.callback.Callback
class ErrorCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.view_error
    }
}