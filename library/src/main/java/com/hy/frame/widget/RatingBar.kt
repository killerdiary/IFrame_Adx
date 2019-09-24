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
class RatingBar : View {

    //缩放比例
    private var ratio = 1f
    //星星默认状态
    private var mStarNormal: Bitmap? = null
    //星星选中状态
    private var mStarSelected: Bitmap? = null
    //星星单个宽度
    private var mStarWidth: Int = 48
    //星星单个高度
    private var mStarHeight: Int = 0
    //选中星星个数
    private var mProgress: Float = 0f
    //最大星星个数
    private var mMaxProgress: Int = 5
    //星星间的距离
    private var mChildSpace: Int = 0
    //步长, 用于触摸更改进度
    private var mStep: Float = 0.5f
    private var mAutoMeasure: Boolean = false

    private var mListener: IProgressListener? = null

    // 画笔
    private var mPaint: Paint? = null
    //private var mTempBmp: Bitmap? = null
    private var tempRect: Rect? = null
    private var dstRect: Rect? = null

    fun getMaxProgress(): Int = mMaxProgress

    fun getProgress(): Float = mProgress

    fun getChildSpace(): Int = mChildSpace

    fun setProgress(progress: Float) {
        this.mProgress = getRealProgress(progress)
        invalidate()
    }

    fun setMaxProgress(maxProgress: Int) {
        this.mMaxProgress = maxProgress
        this.mProgress = getRealProgress(this.mProgress)
        invalidate()
    }

    fun setStarWidth(width: Int) {
        this.mStarWidth = width
        if (this.mStarWidth > 0) {
            resetBitmap()
        }
        this.mProgress = getRealProgress(this.mProgress)
        invalidate()
    }

    fun setStarHeight(height: Int) {
        this.mStarHeight = height
        if (this.mStarHeight > 0) {
            resetBitmap()
        }
        this.mProgress = getRealProgress(this.mProgress)
        invalidate()
    }

    fun setChildSpace(childSpace: Int) {
        this.mChildSpace = childSpace
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
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar, defStyleAttr, 0)
        val ratioWidth = a.getInt(R.styleable.RatingBar_rbRatioWidth, context.resources.displayMetrics.widthPixels).toFloat()
        ratio = context.resources.displayMetrics.widthPixels.toFloat() / ratioWidth

        val starNormalId = a.getResourceId(R.styleable.RatingBar_rbStarNormal, 0)
        if (starNormalId == 0)
            throw IllegalArgumentException("please set the property 'rbStarNormal'")
        this.mStarNormal = BitmapFactory.decodeResource(resources, starNormalId)
        val starSelectedId = a.getResourceId(R.styleable.RatingBar_rbStarSelected, 0)
        if (starSelectedId == 0)
            throw IllegalArgumentException("please set the property 'rbStarSelected'")
        this.mStarSelected = BitmapFactory.decodeResource(resources, starSelectedId)
        this.mStarWidth = a.getInt(R.styleable.RatingBar_rbStarWidth, 48)
        this.mStarHeight = a.getInt(R.styleable.RatingBar_rbStarHeight, this.mStarWidth)
        this.mStarWidth = getRatioSize(this.mStarWidth)
        this.mStarHeight = getRatioSize(this.mStarHeight)
        this.mProgress = a.getFloat(R.styleable.RatingBar_rbProgress, 0f)
        this.mMaxProgress = a.getInt(R.styleable.RatingBar_rbMaxProgress, 5)
        this.mChildSpace = a.getInt(R.styleable.RatingBar_rbChildSpace, 0)
        this.mChildSpace = getRatioSize(this.mChildSpace)
        this.mStep = a.getFloat(R.styleable.RatingBar_rbStep, 0f)
        this.mAutoMeasure = a.getBoolean(R.styleable.RatingBar_rbAutoMeasure, false)
        a.recycle()
        if (this.mStarWidth > 0) {
            resetBitmap()
        }
        this.mProgress = getRealProgress(this.mProgress)
        this.mPaint = Paint()
    }

    private fun getRatioSize(number: Int): Int {
        return (number.toFloat() * ratio).toInt()
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
        if (this.mStarHeight == 0)
            this.mStarHeight = this.mStarWidth
        if (this.mStarNormal != null)
            this.mStarNormal = resetBitmap(this.mStarNormal!!, this.mStarWidth, this.mStarHeight)
        if (this.mStarSelected != null)
            this.mStarSelected = resetBitmap(this.mStarSelected!!, this.mStarWidth, this.mStarHeight)
    }

    private fun resetBitmap(bmp: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bmp, width, height, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //自动计算扩充宽高
        if (this.mAutoMeasure) {
            var width = paddingLeft + paddingRight + (this.mStarNormal?.width ?: 0) * this.mMaxProgress
            if (this.mMaxProgress > 1 && this.mChildSpace > 0)
                width += this.mChildSpace * (this.mMaxProgress - 1)
            val height = paddingTop + paddingBottom + (this.mStarNormal?.height ?: 0)
            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        var left = 0
        val top = paddingTop
        val width = mStarNormal?.width ?: 0

        for (i in 1..this.mMaxProgress) {
            left = paddingLeft + (i - 1) * (width + this.mChildSpace)
            //1. 绘制默认星星
            if (this.mStarNormal != null)
                canvas?.drawBitmap(this.mStarNormal!!, left.toFloat(), top.toFloat(), this.mPaint)
            //2. 绘制选中星星
            if (this.mStarSelected == null)
                continue
            //2.1 如果大于，整颗星
            if (this.mProgress >= i)
                canvas?.drawBitmap(this.mStarSelected!!, left.toFloat(), top.toFloat(), this.mPaint)
            else if (this.mProgress > i - 1) {
                //2.2 绘制不完整星
                val number = this.mProgress % 1
                if (number > 0) {
                    if (this.tempRect == null)
                        this.tempRect = Rect()
                    this.tempRect!!.right = this.mStarWidth
                    this.tempRect!!.bottom = this.mStarHeight
                    if (this.dstRect == null)
                        this.dstRect = Rect()
                    this.dstRect!!.left = left
                    this.dstRect!!.right = left + this.mStarWidth
                    this.dstRect!!.top = top
                    this.dstRect!!.bottom = top + this.mStarHeight
                    //这里处理不完全星星
                    val newWidth = (number * this.mStarSelected!!.width).toInt()
                    if (newWidth > 0) {
                        this.tempRect!!.right = newWidth
                        this.dstRect!!.right = left + newWidth
                        canvas?.drawBitmap(this.mStarSelected!!, this.tempRect!!, this.dstRect!!, this.mPaint)
                    }

                }
            }
        }
        //canvas?.restore()
    }

    interface IProgressListener {
        fun onProgressChange(progress: Float)
    }
}