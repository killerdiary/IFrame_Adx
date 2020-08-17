package com.hy.frame.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hy.frame.common.IAppUI;
import com.hy.frame.common.IApplication;
import com.hy.frame.common.IBaseFragment;
import com.hy.frame.common.IBaseUI;
import com.hy.frame.common.IImageLoader;
import com.hy.frame.common.ILifeUI;
import com.hy.frame.common.ITemplateUI;

/**
 * title BaseActivity
 * author heyan
 * time 19-7-11 上午9:59
 * desc 无
 */
public abstract class BaseXFragment<T extends BaseTemplateUI> extends Fragment implements IBaseUI, ILifeUI, IAppUI, IBaseFragment<Fragment> {

    private IApplication mApp = null;
    private IImageLoader mImageLoader = null;
    private T mTemplateUI = null;

    private Bundle mArgs = null;

    private boolean mInit;
    private boolean mDestroy;
    private boolean mPause;
    private boolean mStop;
    private boolean mResume;


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

    @Override
    @Nullable
    public IImageLoader getImageLoader() {
        return this.mImageLoader;
    }

    @Override
    @Nullable
    public IImageLoader buildImageLoader() {
        return null;
    }

    @Override
    @Nullable
    public IApplication getCurApp() {
        return this.mApp;
    }

    @Override
    @Nullable
    public Activity getCurActivity() {
        return getActivity();
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
        if (getCurContext() == null) return;
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
    @Nullable
    public Context getCurContext() {
        return getContext();
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
    public boolean isInit() {
        return mInit;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!initAttrs()) return null;
        if (this.mTemplateUI == null) {
            initLayout();
        }
        if(this.mTemplateUI != null)
            return this.mTemplateUI.getRootLayout();
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    /**
     * 初始化基本属性
     *
     * @return boolean
     */
    private boolean initAttrs() {
        Activity activity = getCurActivity();
        if (activity == null) {
            return false;
        }
        this.mApp = (IApplication) activity.getApplication();
        this.mArgs = getArguments();
        return true;
    }


    @Override
    public void initLayout() {
        if (this.mInit) return;
        T mTemplateUI = buildTemplateUI();
        if (mTemplateUI != null) {
            mTemplateUI.build();
        }
        this.mTemplateUI = mTemplateUI;
        this.mImageLoader = buildImageLoader();
    }

    @Override
    public void initView() {
        ITemplateUI mTemplateUI = getTemplateUI();
        if (mTemplateUI != null) {
            mTemplateUI.initView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPause = false;
        this.mStop = false;
        this.mResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mPause = true;
        this.mResume = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mStop = true;
    }

    @Override
    public void onDestroy() {
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
