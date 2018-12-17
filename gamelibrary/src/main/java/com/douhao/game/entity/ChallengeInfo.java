package com.douhao.game.entity;

/**
 * Created by zz379 on 2017/6/5.
 */

public class ChallengeInfo {
    private int challengeNumber;    // 挑战次数
    private int challengeScore; // 挑战得分
    private int challengeRank;  // 全国排名

    public int getChallengeNumber() {
        return challengeNumber;
    }

    public void setChallengeNumber(int challengeNumber) {
        this.challengeNumber = challengeNumber;
    }

    public int getChallengeScore() {
        return challengeScore;
    }

    public void setChallengeScore(int challengeScore) {
        this.challengeScore = challengeScore;
    }

    public int getChallengeRank() {
        return challengeRank;
    }

    public void setChallengeRank(int challengeRank) {
        this.challengeRank = challengeRank;
    }
}
