package com.bj.hmxxparents.entity;

/**
 * Created by zz379 on 2017/ic_pinde/4.
 */

public class ChartItemInfo {
    private int color;
    private String content;
    private int number;

    public ChartItemInfo() {
    }

    public ChartItemInfo(String content, int number) {
        this.content = content;
        this.number = number;
    }

    public ChartItemInfo(int color, String content, int number) {
        this.color = color;
        this.content = content;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
