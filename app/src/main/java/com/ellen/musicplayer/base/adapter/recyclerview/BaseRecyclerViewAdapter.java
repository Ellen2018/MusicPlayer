package com.ellen.musicplayer.base.adapter.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerViewAdapter<T extends BaseViewHolder> extends RecyclerView.Adapter<T> {

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onLongClickListener) {
        this.onItemLongClickListener = onLongClickListener;
    }

    public interface OnItemClickListener<T extends BaseViewHolder>{
        void onItemClick(T t, int position);
    }

    public interface OnItemLongClickListener<T extends BaseViewHolder>{
        boolean onItemLongClick(T t, int position);
    }

}
