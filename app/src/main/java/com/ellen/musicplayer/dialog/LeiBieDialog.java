package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class LeiBieDialog extends BaseBottomPopWindow {

    private String leiBieName;
    private String name;
    private List<Music> musicList;

    private TextView tvLeiBieName,tvName;
    private LinearLayout llNextPlay,llAddToGeDan;

    public LeiBieDialog(Activity activity, String leiBieName, String name, List<Music> musicList) {
        super(activity);
        this.leiBieName = leiBieName;
        this.name = name;
        this.musicList = musicList;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_lei_bie,null);
        tvLeiBieName = view.findViewById(R.id.tv_lei_bie_name);
        tvName = view.findViewById(R.id.tv_name);

        llNextPlay = view.findViewById(R.id.ll_next_play);
        llAddToGeDan = view.findViewById(R.id.ll_add_ge_dan);

        llNextPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerManager.getInstance().addNextPlayMusics(musicList);
                dismiss();
            }
        });

        llAddToGeDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast(getContext(),"添加到歌单:"+name);
                dismiss();
            }
        });

        tvLeiBieName.setText(leiBieName);
        tvName.setText(name);
        return view;
    }
}
