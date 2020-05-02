package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.dialog.LeiBieDialog;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class SingerAdapter extends BaseSingleRecyclerViewAdapter<Singer, SingerAdapter.SinglerViewHolder> {

    private Activity activity;
    private View parentView;

    public SingerAdapter(Activity activity,View parentView, List<Singer> dataList) {
        super(activity, dataList);
        this.parentView = parentView;
        this.activity = activity;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_singer;
    }

    @Override
    protected SinglerViewHolder getNewViewHolder(View view) {
        return new SinglerViewHolder(view);
    }

    @Override
    protected void showData(SinglerViewHolder singlerViewHolder, Singer data, int position) {
        singlerViewHolder.tvSingerName.setText(data.getName());
        singlerViewHolder.tvSize.setText(String.valueOf(data.getMusicList().size()));
        Music music = data.getMusicList().get(0);
        Glide.with(getContext())
                .load(MusicBitmap.getArtwork(getContext(),music.getMusicId(),music.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(singlerViewHolder.ivSingerIcon);

        singlerViewHolder.rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeiBieDialog leiBieDialog = new LeiBieDialog(activity,parentView,"歌手",data.getName(),data.getMusicList());
                leiBieDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
            }
        });
    }

    static class SinglerViewHolder extends BaseViewHolder {

        TextView tvSingerName,tvSize;
        ImageView ivSingerIcon;
        RelativeLayout rlMore;

        public SinglerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSingerName = findViewById(R.id.tv_singer_name);
            tvSize = findViewById(R.id.tv_size);
            ivSingerIcon = findViewById(R.id.iv_singer_icon);
            rlMore = findViewById(R.id.rl_more);
        }
    }

}
