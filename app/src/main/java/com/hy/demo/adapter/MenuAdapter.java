package com.hy.demo.adapter;

import android.content.Context;
import android.widget.TextView;


import com.hy.demo.iframe.R;
import com.hy.frame.adapter.BaseAdapter;
import com.hy.frame.adapter.BaseHolder;
import com.hy.frame.adapter.IAdapterListener;
import com.hy.frame.bean.MenuInfo;

import java.util.List;

/**
 * title MenuAdapter
 * author heyan
 * time 19-8-13 下午5:20
 * desc 无
 */
public class MenuAdapter extends BaseAdapter<MenuInfo> {

    public MenuAdapter(Context cxt, List<MenuInfo> datas, IAdapterListener<MenuInfo> mListener) {
        super(cxt, datas, mListener);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_menu;
    }

    @Override
    public void bindItemData(BaseHolder holder, int position) {
        MenuInfo item = getDataItem(position);
        TextView txtTitle = holder.findViewById(R.id.menu_i_txtTitle);
        txtTitle.setText(item.getTitle());
        setOnClickListener(txtTitle, position);
    }

}
