package com.paulliu.zhihudaily.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class NewsExtraEntity implements Parcelable {

    /**
     * long_comments : 1
     * popularity : 546
     * short_comments : 41
     * comments : 42
     */

    private int long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.long_comments);
        dest.writeInt(this.popularity);
        dest.writeInt(this.short_comments);
        dest.writeInt(this.comments);
    }

    public NewsExtraEntity() {
    }

    protected NewsExtraEntity(Parcel in) {
        this.long_comments = in.readInt();
        this.popularity = in.readInt();
        this.short_comments = in.readInt();
        this.comments = in.readInt();
    }

    public static final Parcelable.Creator<NewsExtraEntity> CREATOR = new Parcelable.Creator<NewsExtraEntity>() {
        @Override
        public NewsExtraEntity createFromParcel(Parcel source) {
            return new NewsExtraEntity(source);
        }

        @Override
        public NewsExtraEntity[] newArray(int size) {
            return new NewsExtraEntity[size];
        }
    };
}
