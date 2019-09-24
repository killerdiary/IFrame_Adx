package com.hy.demo.ui.list

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hy.demo.adapter.ListAdapter
import com.hy.demo.app.BaseActivity
import com.hy.demo.iframe.adx.R
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.adapter.IAdapterLongListener
import com.hy.frame.widget.recycler.ColorItemDecoration
import com.hy.frame.widget.recycler.GridItemDecoration
import java.util.*

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
class GridActivity : BaseActivity(), IAdapterLongListener<String> {
    private var cGrid: RecyclerView? = null
    private var datas: MutableList<String>? = null
    private var adapter: ListAdapter? = null


    override fun getLayoutId(): Int {
        return R.layout.v_list_grid
    }

    override fun initView() {
        cGrid = findViewById(R.id.list_grid_cGrid)
        cGrid?.layoutManager = GridLayoutManager(curContext, 3)
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
            cGrid!!.adapter = adapter
            val padding = 10
            cGrid!!.addItemDecoration(GridItemDecoration(cGrid!!, padding, Color.TRANSPARENT).setDividerVertical(padding).setPaddingTop(padding).setPaddingLeft(padding).build())
        } else
            adapter!!.refresh(datas)
    }

    override fun onViewClick(v: View) {

    }

    override fun onViewClick(v: View, item: String, position: Int) {
        templateController?.showToast("点击 $item")

    }

    override fun onViewLongClick(v: View?, item: String?, position: Int) {
        templateController?.showToast("长按 $item")
    }
}
