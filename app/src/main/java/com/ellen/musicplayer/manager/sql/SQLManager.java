package com.ellen.musicplayer.manager.sql;

import android.content.Context;
import android.database.Cursor;

import com.ellen.musicplayer.SQLTag;
import com.ellen.musicplayer.bean.LikeMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.sqlitecreate.createsql.helper.WhereSymbolEnum;
import com.ellen.sqlitecreate.createsql.serach.SerachTableData;
import com.ellen.sqlitecreate.createsql.where.Where;

import java.lang.ref.WeakReference;

public class SQLManager {

    private WeakReference<Context> contextWeakReference;
    private Library library;
    private static volatile SQLManager sqlManager;
    private LikeMusicTable likeMusicTable;
    private NearMusicTable nearMusicTable;
    private PiFuTable piFuTable;

    private SQLManager(){
    }

    public static SQLManager getInstance(){
         if(sqlManager == null){
             synchronized (SQLManager.class){
                 if(sqlManager == null){
                     sqlManager = new SQLManager();
                 }
             }
         }
        return sqlManager;
    }

    public void initLibrary(Context context){
        this.contextWeakReference = new WeakReference<>(context);
        library = new Library(contextWeakReference.get(), SQLTag.LIBRARY_NAME,1);
    }

    public LikeMusicTable getLikeMusicTable() {
        if(likeMusicTable == null){
            likeMusicTable = new LikeMusicTable(library.getWriteDataBase(), LikeMusic.class,SQLTag.LIKE_TABLE_NAME);
            likeMusicTable.onCreateTableIfNotExits();
        }
        return likeMusicTable;
    }

    public NearMusicTable getNearMusicTable() {
        if(nearMusicTable == null){
            nearMusicTable = new NearMusicTable(library.getWriteDataBase(), NearMusic.class,SQLTag.NEAR_TABLE_NAME);
            nearMusicTable.onCreateTableIfNotExits();
        }
        return nearMusicTable;
    }

    public PiFuTable getPiFuTable() {
        if(piFuTable == null){
            piFuTable = new PiFuTable(library.getWriteDataBase(), PiFu.class,SQLTag.PIFU_TABLE_NAME);
            piFuTable.onCreateTableIfNotExits();
        }
        return piFuTable;
    }

    /**
     * 判断此歌曲是否为喜欢曲目
     * @param music
     * @return
     */
    public boolean isLikeMusic(Music music){
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("likeTag", WhereSymbolEnum.EQUAL,music.getWeiOneTag())
                .createSQL();
        String serachSQL = SerachTableData.getInstance()
                .setTableName(SQLTag.LIKE_TABLE_NAME)
                .createSQLAutoWhere(whererSqlWhere);
        Cursor cursor = getLikeMusicTable().serachBySQL(serachSQL);
        if(cursor == null){
            return false;
        }
        return cursor.getCount() != 0;
    }

    /**
     * 添加喜欢曲目
     * @param music
     */
    public void addLikeMusic(Music music){
        LikeMusic likeMusic = new LikeMusic();
        likeMusic.setLikeTag(music.getWeiOneTag());
        likeMusic.setMusic(music);
        likeMusic.setLikeTime(System.currentTimeMillis());
        getLikeMusicTable().saveData(likeMusic);
    }

    /**
     * 移除喜欢曲目
     * @param music
     */
    public void removeLikeMusic(Music music){
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("likeTag", WhereSymbolEnum.EQUAL,music.getWeiOneTag())
                .createSQL();
        getLikeMusicTable().delete(whererSqlWhere);
    }

    /**
     * 删除皮肤数据
     */
    public void deletePiFu(PiFu piFu){
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("piFuId", WhereSymbolEnum.EQUAL,piFu.getPiFuId())
                .createSQL();
        getPiFuTable().delete(whererSqlWhere);
    }

}
