package com.bj.hmxxparents.entity;

/**
 * Created by Administrator on 2018/11/21 0021.
 */

public class StudentInfos {

    /**
     * ret : 1
     * msg :
     * data : {"student_code":"20001","student_name":"米多其","student_img":"5fa376403e42b0fa724c9de2f261088c.jpeg","class_code":"2101","class_name":"幸福1班","schoolcode":"2","schoolname":"幸福学校","schoolimg":"","student_xingbie":"1","student_shengri":"2018-11-04","tianyuan":"0"}
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
         * student_code : 20001
         * student_name : 米多其
         * student_img : 5fa376403e42b0fa724c9de2f261088c.jpeg
         * class_code : 2101
         * class_name : 幸福1班
         * schoolcode : 2
         * schoolname : 幸福学校
         * schoolimg :
         * student_xingbie : 1
         * student_shengri : 2018-11-04
         * tianyuan : 0
         * "xinxiang_status": "1"
         */

        private String student_code;
        private String student_name;
        private String student_img;
        private String class_code;
        private String class_name;
        private String schoolcode;
        private String schoolname;
        private String schoolimg;
        private String student_xingbie;
        private String student_shengri;
        private String tianyuan;
        private String xinxiang_status;

        public String getXinxiang_status() {
            return xinxiang_status;
        }

        public void setXinxiang_status(String xinxiang_status) {
            this.xinxiang_status = xinxiang_status;
        }

        public String getStudent_code() {
            return student_code;
        }

        public void setStudent_code(String student_code) {
            this.student_code = student_code;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStudent_img() {
            return student_img;
        }

        public void setStudent_img(String student_img) {
            this.student_img = student_img;
        }

        public String getClass_code() {
            return class_code;
        }

        public void setClass_code(String class_code) {
            this.class_code = class_code;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getSchoolcode() {
            return schoolcode;
        }

        public void setSchoolcode(String schoolcode) {
            this.schoolcode = schoolcode;
        }

        public String getSchoolname() {
            return schoolname;
        }

        public void setSchoolname(String schoolname) {
            this.schoolname = schoolname;
        }

        public String getSchoolimg() {
            return schoolimg;
        }

        public void setSchoolimg(String schoolimg) {
            this.schoolimg = schoolimg;
        }

        public String getStudent_xingbie() {
            return student_xingbie;
        }

        public void setStudent_xingbie(String student_xingbie) {
            this.student_xingbie = student_xingbie;
        }

        public String getStudent_shengri() {
            return student_shengri;
        }

        public void setStudent_shengri(String student_shengri) {
            this.student_shengri = student_shengri;
        }

        public String getTianyuan() {
            return tianyuan;
        }

        public void setTianyuan(String tianyuan) {
            this.tianyuan = tianyuan;
        }
    }
}
