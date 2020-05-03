package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.supermessagelibrary.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class GeDanManagerDialog extends BaseBottomPopWindow {

    private GeDan geDan;
    private TextView tvGeDanName;
    private LinearLayout llNextPlay, llBianJi, llDelete;

    public GeDanManagerDialog(Activity activity, GeDan geDan) {
        super(activity);
        this.geDan = geDan;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ge_dan_mamager, null);
        tvGeDanName = view.findViewById(R.id.tv_name);
        tvGeDanName.setText(geDan.getGeDanName());
        llNextPlay = view.findViewById(R.id.ll_ge_dan_next_play);
        llBianJi = view.findViewById(R.id.ll_ge_dan_bj);
        llDelete = view.findViewById(R.id.ll_ge_dan_delete);

        llNextPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDan);
                List<Music> musicList = new ArrayList<>();
                for(GeDanMusic geDanMusic:geDanMusicList){
                    musicList.add(geDanMusic.getMusic());
                }
                MediaPlayerManager.getInstance().addNextPlayMusics(musicList);
                dismiss();
            }
        });

        llBianJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGeDanDialog createGeDanDialog = new CreateGeDanDialog(true,geDan);
                FragmentActivity activity = (FragmentActivity) getActivity();
                createGeDanDialog.show(activity.getSupportFragmentManager(),"");
                dismiss();
            }
        });


        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLManager.getInstance().deleteGeDan(geDan);
                MessageManager.getInstance().sendMainThreadMessage(MessageTag.GE_DAN_ID);
                dismiss();
            }
        });

        return view;
    }
}
