package com.paulliu.zhihudaily.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.paulliu.zhihudaily.entity.NewsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/2/24
 *
 * @author Levine
 */

public class ZhihuDailyDbManager {
    private SQLiteDatabase mSQLiteDatabase;

    public ZhihuDailyDbManager(Context context){
        ZhihuDailyDbHelper helper = ZhihuDailyDbHelper.getInstance(context);
        mSQLiteDatabase = helper.getReadableDatabase();
    }

    public boolean isNewsCollected(int id){
        String sql = "select* from " + DbConstants.FavoriteNewsEntry.TABLE_NAME_FAVORITE + " where " +
                DbConstants.FavoriteNewsEntry.NEWS_ID + "=" +id;
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        int count = cursor.getCount();
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public boolean insertFavoriteNews(NewsEntity newsEntity){
        if(newsEntity == null)
            return false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.FavoriteNewsEntry.NEWS_ID, newsEntity.getId());
        contentValues.put(DbConstants.FavoriteNewsEntry.NEWS_TITLE, newsEntity.getTitle());
        if(newsEntity.getImages() != null && newsEntity.getImages().size() > 0)
            contentValues.put(DbConstants.FavoriteNewsEntry.NEWS_IMAGE, newsEntity.getImages().get(0));
        long result = mSQLiteDatabase.insert(DbConstants.FavoriteNewsEntry.TABLE_NAME_FAVORITE, null, contentValues);
        if(result != -1)
            return true;
        else
            return false;
    }

    public boolean deleteFavoriteNews(int newsId){
        long result = mSQLiteDatabase.delete(DbConstants.FavoriteNewsEntry.TABLE_NAME_FAVORITE,
                DbConstants.FavoriteNewsEntry.NEWS_ID + "=?", new String[]{String.valueOf(newsId)});
        if(result == 0)
            return false;
        else
            return true;
    }

    public List<NewsEntity> getFavoriteNewsList(){
        Cursor cursor = mSQLiteDatabase.rawQuery("select* from " + DbConstants.FavoriteNewsEntry.TABLE_NAME_FAVORITE, null);
        List<NewsEntity> list = new ArrayList<>();
        while (cursor.moveToNext()){
            list.add(getNewsEntityByCursor(cursor));
        }
        cursor.close();
        return list;
    }

    private NewsEntity getNewsEntityByCursor(Cursor cursor){
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(cursor.getInt(cursor.getColumnIndex(DbConstants.FavoriteNewsEntry.NEWS_ID)));
        newsEntity.setTitle(cursor.getString(cursor.getColumnIndex(DbConstants.FavoriteNewsEntry.NEWS_TITLE)));
        String imageUrl = cursor.getString(cursor.getColumnIndex(DbConstants.FavoriteNewsEntry.NEWS_IMAGE));
        List<String> list = new ArrayList<>(1);
        list.add(imageUrl);
        newsEntity.setImages(list);
        return newsEntity;
    }
}
