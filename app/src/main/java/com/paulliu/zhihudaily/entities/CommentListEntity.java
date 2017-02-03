package com.paulliu.zhihudaily.entities;

import java.util.List;

/**
 * Created on 2017/2/3
 *
 * @author LLW
 */

public class CommentListEntity {
    private List<CommentEntity> comments;

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }
}
