package com.ellen.musicplayer.utils;

import android.content.Context;

import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.collectionutil.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalSDMusicUtils {

    private static List<Music> musicList;
    private static List<Singer> singerList;
    private static List<ZhuanJi> zhuanJiList;

    /**
     * 获取本地所有歌曲
     *
     * @param context
     * @return
     */
    public static List<Music> getLocalAllMusic(Context context) {
        if (musicList == null) {
            musicList = new ArrayList<>();
        } else {
            return musicList;
        }

        ContentProviderUtils.getMusicPathList(context, new ContentProviderUtils.IntoMusic() {
            @Override
            public void getMusic(String path, String name, String album, String artist,String type, long size, int duration, int musicId, int albumId) {
                Music music = new Music();
                music.setPath(path);
                music.setName(name);
                music.setAlbum(album);
                music.setArtist(artist);
                music.setSize(size);
                music.setType(type);
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
     *
     * @param context
     * @return
     */
    public static List<Singer> getArtist(Context context) {
        if(singerList != null && singerList.size() > 0){return singerList;}
        Music.setBiJiao(1);
        List<Music> musicList = getLocalAllMusic(context);
        if (musicList != null && musicList.size() > 0) {
            List<List<Music>> listList = CollectionUtils.arrange(musicList);
            singerList = new ArrayList<>();
            for (List<Music> list : listList) {
                Singer singer = new Singer();
                singer.setName(list.get(0).getArtist());
                singer.setMusicList(list);
                singerList.add(singer);
            }
            return singerList;
        } else {
            return null;
        }
    }

    /**
     * 获取所有专辑集合
     *
     * @param context
     * @return
     */
    public static List<ZhuanJi> getAlbum(Context context) {
        if(zhuanJiList != null && zhuanJiList.size() > 0){return zhuanJiList;}
        Music.setBiJiao(2);
        List<Music> musicList = getLocalAllMusic(context);
        if (musicList != null && musicList.size() > 0) {
            List<List<Music>> listList = CollectionUtils.arrange(musicList);
            zhuanJiList = new ArrayList<>();
            for (List<Music> list : listList) {
                ZhuanJi zhuanJi = new ZhuanJi();
                zhuanJi.setName(list.get(0).getAlbum());
                zhuanJi.setMusicList(list);
                zhuanJiList.add(zhuanJi);
            }
            return zhuanJiList;
        } else {

            return null;
        }
    }

    public static List<Music> serachMusics(Context context, String serachString) {
        List<Music> musicList = getLocalAllMusic(context);
        List<Music> serachMusics = new ArrayList<>();
        for (Music music : musicList) {
            if (music.getName().contains(serachString)
                    || music.getArtist().contains(serachString)
                    || music.getAlbum().contains(serachString)) {
                serachMusics.add(music);
            }
        }
        return serachMusics;
    }
}
