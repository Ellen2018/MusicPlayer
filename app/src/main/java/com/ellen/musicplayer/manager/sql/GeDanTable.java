package com.ellen.musicplayer.manager.sql;

import android.database.sqlite.SQLiteDatabase;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyReflectionTable;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldType;
import com.ellen.sqlitecreate.createsql.helper.SQLFieldTypeEnum;

public class GeDanTable extends ZxyReflectionTable<GeDan> {

    public GeDanTable(SQLiteDatabase db, Class<? extends GeDan> dataClass) {
        super(db, dataClass);
    }

    public GeDanTable(SQLiteDatabase db, Class<? extends GeDan> dataClass, String autoTableName) {
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
    protected <E> E setConversionValue(GeDan geDan, String classFieldName, Class typeClass) {
        return null;
    }

    @Override
    protected <E> E resumeConversionObject(Object value, String classFieldName, Class typeClass) {
        return null;
    }
}
