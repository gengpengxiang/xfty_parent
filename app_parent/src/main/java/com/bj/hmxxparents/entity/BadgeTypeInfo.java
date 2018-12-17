package com.bj.hmxxparents.entity;

/**
 * Created by zz379 on 2017/2/18.
 */

public class BadgeTypeInfo {

    private int badgeArtNum;    // 艺术
    private int badgeBehaviorNum;   // 品德
    private int badgelanguageNum;   // 语言
    private int badgescienceNum;    // 科学
    private int badgesportNum;  // 运动

    public int getBadgeTotal() {
        return badgeArtNum + badgeBehaviorNum + badgelanguageNum + badgescienceNum + badgesportNum;
    }

    public int getBadgeArtNum() {
        return badgeArtNum;
    }

    public void setBadgeArtNum(int badgeArtNum) {
        this.badgeArtNum = badgeArtNum;
    }

    public int getBadgeBehaviorNum() {
        return badgeBehaviorNum;
    }

    public void setBadgeBehaviorNum(int badgeBehaviorNum) {
        this.badgeBehaviorNum = badgeBehaviorNum;
    }

    public int getBadgelanguageNum() {
        return badgelanguageNum;
    }

    public void setBadgelanguageNum(int badgelanguageNum) {
        this.badgelanguageNum = badgelanguageNum;
    }

    public int getBadgescienceNum() {
        return badgescienceNum;
    }

    public void setBadgescienceNum(int badgescienceNum) {
        this.badgescienceNum = badgescienceNum;
    }

    public int getBadgesportNum() {
        return badgesportNum;
    }

    public void setBadgesportNum(int badgesportNum) {
        this.badgesportNum = badgesportNum;
    }
}
