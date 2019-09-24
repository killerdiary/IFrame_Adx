package com.hy.frame.widget.recycler

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * title BaseItemDecoration
 * author heyan
 * time 19-9-23 下午3:32
 * desc 无
 */
abstract class BaseItemDecoration private constructor(protected var rcyList: RecyclerView, protected var divider: Int) : RecyclerView.ItemDecoration() {
    protected var dividerVertical: Int = 0
    protected var drawable: Drawable? = null
    protected var mPaint: Paint? = null
    protected var paddingTop: Int = 0
    protected var paddingLeft: Int = 0
    protected var paddingRight: Int = 0
    protected var paddingBottom: Int = 0
    protected var adapter: BaseAdapter<*>? = null

    constructor(rcyList: RecyclerView, divider: Int, color: Int) : this(rcyList, divider) {
        this.mPaint = Paint()
        this.mPaint!!.color = color
        this.mPaint!!.flags = Paint.ANTI_ALIAS_FLAG
    }

    constructor(rcyList: RecyclerView, divider: Int, drawable: Drawable) : this(rcyList, divider) {
        this.drawable = drawable
    }

    fun setDividerVertical(dividerVertical: Int): BaseItemDecoration {
        this.dividerVertical = dividerVertical
        return this
    }

    fun setPaddingTop(paddingTop: Int): BaseItemDecoration {
        this.paddingTop = paddingTop
        return this
    }

    fun setPaddingLeft(paddingLeft: Int): BaseItemDecoration {
        this.paddingLeft = paddingLeft
        return this
    }

    fun setPaddingRight(paddingRight: Int): BaseItemDecoration {
        this.paddingRight = paddingRight
        return this
    }


    fun setPaddingBottom(paddingBottom: Int): BaseItemDecoration {
        this.paddingBottom = paddingBottom
        return this
    }

    open fun build(): BaseItemDecoration {
        return this
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mPaint != null && mPaint!!.alpha == 0)
            return
        if (drawable != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && drawable!!.alpha == 0)
            return
        drawDivider(c)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (adapter == null) {
            if (parent.adapter == null || parent.adapter !is BaseAdapter<*>) throw RuntimeException("adapter is not BaseAdapter")
            adapter = parent.adapter as BaseAdapter<*>
            //initData()
        }
        configureItemOutRect(outRect, view)
    }

    internal abstract fun drawDivider(canvas: Canvas)

    internal abstract fun configureItemOutRect(outRect: Rect, view: View)
}
