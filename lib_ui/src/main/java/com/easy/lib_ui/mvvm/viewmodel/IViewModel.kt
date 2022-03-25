package com.easy.lib_ui.mvvm.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver


/**
 * ViewModel 层，让 vm 可以感知 v 的生命周期
 */
interface IViewModel : DefaultLifecycleObserver