package com.ellen.musicplayer.utils;

import android.content.Context;

import com.ellen.musicplayer.bean.FileMusic;
import com.ellen.musicplayer.bean.LiuPai;
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
    private static List<LiuPai> liuPaiList;
    private static List<FileMusic> fileMusicList;

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
            public void getMusic(String path, String name, String album, String artist, String type, long size, int duration, int musicId, int albumId) {
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
        if (singerList != null && singerList.size() > 0) {
            return singerList;
        }
        Music.setBiJiao(1);
        List<Music> musicList = getLocalAllMusic(context);
        if (musicList != null && musicList.size() > 0) {
            List<List<Music>> listList = CollectionUtils.arrange(musicList);
            singerList = new ArrayList<>();
            for (List<Music> list : listList) {
                String singerName = list.get(0).getArtist();
                if (singerName.contains("/")) {
                    //说明有多个歌手，需要切割
                    String[] strings = singerName.split("/");
                    //然后再遍历之前的歌手集合
                    for (String name : strings) {
                        boolean isNew = true;
                        Singer currentSinger = null;
                        for (Singer singer : singerList) {
                            if (name.equals(singer.getName())) {
                                currentSinger = singer;
                                singer.getMusicList().addAll(list);
                                isNew = false;
                                break;
                            }
                        }
                        if (isNew) {
                            Singer singer = new Singer();
                            singer.setName(name);
                            singer.setMusicList(list);
                            singerList.add(singer);
                            currentSinger = singer;
                        }

                        //修复bug
                        for(int i=0;i<currentSinger.getMusicList().size();i++){
                            if (!currentSinger.getMusicList().get(i).getArtist().contains(name)) {
                                currentSinger.getMusicList().remove(i);
                                i--;
                            }
                        }
                    }
                } else if (singerName.contains(" / ")) {
                    //说明有多个歌手，需要切割
                    String[] strings = singerName.split(" / ");
                    //然后再遍历之前的歌手集合
                    for (String name : strings) {
                        boolean isNew = true;
                        Singer currentSinger = null;
                        for (Singer singer : singerList) {
                            if (name.equals(singer.getName())) {
                                singer.getMusicList().addAll(list);
                                isNew = false;
                                break;
                            }
                        }
                        if (isNew) {
                            Singer singer = new Singer();
                            singer.setName(name);
                            singer.setMusicList(list);
                            singerList.add(singer);
                        }

                        //修复bug
                        for(int i=0;i<currentSinger.getMusicList().size();i++){
                            if (!currentSinger.getMusicList().get(i).getArtist().contains(name)) {
                                currentSinger.getMusicList().remove(i);
                                i--;
                            }
                        }
                    }
                } else {
                    Singer singer = new Singer();
                    singer.setName(singerName);
                    singer.setMusicList(list);
                    singerList.add(singer);
                }
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
        if (zhuanJiList != null && zhuanJiList.size() > 0) {
            return zhuanJiList;
        }
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

    /**
     * 获取流派集合
     *
     * @return
     */
    public static List<LiuPai> getLiuPais(Context context) {
        if (liuPaiList != null && liuPaiList.size() > 0) {
            return liuPaiList;
        }
        Music.setBiJiao(3);
        List<Music> musicList = getLocalAllMusic(context);
        if (musicList != null && musicList.size() > 0) {
            liuPaiList = new ArrayList<>();
            List<List<Music>> listList = CollectionUtils.arrange(musicList);
            for (List<Music> list : listList) {
                LiuPai liuPai = new LiuPai();
                liuPai.setName(list.get(0).getType());
                liuPai.setMusicList(list);
                liuPaiList.add(liuPai);
            }
            return liuPaiList;
        } else {
            return null;
        }
    }

    /**
     * 获取歌曲文件夹集合
     *
     * @param context
     * @return
     */
    public static List<FileMusic> getFileMusics(Context context) {
        if (fileMusicList != null && fileMusicList.size() > 0) {
            return fileMusicList;
        }
        Music.setBiJiao(4);
        List<Music> musicList = getLocalAllMusic(context);
        if (musicList != null && musicList.size() > 0) {
            fileMusicList = new ArrayList<>();
            List<List<Music>> listList = CollectionUtils.arrange(musicList);
            for (List<Music> list : listList) {
                FileMusic fileMusic = new FileMusic();
                fileMusic.setName(list.get(0).getFatherPath());
                fileMusic.setMusicList(list);
                fileMusicList.add(fileMusic);
            }
            return fileMusicList;
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

    /**
     * 查询歌手
     *
     * @param context
     * @param singerName
     * @return
     */
    public static Singer getSinger(Context context, String singerName) {
        List<Singer> singerList = getArtist(context);
        Singer result = null;
        for (Singer singer : singerList) {
            if (singer.getName().equals(singerName)) {
                result = singer;
                break;
            }
        }
        return result;
    }

    /**
     * 查询专辑
     *
     * @param context
     * @param zhuanJiName
     * @return
     */
    public static ZhuanJi getZhuanJi(Context context, String zhuanJiName) {
        List<ZhuanJi> zhuanJiList = getAlbum(context);
        ZhuanJi result = null;
        for (ZhuanJi zhuanJi : zhuanJiList) {
            if (zhuanJi.getName().equals(zhuanJiName)) {
                result = zhuanJi;
                break;
            }
        }
        return result;
    }
}
