package com.bj.hmxxparents.report.term.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19 0019.
 */

public class Report6 {

    /**
     * ret : 1
     * msg :
     * data : {"huizhang":[{"studentid":"20001","name":"米多其","img":"01342eb49f20c6d87890e034e2132619.jpeg","pm":"1","value":"97","badge":"9"},{"studentid":"20002","name":"哆咪奇","img":"77c85fcf7d2c88fef7abb29be5e22656.jpeg","pm":"2","value":"62","badge":"0"},{"studentid":"20003","name":"奇米多","img":"348cab0ce3ee02ff4e6aff3200d4e580.jpeg","pm":"3","value":"51","badge":"0"}],"dianzan":[{"studentid":"20001","name":"米多其","img":"01342eb49f20c6d87890e034e2132619.jpeg","pm":"1","value":"97","badge":"9"},{"studentid":"20002","name":"哆咪奇","img":"77c85fcf7d2c88fef7abb29be5e22656.jpeg","pm":"2","value":"62","badge":"0"},{"studentid":"20003","name":"奇米多","img":"348cab0ce3ee02ff4e6aff3200d4e580.jpeg","pm":"3","value":"51","badge":"0"}]}
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
        private List<HuizhangBean> huizhang;
        private List<DianzanBean> dianzan;

        public List<HuizhangBean> getHuizhang() {
            return huizhang;
        }

        public void setHuizhang(List<HuizhangBean> huizhang) {
            this.huizhang = huizhang;
        }

        public List<DianzanBean> getDianzan() {
            return dianzan;
        }

        public void setDianzan(List<DianzanBean> dianzan) {
            this.dianzan = dianzan;
        }

        public static class HuizhangBean {
            /**
             * studentid : 20001
             * name : 米多其
             * img : 01342eb49f20c6d87890e034e2132619.jpeg
             * pm : 1
             * value : 97
             * badge : 9
             */

            private String studentid;
            private String name;
            private String img;
            private String pm;
            private String value;
            private String badge;

            public String getStudentid() {
                return studentid;
            }

            public void setStudentid(String studentid) {
                this.studentid = studentid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPm() {
                return pm;
            }

            public void setPm(String pm) {
                this.pm = pm;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }
        }

        public static class DianzanBean {
            /**
             * studentid : 20001
             * name : 米多其
             * img : 01342eb49f20c6d87890e034e2132619.jpeg
             * pm : 1
             * value : 97
             * badge : 9
             */

            private String studentid;
            private String name;
            private String img;
            private String pm;
            private String value;
            private String badge;

            public String getStudentid() {
                return studentid;
            }

            public void setStudentid(String studentid) {
                this.studentid = studentid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getPm() {
                return pm;
            }

            public void setPm(String pm) {
                this.pm = pm;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }
        }
    }
}
