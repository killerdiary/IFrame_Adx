package com.hy.frame.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.hy.frame.common.*
import com.hy.frame.ui.simple.TemplateController
import com.hy.frame.util.MyLog
import com.hy.iframe.adx.R


/**
 * 父类Fragment
 * author HeYan
 * time 2015/12/23 17:12
 */
abstract class BaseFragment : Fragment(), IBaseUI, IBaseTemplateUI, IBaseFragment<Fragment>, View.OnClickListener {

    private var mTemplateController: ITemplateController? = null
    private var mImageLoader: IImageLoader? = null

    private var mLastTime: Long = 0
    private var mArgs: Bundle? = null
    private var mLayout: View? = null


    private var mInit: Boolean = false
    private var mDestroy: Boolean = false
    private var mPause: Boolean = false
    private var mStop: Boolean = false
    private var mResume: Boolean = false


    override fun isSingleLayout(): Boolean {
        return true
    }

    override fun getTemplateController(): ITemplateController? {
        return this.mTemplateController
    }

    override fun buildTemplateController(): ITemplateController {
        return TemplateController(this)
    }

    override fun getImageLoader(): IImageLoader? {
        return this.mImageLoader
    }

    override fun buildImageLoader(): IImageLoader? {
        return null
    }

    /**
     * 依赖Activity
     */
    override fun isTranslucentStatus(): Boolean {
        val activity = curActivity
        if (activity is IBaseTemplateUI) {
            val template = activity as IBaseTemplateUI
            return template.isTranslucentStatus
        }
        return false
    }

    /**
     * 依赖Activity
     */
    override fun getStatusBarHeight(): Int {
        val activity = curActivity
        if (activity is IBaseTemplateUI) {
            val template = activity as IBaseTemplateUI
            return template.statusBarHeight
        }
        return 0
    }

    override fun getCurApp(): IBaseApplication? {
        val activity = curActivity
        if (activity is IBaseTemplateUI) {
            val template = activity as IBaseTemplateUI
            return template.curApp
        }
        return null
    }

    override fun getCurActivity(): Activity? {
        return activity
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
        b.putString(IBaseTemplateUI.ARG_LAST_ACT, curActivity!!.javaClass.simpleName)
        i.putExtra(IBaseTemplateUI.ARG_BUNDLE, b)
        i.setClass(curContext, cls)
        if (requestCode != 0)
            startActivityForResult(i, requestCode)
        else
            startActivity(i)
    }

    override fun getCurContext(): Context {
        return context!!
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

    override fun <T : View?> findViewById(id: Int): T? {
        return mLayout?.findViewById<T>(id)
    }

    override fun <T : View> findViewById(id: Int, parent: View?): T? {
        return if (parent != null) parent.findViewById(id) else findViewById(id)
    }

    fun <T : View> setOnClickListener(id: Int): T? {
        return setOnClickListener(id, null)
    }

    override fun <T : View> setOnClickListener(id: Int, parent: View?): T? {
        val v = findViewById<T>(id, parent)
        v?.setOnClickListener(this)
        return v
    }


    override fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (time - this.mLastTime < 50L) return true
        this.mLastTime = time
        return false
    }

    override fun onClick(v: View) {
        if (!isFastClick)
            onViewClick(v)
    }


    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        if (!initAttrs()) return
//        if (isPermissionDenied) {
//            finish()
//            return
//        }
//        if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
//            requestedOrientation = screenOrientation
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (isTranslucentStatus) {
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            } else {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            }
//        }
//        initLayout()
//        if (this.mLayout != null)
//            setContentView(this.mLayout)
//        initView()
//        initData()
//    }

    override fun isInit(): Boolean = mInit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this.mLayout == null) {
            initAttrs()
            initLayout()
            initView()
            mInit = false
        }
        return this.mLayout
    }

    override fun onResume() {
        super.onResume()
        this.mPause = false
        this.mStop = false
        this.mResume = true
        if (!mInit) {
            mInit = true
            initData()
        }
    }

    /**
     * 初始化基本属性
     *
     * @return boolean
     */
    private fun initAttrs(): Boolean {
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
        this.mTemplateController?.onDestroy()
        this.mTemplateController = null
        this.mImageLoader?.onDestroy()
        this.mImageLoader = null
        this.mLastTime = 0
        this.mArgs = null
        this.mLayout = null
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
