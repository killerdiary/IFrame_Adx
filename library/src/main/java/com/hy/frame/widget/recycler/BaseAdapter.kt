package com.hy.frame.widget.recycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.adapter.IAdapterLongListener
import com.hy.iframe.adx.R


/**
 * Base Adapter For RecyclerView
 * @author HeYan
 * @time 2017/9/25 11:45
 */
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseHolder>, IBaseAdapter<T> {

    private val mCxt: Context
    private var mDatas: MutableList<T>? = null
    private val mListener: IAdapterListener<T>?
    private var mClickListener: View.OnClickListener? = null
    private var mLongClickListener: View.OnLongClickListener? = null

    constructor (mCxt: Context) : this(mCxt, null)

    constructor(mCxt: Context, datas: MutableList<T>?) : this(mCxt, datas, null)

    constructor(cxt: Context, datas: MutableList<T>?, mListener: IAdapterListener<T>?) {
        this.mCxt = cxt
        this.mDatas = datas
        this.mListener = mListener
    }

    //----------IBaseAdapter START---------------------------------

    override val context: Context
        get() = this.mCxt

    override val datas: MutableList<T>?
        get() = this.mDatas

    override val dataCount: Int
        get() = this.mDatas?.size ?: 0

    override val isBindDataId: Boolean
        get() = true

    override val listener: IAdapterListener<T>?
        get() = this.mListener

    override val longListener: IAdapterLongListener<T>?
        get() = if (listener != null && listener is IAdapterLongListener<*>) {
            listener as IAdapterLongListener<T>
        } else null


    override val itemView: View
        get() = inflate(itemLayoutId)

    override fun getCurPosition(position: Int): Int = position

    override fun getDataId(position: Int): Int = position

    override fun getDataItem(position: Int): T? {
        if (position < dataCount)
            return datas!![position]
        return null
    }

    override fun setOnClickListener(v: View, position: Int) {
        if (this.mClickListener == null) {
            this.mClickListener = View.OnClickListener { child ->
                val p = child.getTag(R.id.adapter_position) as Int
                listener?.onViewClick(child, getDataItem(p), p)
            }
        }
        v.setTag(R.id.adapter_position, position)
        v.setOnClickListener(this.mClickListener)
    }

    override fun setOnLongClickListener(v: View, position: Int) {
        if (this.mLongClickListener == null) {
            this.mLongClickListener = View.OnLongClickListener { child ->
                val p = child.getTag(R.id.adapter_position) as Int
                longListener?.onViewLongClick(child, getDataItem(p), p)
                true //拦截点击事件
            }
        }
        v.setTag(R.id.adapter_position, position)
        v.setOnLongClickListener(this.mLongClickListener)
    }

    override fun refresh() {
        this.notifyDataSetChanged()
    }

    override fun refresh(datas: MutableList<T>?) {
        if (this.mDatas != null) {
            this.mDatas!!.clear()
            if (datas != null) this.mDatas!!.addAll(datas)
        } else {
            this.mDatas = datas
        }
        refresh()
    }

    override fun inflate(resId: Int): View = View.inflate(context, resId, null)

    override fun getItemHolder(v: View): BaseHolder = BaseHolder(v)
    //----------IBaseAdapter END---------------------------------

    //----------Adapter START---------------------------------

    override fun getItemViewType(position: Int): Int = TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder = getItemHolder(itemView)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = dataCount

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        bindItemData(holder, position)
    }

    //----------Adapter END---------------------------------

    //    override val itemLayoutId: Int
//        get() = View.NO_ID
//    override fun bindItemData(holder: BaseHolder, position: Int) {
//    }

    companion object {
        const val TYPE_ITEM = 0
    }
}
