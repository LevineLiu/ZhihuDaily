package com.paulliu.zhihudaily.entities;

/**
 * Created on 2017/1/9
 *
 * @author LLW
 */

public class NewVersion {

    /**
     * status : 1    //0为最新版本，1为旧版本
     * msg : 【更新内容】（后略）
     * latest : 2.2.0
     */

    private int status;
    private String msg;
    private String latest;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }
}
