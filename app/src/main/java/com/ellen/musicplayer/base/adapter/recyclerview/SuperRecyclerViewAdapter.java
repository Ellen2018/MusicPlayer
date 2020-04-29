package com.ellen.musicplayer.base.adapter.recyclerview;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * 不需要单独创建适配器类，一个类搞定所有RecyclerView适配需求
 * 貌似存在些问题，弃用
 * @param <T>
 */
public class SuperRecyclerViewAdapter<T> extends BaseSingleRecyclerViewAdapter<T, BaseViewHolder> {

    private int layoutId;
    private BindData<T> bindData;

    public SuperRecyclerViewAdapter(Context context, int layoutId, List<T> dataList) {
        super(context, dataList);
        this.layoutId = layoutId;
    }

    @Override
    protected int getItemLayoutId() {
        return layoutId;
    }

    @Override
    protected BaseViewHolder getNewViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    @Override
    protected void showData(BaseViewHolder baseViewHolder, T data, int position) {
        if (bindData != null) {
            bindData.bindData(baseViewHolder, data, position);
        }
    }

    public void setBindData(BindData<T> bindData) {
        this.bindData = bindData;
    }

    public interface BindData<T> {
        void bindData(BaseViewHolder baseViewHolder, T data, int position);
    }
}
