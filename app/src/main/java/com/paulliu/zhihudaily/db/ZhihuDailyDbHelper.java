package com.paulliu.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created on 2017/2/24
 *
 * @author Levine
 */

public class ZhihuDailyDbHelper extends SQLiteOpenHelper{
    private static ZhihuDailyDbHelper mHelper;

    /**
     * one helper instance，one db connection
     */
    public static synchronized ZhihuDailyDbHelper getInstance(Context context){
        if(mHelper == null)
            mHelper = new ZhihuDailyDbHelper(context.getApplicationContext());
        return mHelper;
    }

    private ZhihuDailyDbHelper(Context context){
        super(context, DbConstants.DB_NAME, null, DbConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.CREATE_FAVORITE_NEWS_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConstants.DELETE_FAVORITE_NEWS_ENTRY);
        onCreate(db);
    }
}
