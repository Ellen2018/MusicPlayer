package com.ellen.musicplayer.utils;

import android.content.Context;
import android.util.Log;

import com.ellen.musicplayer.bean.FileMusic;
import com.ellen.musicplayer.bean.LiuPai;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.collectionutil.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                String pyName = PinYinUtil.getPingYin(name);
                //方便排序
                if (!PinYinUtil.isFirstZiMuShuZi(pyName)) {
                    music.setPyName("~" + pyName);
                } else {
                    music.setPyName(pyName);
                }
                String pySingerName = PinYinUtil.getPingYin(artist);
                //方便排序
                if (!PinYinUtil.isFirstZiMuShuZi(pySingerName)) {
                    music.setPySingerName("~" + pySingerName);
                } else {
                    music.setPySingerName(pySingerName);
                }
                String pyAlbumName = PinYinUtil.getPingYin(album);
                //方便排序
                if (!PinYinUtil.isFirstZiMuShuZi(pyAlbumName)) {
                    music.setPyAlbumName("~" + pyAlbumName);
                } else {
                    music.setPyAlbumName(pyAlbumName);
                }
                musicList.add(music);
            }
        });

        return musicList = CollectionUtils.sort(musicList);
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
            Set<String> singerSet = new HashSet<>();
            for(List<Music> list : listList){
               String singerName = list.get(0).getArtist();
               if(singerName.contains("/")){
                   String[] strings = singerName.split("/");
                   for(String s:strings){
                       singerSet.add(s);
                   }
               }else {
                   singerSet.add(singerName);
               }
            }
            for(String s:singerSet){
                Singer singer = new Singer();
                singer.setName(s);
                String pySingerName = PinYinUtil.getPingYin(s);
                //方便排序
                if (!PinYinUtil.isFirstZiMuShuZi(pySingerName)) {
                    singer.setPyName("~" + pySingerName);
                } else {
                    singer.setPyName(pySingerName);
                }
                singer.setMusicList(new ArrayList<>());
                for(Music music:musicList){
                    if(music.getArtist().contains(s)){
                        singer.getMusicList().add(music);
                    }
                }
                singerList.add(singer);
            }

            return singerList = CollectionUtils.sort(singerList);
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
                zhuanJi.setPyAlbumName(list.get(0).getPyAlbumName());
                zhuanJi.setMusicList(list);
                zhuanJiList.add(zhuanJi);
            }
            return zhuanJiList = CollectionUtils.sort(zhuanJiList);
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
