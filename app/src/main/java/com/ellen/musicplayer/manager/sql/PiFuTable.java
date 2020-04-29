package com.ellen.musicplayer.manager.sql;

import android.database.sqlite.SQLiteDatabase;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyReflectionTable;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldType;

public class PiFuTable extends ZxyReflectionTable<PiFu> {

    public PiFuTable(SQLiteDatabase db, Class<? extends PiFu> dataClass) {
        super(db, dataClass);
    }

    public PiFuTable(SQLiteDatabase db, Class<? extends PiFu> dataClass, String autoTableName) {
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
    protected <E> E setConversionValue(PiFu piFu, String classFieldName, Class typeClass) {
        return null;
    }

    @Override
    protected <E> E resumeConversionObject(Object value, String classFieldName, Class typeClass) {
        return null;
    }
}
