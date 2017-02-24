package com.paulliu.zhihudaily.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 2017/1/16
 *
 * @author LLW
 */

public class NewsEntity implements Parcelable {
    /**
     * title : 中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）
     * ga_prefix : 052321
     * images : ["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"]
     * type : 0
     * id : 3930445
     */

    private String title;
    private String ga_prefix;
    private int type;
    private int id;
    private List<String> images;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public NewsEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.ga_prefix);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
        dest.writeStringList(this.images);
        dest.writeString(this.image);
    }

    protected NewsEntity(Parcel in) {
        this.title = in.readString();
        this.ga_prefix = in.readString();
        this.type = in.readInt();
        this.id = in.readInt();
        this.images = in.createStringArrayList();
        this.image = in.readString();
    }

    public static final Creator<NewsEntity> CREATOR = new Creator<NewsEntity>() {
        @Override
        public NewsEntity createFromParcel(Parcel source) {
            return new NewsEntity(source);
        }

        @Override
        public NewsEntity[] newArray(int size) {
            return new NewsEntity[size];
        }
    };
}
