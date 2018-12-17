package com.bj.hmxxparents.countryside.honorroll.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/18 0018.
 */

public class Honor {

    /**
     * ret : 1
     * msg : 查询成功
     * data : [{"fenlei_img":"sipin.png","class_name":"一年级1班","time":"11.11-11.18","student_pm":[{"id":"1","phcode":"1","paiming":"1","huizhangnum":"99","student_code":"10001","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"鲍一诺","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"2","phcode":"1","paiming":"2","huizhangnum":"98","student_code":"10002","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"常依辰","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"3","phcode":"1","paiming":"3","huizhangnum":"97","student_code":"10003","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"崔熙媛","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"4","phcode":"1","paiming":"4","huizhangnum":"96","student_code":"10004","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"单伟伦","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"5","phcode":"1","paiming":"5","huizhangnum":"95","student_code":"10005","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"邓安琪","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"6","phcode":"1","paiming":"6","huizhangnum":"94","student_code":"10006","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"刁由明","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"7","phcode":"1","paiming":"7","huizhangnum":"93","student_code":"10007","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"段之涵","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"8","phcode":"1","paiming":"8","huizhangnum":"92","student_code":"10008","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"樊震嘉","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"}]},{"fenlei_img":"keji.png","class_name":"一年级1班","time":"11.11-11.18","student_pm":[{"id":"9","phcode":"2","paiming":"1","huizhangnum":"99","student_code":"10009","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"郭晨宇","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"10","phcode":"2","paiming":"2","huizhangnum":"98","student_code":"10010","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"胡博涵","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"11","phcode":"2","paiming":"3","huizhangnum":"97","student_code":"10011","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"黄俊泽","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"12","phcode":"2","paiming":"4","huizhangnum":"96","student_code":"10012","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"黄馨钰","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"13","phcode":"2","paiming":"5","huizhangnum":"95","student_code":"10013","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"贾凯媛","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"14","phcode":"2","paiming":"6","huizhangnum":"94","student_code":"10014","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"李安雅","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"15","phcode":"2","paiming":"7","huizhangnum":"93","student_code":"10015","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"李美佳","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"16","phcode":"2","paiming":"8","huizhangnum":"92","student_code":"10016","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"李玥彤","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"}]}]
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
         * fenlei_img : sipin.png
         * class_name : 一年级1班
         * time : 11.11-11.18
         * student_pm : [{"id":"1","phcode":"1","paiming":"1","huizhangnum":"99","student_code":"10001","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"鲍一诺","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"2","phcode":"1","paiming":"2","huizhangnum":"98","student_code":"10002","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"常依辰","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"3","phcode":"1","paiming":"3","huizhangnum":"97","student_code":"10003","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"崔熙媛","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"4","phcode":"1","paiming":"4","huizhangnum":"96","student_code":"10004","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"单伟伦","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"5","phcode":"1","paiming":"5","huizhangnum":"95","student_code":"10005","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"邓安琪","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"6","phcode":"1","paiming":"6","huizhangnum":"94","student_code":"10006","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"刁由明","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"7","phcode":"1","paiming":"7","huizhangnum":"93","student_code":"10007","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"段之涵","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"},{"id":"8","phcode":"1","paiming":"8","huizhangnum":"92","student_code":"10008","img":"f3d25d61873edf630296b7ba54a70367.JPEG","student_name":"樊震嘉","updatetime":"2018-11-18 11:49:47","createtime":"2018-11-18 11:49:47"}]
         */

        private String fenlei_img;
        private String class_name;
        private String time;
        private List<StudentPmBean> student_pm;

        public String getFenlei_img() {
            return fenlei_img;
        }

        public void setFenlei_img(String fenlei_img) {
            this.fenlei_img = fenlei_img;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<StudentPmBean> getStudent_pm() {
            return student_pm;
        }

        public void setStudent_pm(List<StudentPmBean> student_pm) {
            this.student_pm = student_pm;
        }

        public static class StudentPmBean {
            /**
             * id : 1
             * phcode : 1
             * paiming : 1
             * huizhangnum : 99
             * student_code : 10001
             * img : f3d25d61873edf630296b7ba54a70367.JPEG
             * student_name : 鲍一诺
             * updatetime : 2018-11-18 11:49:47
             * createtime : 2018-11-18 11:49:47
             */

            private String id;
            private String phcode;
            private String paiming;
            private String huizhangnum;
            private String student_code;
            private String img;
            private String student_name;
            private String updatetime;
            private String createtime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhcode() {
                return phcode;
            }

            public void setPhcode(String phcode) {
                this.phcode = phcode;
            }

            public String getPaiming() {
                return paiming;
            }

            public void setPaiming(String paiming) {
                this.paiming = paiming;
            }

            public String getHuizhangnum() {
                return huizhangnum;
            }

            public void setHuizhangnum(String huizhangnum) {
                this.huizhangnum = huizhangnum;
            }

            public String getStudent_code() {
                return student_code;
            }

            public void setStudent_code(String student_code) {
                this.student_code = student_code;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }
        }
    }
}
