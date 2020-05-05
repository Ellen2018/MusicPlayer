package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.FileMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.ui.dialog.LeiBieDialog;

import java.util.List;

public class FileMusicAdapter extends BaseSingleRecyclerViewAdapter<FileMusic, FileMusicAdapter.ZhuanJiViewHolder> {


    private View parentView;
    private Activity activity;

    public FileMusicAdapter(Activity activity,View view, List<FileMusic> dataList) {
        super(activity, dataList);
        this.activity = activity;
        this.parentView = view;
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
                LeiBieDialog leiBieDialog = new LeiBieDialog(activity,parentView,"文件夹",data.getName(),data.getMusicList());
                leiBieDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
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
