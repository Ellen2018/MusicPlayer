package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;

import java.util.List;

public class GeDanAdapter extends BaseSingleRecyclerViewAdapter<GeDan, GeDanAdapter.GeDanViewHolder> {

    public GeDanAdapter(Context context, List<GeDan> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_ge_dan;
    }

    @Override
    protected GeDanViewHolder getNewViewHolder(View view) {
        return new GeDanViewHolder(view);
    }

    @Override
    protected void showData(GeDanViewHolder geDanViewHolder, GeDan data, int position) {

    }


    static class GeDanViewHolder extends BaseViewHolder{
        public GeDanViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
