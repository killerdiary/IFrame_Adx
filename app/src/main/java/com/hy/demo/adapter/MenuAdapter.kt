package com.hy.demo.adapter

import android.content.Context
import android.widget.TextView


import com.hy.demo.iframe.adx.R
import com.hy.frame.adapter.BaseAdapter
import com.hy.frame.adapter.BaseHolder
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.bean.MenuInfo

/**
 * title MenuAdapter
 * author heyan
 * time 19-8-13 下午5:20
 * desc 无
 */
class MenuAdapter(cxt: Context, datas: MutableList<MenuInfo>?, mListener: IAdapterListener<MenuInfo>) : BaseAdapter<MenuInfo>(cxt, datas, mListener) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_menu
    }

    override fun bindItemData(holder: BaseHolder, position: Int) {
        val item = getDataItem(position)
        val txtTitle = holder.findViewById<TextView>(R.id.menu_i_txtTitle)
        txtTitle.setText(item.title)
        setOnClickListener(txtTitle, position)
    }

}
