package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.ui.dialog.MusicMessageDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;

import java.util.List;

public class NearTimesMusicAdapter extends BaseSingleRecyclerViewAdapter<NearMusic, NearTimesMusicAdapter.NearMusicViewHolder> {

    private View parentView;

    public NearTimesMusicAdapter(Context context, View parentView, List<NearMusic> dataList) {
        super(context, dataList);
        this.parentView = parentView;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_times_near_music;
    }

    @Override
    protected NearMusicViewHolder getNewViewHolder(View view) {
        return new NearMusicViewHolder(view);
    }

    @Override
    protected void showData(NearMusicViewHolder nearMusicViewHolder, NearMusic nearMusic, int position) {
        Music data = nearMusic.getMusic();
        nearMusicViewHolder.tvMusicName.setText(data.getName());
        nearMusicViewHolder.tvSingerName.setText(data.getArtist());
        nearMusicViewHolder.tvPosition.setText(String.valueOf(position + 1));
        nearMusicViewHolder.tvPlayTimes.setText(String.valueOf(nearMusic.getPlayTimes())+"次");
        Music music = MediaPlayerManager.getInstance().currentOpenMusic();
        if (music != null) {
            if (data.getPath().equals(MediaPlayerManager.getInstance().currentOpenMusic().getPath())) {
                nearMusicViewHolder.ivPlayStatus.setVisibility(View.VISIBLE);
                nearMusicViewHolder.tvPosition.setVisibility(View.GONE);
            } else {
                nearMusicViewHolder.ivPlayStatus.setVisibility(View.GONE);
                nearMusicViewHolder.tvPosition.setVisibility(View.VISIBLE);
            }
        } else {
            nearMusicViewHolder.ivPlayStatus.setVisibility(View.GONE);
            nearMusicViewHolder.tvPosition.setVisibility(View.VISIBLE);
        }

        nearMusicViewHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicMessageDialog musicMessageDialog = new MusicMessageDialog((Activity) getContext(),parentView,data);
                musicMessageDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
            }
        });
    }

    static class NearMusicViewHolder extends BaseViewHolder{

        TextView tvMusicName, tvSingerName, tvPosition,tvPlayTimes;
        ImageView ivPlayStatus,ivMore;

        public NearMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvPosition = findViewById(R.id.tv_position);
            ivPlayStatus = findViewById(R.id.iv_play_status);
            ivMore = findViewById(R.id.iv_more);
            tvPlayTimes = findViewById(R.id.tv_play_times);
        }
    }
}
