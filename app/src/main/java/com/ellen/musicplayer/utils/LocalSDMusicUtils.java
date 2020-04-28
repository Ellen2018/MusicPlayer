package com.ellen.musicplayer.utils;

import android.content.Context;

import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.collectionutil.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalSDMusicUtils {

    private static List<Music> musicList;

    /**
     * 获取本地所有歌曲
     * @param context
     * @return
     */
    public static List<Music> getLocalAllMusic(Context context){
        if(musicList == null) {
            musicList = new ArrayList<>();
        }else {
            return musicList;
        }

        ContentProviderUtils.getMusicPathList(context, new ContentProviderUtils.IntoMusic() {
            @Override
            public void getMusic(String path, String name, String album, String artist, long size, int duration, int musicId, int albumId) {
                Music music = new Music();
                music.setPath(path);
                music.setName(name);
                music.setAlbum(album);
                music.setArtist(artist);
                music.setSize(size);
                music.setDuration(duration);
                music.setMusicId(musicId);
                music.setAlbumId(albumId);
                musicList.add(music);
            }
        });

        return musicList;
    }

    /**
     * 获取所有歌手集合
     * @param context
     * @return
     */
    public static List<List<Music>> getArtist(Context context){
        Music.setBiJiao(1);
        List<Music> musicList = getLocalAllMusic(context);
        return CollectionUtils.arrange(musicList);
    }

    /**
     * 获取所有专辑集合
     * @param context
     * @return
     */
    public static List<List<Music>> getAlbum(Context context){
        Music.setBiJiao(2);
        List<Music> musicList = getLocalAllMusic(context);
        return CollectionUtils.arrange(musicList);
    }

}
