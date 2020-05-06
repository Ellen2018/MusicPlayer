package com.ellen.musicplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.ui.activity.GeDanActivity;
import com.ellen.musicplayer.ui.activity.ManyChooseActivity;
import com.ellen.musicplayer.ui.activity.SortActivity;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class JumpSortUtils {

    /**
     * 跳转到分类界面(不支持删除)
     * @param context
     * @param sortTitle
     * @param sortContent
     * @param musicList
     */
    public static void jumpToSort(Context context,String sortTitle, String sortContent, List<Music> musicList){
        Intent intent = new Intent(context, SortActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SortActivity.SORT_MUSIC_LIST, (Serializable) musicList);
        intent.putExtras(bundle);
        intent.putExtra(SortActivity.SORT_TITLE, sortTitle);
        intent.putExtra(SortActivity.SORT_CONTENT,sortContent);
        context.startActivity(intent);
    }

    public static void jumpToGeDanMusicList(Context context, GeDan geDan){
        Intent intent = new Intent(context, GeDanActivity.class);
        intent.putExtra(GeDanActivity.GE_DAN_JSON,new Gson().toJson(geDan));
        context.startActivity(intent);
    }

    public static void jumpToMusicList(Context context, List<Music> musicList){
        Intent intent = new Intent(context, ManyChooseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ManyChooseActivity.MUSIC_ACTIVITY_MUSIC_LIST, (Serializable) musicList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
