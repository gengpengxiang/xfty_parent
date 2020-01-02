package com.bj.hmxxparents.read.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/6 0006.
 */

public class SignRecord {


    /**
     * ret : 1
     * msg : 查询成功
     * data : {"qiandao_date":["4","5","6","7","8"],"qiandao_date2":["2019-01-04","2019-01-05","2019-01-06","2019-01-07","2019-01-08"],"qiandao_data":[{"student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","qiandao":"1","createtime":"2019-01-04 10:00:17","title":"小王子","num":"999字","time":"30分钟"},{"student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","qiandao":"2","createtime":"2019-01-05 10:00:17","title":"小王子","num":"1111字","time":"90分钟"},{"student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","qiandao":"3","createtime":"2019-01-06 10:00:17","title":"小王子","num":"888字","time":"60分钟"},{"student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","qiandao":"4","createtime":"2019-01-07 15:13:12","title":"小王子","num":"3333字","time":"120分钟"},{"student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","qiandao":"5","createtime":"2019-01-08 09:13:01","title":"嗯嗯","num":"嗯嗯","time":"你"}]}
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
        private List<String> qiandao_date;
        private List<String> qiandao_date2;
        private List<QiandaoDataBean> qiandao_data;

        public List<String> getQiandao_date() {
            return qiandao_date;
        }

        public void setQiandao_date(List<String> qiandao_date) {
            this.qiandao_date = qiandao_date;
        }

        public List<String> getQiandao_date2() {
            return qiandao_date2;
        }

        public void setQiandao_date2(List<String> qiandao_date2) {
            this.qiandao_date2 = qiandao_date2;
        }

        public List<QiandaoDataBean> getQiandao_data() {
            return qiandao_data;
        }

        public void setQiandao_data(List<QiandaoDataBean> qiandao_data) {
            this.qiandao_data = qiandao_data;
        }

        public static class QiandaoDataBean {
            /**
             * student_name : 米多其
             * student_img : 01342eb49f20c6d87890e034e2132619.jpeg
             * qiandao : 1
             * createtime : 2019-01-04 10:00:17
             * title : 小王子
             * num : 999字
             * time : 30分钟
             */

            private String student_name;
            private String student_img;
            private String qiandao;
            private String createtime;
            private String title;
            private String num;
            private String time;

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

            public String getQiandao() {
                return qiandao;
            }

            public void setQiandao(String qiandao) {
                this.qiandao = qiandao;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
