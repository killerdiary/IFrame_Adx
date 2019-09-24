package com.hy.demo.app

import com.hy.frame.common.BaseApplication

/**
 * title 无
 * author heyan
 * time 19-7-11 下午2:26
 * desc 无
 */
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.instance = this
    }

    override fun isLoggable(): Boolean {
        return true
    }

    companion object {
        var instance: MyApplication? = null
            private set
    }

}
