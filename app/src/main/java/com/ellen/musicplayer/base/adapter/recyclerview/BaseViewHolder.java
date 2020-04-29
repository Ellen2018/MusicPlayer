package com.ellen.musicplayer.base.adapter.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mItemView = itemView;
    }

    public  <T extends View> T findViewById(int id){
        return this.mItemView.findViewById(id);
    }

}
