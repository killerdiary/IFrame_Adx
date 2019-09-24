package com.hy.frame.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.hy.frame.common.*
import com.hy.frame.ui.simple.TemplateController
import com.hy.frame.util.MyLog
import com.hy.frame.util.ResUtil
import com.hy.iframe.adx.R

/**
 * title BaseActivity
 * author heyan
 * time 19-7-11 上午9:59
 * desc 无
 */
abstract class BaseActivity : AppCompatActivity(), IBaseUI, IBaseTemplateUI, IBaseActivity, View.OnClickListener {

    private var mApp: IBaseApplication? = null
    private var mTemplateController: ITemplateController? = null
    private var mImageLoader: IImageLoader? = null
    private var mLastSkipAct: String? = null //获取上一级的Activity名

    private var mLastTime: Long = 0
    private var mArgs: Bundle? = null
    private var mLayout: View? = null

    private var mDestroy: Boolean = false
    private var mPause: Boolean = false
    private var mStop: Boolean = false
    private var mResume: Boolean = false

    override fun getScreenOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun isPermissionDenied(): Boolean {
        return false
    }

    override fun getLastSkipAct(): String? {
        return this.mLastSkipAct
    }

    override fun isSingleLayout(): Boolean {
        return true
    }

    override fun getTemplateController(): ITemplateController? {
        return this.mTemplateController
    }

    override fun buildTemplateController(): ITemplateController? {
        return TemplateController(this)
    }

    override fun getImageLoader(): IImageLoader? {
        return this.mImageLoader
    }

    override fun buildImageLoader(): IImageLoader? {
        return null
    }

    override fun isTranslucentStatus(): Boolean {
        return false
    }

    override fun getStatusBarHeight(): Int {
        return ResUtil.getStatusBarHeight(curContext)
    }

    override fun getCurApp(): IBaseApplication? {
        return this.mApp
    }

    override fun getCurActivity(): Activity {
        return this
    }

    override fun getArgs(): Bundle? {
        return this.mArgs
    }

    override fun getStrings(vararg ids: Int): String {
        val sb = StringBuilder()
        for (id in ids) {
            sb.append(getString(id))
        }
        return sb.toString()
    }

    override fun onLeftClick() {
        onBackPressed()
    }

    override fun onRightClick() {

    }

    override fun onLoadViewClick() {

    }

    override fun startAct(cls: Class<*>) {
        startAct(cls, null, null)
    }

    override fun startAct(cls: Class<*>, bundle: Bundle?, intent: Intent?) {
        startActForResult(cls, 0, bundle, intent)
    }

    override fun startActForResult(cls: Class<*>, requestCode: Int) {
        startActForResult(cls, requestCode, null, null)
    }

    override fun startActForResult(cls: Class<*>, requestCode: Int, bundle: Bundle?, intent: Intent?) {
        var i = intent
        if (i == null)
            i = Intent()
        var b = bundle
        if (b == null)
            b = Bundle()
        b.putString(IBaseTemplateUI.ARG_LAST_ACT, javaClass.simpleName)
        i.putExtra(IBaseTemplateUI.ARG_BUNDLE, b)
        i.setClass(curContext, cls)
        if (requestCode != 0)
            startActivityForResult(i, requestCode)
        else
            startActivity(i)
    }

    override fun getCurContext(): Context {
        return this
    }

    override fun getBaseLayoutId(): Int {
        return R.layout.v_base
    }
//    @Override
//    public int getLayoutId() {
//        return 0;
//    }

    override fun getLayoutView(): View? {
        return null
    }
//
//    @Override
//    public void initView() {
//
//    }
//
//    @Override
//    public void initData() {
//
//    }
//
//    @Override
//    public void onViewClick(View v) {
//
//    }

    override fun <T : View> findViewById(id: Int, parent: View?): T? {
        return if (parent != null) parent.findViewById(id) else findViewById(id)
    }

    override fun <T : View> setOnClickListener(id: Int, parent: View): T? {
        val v = findViewById<T>(id, parent)
        v?.setOnClickListener(this)
        return v
    }


    override fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - this.mLastTime < 500L) return true
        this.mLastTime = time
        return false
    }

    override fun onClick(v: View) {
        if (!isFastClick)
            onViewClick(v)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun finish() {
        if (this.mApp != null)
            this.mApp!!.activityCache.remove(this)
        super.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initAttrs()) return
        if (isPermissionDenied) {
            finish()
            return
        }
        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            requestedOrientation = screenOrientation
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isTranslucentStatus) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        initLayout()
        if (this.mLayout != null)
            setContentView(this.mLayout)
        initView()
        initData()
    }

    /**
     * 初始化基本属性
     *
     * @return boolean
     */
    private fun initAttrs(): Boolean {
        if (application !is BaseApplication) {
            MyLog.e("Application configuration exception, currently application must extends BaseApplication")
            setContentView(R.layout.v_frame_warn)
            return false
        }
        this.mApp = application as IBaseApplication
        if (intent.hasExtra(IBaseTemplateUI.ARG_BUNDLE))
            this.mArgs = intent.getBundleExtra(IBaseTemplateUI.ARG_BUNDLE)
        else
            this.mArgs = intent.extras
        if (this.mArgs != null)
            this.mLastSkipAct = this.mArgs!!.getString(IBaseTemplateUI.ARG_LAST_ACT)
        this.mApp!!.activityCache.add(this)
        this.mTemplateController = buildTemplateController()
        this.mImageLoader = buildImageLoader()
        return true
    }

    override fun initLayout(): View? {
        if (this.mLayout != null) return this.mLayout
        var cLayout: View? = null
        val cToolbar: ViewGroup?
        val cMain: ViewGroup?
        val customView = layoutView
        if (isSingleLayout) {
            if (customView != null) {
                cLayout = customView
            } else if (layoutId != 0) {
                cLayout = View.inflate(curContext, layoutId, null)
            }
        } else if (baseLayoutId != 0) {
            cLayout = View.inflate(curContext, baseLayoutId, null)
        }
        if (cLayout == null) return null
        cToolbar = findViewById(R.id.base_cToolBar, cLayout)
        cMain = findViewById(R.id.base_cMain, cLayout)
        if (!isSingleLayout && cMain != null) {
            if (customView != null) {
                cMain.addView(customView)
            } else if (layoutId != 0) {
                View.inflate(curContext, layoutId, cMain)
            }
        }
        if (this.mTemplateController != null) {
            this.mTemplateController!!.init(cToolbar, cMain)
        }
        this.mLayout = cLayout
        return this.mLayout
    }

    override fun onResume() {
        super.onResume()
        this.mPause = false
        this.mStop = false
        this.mResume = true
    }

    override fun onPause() {
        super.onPause()
        this.mPause = true
        this.mResume = false
    }

    override fun onStop() {
        super.onStop()
        this.mStop = true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mDestroy = true
    }

    fun isIDestroy(): Boolean {
        return mDestroy
    }

    fun isIPause(): Boolean {
        return mPause
    }

    fun isIStop(): Boolean {
        return mStop
    }

    fun isIResume(): Boolean {
        return mResume
    }
}
