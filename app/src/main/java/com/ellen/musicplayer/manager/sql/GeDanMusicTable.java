package com.ellen.musicplayer.manager.sql;

import android.database.sqlite.SQLiteDatabase;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyReflectionTable;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldType;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldTypeEnum;
import com.google.gson.Gson;

public class GeDanMusicTable extends ZxyReflectionTable<GeDanMusic> {

    public GeDanMusicTable(SQLiteDatabase db, Class<? extends GeDanMusic> dataClass) {
        super(db, dataClass);
    }

    public GeDanMusicTable(SQLiteDatabase db, Class<? extends GeDanMusic> dataClass, String autoTableName) {
        super(db, dataClass, autoTableName);
    }

    @Override
    protected SQLFieldType getSQLFieldType(String classFieldName, Class typeClass) {
        return new SQLFieldType(getSQlStringType(typeClass), null);
    }

    @Override
    protected String getSQLFieldName(String classFieldName, Class typeClass) {
        return classFieldName;
    }

    @Override
    protected Object setBooleanValue(String classFieldName, boolean value) {
        return null;
    }

    @Override
    protected boolean isAutoCreateTable() {
        return false;
    }

    @Override
    protected SQLFieldType conversionSQLiteType(String classFieldName, Class typeClass) {
        return new SQLFieldType(SQLFieldTypeEnum.TEXT,null);
    }

    @Override
    protected <E> E setConversionValue(GeDanMusic likeMusic, String classFieldName, Class typeClass) {
        String json = new Gson().toJson(likeMusic.getMusic());
        return (E) json;
    }

    @Override
    protected <E> E resumeConversionObject(Object value, String classFieldName, Class typeClass) {
        String json  = (String) value;
        Music music = new Gson().fromJson(json, Music.class);
        return (E) music;
    }
}
