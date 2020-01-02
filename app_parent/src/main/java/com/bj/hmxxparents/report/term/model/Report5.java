package com.bj.hmxxparents.report.term.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19 0019.
 */

public class Report5 {

    /**
     * ret : 1
     * msg :
     * data : [{"monthtime":"2018-09-01","weekdata_dz":"0","weekdata_hz":"0","class_avg_dz":"0","class_avg_hz":"0","class_student_num":"3"},{"monthtime":"2018-10-01","weekdata_dz":"0","weekdata_hz":"0","class_avg_dz":"0","class_avg_hz":"0","class_student_num":"3"},{"monthtime":"2018-11-01","weekdata_dz":"0","weekdata_hz":"0","class_avg_dz":"0","class_avg_hz":"0","class_student_num":"3"},{"monthtime":"2018-12-01","weekdata_dz":"4","weekdata_hz":"7","class_avg_dz":"1.3333333333333","class_avg_hz":"2.3333333333333","class_student_num":"3"}]
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
         * monthtime : 2018-09-01
         * weekdata_dz : 0
         * weekdata_hz : 0
         * class_avg_dz : 0
         * class_avg_hz : 0
         * class_student_num : 3
         */

        private String monthtime;
        private String weekdata_dz;
        private String weekdata_hz;
        private String class_avg_dz;
        private String class_avg_hz;
        private String class_student_num;

        public String getMonthtime() {
            return monthtime;
        }

        public void setMonthtime(String monthtime) {
            this.monthtime = monthtime;
        }

        public String getWeekdata_dz() {
            return weekdata_dz;
        }

        public void setWeekdata_dz(String weekdata_dz) {
            this.weekdata_dz = weekdata_dz;
        }

        public String getWeekdata_hz() {
            return weekdata_hz;
        }

        public void setWeekdata_hz(String weekdata_hz) {
            this.weekdata_hz = weekdata_hz;
        }

        public String getClass_avg_dz() {
            return class_avg_dz;
        }

        public void setClass_avg_dz(String class_avg_dz) {
            this.class_avg_dz = class_avg_dz;
        }

        public String getClass_avg_hz() {
            return class_avg_hz;
        }

        public void setClass_avg_hz(String class_avg_hz) {
            this.class_avg_hz = class_avg_hz;
        }

        public String getClass_student_num() {
            return class_student_num;
        }

        public void setClass_student_num(String class_student_num) {
            this.class_student_num = class_student_num;
        }
    }
}
