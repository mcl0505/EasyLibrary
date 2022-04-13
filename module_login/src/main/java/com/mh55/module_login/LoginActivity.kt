package com.mh55.module_login

import com.alibaba.android.arouter.facade.annotation.Route
import com.easy.lib_base.constant.ARouterPath.PATH_LOGIN_ACTIVITY
import com.easy.lib_ui.activity.BaseActivity
import com.easy.lib_ui.mvvm.IStateObserver
import com.easy.lib_util.ext.singleClick
import com.mh55.module_login.databinding.ActivityLoginBinding
import com.mh55.module_login.http.vm.LoginViewModel

@Route(path = PATH_LOGIN_ACTIVITY)
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login,BR.viewModel) {
    override fun setTitleText(): String = "登录"

    override fun initData() {
        super.initData()
        mBinding.tvLogin.singleClick {
            mViewModel.login()
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()
        mViewModel.mLogin.observe(this,object : IStateObserver<String>(){
            override fun onDataChange(data: String?) {

            }
        })
    }
}