package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;

import java.util.List;

public class SxAdapter extends BaseSingleRecyclerViewAdapter<String, SxAdapter.SxViewHolder> {

    public SxAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_din_shi;
    }

    @Override
    protected SxViewHolder getNewViewHolder(View view) {
        return new SxViewHolder(view);
    }

    @Override
    protected void showData(SxViewHolder sxViewHolder, String data, int position) {
        sxViewHolder.tvZiMu.setText(data);
    }

    static class SxViewHolder extends BaseViewHolder{
        TextView tvZiMu;
        public SxViewHolder(@NonNull View itemView) {
            super(itemView);
            tvZiMu = findViewById(R.id.tv_time);
        }
    }
}
