package com.hy.frame.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hy.iframe.adx.R


/**
 * title 无
 * author heyan
 * time 19-4-22 上午11:47
 * desc 无
 */
class ProgressBar : View {
    //默认状态
    private var mNormal: Bitmap? = null
    //选中状态
    private var mSelected: Bitmap? = null
    //宽度
    private var mWidth: Int = 48
    //高度
    private var mHeight: Int = 0
    //当前进度
    private var mProgress: Float = 0f
    //最大
    private var mMaxProgress: Int = 100
    //步长, 用于触摸更改进度
    private var mStep: Float = 1f

    private var mListener: IProgressListener? = null

    // 画笔
    private var mPaint: Paint? = null
    private var tempRect: Rect? = null
    fun getMaxProgress(): Int = mMaxProgress

    fun getProgress(): Float = mProgress

    fun setProgress(progress: Float) {
        this.mProgress = getRealProgress(progress)
        invalidate()
    }

    fun setMaxProgress(maxProgress: Int) {
        this.mMaxProgress = maxProgress
        this.mProgress = getRealProgress(this.mProgress)
        invalidate()
    }

    fun setStep(step: Float) {
        this.mStep = step
        this.mProgress = getRealProgress(this.mProgress)
        invalidate()
    }

    fun setListener(listener: IProgressListener) {
        this.mListener = listener
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (context == null) return
        val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, 0)
        val normalId = a.getResourceId(R.styleable.ProgressBar_pbNormal, 0)
        if (normalId == 0)
            throw IllegalArgumentException("please set the property 'pbNormal'")
        this.mNormal = BitmapFactory.decodeResource(resources, normalId)
        val selectedId = a.getResourceId(R.styleable.ProgressBar_pbSelected, 0)
        if (selectedId == 0)
            throw IllegalArgumentException("please set the property 'pbSelected'")
        this.mSelected = BitmapFactory.decodeResource(resources, selectedId)
        this.mProgress = a.getFloat(R.styleable.ProgressBar_pbProgress, 0f)
        this.mMaxProgress = a.getInt(R.styleable.ProgressBar_pbMaxProgress, 100)
        this.mStep = a.getFloat(R.styleable.ProgressBar_pbStep, 0f)
        a.recycle()
        this.mPaint = Paint()
        this.mProgress = getRealProgress(this.mProgress)
    }

    private fun initRealProgress() {
        this.mProgress = getRealProgress(this.mProgress)
        if (this.tempRect == null)
            this.tempRect = Rect()
        this.tempRect!!.right = this.mWidth
        this.tempRect!!.bottom = this.mHeight
        if (this.mProgress > 0 && this.mProgress < this.mMaxProgress) {
            //这里处理不完全星星
            val newWidth = (this.mProgress * this.mWidth / this.mMaxProgress).toInt()
            if (newWidth > 0 && newWidth <= this.mWidth) {
                this.tempRect!!.right = newWidth
            }
        }
    }

    private fun getRealProgress(progress: Float): Float {
        if (progress <= 0f) return 0f
        if (progress >= this.mMaxProgress) return this.mMaxProgress.toFloat()
        if (this.mStep <= 0 || this.mStep >= this.mMaxProgress) return progress
        var newProgress: Float = (progress / this.mStep).toInt() * this.mStep
        val number = progress % this.mStep
        if (number > 0)
            newProgress += this.mStep
        if (newProgress >= this.mMaxProgress)
            return this.mMaxProgress.toFloat()
        return newProgress
    }

    private fun resetBitmap() {
        if (this.mHeight == 0)
            this.mHeight = this.mWidth
        if (this.mNormal != null)
            this.mNormal = resetBitmap(this.mNormal!!, this.mWidth, this.mHeight)
        if (this.mSelected != null)
            this.mSelected = resetBitmap(this.mSelected!!, this.mWidth, this.mHeight)
    }

    private fun resetBitmap(bmp: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bmp, width, height, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.mWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        this.mHeight = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(this.mWidth, this.mHeight)
        //自动计算扩充宽高
        if (this.mNormal == null || this.mSelected == null) return
        if (this.mNormal!!.width == this.mWidth) return
        resetBitmap()
        initRealProgress()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        val left = 0f
        val top = 0f
        //1. 绘制默认
        if (this.mNormal != null)
            canvas?.drawBitmap(this.mNormal!!, left, top, this.mPaint)
        //2. 绘制进度
        if (this.mSelected == null)
            return
        if (this.mProgress >= this.mMaxProgress)
            canvas?.drawBitmap(this.mSelected!!, left, top, this.mPaint)
        else if (this.mProgress > 0) {
            //2.2 绘制不完整
            if (this.tempRect == null)
                this.tempRect = Rect()
            this.tempRect!!.right = this.mWidth
            this.tempRect!!.bottom = this.mHeight

            //这里处理不完全星星
            val newWidth = (this.mProgress * this.mWidth / this.mMaxProgress).toInt()
            if (newWidth > 0 && newWidth <= this.mWidth) {
                this.tempRect!!.right = newWidth
            }
            canvas?.drawBitmap(this.mSelected!!, this.tempRect!!, this.tempRect!!, this.mPaint)
        }
    }

    interface IProgressListener {
        fun onProgressChange(progress: Float)
    }
}