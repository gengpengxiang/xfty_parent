package com.bj.hmxxparents.entity;

/**
 * Created by zz379 on 2017/2/12.
 */

public class ClassNewsInfo {

    private String newsId;
    private String newsPicture;
    private String newsType;    // 3 ~ ic_pinde 代表徽章  z1 代表点赞
    private String newsTitle;
    private String newsTime;
    private String newsThanksStatus;
    private String teacherPic;
    private String teacherName;
    private String teacherPhone;
    private String studentPic;
    private String studentName;
    private String newsDesc;

    private String xueke;
    private int huode_num;

    private String xueke_img;

    public String getXueke_img() {
        return xueke_img;
    }

    public void setXueke_img(String xueke_img) {
        this.xueke_img = xueke_img;
    }

    public int getHuode_num() {
        return huode_num;
    }

    public void setHuode_num(int huode_num) {
        this.huode_num = huode_num;
    }

    public String getXueke() {
        return xueke;
    }

    public void setXueke(String xueke) {
        this.xueke = xueke;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsPicture() {
        return newsPicture;
    }

    public void setNewsPicture(String newsPicture) {
        this.newsPicture = newsPicture;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsThanksStatus() {
        return newsThanksStatus;
    }

    public void setNewsThanksStatus(String newsThanksStatus) {
        this.newsThanksStatus = newsThanksStatus;
    }

    public String getTeacherPic() {
        return teacherPic;
    }

    public void setTeacherPic(String teacherPic) {
        this.teacherPic = teacherPic;
    }

    public String getStudentPic() {
        return studentPic;
    }

    public void setStudentPic(String studentPic) {
        this.studentPic = studentPic;
    }

    //-------------------------------分割线-----------------------------------------------------
    public static final int ITEM_SHOW_TYPE_NORMAL = 1;
    public static final int ITEM_SHOW_TYPE_EMPTY = 0;

    private int itemShowType = ITEM_SHOW_TYPE_NORMAL;

    public int getItemShowType() {
        return itemShowType;
    }

    public void setItemShowType(int itemShowType) {
        this.itemShowType = itemShowType;
    }

    public static ClassNewsInfo newInstanceForEmptyView() {
        ClassNewsInfo newsInfo = new ClassNewsInfo();
        newsInfo.setItemShowType(ITEM_SHOW_TYPE_EMPTY);
        return newsInfo;
    }
}
