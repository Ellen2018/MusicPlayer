package com.ellen.musicplayer.base.adapter.recyclerview;


import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T extends RecyclerView.ViewHolder>{
        void onItemClick(T t, int position);
    }

    public interface OnItemLongClickListener<T extends RecyclerView.ViewHolder>{
        boolean onItemLoncClick(T t,int position);
    }


}
