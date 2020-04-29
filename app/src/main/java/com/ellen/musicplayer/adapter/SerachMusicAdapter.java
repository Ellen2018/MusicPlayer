package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.ZhuoSeUtils;

import java.util.List;

public class SerachMusicAdapter extends BaseSingleRecyclerViewAdapter<Music, SerachMusicAdapter.MusicViewHolder> {

    private String serachTag = "";

    public String getSerachTag() {
        return serachTag;
    }

    public void setSerachTag(String serachTag) {
        this.serachTag = serachTag;
    }

    public SerachMusicAdapter(Context context, List<Music> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_music_serach;
    }

    @Override
    protected MusicViewHolder getNewViewHolder(View view) {
        return new MusicViewHolder(view);
    }

    @Override
    protected void showData(final MusicViewHolder musicViewHolder, Music data, final int position) {
        if(serachTag.length() > 0) {
            if(data.getName().contains(serachTag)) {
                musicViewHolder.tvMusicName.setText(ZhuoSeUtils.getSpannable(data.getName(), serachTag));
            }else {
                musicViewHolder.tvMusicName.setText(data.getName());
            }
            if(data.getArtist().contains(serachTag)) {
                musicViewHolder.tvSingerName.setText(ZhuoSeUtils.getSpannable(data.getArtist(), serachTag));
            }else {
                musicViewHolder.tvSingerName.setText(data.getArtist());
            }
            if(data.getAlbum().contains(serachTag)) {
                musicViewHolder.tvAlbumName.setText(ZhuoSeUtils.getSpannable(data.getAlbum(), serachTag));
            }else {
                musicViewHolder.tvAlbumName.setText(data.getAlbum());
            }
        }else {
            musicViewHolder.tvMusicName.setText(data.getName());
            musicViewHolder.tvSingerName.setText(data.getArtist());
            musicViewHolder.tvAlbumName.setText(data.getAlbum());
        }

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

        TextView tvMusicName, tvSingerName, tvAlbumName,tvPosition;
        ImageView ivPlayStatus;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvPosition = findViewById(R.id.tv_position);
            tvAlbumName = findViewById(R.id.tv_album_name);
            ivPlayStatus = findViewById(R.id.iv_play_status);
        }
    }
}
