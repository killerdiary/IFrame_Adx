package com.hy.demo.ui

import android.view.View
import android.widget.TextView

import com.hy.demo.app.BaseActivity
import com.hy.demo.iframe.adx.BuildConfig
import com.hy.demo.iframe.adx.R
import com.hy.frame.util.TimerUtil


/**
 * title 无
 * author heyan
 * time 19-8-15 上午10:00
 * desc 无
 */
class LaunchActivity : BaseActivity(), TimerUtil.ICallback {
    private var txtMsg: TextView? = null
    private var timer: TimerUtil? = null

    override fun getLayoutId(): Int {
        return R.layout.v_launch
    }

    override fun initView() {
        txtMsg = findViewById(R.id.launch_txtMsg)
    }

    override fun initData() {
        txtMsg!!.text = getString(R.string.launch_str, BuildConfig.VERSION_NAME)
        timer = TimerUtil(curContext)
        timer!!.delayed(1500L, this)
    }

    override fun onViewClick(v: View) {

    }

    override fun doNext() {
        timer!!.cancel()
        timer = null
        txtMsg = null
        startAct(MainActivity::class.java)
        finish()
    }
}
