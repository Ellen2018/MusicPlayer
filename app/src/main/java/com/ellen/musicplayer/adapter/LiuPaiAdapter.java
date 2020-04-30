package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.LiuPai;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class LiuPaiAdapter extends BaseSingleRecyclerViewAdapter<LiuPai, LiuPaiAdapter.ZhuanJiViewHolder> {


    public LiuPaiAdapter(Context context, List<LiuPai> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_liu_pai;
    }

    @Override
    protected ZhuanJiViewHolder getNewViewHolder(View view) {
        return new ZhuanJiViewHolder(view);
    }

    @Override
    protected void showData(ZhuanJiViewHolder singlerViewHolder, LiuPai data, int position) {
        singlerViewHolder.tvLiuPaiName.setText(data.getName());
        singlerViewHolder.tvSize.setText(String.valueOf(data.getMusicList().size()));
        Music music = data.getMusicList().get(0);
        Glide.with(getContext())
                .load(MusicBitmap.getArtwork(getContext(),music.getMusicId(),music.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(singlerViewHolder.ivLiuPaiIcon);

        singlerViewHolder.rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(getContext(),data.getName());
            }
        });
    }

    static class ZhuanJiViewHolder extends BaseViewHolder {

        TextView tvLiuPaiName,tvSize;
        ImageView ivLiuPaiIcon;
        RelativeLayout rlMore;

        public ZhuanJiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLiuPaiName = findViewById(R.id.tv_liu_pai_name);
            tvSize = findViewById(R.id.tv_size);
            ivLiuPaiIcon = findViewById(R.id.iv_liu_pai_icon);
            rlMore = findViewById(R.id.rl_more);
        }
    }

}
