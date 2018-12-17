package com.bj.hmxxparents.entity;

/**
 * Created by zz379 on 2017/5/6.
 */

public class StudentHobbyInfo {
    public static final int SHOW_TYPE_ITEM = 101;
    public static final int SHOW_TYPE_CATEGORY = 102;
    public static final int SHOW_TYPE_NONE = 103;

    private String hobbyID;     // 兴趣ID
    private String hobbyName;   // 兴趣名字
    private String hobbyPic;    // 兴趣图片
    private String hobbyCategory;   // 兴趣分类

    private int hobbyShowType;   // 兴趣显示方式：1，2，3
    private boolean hobbyIsChecked;  // 是否选中

    public String getHobbyID() {
        return hobbyID;
    }

    public void setHobbyID(String hobbyID) {
        this.hobbyID = hobbyID;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getHobbyPic() {
        return hobbyPic;
    }

    public void setHobbyPic(String hobbyPic) {
        this.hobbyPic = hobbyPic;
    }

    public String getHobbyCategory() {
        return hobbyCategory;
    }

    public void setHobbyCategory(String hobbyCategory) {
        this.hobbyCategory = hobbyCategory;
    }

    public int getHobbyShowType() {
        return hobbyShowType;
    }

    public void setHobbyShowType(int hobbyShowType) {
        this.hobbyShowType = hobbyShowType;
    }

    public boolean isHobbyIsChecked() {
        return hobbyIsChecked;
    }

    public void setHobbyIsChecked(boolean hobbyIsChecked) {
        this.hobbyIsChecked = hobbyIsChecked;
    }
}
