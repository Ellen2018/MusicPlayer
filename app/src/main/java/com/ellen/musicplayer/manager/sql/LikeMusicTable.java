package com.ellen.musicplayer.manager.sql;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyReflectionTable;
import com.ellen.musicplayer.bean.LikeMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldType;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldTypeEnum;
import com.google.gson.Gson;

public class LikeMusicTable extends ZxyReflectionTable<LikeMusic> {

    public LikeMusicTable(SQLiteDatabase db, Class<? extends LikeMusic> dataClass) {
        super(db, dataClass);
    }

    public LikeMusicTable(SQLiteDatabase db, Class<? extends LikeMusic> dataClass, String autoTableName) {
        super(db, dataClass, autoTableName);
    }

    @Override
    protected SQLFieldType getSQLFieldType(String classFieldName, Class typeClass) {
        return new SQLFieldType(getSQlStringType(typeClass), null);
    }

    @Override
    protected String getSQLFieldName(String classFieldName, Class typeClass) {
        Log.e("Ellen2018:like",classFieldName);
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
    protected <E> E setConversionValue(LikeMusic likeMusic, String classFieldName, Class typeClass) {
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
