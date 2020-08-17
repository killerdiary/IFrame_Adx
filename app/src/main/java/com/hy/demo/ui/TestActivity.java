package com.hy.demo.ui;

import android.os.Message;
import android.view.View;

import com.hy.demo.app.BaseActivity;
import com.hy.demo.iframe.R;
import com.hy.frame.base.BaseTemplateUI;
import com.hy.frame.common.IAppUI;
import com.hy.frame.util.MyHandler;

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
public class TestActivity extends BaseActivity<TestActivity.TemplateUI> {
    private MyHandler handler = null;

    @Override
    public TestActivity.TemplateUI buildTemplateUI() {
        return new TestActivity.TemplateUI(this);
    }

    @Override
    public void initData() {
        initHeader(android.R.drawable.ic_menu_revert, R.string.appName, R.string.confirm);
        getTemplateUI().showLoading("测试...");
        handler = new MyHandler(getCurContext(), new MyHandler.HandlerListener() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    getTemplateUI().showCView();
                } else {
                    getTemplateUI().hideLoadingDialog();
                }
            }
        });
        handler.sendEmptyMessageDelayed(0, 3500L);
    }

    @Override
    public void onViewClick(View v) {

    }

    @Override
    public void onRightClick() {
        getTemplateUI().showLoadingDialog("提交中 ....");
        handler.sendEmptyMessageDelayed(1, 3500L);
    }


    static class TemplateUI extends BaseTemplateUI {


        public TemplateUI(IAppUI iUI) {
            super(iUI);
        }

        /**
         * LayoutId 默认值为0
         */
        @Override
        public int getLayoutId() {
            return R.layout.v_main;
        }

        @Override
        public void initView() {

        }

    }
}
