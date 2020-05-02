package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.manager.sql.SQLManager;

import org.w3c.dom.Text;

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
         geDanViewHolder.tvGeDanName.setText(data.getGeDanName());
         geDanViewHolder.tvGeDanCount.setText(String.valueOf(SQLManager.getInstance().getGeDanMusicListByName(data).size()));
    }


    static class GeDanViewHolder extends BaseViewHolder{
        TextView tvGeDanName,tvGeDanCount;
        public GeDanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGeDanName = findViewById(R.id.tv_ge_dan_name);
            tvGeDanCount = findViewById(R.id.tv_ge_dan_music_count);
        }
    }
}
