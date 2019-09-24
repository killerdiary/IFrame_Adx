package com.hy.demo.ui

import com.hy.demo.iframe.adx.R

/**
 * title 无
 * author heyan
 * time 19-8-15 上午9:59
 * desc 无
 */
class MainActivity : MenuActivity() {

    override fun initHeader(drawLeft: Int, titleId: Int) {
        templateController?.setTitle(titleId)
    }

    override fun initData() {
        args?.putInt(ARG_XMLID, R.xml.menu_main)
        args?.putInt(ARG_TITLEID, R.string.appName)
        super.initData()
    }

    override fun onBackPressed() {
        curApp?.exit()
    }
}
