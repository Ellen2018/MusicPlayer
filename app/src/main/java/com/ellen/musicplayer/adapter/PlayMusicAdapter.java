package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.dialog.MusicMessageDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;

import java.util.List;

public class PlayMusicAdapter extends BaseSingleRecyclerViewAdapter<Music, PlayMusicAdapter.MusicViewHolder> {


    private View parentView;

    public PlayMusicAdapter(Activity activity, View parentView, List<Music> dataList) {
        super(activity, dataList);
        this.parentView = parentView;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_play_list;
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
            if (position == MediaPlayerManager.getInstance().getCurrentPlayPosition()) {
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

        musicViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaPlayerManager.getInstance().deletePlayList(position);
                notifyDataSetChanged();

            }
        });
    }

    static class MusicViewHolder extends BaseViewHolder {

        TextView tvMusicName, tvSingerName, tvPosition;
        ImageView ivPlayStatus,ivDelete;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvPosition = findViewById(R.id.tv_position);
            ivPlayStatus = findViewById(R.id.iv_play_status);
            ivDelete = findViewById(R.id.iv_delete);
        }
    }
}
