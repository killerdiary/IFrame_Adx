package com.hy.frame.widget.recycler

import android.content.Context
import android.view.View

import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.adapter.IAdapterLongListener

/**
 * title IBaseAdapter
 * author heyan
 * time 19-8-13 下午2:45
 * desc List 和 Array不能 一起用
 */
interface IBaseAdapter<T> {
    val context: Context
    //    /**
    //     * 设置数据源
    //     *
    //     * @param datas 数据源
    //     */
    //    void setDatas(List<T> datas);
    //
    //    /**
    //     * 设置数据源
    //     *
    //     * @param datas 数据源
    //     */
    //    void setArray(T[] datas);
    //

    /**
     * 获取数据源
     */
    val datas: MutableList<T>?
    //
    //    /**
    //     * 获取数据源
    //     */
    //    T[] getArray();

    /**
     * 获取数据条数
     */
    val dataCount: Int

    /**
     * 是否需要绑定ID
     */
    val isBindDataId: Boolean

    //    /**
    //     * 设置监听器
    //     *
    //     * @param listener 监听器
    //     */
    //    void setListener(IAdapterListener listener);

    /**
     * 获取监听器
     */
    val listener: IAdapterListener<T>?

    /**
     * 获取长按监听器
     */
    val longListener: IAdapterLongListener<T>?

    val itemLayoutId: Int

    val itemView: View

    /**
     * 获取数据的Index
     *
     * @param position 第几条
     */
    fun getCurPosition(position: Int): Int

    /**
     * 获取某条数据对应View Id
     *
     * @param position 第几条
     */
    fun getDataId(position: Int): Int

    /**
     * 获取某条数据
     *
     * @param position 第几条
     */
    fun getDataItem(position: Int): T?


    fun setOnClickListener(v: View, position: Int)

    fun setOnLongClickListener(v: View, position: Int)

    /**
     * 主动刷新
     */
    fun refresh()

    /**
     * 刷新数据
     *
     * @param datas 数据源
     */
    fun refresh(datas: MutableList<T>?)

    fun inflate(resId: Int): View

    fun getItemHolder(v: View): BaseHolder

    fun bindItemData(holder: BaseHolder, position: Int)

    //    /**
    //     * 刷新数据
    //     *
    //     * @param datas 数据源
    //     */
    //    void refreshArray(T[] datas);
}
