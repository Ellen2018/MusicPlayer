package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.MusicMessageMenu;

import java.util.List;

public class MusicMessageMenuAdapter extends BaseSingleRecyclerViewAdapter<MusicMessageMenu, MusicMessageMenuAdapter.MusicMessageMenuViewHolder> {


    public MusicMessageMenuAdapter(Context context, List<MusicMessageMenu> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_music_message_menu;
    }

    @Override
    protected MusicMessageMenuViewHolder getNewViewHolder(View view) {
        return new MusicMessageMenuViewHolder(view);
    }

    @Override
    protected void showData(MusicMessageMenuViewHolder musicMessageMenuViewHolder, MusicMessageMenu data, int position) {
        musicMessageMenuViewHolder.ivMenuIcon.setImageResource(data.getMenuIconId());
        musicMessageMenuViewHolder.tvMenuContent.setText(data.getStartString()+data.getContent());
    }

    static class MusicMessageMenuViewHolder extends BaseViewHolder{

        private ImageView ivMenuIcon;
        private TextView tvMenuContent;

        public MusicMessageMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMenuIcon = findViewById(R.id.iv_menu_icon);
            tvMenuContent = findViewById(R.id.tv_menu_content);
        }
    }

}
