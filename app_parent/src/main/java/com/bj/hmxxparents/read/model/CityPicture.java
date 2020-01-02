package com.bj.hmxxparents.read.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/15 0015.
 */

public class CityPicture {


    /**
     * ret : 1
     * msg : 查询成功
     * data : ["http://spark-app.oss-cn-qingdao.aliyuncs.com/yuedudaka/yibin/1.jpg","http://spark-app.oss-cn-qingdao.aliyuncs.com/yuedudaka/yibin/2.jpg","http://spark-app.oss-cn-qingdao.aliyuncs.com/yuedudaka/yibin/3.jpg","http://spark-app.oss-cn-qingdao.aliyuncs.com/yuedudaka/yibin/4.jpg","http://spark-app.oss-cn-qingdao.aliyuncs.com/yuedudaka/yibin/5.jpg"]
     */

    private String ret;
    private String msg;
    private List<String> data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
