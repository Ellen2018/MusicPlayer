package com.ellen.musicplayer.manager.sql;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.ellen.musicplayer.SQLTag;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.sqlitecreate.createsql.helper.WhereSymbolEnum;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.sqlitecreate.createsql.serach.SerachTableData;
import com.ellen.sqlitecreate.createsql.where.Where;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    private WeakReference<Context> contextWeakReference;
    private Library library;
    private static volatile SQLManager sqlManager;
    private GeDanMusicTable likeGeDanMusicTable;
    private NearMusicTable nearMusicTable;
    private PiFuTable piFuTable;
    private GeDanTable geDanTable;

    private SQLManager() {
    }

    public static SQLManager getInstance() {
        if (sqlManager == null) {
            synchronized (SQLManager.class) {
                if (sqlManager == null) {
                    sqlManager = new SQLManager();
                }
            }
        }
        return sqlManager;
    }

    public void initLibrary(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        library = new Library(contextWeakReference.get(), SQLTag.LIBRARY_NAME, 1);
    }

    public GeDanTable getGeDanTable() {
        if (geDanTable == null) {
            geDanTable = new GeDanTable(library.getWriteDataBase(), GeDan.class, SQLTag.GE_DAN_NAME);
            geDanTable.onCreateTableIfNotExits();
        }
        return geDanTable;
    }

    public GeDanMusicTable getLikeGeDanMusicTable() {
        if (likeGeDanMusicTable == null) {
            likeGeDanMusicTable = new GeDanMusicTable(library.getWriteDataBase(), GeDanMusic.class, SQLTag.LIKE_TABLE_NAME);
            likeGeDanMusicTable.onCreateTableIfNotExits();
        }
        return likeGeDanMusicTable;
    }

    public NearMusicTable getNearMusicTable() {
        if (nearMusicTable == null) {
            nearMusicTable = new NearMusicTable(library.getWriteDataBase(), NearMusic.class, SQLTag.NEAR_TABLE_NAME);
            nearMusicTable.onCreateTableIfNotExits();
        }
        return nearMusicTable;
    }

    public PiFuTable getPiFuTable() {
        if (piFuTable == null) {
            piFuTable = new PiFuTable(library.getWriteDataBase(), PiFu.class, SQLTag.PIFU_TABLE_NAME);
            piFuTable.onCreateTableIfNotExits();
        }
        return piFuTable;
    }

    /**
     * 判断此歌曲是否为喜欢曲目
     *
     * @param music
     * @return
     */
    public boolean isLikeMusic(Music music) {
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("likeTag", WhereSymbolEnum.EQUAL, music.getWeiOneTag())
                .createSQL();
        String serachSQL = SerachTableData.getInstance()
                .setTableName(SQLTag.LIKE_TABLE_NAME)
                .createSQLAutoWhere(whererSqlWhere);
        Cursor cursor = getLikeGeDanMusicTable().serachBySQL(serachSQL);
        if (cursor == null) {
            return false;
        }
        return cursor.getCount() != 0;
    }

    /**
     * 添加喜欢曲目
     *
     * @param music
     */
    public void addLikeMusic(Music music) {
        GeDanMusic likeMusic = new GeDanMusic();
        likeMusic.setLikeTag(music.getWeiOneTag());
        likeMusic.setMusic(music);
        likeMusic.setLikeTime(System.currentTimeMillis());
        getLikeGeDanMusicTable().saveData(likeMusic);
    }

    /**
     * 移除喜欢曲目
     *
     * @param music
     */
    public void removeLikeMusic(Music music) {
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("likeTag", WhereSymbolEnum.EQUAL, music.getWeiOneTag())
                .createSQL();
        getLikeGeDanMusicTable().delete(whererSqlWhere);
    }

    /**
     * 删除皮肤数据
     */
    public void deletePiFu(PiFu piFu) {
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("piFuId", WhereSymbolEnum.EQUAL, piFu.getPiFuId())
                .createSQL();
        getPiFuTable().delete(whererSqlWhere);
    }

    /**
     * 获取随机size首歌
     * size >= 本地最大曲目 则返回整个本地列表，否则从本地列表里抽取size首歌
     */
    public List<Music> getMostMusic(Context context, int size) {
        List<Music> musicList = LocalSDMusicUtils.getLocalAllMusic(context);
        if (musicList.size() <= size) {
            return musicList;
        } else {
            List<Music> musicList1 = new ArrayList<>();
            List<Integer> integerList = new ArrayList<>();
            //随机产生size个数
            for (int i = 0; i < size; i++) {
                boolean isSame = false;
                do {
                    isSame = false;
                    int currentPosition = (int) ((Math.random() * musicList.size()));
                    for (int j = 0; j < integerList.size(); j++) {
                        if (currentPosition == integerList.get(j)) {
                            isSame = true;
                        }
                    }
                    if (!isSame) {
                        integerList.add(currentPosition);
                    }
                } while (isSame);
            }
            for (int postion : integerList) {
                musicList1.add(musicList.get(postion));
            }
            return musicList1;
        }
    }


    /**
     * 创建歌单
     *
     * @param geDanName 歌单名
     * @return 歌单是否创建成功
     */
    public boolean createGeDan(String geDanName, List<Music> musicList) {
        //先查询一下是否之前创建了此歌单
        if (isCreateGeDan(geDanName)) {
            return false;
        } else {
            //添加数据到歌单管理表
            GeDan geDan = new GeDan();
            geDan.setGeDanName(geDanName);
            geDan.setGeDanSqlTableName(System.currentTimeMillis());
            getGeDanTable().saveData(geDan);

            //开始创建歌单
            String tableName = SQLTag.GE_DAN_NAME + "_" + geDan.getGeDanSqlTableName();
            GeDanMusicTable geDanMusicTable = new GeDanMusicTable(library.getWriteDataBase(), GeDanMusic.class, tableName);
            geDanMusicTable.onCreateTableIfNotExits();

            //添加数据

            if (musicList != null) {
                List<GeDanMusic> geDanMusicList = new ArrayList<>();
                for (Music music : musicList) {
                    GeDanMusic geDanMusic = new GeDanMusic();
                    geDanMusic.setLikeTag(music.getWeiOneTag());
                    geDanMusic.setLikeTime(System.currentTimeMillis());
                    geDanMusic.setMusic(music);
                    geDanMusicList.add(geDanMusic);
                }
                geDanMusicTable.saveData(geDanMusicList);
            }

            return true;
        }
    }

    public GeDanMusicTable geDanMusicTable(GeDan geDan){
        return new GeDanMusicTable(library.getWriteDataBase(),GeDanMusic.class,SQLTag.GE_DAN_NAME +"_"+geDan.getGeDanSqlTableName());
    }

    private boolean isGeDanContainsMusic(GeDanMusicTable geDanMusicTable, String tableName, GeDanMusic geDanMusic) {
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("likeTag", WhereSymbolEnum.EQUAL, geDanMusic.getLikeTag())
                .createSQL();
        String serachSQL = SerachTableData.getInstance()
                .setTableName(tableName)
                .createSQLAutoWhere(whererSqlWhere);
        Cursor cursor = geDanMusicTable.serachBySQL(serachSQL);
        if (cursor == null) {
            return false;
        }
        return cursor.getCount() != 0;
    }

    /**
     * 添加歌曲到歌单
     *
     * @param musicList
     * @param geDan
     */
    public void addMusicsToGeDan(List<Music> musicList, GeDan geDan) {
        if (musicList != null && musicList.size() > 0) {
            String tableName = SQLTag.GE_DAN_NAME + "_" + geDan.getGeDanSqlTableName();
            GeDanMusicTable geDanMusicTable = new GeDanMusicTable(library.getWriteDataBase(), GeDanMusic.class, tableName);
            geDanMusicTable.onCreateTableIfNotExits();
            List<GeDanMusic> geDanMusicList = new ArrayList<>();
            for (Music music : musicList) {
                GeDanMusic geDanMusic = new GeDanMusic();
                geDanMusic.setLikeTag(music.getWeiOneTag());
                geDanMusic.setLikeTime(System.currentTimeMillis());
                geDanMusic.setMusic(music);
                if (!isGeDanContainsMusic(geDanMusicTable, tableName, geDanMusic)) {
                    geDanMusicList.add(geDanMusic);
                }
            }
            geDanMusicTable.saveData(geDanMusicList);
        }
    }

    public List<GeDanMusic> getGeDanMusicListByName(GeDan geDan) {
        String tableName = SQLTag.GE_DAN_NAME + "_" + geDan.getGeDanSqlTableName();
        GeDanMusicTable geDanMusicTable = new GeDanMusicTable(library.getWriteDataBase(), GeDanMusic.class, tableName);
        geDanMusicTable.onCreateTableIfNotExits();
        return geDanMusicTable.getAllDatas(Order.getInstance(false)
                .setFirstOrderFieldName("likeTime")
                .setIsDesc(true).createSQL());
    }

    /**
     * 判断此歌曲是否为喜欢曲目
     *
     * @param geDanName
     * @return
     */
    public boolean isCreateGeDan(String geDanName) {
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("geDanName", WhereSymbolEnum.EQUAL, geDanName)
                .createSQL();
        String serachSQL = SerachTableData.getInstance()
                .setTableName(SQLTag.GE_DAN_NAME)
                .createSQLAutoWhere(whererSqlWhere);
        Cursor cursor = getGeDanTable().serachBySQL(serachSQL);
        if (cursor == null) {
            return false;
        }
        return cursor.getCount() != 0;
    }

    /**
     * 判断最近列表是否已经存在该歌曲
     * @param music
     * @return
     */
    public boolean isContansNearMusic(Music music){
        String whererSqlWhere = Where
                .getInstance(false)
                .addAndWhereValue("nearTag", WhereSymbolEnum.EQUAL, music.getWeiOneTag())
                .createSQL();
        String serachSQL = SerachTableData.getInstance()
                .setTableName(SQLTag.NEAR_TABLE_NAME)
                .createSQLAutoWhere(whererSqlWhere);
        Cursor cursor = getNearMusicTable().serachBySQL(serachSQL);
        if (cursor == null) {
            return false;
        }
        return cursor.getCount() != 0;
    }

    public NearMusic getNearMusicByTag(String tag){
        NearMusic result = null;
        List<NearMusic> nearMusicList = getNearMusicTable().getAllDatas(null);
        if(nearMusicList != null && nearMusicList.size() > 0){
            for(NearMusic nearMusic : nearMusicList){
                if(nearMusic.getNearTag().equals(tag)){
                    result = nearMusic;
                    break;
                }
            }
        }
        return result;
    }
}
