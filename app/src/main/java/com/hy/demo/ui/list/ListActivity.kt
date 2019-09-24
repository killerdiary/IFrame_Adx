package com.hy.demo.ui.list

import android.graphics.Color
import android.view.View
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.hy.demo.adapter.ListAdapter
import com.hy.demo.app.BaseActivity
import com.hy.demo.iframe.adx.R
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.widget.recycler.GridItemDecoration
import com.hy.frame.widget.recycler.LinearItemDecoration

import java.util.ArrayList

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
class ListActivity : BaseActivity(), IAdapterListener<String> {
    private var cList: RecyclerView? = null
    private var datas: MutableList<String>? = null
    private var adapter: ListAdapter? = null


    override fun getLayoutId(): Int {
        return R.layout.v_list_list
    }

    override fun initView() {
        cList = findViewById(R.id.list_list_cList)
        cList?.layoutManager = LinearLayoutManager(curContext)
    }

    override fun initData() {
        initHeader(android.R.drawable.ic_menu_revert, R.string.menu_list_list)
        requestData()
    }


    fun requestData() {
        datas = ArrayList()
        for (i in 0..99) {
            datas!!.add("测试$i")
        }
        updateUI()
    }

    fun updateUI() {
        if (adapter == null) {
            adapter = ListAdapter(curContext, datas, this)
            cList!!.adapter = adapter
            val padding = 10
            cList!!.addItemDecoration(LinearItemDecoration(cList!!, padding, Color.TRANSPARENT).setPaddingTop(padding).build())
        } else
            adapter!!.refresh(datas)
    }

    override fun onViewClick(v: View) {

    }

    override fun onViewClick(v: View, item: String, position: Int) {
        templateController?.showToast(item)

    }

}
