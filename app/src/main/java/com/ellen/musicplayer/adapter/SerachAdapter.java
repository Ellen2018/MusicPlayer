package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.base.adapter.recyclerview.BaseMultipleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.SerachBean;

import java.util.List;

public class SerachAdapter extends BaseMultipleRecyclerViewAdapter {

    private List<SerachBean> serachBeans;

    public SerachAdapter(Context context, List<SerachBean> serachBeans) {
        super(context);
        this.serachBeans = serachBeans;
    }

    @Override
    protected int getMultipleItemViewType(int position) {
        return serachBeans.get(position).getType();
    }

    @Override
    protected int getItemSize() {
        return serachBeans.size();
    }

    @Override
    protected BaseViewHolder getNewBaseViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if(itemType == 0){
            //音乐
        }else if(itemType == 1){
            //歌手
        }else if(itemType == 2) {
            //专辑
        }else {
            //歌单
        }
        return null;
    }

    @Override
    protected void showData(BaseViewHolder baseViewHolder, int position) {

    }
}
