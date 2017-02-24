package com.paulliu.zhihudaily.entity;

import java.util.List;

/**
 * Created on 2017/1/16
 *
 * @author LLW
 */

public class DailyNews {
    private String date;
    private List<NewsEntity> stories;
    private List<NewsEntity> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<NewsEntity> getStories() {
        return stories;
    }

    public void setStories(List<NewsEntity> stories) {
        this.stories = stories;
    }

    public List<NewsEntity> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<NewsEntity> top_stories) {
        this.top_stories = top_stories;
    }
}
