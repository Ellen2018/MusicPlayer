package com.ellen.musicplayer.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ellen.dhcsqlitelibrary.table.reflection.ZxyLibrary;

public class Library extends ZxyLibrary {

    public Library(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public void onZxySQLiteCreate(SQLiteDatabase db) {

    }

    @Override
    public void onZxySQLiteUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
