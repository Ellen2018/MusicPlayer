package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;

import java.util.List;

public class SelectorSingerAdapter extends BaseSingleRecyclerViewAdapter<String, SelectorSingerAdapter.SelectorSingerViewHolder> {

    public SelectorSingerAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_selctor_singer;
    }

    @Override
    protected SelectorSingerViewHolder getNewViewHolder(View view) {
        return new SelectorSingerViewHolder(view);
    }

    @Override
    protected void showData(SelectorSingerViewHolder selectorSingerViewHolder, String data, int position) {
           selectorSingerViewHolder.tvSingerName.setText(data);
    }

    static class SelectorSingerViewHolder extends BaseViewHolder{

        TextView tvSingerName;

        public SelectorSingerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSingerName = findViewById(R.id.tv_singer_name);
        }
    }

}
