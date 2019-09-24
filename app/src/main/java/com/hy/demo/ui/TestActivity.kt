package com.hy.demo.ui

import android.view.View
import com.hy.demo.app.BaseActivity
import com.hy.demo.iframe.adx.R
import com.hy.frame.util.MyHandler

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
class TestActivity : BaseActivity() {
    private var handler: MyHandler? = null

    override fun getLayoutId(): Int {
        return R.layout.v_main
    }

    override fun initView() {
        initHeader(android.R.drawable.ic_menu_revert, R.string.appName, R.string.confirm)
        templateController?.showLoading("测试...")
    }

    override fun initData() {

        handler = MyHandler(curContext, MyHandler.HandlerListener { msg ->
            if (msg.what == 0) {
                templateController?.showCView()
            } else {
                templateController?.hideLoadingDialog()
            }
        })
        handler!!.sendEmptyMessageDelayed(0, 3500L)
    }

    override fun onViewClick(v: View) {

    }

    override fun onRightClick() {
        templateController?.showLoadingDialog("提交中 ....")
        handler!!.sendEmptyMessageDelayed(1, 3500L)
    }
}
