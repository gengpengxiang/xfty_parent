package com.bj.hmxxparents.report.term.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/27 0027.
 */

public class Fengchao {

    /**
     * ret : 1
     * msg :
     * data : {"yemian":"1","jihao":[13,17,11,12,9,15,7],"hzsum":"7"}
     */

    private String ret;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * yemian : 1
         * jihao : [13,17,11,12,9,15,7]
         * hzsum : 7
         */

        private String yemian;
        private String hzsum;
        private List<Integer> jihao;

        public String getYemian() {
            return yemian;
        }

        public void setYemian(String yemian) {
            this.yemian = yemian;
        }

        public String getHzsum() {
            return hzsum;
        }

        public void setHzsum(String hzsum) {
            this.hzsum = hzsum;
        }

        public List<Integer> getJihao() {
            return jihao;
        }

        public void setJihao(List<Integer> jihao) {
            this.jihao = jihao;
        }
    }
}
