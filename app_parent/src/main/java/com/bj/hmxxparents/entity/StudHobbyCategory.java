package com.bj.hmxxparents.entity;

import java.util.List;

/**
 * Created by zz379 on 2017/5/8.
 */

public class StudHobbyCategory {

    private String typeName;
    private List<StudentHobbyInfo> hobbyInfoList;
    private List<StudentHobbyInfo> hobbySelectList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<StudentHobbyInfo> getHobbyInfoList() {
        return hobbyInfoList;
    }

    public void setHobbyInfoList(List<StudentHobbyInfo> hobbyInfoList) {
        this.hobbyInfoList = hobbyInfoList;
    }

    public List<StudentHobbyInfo> getHobbySelectList() {
        return hobbySelectList;
    }

    public void setHobbySelectList(List<StudentHobbyInfo> hobbySelectList) {
        this.hobbySelectList = hobbySelectList;
    }
}
