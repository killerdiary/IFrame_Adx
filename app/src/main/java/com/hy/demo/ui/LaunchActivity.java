package com.hy.demo.ui;

import android.view.View;
import android.widget.TextView;

import com.hy.demo.app.BaseActivity;

import com.hy.demo.iframe.BuildConfig;
import com.hy.demo.iframe.R;
import com.hy.frame.base.BaseTemplateUI;
import com.hy.frame.common.IAppUI;
import com.hy.frame.util.TimerUtil;

/**
 * title 无
 * author heyan
 * time 19-8-15 上午10:00
 * desc 无
 */
public class LaunchActivity extends BaseActivity<LaunchActivity.TemplateUI> implements TimerUtil.ICallback {

    @Override
    public TemplateUI buildTemplateUI() {
        return new TemplateUI(this);
    }

    private TimerUtil timer;

    @Override
    public void initData() {
        getTemplateUI().setMsg(getString(R.string.launch_str, BuildConfig.VERSION_NAME));
        timer = new TimerUtil(getCurContext());
        timer.delayed(1500L, this);
    }

    @Override
    public void onViewClick(View v) {

    }

    @Override
    public void doNext() {
        timer.cancel();
        timer = null;
        startAct(MainActivity.class);
        finish();
    }

    static class TemplateUI extends BaseTemplateUI {
        private TextView txtMsg;

        public TemplateUI(IAppUI iUI) {
            super(iUI);
        }

        /**
         * LayoutId 默认值为0
         */
        @Override
        public int getLayoutId() {
            return R.layout.v_launch;
        }

        @Override
        public void initView() {
            txtMsg = findViewById(R.id.launch_txtMsg);
        }

        public void setMsg(String msg) {
            txtMsg.setText(msg);
        }
    }
}
