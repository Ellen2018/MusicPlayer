package com.ellen.musicplayer.bean;

import com.ellen.musicplayer.utils.collectionutil.ArrangeInterface;

import java.io.File;

public class Music implements ArrangeInterface<Music> {

    /**
     * 1->按照歌手进行分类
     * 2->按照专辑分类
     * 3->按照父目录分类
     */
    private static int biJiao = 1;

    public static int getBiJiao() {
        return biJiao;
    }

    public static void setBiJiao(int biJiao) {
        Music.biJiao = biJiao;
    }

    /**
     * 歌曲本地路径
     */
    private String path;
    /**
     * 歌曲名
     */
    private String name;
    /**
     * 歌曲专辑
     */
    private String album;
    /**
     * 歌手名
     */
    private String artist;
    /**
     * 歌曲文件大小
     */
    private long size;
    /**
     * 歌曲时长
     */
    private int duration;
    /**
     * 歌曲id
     */
    private int musicId;
    /**
     * 专辑id
     */
    private int albumId;
    /**
     * 父目录
     */
    private String fatherPath;
    /**
     * 类别
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        fatherPath = new File(path).getParentFile().getAbsolutePath();
    }

    public String getFatherPath() {
        return fatherPath;
    }

    public void setFatherPath(String fatherPath) {
        this.fatherPath = fatherPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    @Override
    public boolean identical(Music music) {
        if(biJiao == 1) {
            return this.getArtist().equals(music.getArtist());
        }else {
            return this.getAlbum().equals(music.getAlbum());
        }
    }

    /**
     * 歌曲的唯一标识
     * @return
     */
    public String getWeiOneTag(){
        return this.getMusicId()+"_"+this.getAlbumId();
    }
}
