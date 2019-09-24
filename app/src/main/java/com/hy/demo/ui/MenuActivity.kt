package com.hy.demo.ui

import android.os.Bundle
import android.view.View
import android.widget.GridView

import com.hy.demo.adapter.MenuAdapter
import com.hy.demo.app.BaseActivity
import com.hy.demo.iframe.adx.R
import com.hy.frame.adapter.IAdapterListener
import com.hy.frame.bean.MenuInfo
import com.hy.frame.common.IBaseTemplateUI
import com.hy.frame.util.FormatUtil
import com.hy.frame.util.ResUtil

import org.json.JSONArray
import org.json.JSONObject

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
open class MenuActivity : BaseActivity(), IAdapterListener<MenuInfo> {
    private var cGrd: GridView? = null
    private var datas: MutableList<MenuInfo>? = null
    private var adapter: MenuAdapter? = null
    protected var xmlId: Int = 0
    protected var titleId: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.v_menu
    }

    override fun initView() {
        cGrd = findViewById(R.id.menu_cGrd)
    }

    override fun initData() {
        if (args != null) {
            xmlId = args!!.getInt(ARG_XMLID)
            titleId = args!!.getInt(ARG_TITLEID)
        }
        if (xmlId <= 0) {
            finish()
            return
        }
        initHeader(android.R.drawable.ic_menu_revert, titleId)
        requestData()
    }


    fun requestData() {
        datas = ResUtil.getMenus(curContext, xmlId)
        updateUI()
    }

    fun updateUI() {
        if (adapter == null) {
            adapter = MenuAdapter(curContext, datas, this)
            cGrd!!.adapter = adapter
        } else
            adapter!!.refresh(datas)
    }

    override fun onViewClick(v: View) {

    }

    override fun onViewClick(v: View, item: MenuInfo, position: Int) {
        //getTemplateController().showToast(position + "");
        val clsStr = item.getValue(KEY_CLS)
        val menuStr = item.getValue(KEY_MENU)
        if (FormatUtil.isNoEmpty(menuStr)) {
            val xmlId = getXmlId(menuStr)
            MenuActivity.startAct(this, xmlId, item.title)
            return
        }
        if (FormatUtil.isEmpty(clsStr)) return
        val args = item.getValue(KEY_ARGS)
        val bundle = Bundle()
        if (FormatUtil.isNoEmpty(args)) {
            val json: JSONArray
            try {
                json = JSONArray(args)
                var type: String
                var key: String
                var obj: JSONObject
                if (json.length() > 0) {
                    for (i in 0 until json.length()) {
                        obj = json.get(0) as JSONObject
                        type = obj.getString("type")
                        key = obj.getString("key")
                        if (type.equals("int", ignoreCase = true)) {
                            bundle.putInt(key, obj.getInt("value"))
                        } else if (type.equals("long", ignoreCase = true)) {
                            bundle.putLong(key, obj.getLong("value"))
                        } else if (type.equals("boolean", ignoreCase = true)) {
                            bundle.putBoolean(key, obj.getBoolean("value"))
                        } else if (type.equals("double", ignoreCase = true)) {
                            bundle.putDouble(key, obj.getDouble("value"))
                        } else {
                            bundle.putString(key, obj.getString("value"))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        try {
            val cls = Class.forName(clsStr)
            startAct(cls, bundle, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getXmlId(name: String): Int {
        return resources.getIdentifier(name, "xml", packageName)
    }

    companion object {
        val ARG_XMLID = "arg_xmlid"
        val ARG_TITLEID = "arg_titleid"
        val KEY_CLS = "cls"
        val KEY_MENU = "menu"
        val KEY_ARGS = "args"

        fun startAct(tempUI: IBaseTemplateUI, xmlId: Int, titleId: Int) {
            val bundle = Bundle()
            bundle.putInt(ARG_XMLID, xmlId)
            bundle.putInt(ARG_TITLEID, titleId)
            tempUI.startAct(MenuActivity::class.java, bundle, null)
        }
    }
}
