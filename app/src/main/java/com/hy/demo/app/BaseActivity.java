package com.hy.demo.app;

import android.content.pm.ActivityInfo;

import com.hy.frame.base.BaseTemplateUI;
import com.hy.frame.common.IImageLoader;

/**
 * title 无
 * author heyan
 * time 19-7-11 下午2:30
 * desc 无
 */
public abstract class BaseActivity<T extends BaseTemplateUI> extends com.hy.frame.base.BaseXActivity<T> {

    @Override
    public int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    protected void initHeader(int drawLeft, int titleId, int strRightId) {
        getTemplateUI().setHeaderLeft(drawLeft);
        getTemplateUI().setTitle(titleId);
        getTemplateUI().setHeaderRightTxt(getString(strRightId));
    }

    protected void initHeader(int drawLeft, int titleId) {
        getTemplateUI().setHeaderLeft(drawLeft);
        getTemplateUI().setTitle(titleId);
    }

    @Override
    public IImageLoader buildImageLoader() {
        return null;
    }
}
