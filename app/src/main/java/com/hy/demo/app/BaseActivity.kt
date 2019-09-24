package com.hy.demo.app

import android.content.pm.ActivityInfo

/**
 * title 无
 * author heyan
 * time 19-7-11 下午2:30
 * desc 无
 */
abstract class BaseActivity : com.hy.frame.ui.BaseActivity() {
    override fun isSingleLayout(): Boolean {
        return false
    }

    override fun getScreenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    protected fun initHeader(drawLeft: Int, titleId: Int, strRightId: Int) {
        templateController!!.setHeaderLeft(drawLeft)
        templateController!!.setTitle(titleId)
        templateController!!.setHeaderRightTxt(getString(strRightId))
    }

    protected open fun initHeader(drawLeft: Int, titleId: Int) {
        templateController!!.setHeaderLeft(drawLeft)
        templateController!!.setTitle(titleId)
    }
}
