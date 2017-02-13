package com.paulliu.zhihudaily.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class EditorEntity implements Parcelable {
    /**
     * url : http://www.zhihu.com/people/deng-ruo-xu
     * bio : 好奇心日报
     * id : 82
     * avatar : http://pic2.zhimg.com/d3b31fa32_m.jpg
     * name : 邓若虚
     */

    private String url;
    private String bio;
    private int id;
    private String avatar;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.bio);
        dest.writeInt(this.id);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
    }

    public EditorEntity() {
    }

    protected EditorEntity(Parcel in) {
        this.url = in.readString();
        this.bio = in.readString();
        this.id = in.readInt();
        this.avatar = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<EditorEntity> CREATOR = new Parcelable.Creator<EditorEntity>() {
        @Override
        public EditorEntity createFromParcel(Parcel source) {
            return new EditorEntity(source);
        }

        @Override
        public EditorEntity[] newArray(int size) {
            return new EditorEntity[size];
        }
    };
}
