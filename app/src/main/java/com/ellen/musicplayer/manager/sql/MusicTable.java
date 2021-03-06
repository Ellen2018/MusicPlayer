package com.ellen.musicplayer.manager.sql;

import android.database.sqlite.SQLiteDatabase;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyReflectionTable;
import com.ellen.musicplayer.bean.Music;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldType;

public class MusicTable extends ZxyReflectionTable<Music> {


    public MusicTable(SQLiteDatabase db, Class<? extends Music> dataClass) {
        super(db, dataClass);
    }

    public MusicTable(SQLiteDatabase db, Class<? extends Music> dataClass, String autoTableName) {
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
        if(value){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    protected boolean isAutoCreateTable() {
        return false;
    }

    @Override
    protected SQLFieldType conversionSQLiteType(String classFieldName, Class typeClass) {
        return null;
    }

    @Override
    protected <E> E setConversionValue(Music music, String classFieldName, Class typeClass) {
        return null;
    }

    @Override
    protected <E> E resumeConversionObject(Object value, String classFieldName, Class typeClass) {
        return null;
    }
}
