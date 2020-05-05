package com.ellen.musicplayer.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.TimeUtils;

import java.io.File;
import java.text.DecimalFormat;

public class MusicLocalMessageDilaog extends BaseBottomPopWindow {

    private Music music;
    private ImageView ivMusicIcon;
    private TextView tvMusicName,tvsingerName,tvFileName,tvplayTime,tvFileSize,tvFilePath;

    public MusicLocalMessageDilaog(Activity activity, Music music) {
        super(activity);
        this.music = music;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_music_local_message,null);
        ivMusicIcon = view.findViewById(R.id.iv_music_icon);
        Glide.with(getContext())
                .load(MusicBitmap.getArtwork(getContext(),music.getMusicId(),music.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(ivMusicIcon);

        tvMusicName = view.findViewById(R.id.tv_music_name);
        tvsingerName = view.findViewById(R.id.tv_singer_name);
        tvFileName = view.findViewById(R.id.tv_file_name);
        tvplayTime = view.findViewById(R.id.tv_player_time);
        tvFileSize = view.findViewById(R.id.tv_file_size);
        tvFilePath = view.findViewById(R.id.tv_file_path);

        tvMusicName.setText(music.getName());
        tvsingerName.setText(music.getArtist());
        long MB = 1024 * 1024;//定义MB的计算常量
        tvFileName.setText(new File(music.getPath()).getName());
        tvplayTime.setText(TimeUtils.format(music.getDuration()));
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        tvFileSize.setText(df.format(music.getSize() / (float) MB) + " MB   ");
        tvFilePath.setText(new File(music.getPath()).getParentFile().getAbsolutePath());
        return view;
    }
}
