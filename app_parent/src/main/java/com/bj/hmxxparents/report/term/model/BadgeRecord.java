package com.bj.hmxxparents.report.term.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/27 0027.
 */

public class BadgeRecord {

    /**
     * ret : 1
     * msg :
     * data : [{"stime":"2018-12-25","etime":"2019-01-01","huizhang":["4","4","3","6","7","5"],"time_status":"1"},{"stime":"2018-12-25","etime":"2019-01-01","huizhang":["9"],"time_status":"0"}]
     */

    private String ret;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * stime : 2018-12-25
         * etime : 2019-01-01
         * huizhang : ["4","4","3","6","7","5"]
         * time_status : 1
         */

        private String stime;
        private String etime;
        private String time_status;
        private List<String> huizhang;

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public String getEtime() {
            return etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }

        public String getTime_status() {
            return time_status;
        }

        public void setTime_status(String time_status) {
            this.time_status = time_status;
        }

        public List<String> getHuizhang() {
            return huizhang;
        }

        public void setHuizhang(List<String> huizhang) {
            this.huizhang = huizhang;
        }
    }
}
