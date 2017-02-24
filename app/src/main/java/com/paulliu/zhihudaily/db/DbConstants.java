package com.paulliu.zhihudaily.db;

import android.provider.BaseColumns;

/**
 * Created on 2017/2/24
 *
 * @author Levine
 */

class DbConstants {
    final static String DB_NAME = "zhihu_daily_sqlite";
    final static int DB_VERSION = 1;

    final static String CREATE_FAVORITE_NEWS_ENTRY = "CREATE TABLE " + FavoriteNewsEntry.TABLE_NAME_FAVORITE +
            " (" + FavoriteNewsEntry.NEWS_ID + " INTEGER PRIMARY KEY, " + FavoriteNewsEntry.NEWS_IMAGE + " TEXT, " +
            FavoriteNewsEntry.NEWS_TITLE + " TEXT)";
    final static String DELETE_FAVORITE_NEWS_ENTRY = "DROP TABLE IF EXISTS " + FavoriteNewsEntry.TABLE_NAME_FAVORITE;

    static class FavoriteNewsEntry implements BaseColumns {
        final static String TABLE_NAME_FAVORITE = "favorite_news";
        final static String NEWS_ID = "news_id";
        final static String NEWS_IMAGE = "news_image";
        final static String NEWS_TITLE = "news_title";
    }
}
