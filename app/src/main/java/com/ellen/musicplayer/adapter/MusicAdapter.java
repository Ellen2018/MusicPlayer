package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.ui.dialog.MusicMessageDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.ZhuoSeUtils;

import java.util.List;

public class MusicAdapter extends BaseSingleRecyclerViewAdapter<Music, MusicAdapter.MusicViewHolder> {


    private View parentView;
    private DeleteCallback deleteCallback;
    private String serachTag;

    public void setSerachTag(String serachTag) {
        this.serachTag = serachTag;
    }

    public DeleteCallback getDeleteCallback() {
        return deleteCallback;
    }

    public void setDeleteCallback(DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }

    public MusicAdapter(Activity activity, View parentView, List<Music> dataList) {
        super(activity, dataList);
        this.parentView = parentView;
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
        if(TextUtils.isEmpty(serachTag)) {
            musicViewHolder.tvMusicName.setText(data.getName());
            musicViewHolder.tvSingerName.setText(data.getArtist());
        }else {
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

        musicViewHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicMessageDialog musicMessageDialog = new MusicMessageDialog((Activity) getContext(),parentView,data);
                if(deleteCallback != null) {
                    musicMessageDialog.setDeleteInterface(new MusicMessageDialog.DeleteInterface() {
                        @Override
                        public void delete() {
                            deleteCallback.delelte(data);
                        }
                    });
                }
                musicMessageDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
            }
        });
    }

    static class MusicViewHolder extends BaseViewHolder {

        TextView tvMusicName, tvSingerName, tvPosition;
        ImageView ivPlayStatus,ivMore;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvPosition = findViewById(R.id.tv_position);
            ivPlayStatus = findViewById(R.id.iv_play_status);
            ivMore = findViewById(R.id.iv_more);
        }
    }

    public interface DeleteCallback{
        void delelte(Music music);
    }
}
