package com.hy.demo.adapter

import android.content.Context
import android.widget.TextView
import com.hy.demo.iframe.adx.R
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.widget.recycler.BaseAdapter

/**
 * title ListAdapter
 * author heyan
 * time 19-8-13 下午5:20
 * desc 无
 */
class ListAdapter(cxt: Context, datas: MutableList<String>?, mListener: IAdapterListener<String>) : BaseAdapter<String>(cxt, datas, mListener) {

    override val isBindDataId: Boolean
        get() = false

    override val itemLayoutId: Int
        get() = R.layout.item_menu

    override fun bindItemData(holder: com.hy.frame.widget.recycler.BaseHolder, position: Int) {
        val item = getDataItem(position)
        val txtTitle = holder.findViewById<TextView>(R.id.menu_i_txtTitle)
        txtTitle.text = item
        setOnClickListener(txtTitle, position)
        setOnLongClickListener(txtTitle, position)
    }

}
