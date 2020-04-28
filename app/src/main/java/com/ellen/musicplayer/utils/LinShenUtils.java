package com.ellen.musicplayer.utils;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import com.ellen.musicplayer.bean.Music;

import java.io.File;

/**
 * 铃声工具类
 */
public class LinShenUtils {

    // 将铃声的路径插入contentResolver，以数据库的形式插入
    /**
     * 设置默认振铃
     */
    public static void setRingtoneImpl(Context context,Music music) {
        ContentValues content = new ContentValues();
        content.put(MediaStore.MediaColumns.DATA, music.getPath());
        content.put(MediaStore.MediaColumns.TITLE, music.getName());
        //content.put(MediaStore.MediaColumns.SIZE, ringtoneFile);
        content.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        //  content.put(MediaStore.Audio.Media.ARTIST, "Madonna");
        //content.put(MediaStore.Audio.Media.DURATION, 230);
        content.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        content.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        content.put(MediaStore.Audio.Media.IS_ALARM, true);
        content.put(MediaStore.Audio.Media.IS_MUSIC, false);
        // 获取文件是external还是internal的uri路径
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(music.getPath());
        // 铃声通过contentvaues插入到数据库
        final Uri newUri = context.getContentResolver().insert(uri, content);
        RingtoneManager.setActualDefaultRingtoneUri(context.getApplicationContext(),
                RingtoneManager.TYPE_RINGTONE, newUri);
    }

}
