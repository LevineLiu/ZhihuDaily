package com.paulliu.zhihudaily.entities;

import java.util.List;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class ThemeEntity {
    /**
     * stories :
     * description : 除了经典和新片，我们还关注技术和产业
     * background : http://p1.zhimg.com/80/0b/800b79a4821a535de31b349ffdc9eabb.jpg
     * color : 14483535
     * name : 电影日报
     * image : http://p1.zhimg.com/dd/f1/ddf10a04227ea50fd59746dbcd13c728.jpg
     * editors :
     * image_source :
     */

    private List<NewsEntity> stories;
    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private List<EditorEntity> editors;
    private String image_source;

    public List<NewsEntity> getStories() {
        return stories;
    }

    public void setStories(List<NewsEntity> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<EditorEntity> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorEntity> editors) {
        this.editors = editors;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
