package com.example.coalreport.entity;

import android.graphics.Bitmap;
import android.widget.VideoView;

import com.dueeeke.videocontroller.component.PrepareView;

import java.io.Serializable;


/**
 * @author a-cper-cpu
 * 视频实体类
 */

public class VideoEntity implements Serializable {

    //id
    private int vid;
    //标题
    private String vtitle;
    //作者
    private String author;
    //点赞数
    private int dzCount;
    //评论数
    private int commentCount;
    //收藏数
    private int vCollect;
    //发布时间
    private String createTime;
    //头像
    private String headurl;
    //举报文字
    private String text_report;
    //视频
    private String videourl;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDzCount() {
        return dzCount;
    }

    public void setDzCount(int dzCount) {
        this.dzCount = dzCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getvCollect() {
        return vCollect;
    }

    public void setvCollect(int vCollect) {
        this.vCollect = vCollect;
    }

//    public String getVideoUrl() {
//        return videoUrl;
//    }
//
//    public void setVideoUrl(String videoUrl) {
//        this.videoUrl = videoUrl;
//    }


    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getText_report() {
        return text_report;
    }

    public void setText_report(String text_report) {
        this.text_report = text_report;
    }
}
