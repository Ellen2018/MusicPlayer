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
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class ZhuanJiAdapter extends BaseSingleRecyclerViewAdapter<ZhuanJi, ZhuanJiAdapter.ZhuanJiViewHolder> {


    public ZhuanJiAdapter(Context context, List<ZhuanJi> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_zhuan_ji;
    }

    @Override
    protected ZhuanJiViewHolder getNewViewHolder(View view) {
        return new ZhuanJiViewHolder(view);
    }

    @Override
    protected void showData(ZhuanJiViewHolder singlerViewHolder, ZhuanJi data, int position) {
        singlerViewHolder.tvZhuanJiName.setText(data.getName());
        singlerViewHolder.tvSize.setText(String.valueOf(data.getMusicList().size()));
        Music music = data.getMusicList().get(0);
        Glide.with(getContext())
                .load(MusicBitmap.getArtwork(getContext(),music.getMusicId(),music.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(singlerViewHolder.ivZhuanJiIcon);

        singlerViewHolder.rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(getContext(),data.getName());
            }
        });
    }

    static class ZhuanJiViewHolder extends BaseViewHolder {

        TextView tvZhuanJiName,tvSize;
        ImageView ivZhuanJiIcon;
        RelativeLayout rlMore;

        public ZhuanJiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvZhuanJiName = findViewById(R.id.tv_zhuan_ji_name);
            tvSize = findViewById(R.id.tv_size);
            ivZhuanJiIcon = findViewById(R.id.iv_zhuan_ji_icon);
            rlMore = findViewById(R.id.rl_more);
        }
    }

}
