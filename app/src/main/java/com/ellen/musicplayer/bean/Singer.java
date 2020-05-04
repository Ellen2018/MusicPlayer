package com.ellen.musicplayer.bean;

import com.ellen.musicplayer.utils.collectionutil.CompareableInterface;

import java.util.List;

public class Singer implements CompareableInterface<Singer> {

    private String name;
    private String pyName;
    private List<Music> musicList;

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    @Override
    public int compareTo(Singer singer) {
        String str2 = pyName;
        String str1 = singer.getPyName();

        int length = str1.length() >= str2.length() ? str2.length() : str1.length();

        int result = 0;

        for (int i = 0; i < length; i++) {
            char c1 = str2.charAt(i);
            char c2 = str1.charAt(i);
            //转成小写
            if (c1 >= 65 && c1 <= 90) {
                c1 = (char) (c1 + 32);
            }
            if (c2 >= 65 && c2 <= 90) {
                c2 = (char) (c2 + 32);
            }
            result = c1 - c2;
            if (result != 0) {
                break;
            }
        }
        if (result == 0) {
            result = str2.length() - str1.length();
        }

        return result;
    }
}
