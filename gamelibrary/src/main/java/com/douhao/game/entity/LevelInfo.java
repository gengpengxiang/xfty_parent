package com.douhao.game.entity;

/**
 * Created by zz379 on 2017/6/5.
 */

public class LevelInfo {

    private int lvlNumber;  // 当前关卡
    private String lvlContent;  // 关卡内容
    private String lvlTrueReading;  // 正确读音
    private String shitiCode;  // 当前关卡对应的试题的Code
    private int lvlScore;   // 当前关卡得分
    private int lvlbeatUsers;   // 打败了多少玩家
    private String voicePath;   // 录音文件地址

    public LevelInfo() {
    }


    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getShitiCode() {
        return shitiCode;
    }

    public void setShitiCode(String shitiCode) {
        this.shitiCode = shitiCode;
    }

    public int getLvlNumber() {
        return lvlNumber;
    }

    public void setLvlNumber(int lvlNumber) {
        this.lvlNumber = lvlNumber;
    }

    public int getLvlScore() {
        return lvlScore;
    }

    public void setLvlScore(int lvlScore) {
        this.lvlScore = lvlScore;
    }

    public int getLvlbeatUsers() {
        return lvlbeatUsers;
    }

    public void setLvlbeatUsers(int lvlbeatUsers) {
        this.lvlbeatUsers = lvlbeatUsers;
    }

    public String getLvlContent() {
        return lvlContent;
    }

    public void setLvlContent(String lvlContent) {
        this.lvlContent = lvlContent;
    }

    public String getLvlTrueReading() {
        return lvlTrueReading;
    }

    public void setLvlTrueReading(String lvlTrueReading) {
        this.lvlTrueReading = lvlTrueReading;
    }
}
