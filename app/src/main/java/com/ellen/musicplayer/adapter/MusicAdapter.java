package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
        musicViewHolder.tvPosition.setText(String.valueOf(position + 1));
        Music music = MediaPlayerManager.getInstance().currentOpenMusic();
        if (music != null) {
            if (data.getPath().equals(MediaPlayerManager.getInstance().currentOpenMusic().getPath())) {
                musicViewHolder.ivPlayStatus.setVisibility(View.VISIBLE);
                musicViewHolder.tvPosition.setVisibility(View.GONE);
            } else {
                musicViewHolder.ivPlayStatus.setVisibility(View.GONE);
                musicViewHolder.tvPosition.setVisibility(View.VISIBLE);
            }
        } else {
            musicViewHolder.ivPlayStatus.setVisibility(View.GONE);
            musicViewHolder.tvPosition.setVisibility(View.VISIBLE);
        }
    }

    static class MusicViewHolder extends BaseViewHolder {

        TextView tvMusicName, tvSingerName, tvPosition;
        ImageView ivPlayStatus;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvPosition = findViewById(R.id.tv_position);
            ivPlayStatus = findViewById(R.id.iv_play_status);
        }
    }
}
