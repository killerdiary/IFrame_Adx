package com.hy.frame.widget.recycler

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * title BaseHolder
 * author heyan
 * time 19-8-13 下午3:14
 * desc 无
 */
open class BaseHolder(private val mLayout: View, width: Int = ViewGroup.LayoutParams.MATCH_PARENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) : RecyclerView.ViewHolder(mLayout) {
    private val views: SparseArray<View> = SparseArray()

    init {
        if (mLayout.layoutParams == null) {
            mLayout.layoutParams = ViewGroup.LayoutParams(width, height)
        }
    }

    /**
     * 获取 控件
     *
     * @param id 布局中某个组件的id
     */
    fun <V : View> findViewById(id: Int): V? {
        var v: View? = this.views.get(id)
        if (v == null) {
            v = mLayout.findViewById(id)
            views.put(id, v)
        }
        return mLayout.findViewById(id)
    }


    /**
     * 获取 控件
     *
     * @param id     布局中某个组件的id
     * @param parent parent
     */
    fun <V : View> findViewById(id: Int, parent: View?): V? {
        return if (parent != null) parent.findViewById(id) else mLayout.findViewById(id)
    }
}
