package com.bj.hmxxparents.entity;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class MessageEvent {
    private String message;
    private String type;

    private String key;
    private String param1,param2,param3;


    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, String param1) {
        this.message = message;
        this.param1 = param1;
    }


    public MessageEvent(String message, String param1, String param2) {
        this.message = message;
        this.param1 = param1;
        this.param2 = param2;
    }

    public MessageEvent(String message, String param1, String param2, String param3) {
        this.message = message;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
}
