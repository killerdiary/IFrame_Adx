package com.hy.frame.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hy.frame.common.IAppUI;
import com.hy.frame.common.IApplication;
import com.hy.frame.common.IBaseActivity;
import com.hy.frame.common.IBaseUI;
import com.hy.frame.common.IImageLoader;
import com.hy.frame.common.ILifeUI;
import com.hy.frame.common.ITemplateUI;
import com.hy.frame.util.LogUtil;
import com.hy.iframe.R;

/**
 * title BaseActivity
 * author heyan
 * time 19-7-11 上午9:59
 * desc 无
 */
public abstract class BaseXActivity<T extends BaseTemplateUI> extends AppCompatActivity implements IBaseUI, ILifeUI, IAppUI, IBaseActivity {

    private IApplication mApp = null;
    private IImageLoader mImageLoader = null;
    private T mTemplateUI = null;
    private String mLastSkipAct = null; //获取上一级的Activity名

    private Bundle mArgs = null;

    private boolean mDestroy;
    private boolean mPause;
    private boolean mStop;
    private boolean mResume;

    @Override
    public int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    @Override
    public boolean isPermissionDenied() {
        return false;
    }

    @Override
    public String getLastSkipAct() {
        return this.mLastSkipAct;
    }

    /**
     * 设置模板[ITemplateControl]
     */
    @Nullable
    public abstract T buildTemplateUI();

    /**
     * 获取模板[ITemplateControl]
     */
    @Nullable
    public T getTemplateUI() {
        return this.mTemplateUI;
    }

    /**
     * 获取图片加载器
     */
    @Nullable
    public IImageLoader getImageLoader() {
        if (isIDestroy()) return null;
        if (this.mImageLoader == null)
            this.mImageLoader = buildImageLoader();
        return this.mImageLoader;
    }

    @Nullable
    public abstract IImageLoader buildImageLoader();

    /**
     * 设置图片加载器
     *
     * @return IImageLoader 用于子类拓展
     */
    @Override
    @Nullable
    public IApplication getCurApp() {
        return this.mApp;
    }

    @Override
    @Nullable
    public Activity getCurActivity() {
        return this;
    }

    @Override
    @Nullable
    public Bundle getArgs() {
        return this.mArgs;
    }

    public String getStrings(int... ids) {
        StringBuilder sb = new StringBuilder();
        for (int id : ids) {
            sb.append(getString(id));
        }
        return sb.toString();
    }

    @Override
    public void onLeftClick() {
        onBackPressed();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onLoadViewClick() {

    }

    @Override
    public void startAct(Class<?> cls) {
        startAct(cls, null, null);
    }

    @Override
    public void startAct(Class<?> cls, Bundle bundle, Intent intent) {
        startActForResult(cls, 0, bundle, intent);
    }

    @Override
    public void startActForResult(Class<?> cls, int requestCode) {
        startActForResult(cls, requestCode, null, null);
    }

    @Override
    public void startActForResult(Class<?> cls, int requestCode, Bundle bundle, Intent intent) {
        Intent i = intent;
        if (i == null)
            i = new Intent();
        Bundle b = bundle;
        if (b == null)
            b = new Bundle();
        b.putString(ARG_LAST_ACT, getClass().getSimpleName());
        i.putExtra(ARG_BUNDLE, b);
        i.setClass(getCurContext(), cls);
        if (requestCode != 0)
            startActivityForResult(i, requestCode);
        else
            startActivity(i);
    }

    @Override
    public Context getCurContext() {
        return this;
    }

//    @Override
//    public void initView() {
//
//    }

//    @Override
//    public void initData() {
//
//    }
//
//    @Override
//    public void onViewClick(View v) {
//
//    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        if (this.mApp != null)
            this.mApp.getActivityCache().remove(this);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!initAttrs()) return;
        if (isPermissionDenied()) {
            finish();
            return;
        }
        if (getScreenOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            setRequestedOrientation(getScreenOrientation());
        }
        initLayout();
        initView();
        initData();
    }

    /**
     * 初始化基本属性
     *
     * @return boolean
     */
    private boolean initAttrs() {
        if (!(getApplication() instanceof BaseApplication)) {
            LogUtil.e("Application configuration exception, currently application must extends BaseApplication");
            setContentView(R.layout.v_frame_warn);
            return false;
        }
        this.mApp = (IApplication) getApplication();
        if (getIntent().hasExtra(ARG_BUNDLE))
            this.mArgs = getIntent().getBundleExtra(ARG_BUNDLE);
        else
            this.mArgs = getIntent().getExtras();
        if (this.mArgs != null)
            this.mLastSkipAct = this.mArgs.getString(ARG_LAST_ACT);
        this.mApp.getActivityCache().add(this);
        return true;
    }

    @Override
    public void initLayout() {
        T mTemplateUI = buildTemplateUI();
        if (mTemplateUI != null) {
            mTemplateUI.build();
            setContentView(mTemplateUI.getRootLayout());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (mTemplateUI.isTranslucentStatus()) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }
        }
        this.mTemplateUI = mTemplateUI;
    }

    @Override
    public void initView() {
        ITemplateUI mTemplateUI = getTemplateUI();
        if (mTemplateUI != null) {
            mTemplateUI.initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mPause = false;
        this.mStop = false;
        this.mResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPause = true;
        this.mResume = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mDestroy = true;
        if (this.mTemplateUI != null) {
            this.mTemplateUI.onDestroy();
        }
        if (this.mImageLoader != null) {
            this.mImageLoader.onDestroy();
        }
    }

    @Override
    public boolean isIDestroy() {
        return mDestroy;
    }

    @Override
    public boolean isIPause() {
        return mPause;
    }

    @Override
    public boolean isIStop() {
        return mStop;
    }

    @Override
    public boolean isIResume() {
        return mResume;
    }
}
