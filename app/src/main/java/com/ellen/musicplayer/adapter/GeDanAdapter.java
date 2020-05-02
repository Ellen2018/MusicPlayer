package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.GeDanMusicTable;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.sqlitecreate.createsql.order.Order;

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
        List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(data);
        if (data.getGeDanSqlTableName() == 0) {
            GeDanMusicTable geDanMusicTable = SQLManager.getInstance().getLikeGeDanMusicTable();
            //我喜欢
            geDanMusicList =   geDanMusicTable.getAllDatas(Order.getInstance(false)
                    .setFirstOrderFieldName("likeTime")
                    .setIsDesc(true).createSQL());
        }else {
            geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(data);
        }
        geDanViewHolder.tvGeDanName.setText(data.getGeDanName());
        geDanViewHolder.tvGeDanCount.setText(String.valueOf(geDanMusicList.size()));
        if (geDanMusicList.size() > 0) {
            Music music = geDanMusicList.get(0).getMusic();
            Glide.with(getContext())
                    .load(MusicBitmap.getArtwork(getContext(), music.getMusicId(), music.getAlbumId()))
                    .error(R.mipmap.default_music_icon)
                    .into(geDanViewHolder.ivGeDanIcon);
        }else {
            geDanViewHolder.ivGeDanIcon.setImageResource(R.mipmap.default_music_icon);
        }
    }


    static class GeDanViewHolder extends BaseViewHolder {
        TextView tvGeDanName, tvGeDanCount;
        ImageView ivGeDanIcon;

        public GeDanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGeDanName = findViewById(R.id.tv_ge_dan_name);
            tvGeDanCount = findViewById(R.id.tv_ge_dan_music_count);
            ivGeDanIcon = findViewById(R.id.iv_ge_dan_icon);
        }
    }
}
