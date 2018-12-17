package com.bj.hmxxparents.countryside.topic.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/23 0023.
 */

public class TopicDetail {


    /**
     * ret : 1
     * msg : 查询成功
     * data : {"tianyuanid":"3","teacher_img":"bd286adc65af02d6b474564831eaa09a.jpeg","teacher_name":"159","teacher_type":"2","date":"2018-11-22 17:50:47","pageview":"4","dianzan_status":1,"dianzannum":"1","commentnum":"2","fenxiangnum":"0","content":[{"content":"1111","ordernumber":"1"}],"img":[{"img":"1111.png","ordernumber":"2"}],"huifu_list":[{"name":"幸福博士","time":"2018-11-22 18:27:21","huifu_content":"wwwwww"},{"name":"幸福博士","time":"2018-11-22 18:06:40","huifu_content":"wwwwww"}]}
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
         * tianyuanid : 3
         * teacher_img : bd286adc65af02d6b474564831eaa09a.jpeg
         * teacher_name : 159
         * teacher_type : 2
         * date : 2018-11-22 17:50:47
         * pageview : 4
         * dianzan_status : 1
         * dianzannum : 1
         * commentnum : 2
         * fenxiangnum : 0
         * fenxiangurl: ""
         * dongtai_del: ""
         * content : [{"content":"1111","ordernumber":"1"}]
         * img : [{"img":"1111.png","ordernumber":"2"}]
         * huifu_list : [{"name":"幸福博士","time":"2018-11-22 18:27:21","huifu_content":"wwwwww"},{"name":"幸福博士","time":"2018-11-22 18:06:40","huifu_content":"wwwwww"}]
         */

        private String tianyuanid;
        private String teacher_img;
        private String teacher_name;
        private String teacher_type;
        private String date;
        private String pageview;
        private int dianzan_status;
        private String dianzannum;
        private String commentnum;
        private String fenxiangnum;
        private String fenxiangurl;
        private String dongtai_del;
        private List<ContentBean> content;
        private List<ImgBean> img;
        private List<HuifuListBean> huifu_list;

        public String getFenxiangurl() {
            return fenxiangurl;
        }

        public String getDongtai_del() {
            return dongtai_del;
        }

        public void setDongtai_del(String dongtai_del) {
            this.dongtai_del = dongtai_del;
        }

        public void setFenxiangurl(String fenxiangurl) {
            this.fenxiangurl = fenxiangurl;
        }

        public String getTianyuanid() {
            return tianyuanid;
        }

        public void setTianyuanid(String tianyuanid) {
            this.tianyuanid = tianyuanid;
        }

        public String getTeacher_img() {
            return teacher_img;
        }

        public void setTeacher_img(String teacher_img) {
            this.teacher_img = teacher_img;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_type() {
            return teacher_type;
        }

        public void setTeacher_type(String teacher_type) {
            this.teacher_type = teacher_type;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPageview() {
            return pageview;
        }

        public void setPageview(String pageview) {
            this.pageview = pageview;
        }

        public int getDianzan_status() {
            return dianzan_status;
        }

        public void setDianzan_status(int dianzan_status) {
            this.dianzan_status = dianzan_status;
        }

        public String getDianzannum() {
            return dianzannum;
        }

        public void setDianzannum(String dianzannum) {
            this.dianzannum = dianzannum;
        }

        public String getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(String commentnum) {
            this.commentnum = commentnum;
        }

        public String getFenxiangnum() {
            return fenxiangnum;
        }

        public void setFenxiangnum(String fenxiangnum) {
            this.fenxiangnum = fenxiangnum;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public List<ImgBean> getImg() {
            return img;
        }

        public void setImg(List<ImgBean> img) {
            this.img = img;
        }

        public List<HuifuListBean> getHuifu_list() {
            return huifu_list;
        }

        public void setHuifu_list(List<HuifuListBean> huifu_list) {
            this.huifu_list = huifu_list;
        }

        public static class ContentBean {
            /**
             * content : 1111
             * ordernumber : 1
             */

            private String content;
            private String ordernumber;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getOrdernumber() {
                return ordernumber;
            }

            public void setOrdernumber(String ordernumber) {
                this.ordernumber = ordernumber;
            }
        }

        public static class ImgBean {
            /**
             * img : 1111.png
             * ordernumber : 2
             */

            private String img;
            private String ordernumber;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getOrdernumber() {
                return ordernumber;
            }

            public void setOrdernumber(String ordernumber) {
                this.ordernumber = ordernumber;
            }
        }

        public static class HuifuListBean {
            /**
             * name : 幸福博士
             * time : 2018-11-22 18:27:21
             * img:"
             * huifu_content : wwwwww
             */

            private String name;
            private String time;
            private String img;
            private String huifu_content;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getHuifu_content() {
                return huifu_content;
            }

            public void setHuifu_content(String huifu_content) {
                this.huifu_content = huifu_content;
            }
        }
    }
}
