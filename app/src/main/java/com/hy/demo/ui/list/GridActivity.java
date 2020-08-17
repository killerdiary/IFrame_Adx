package com.hy.demo.ui.list;

import android.view.View;
import android.widget.GridView;

import com.hy.demo.adapter.ListAdapter;
import com.hy.demo.app.BaseActivity;
import com.hy.demo.iframe.R;
import com.hy.frame.adapter.IAdapterListener;
import com.hy.frame.base.BaseTemplateUI;
import com.hy.frame.common.IAppUI;

import java.util.ArrayList;
import java.util.List;

/**
 * title 无
 * author heyan
 * time 19-7-11 下午3:27
 * desc 无
 */
public class GridActivity extends BaseActivity<GridActivity.TemplateUI> {

    private List<String> datas;

    @Override
    public GridActivity.TemplateUI buildTemplateUI() {
        return new GridActivity.TemplateUI(this);
    }

    @Override
    public void initData() {
        initHeader(android.R.drawable.ic_menu_revert, R.string.menu_list_list);
        requestData();
    }

    public void requestData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("测试" + i);
        }
        updateUI();
    }

    public void updateUI() {
        getTemplateUI().updateUI(datas);
    }

    @Override
    public void onViewClick(View v) {

    }


    static class TemplateUI extends BaseTemplateUI implements IAdapterListener<String> {
        private GridView cGrid;
        private ListAdapter adapter;

        public TemplateUI(IAppUI iUI) {
            super(iUI);
        }

        /**
         * LayoutId 默认值为0
         */
        @Override
        public int getLayoutId() {
            return R.layout.v_list_grid;
        }

        public void initView() {
            cGrid = findViewById(R.id.list_grid_cGrid);
        }

        public void updateUI(List<String> datas) {
            if (adapter == null) {
                adapter = new ListAdapter(getCurContext(), datas, this);
                cGrid.setAdapter(adapter);
            } else
                adapter.refresh(datas);
        }

        @Override
        public void onViewClick(View v, String item, int position) {
            showToast(item);

        }
    }
}
