package com.ellen.musicplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.ui.activity.MusicListActivity;
import com.ellen.musicplayer.ui.activity.SortActivity;

import java.io.Serializable;
import java.util.List;

public class JumpSortUtils {

    public static void jumpToSort(Context context,String sortTitle, String sortContent, List<Music> musicList){
        Intent intent = new Intent(context, SortActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SortActivity.SORT_MUSIC_LIST, (Serializable) musicList);
        intent.putExtras(bundle);
        intent.putExtra(SortActivity.SORT_TITLE, sortTitle);
        intent.putExtra(SortActivity.SORT_CONTENT,sortContent);
        context.startActivity(intent);
    }

    public static void jumpToMusicList(Context context, List<Music> musicList){
        Intent intent = new Intent(context, MusicListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MusicListActivity.MUSIC_ACTIVITY_MUSIC_LIST, (Serializable) musicList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
