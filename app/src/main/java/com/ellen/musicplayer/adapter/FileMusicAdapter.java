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
import com.ellen.musicplayer.bean.FileMusic;
import com.ellen.musicplayer.bean.LiuPai;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class FileMusicAdapter extends BaseSingleRecyclerViewAdapter<FileMusic, FileMusicAdapter.ZhuanJiViewHolder> {


    public FileMusicAdapter(Context context, List<FileMusic> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_file_music;
    }

    @Override
    protected ZhuanJiViewHolder getNewViewHolder(View view) {
        return new ZhuanJiViewHolder(view);
    }

    @Override
    protected void showData(ZhuanJiViewHolder singlerViewHolder, FileMusic data, int position) {
        singlerViewHolder.tvFileName.setText(data.getName());
        singlerViewHolder.tvSize.setText(String.valueOf(data.getMusicList().size()));
        Music music = data.getMusicList().get(0);
        singlerViewHolder.rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(getContext(),data.getName());
            }
        });
    }

    static class ZhuanJiViewHolder extends BaseViewHolder {

        TextView tvFileName,tvSize;
        RelativeLayout rlMore;

        public ZhuanJiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = findViewById(R.id.tv_file_name);
            tvSize = findViewById(R.id.tv_size);
            rlMore = findViewById(R.id.rl_more);
        }
    }

}
