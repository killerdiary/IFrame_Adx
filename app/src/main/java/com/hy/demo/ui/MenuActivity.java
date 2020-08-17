package com.hy.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.hy.demo.adapter.MenuAdapter;
import com.hy.demo.app.BaseActivity;
import com.hy.demo.iframe.R;
import com.hy.frame.adapter.IAdapterListener;
import com.hy.frame.base.BaseTemplateUI;
import com.hy.frame.bean.MenuInfo;
import com.hy.frame.common.IAppUI;
import com.hy.frame.common.ITemplateUI;
import com.hy.frame.util.FormatUtil;
import com.hy.frame.util.ResUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
public class MenuActivity extends BaseActivity<MenuActivity.TemplateUI> {
    private List<MenuInfo> datas;
    protected int xmlId;
    protected int titleId;
    protected static final String ARG_XMLID = "arg_xmlid";
    protected static final String ARG_TITLEID = "arg_titleid";
    private static final String KEY_CLS = "cls";
    private static final String KEY_MENU = "menu";
    private static final String KEY_ARGS = "args";

    @Override
    public TemplateUI buildTemplateUI() {
        return new TemplateUI(this);
    }

    @Override
    public void initData() {
        if (getArgs() != null) {
            xmlId = getArgs().getInt(ARG_XMLID);
            titleId = getArgs().getInt(ARG_TITLEID);
        }
        if (xmlId <= 0) {
            finish();
            return;
        }
        initHeader(android.R.drawable.ic_menu_revert, titleId);
        requestData();
    }


    public void requestData() {
        datas = ResUtil.getMenus(getCurContext(), xmlId);
        updateUI();
    }

    public void updateUI() {
        getTemplateUI().updateUI(datas);
    }

    @Override
    public void onViewClick(View v) {

    }


    public static void startAct(IAppUI tempUI, int xmlId, int titleId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_XMLID, xmlId);
        bundle.putInt(ARG_TITLEID, titleId);
        tempUI.startAct(MenuActivity.class, bundle, null);
    }


    static class TemplateUI extends BaseTemplateUI implements IAdapterListener<MenuInfo> {
        private GridView cGrd;

        private MenuAdapter adapter;

        public TemplateUI(IAppUI iUI) {
            super(iUI);
        }

        /**
         * LayoutId 默认值为0
         */
        @Override
        public int getLayoutId() {
            return R.layout.v_menu;
        }

        public void initView() {
            cGrd = findViewById(R.id.menu_cGrd);
        }

        public void updateUI(List<MenuInfo> datas) {
            if (adapter == null) {
                adapter = new MenuAdapter(getCurContext(), datas, this);
                cGrd.setAdapter(adapter);
            } else
                adapter.refresh(datas);
        }

        private int getXmlId(String name) {
            return getResources().getIdentifier(name, "xml", getCurContext().getPackageName());
        }

        @Override
        public void onViewClick(View v, MenuInfo item, int position) {
            //getTemplateUI().showToast(position + "");
            String clsStr = item.getValue(KEY_CLS);
            String menuStr = item.getValue(KEY_MENU);
            if (FormatUtil.isNoEmpty(menuStr)) {
                int xmlId = getXmlId(menuStr);
                MenuActivity.startAct(iUI, xmlId, item.getTitle());
                return;
            }
            if (FormatUtil.isEmpty(clsStr)) return;
            String args = item.getValue(KEY_ARGS);
            Bundle bundle = new Bundle();
            if (FormatUtil.isNoEmpty(args)) {
                JSONArray json;
                try {
                    json = new JSONArray(args);
                    String type;
                    String key;
                    JSONObject obj;
                    if (json.length() > 0) {
                        for (int i = 0; i < json.length(); i++) {
                            obj = (JSONObject) json.get(0);
                            type = obj.getString("type");
                            key = obj.getString("key");
                            if (type.equalsIgnoreCase("int")) {
                                bundle.putInt(key, obj.getInt("value"));
                            } else if (type.equalsIgnoreCase("long")) {
                                bundle.putLong(key, obj.getLong("value"));
                            } else if (type.equalsIgnoreCase("boolean")) {
                                bundle.putBoolean(key, obj.getBoolean("value"));
                            } else if (type.equalsIgnoreCase("double")) {
                                bundle.putDouble(key, obj.getDouble("value"));
                            } else {
                                bundle.putString(key, obj.getString("value"));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Class<?> cls = Class.forName(clsStr);
                iUI.startAct(cls, bundle, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
