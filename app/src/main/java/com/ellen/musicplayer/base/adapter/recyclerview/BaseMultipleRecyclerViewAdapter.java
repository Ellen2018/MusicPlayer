package com.ellen.musicplayer.base.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public abstract class BaseMultipleRecyclerViewAdapter extends BaseRecyclerViewAdapter<BaseViewHolder> {

    private WeakReference<Context> contextWeakReference;

    public BaseMultipleRecyclerViewAdapter(Context context){
        contextWeakReference = new WeakReference<>(context);
    }

    public Context getContext(){
        return contextWeakReference.get();
}

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        return getNewBaseViewHolder(viewGroup,itemType);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder baseViewHolder, final int position) {
        showData(baseViewHolder,position);
        if(onItemClickListener != null){
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(baseViewHolder,position);
                }
            });
        }
        if(onItemLongClickListener != null){
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLoncClick(baseViewHolder,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getItemSize();
    }

    @Override
    public int getItemViewType(int position) {
        return getMultipleItemViewType(position);
    }

    protected abstract int getMultipleItemViewType(int position);
    protected abstract int getItemSize();
    protected abstract BaseViewHolder getNewBaseViewHolder(@NonNull ViewGroup viewGroup, int itemType);
    protected abstract void showData(BaseViewHolder baseViewHolder, int position);
}
