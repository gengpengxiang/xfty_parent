package com.bj.hmxxparents.entity;

/**
 * Created by zz379 on 2016/12/25.
 */
public class KidClassInfo {

    private Long id;
    private String kidId;
    private String kidName;
    private String kidImg;
    private String schoolId;
    private String schoolName;
    private String schoolImg;
    private String classId;
    private String className;
    private String kidGender;   // 性别
    private String kidBirthday; // 生日
    private String kidRelation; // 关系
    private String errorCode;
    private String message;

    private String tianyuan;

    public String getTianyuan() {
        return tianyuan;
    }

    public void setTianyuan(String tianyuan) {
        this.tianyuan = tianyuan;
    }

    public String getSchoolImg() {
        return schoolImg;
    }

    public void setSchoolImg(String schoolImg) {
        this.schoolImg = schoolImg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KidClassInfo(Long id, String kidId, String kidName, String kidImg, String schoolId,
            String schoolName, String schoolImg, String classId, String className, String kidGender,
            String kidBirthday, String kidRelation) {
        this.id = id;
        this.kidId = kidId;
        this.kidName = kidName;
        this.kidImg = kidImg;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.schoolImg = schoolImg;
        this.classId = classId;
        this.className = className;
        this.kidGender = kidGender;
        this.kidBirthday = kidBirthday;
        this.kidRelation = kidRelation;
    }

    public KidClassInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKidId() {
        return this.kidId;
    }

    public void setKidId(String kidId) {
        this.kidId = kidId;
    }

    public String getKidName() {
        return this.kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public String getKidImg() {
        return this.kidImg;
    }

    public void setKidImg(String kidImg) {
        this.kidImg = kidImg;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getKidGender() {
        return kidGender;
    }

    public void setKidGender(String kidGender) {
        this.kidGender = kidGender;
    }

    public String getKidBirthday() {
        return kidBirthday;
    }

    public void setKidBirthday(String kidBirthday) {
        this.kidBirthday = kidBirthday;
    }

    public String getKidRelation() {
        return kidRelation;
    }

    public void setKidRelation(String kidRelation) {
        this.kidRelation = kidRelation;
    }
}
