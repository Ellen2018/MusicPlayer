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
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.dialog.LeiBieDialog;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class ZhuanJiAdapter extends BaseSingleRecyclerViewAdapter<ZhuanJi, ZhuanJiAdapter.ZhuanJiViewHolder> {


    private Activity activity;
    private View view;

    public ZhuanJiAdapter(Activity activity,View view, List<ZhuanJi> dataList) {
        super(activity, dataList);
        this.activity = activity;
        this.view = view;
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
                LeiBieDialog leiBieDialog = new LeiBieDialog(activity,"专辑",data.getName(),data.getMusicList());
                leiBieDialog.showAtLocation(view, Gravity.BOTTOM,0,0);
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
