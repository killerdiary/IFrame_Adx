package com.hy.demo.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hy.frame.adapter.BaseAdapter;
import com.hy.frame.adapter.BaseHolder;
import com.hy.frame.adapter.IAdapterListener;

import java.util.List;

/**
 * title 无
 * author heyan
 * time 19-8-13 下午5:20
 * desc 无
 */
public class TestAdapter extends BaseAdapter<String> {

    public TestAdapter(Context cxt, List<String> datas, IAdapterListener<String> mListener) {
        super(cxt, datas, mListener);
    }

    @Override
    public boolean isBindDataId() {
        return false;
    }

    @Override
    public int getItemLayoutId() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void bindItemData(BaseHolder holder, int position) {
        TextView txtTitle = holder.findViewById(android.R.id.text1);
        txtTitle.setText("测试" + position);
    }

}
