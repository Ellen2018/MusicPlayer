package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;

import java.util.List;

public class MusicAdapter extends BaseSingleRecyclerViewAdapter<Music, MusicAdapter.MusicViewHolder> {


    public MusicAdapter(Context context, List<Music> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_music;
    }

    @Override
    protected MusicViewHolder getNewViewHolder(View view) {
        return new MusicViewHolder(view);
    }

    @Override
    protected void showData(final MusicViewHolder musicViewHolder, Music data, final int position) {
        musicViewHolder.tvMusicName.setText(data.getName());
        musicViewHolder.tvSingerName.setText(data.getArtist());
        Music music = MediaPlayerManager.getInstance().currentOpenMusic();
        if(music != null) {
            if (data.getPath().equals(MediaPlayerManager.getInstance().currentOpenMusic().getPath())) {
                musicViewHolder.playStatus.setVisibility(View.VISIBLE);
            } else {
                musicViewHolder.playStatus.setVisibility(View.GONE);
            }
        }else {
            musicViewHolder.playStatus.setVisibility(View.GONE);
        }
    }

    static class MusicViewHolder extends BaseViewHolder{

        TextView tvMusicName,tvSingerName;
        LinearLayout llItem;
        View playStatus;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            llItem = findViewById(R.id.ll_item);
            playStatus = findViewById(R.id.view_play_status);
        }
    }
}
