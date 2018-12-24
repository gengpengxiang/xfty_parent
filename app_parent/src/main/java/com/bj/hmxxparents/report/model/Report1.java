package com.bj.hmxxparents.report.model;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class Report1 {


    /**
     * ret : 1
     * msg :
     * data : {"student_hz_dz":{"studentid":"20001","name":"米多其","pid":"20001","img":"01342eb49f20c6d87890e034e2132619.jpeg","bpm":"1","dpm":"1","value":"107","badge":"2","sum":"2","classcode":"2101"},"class_avg":{"davg":"81.0000","bavg":"0.6667","dmax":"107","bmax":"2","classcode":"2101"}}
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
         * student_hz_dz : {"studentid":"20001","name":"米多其","pid":"20001","img":"01342eb49f20c6d87890e034e2132619.jpeg","bpm":"1","dpm":"1","value":"107","badge":"2","sum":"2","classcode":"2101"}
         * class_avg : {"davg":"81.0000","bavg":"0.6667","dmax":"107","bmax":"2","classcode":"2101"}
         */

        private StudentHzDzBean student_hz_dz;
        private ClassAvgBean class_avg;

        public StudentHzDzBean getStudent_hz_dz() {
            return student_hz_dz;
        }

        public void setStudent_hz_dz(StudentHzDzBean student_hz_dz) {
            this.student_hz_dz = student_hz_dz;
        }

        public ClassAvgBean getClass_avg() {
            return class_avg;
        }

        public void setClass_avg(ClassAvgBean class_avg) {
            this.class_avg = class_avg;
        }

        public static class StudentHzDzBean {
            /**
             * studentid : 20001
             * name : 米多其
             * pid : 20001
             * img : 01342eb49f20c6d87890e034e2132619.jpeg
             * bpm : 1
             * dpm : 1
             * value : 107
             * badge : 2
             * sum : 2
             * classcode : 2101
             */

            private String studentid;
            private String name;
            private String pid;
            private String img;
            private String bpm;
            private String dpm;
            private String value;
            private String badge;
            private String sum;
            private String classcode;

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

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getBpm() {
                return bpm;
            }

            public void setBpm(String bpm) {
                this.bpm = bpm;
            }

            public String getDpm() {
                return dpm;
            }

            public void setDpm(String dpm) {
                this.dpm = dpm;
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

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getClasscode() {
                return classcode;
            }

            public void setClasscode(String classcode) {
                this.classcode = classcode;
            }
        }

        public static class ClassAvgBean {
            /**
             * davg : 81.0000
             * bavg : 0.6667
             * dmax : 107
             * bmax : 2
             * classcode : 2101
             */

            private String davg;
            private String bavg;
            private String dmax;
            private String bmax;
            private String classcode;

            public String getDavg() {
                return davg;
            }

            public void setDavg(String davg) {
                this.davg = davg;
            }

            public String getBavg() {
                return bavg;
            }

            public void setBavg(String bavg) {
                this.bavg = bavg;
            }

            public String getDmax() {
                return dmax;
            }

            public void setDmax(String dmax) {
                this.dmax = dmax;
            }

            public String getBmax() {
                return bmax;
            }

            public void setBmax(String bmax) {
                this.bmax = bmax;
            }

            public String getClasscode() {
                return classcode;
            }

            public void setClasscode(String classcode) {
                this.classcode = classcode;
            }
        }
    }
}
