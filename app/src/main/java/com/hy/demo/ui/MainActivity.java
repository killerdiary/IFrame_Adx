package com.hy.demo.ui;

import com.hy.demo.iframe.R;

/**
 * title 无
 * author heyan
 * time 19-8-15 上午9:59
 * desc 无
 */
public class MainActivity extends MenuActivity {

    @Override
    protected void initHeader(int drawLeft, int titleId) {
        getTemplateUI().setTitle(titleId);
    }

    @Override
    public void initData() {
        getArgs().putInt(ARG_XMLID,R.xml.menu_main);
        getArgs().putInt(ARG_TITLEID,R.string.appName);
        super.initData();
    }

    @Override
    public void onBackPressed() {
        getCurApp().exit();
    }
}
