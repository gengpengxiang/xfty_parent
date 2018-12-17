package com.bj.hmxxparents.entity;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zz379 on 2017/ic_pinde/6.
 */

public class ReportInfo {
    public String schoolReportState;   // 1 代表打开入口
    public int reportRealPay;  // 实际支付价格
    public int reportPrice;    // 原价

    public boolean isUserPaySuccess;  // 用户是否购买过

    public String userBadgeRank;   // 学生徽章排名
    public String userBadgeCount;  // 我的徽章总数
    public String classBadgeAvg;   // 班级徽章平均数

    public String userCommendRank; // 学生点赞排名
    public String userCommendCount;    // 点赞数量
    public String classCommendAvg; // 班级点赞平均数

    public LinkedHashMap<String, Integer> userBadgePieMap;   // 徽章分布
    public LinkedHashMap<String, Integer> userCommendPieMap; // 点赞分布

    public LinkedHashMap<Integer, Float> userBadgeLineMap;   // 徽章折线分布
    public LinkedHashMap<Integer, Float> classBadgeLineMap;   // 班级徽章折线分布
    public LinkedHashMap<Integer, Float> userCommendLineMap; // 点赞折线分布
    public LinkedHashMap<Integer, Float> classCommendLineMap; // 班级点赞折线分布

    public List<User> badgeRankList;       // 徽章荣誉榜
    public List<User> commendRankList;     // 点赞荣誉榜

    public static class User {
        public String username;
        public String count;
        public String photoPath;
        public String pm;   //  排名
    }
}
