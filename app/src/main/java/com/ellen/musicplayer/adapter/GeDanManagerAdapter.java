package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.dialog.GeDanManagerDialog;
import com.ellen.musicplayer.manager.sql.GeDanMusicTable;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.ui.activity.GeDanManagerActivity;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.sqlitecreate.createsql.order.Order;

import java.util.List;

public class GeDanManagerAdapter extends BaseSingleRecyclerViewAdapter<GeDan, GeDanManagerAdapter.GeDanViewHolder> {

    private View parentView;

    public GeDanManagerAdapter(Activity activity, View parentView, List<GeDan> dataList) {
        super(activity, dataList);
        this.parentView = parentView;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_ge_dan_manager;
    }

    @Override
    protected GeDanViewHolder getNewViewHolder(View view) {
        return new GeDanViewHolder(view);
    }

    @Override
    protected void showData(GeDanViewHolder geDanViewHolder, GeDan data, int position) {
        List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(data);
        geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(data);
        geDanViewHolder.tvGeDanName.setText(data.getGeDanName());
        geDanViewHolder.tvGeDanCount.setText(String.valueOf(geDanMusicList.size()));
        geDanViewHolder.ivGeDanMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeDanManagerDialog geDanManagerDialog = new GeDanManagerDialog((Activity) getContext(),data);
                geDanManagerDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
            }
        });
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
        ImageView ivGeDanIcon,ivGeDanMore;

        public GeDanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGeDanName = findViewById(R.id.tv_ge_dan_name);
            tvGeDanCount = findViewById(R.id.tv_ge_dan_music_count);
            ivGeDanIcon = findViewById(R.id.iv_ge_dan_icon);
            ivGeDanMore = findViewById(R.id.iv_ge_dan_more);
        }
    }
}
