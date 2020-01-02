package com.bj.hmxxparents.countryside.topic.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22 0022.
 */

public class Topic {

    /**
     * ret : 1
     * msg : 查询成功
     * data : [{"tianyuanid":"6","teacher_img":"bd286adc65af02d6b474564831eaa09a.jpeg","teacher_name":"159","time":"11-23 15:50","pageview":"0","dianzan_status":0,"dianzannum":"2","commentnum":"0","fenxiangnum":"0","fenxiangurl":"http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=6","content":[{"content":"1111","ordernumber":"1"}],"img":[{"img":"bd286adc65af02d6b474564831eaa09a.jpeg","ordernumber":"2"}]},{"tianyuanid":"4","teacher_img":"bd286adc65af02d6b474564831eaa09a.jpeg","teacher_name":"159","time":"11-22 17:51","pageview":"6","dianzan_status":0,"dianzannum":"0","commentnum":"0","fenxiangnum":"0","fenxiangurl":"http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=4","content":[{"content":"1111","ordernumber":"1"}],"img":[{"img":"bd286adc65af02d6b474564831eaa09a.jpeg","ordernumber":"2"},{"img":"bd286adc65af02d6b474564831eaa09a.jpeg","ordernumber":"3"}]},{"tianyuanid":"3","teacher_img":"bd286adc65af02d6b474564831eaa09a.jpeg","teacher_name":"159","time":"11-22 17:50","pageview":"4","dianzan_status":0,"dianzannum":"0","commentnum":"4","fenxiangnum":"6","fenxiangurl":"http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=3","content":[{"content":"1111","ordernumber":"1"}],"img":[{"img":"bd286adc65af02d6b474564831eaa09a.jpeg","ordernumber":"2"}]}]
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
         * tianyuanid : 6
         * "status": "0",
         * teacher_img : bd286adc65af02d6b474564831eaa09a.jpeg
         * teacher_name : 159
         * time : 11-23 15:50
         * pageview : 0
         * dianzan_status : 0
         * dianzannum : 2
         * commentnum : 0
         * fenxiangnum : 0
         * fenxiangurl : http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=6
         * content : [{"content":"1111","ordernumber":"1"}]
         * img : [{"img":"bd286adc65af02d6b474564831eaa09a.jpeg","ordernumber":"2"}]
         */

        private String tianyuanid;
        private String status;
        private String teacher_img;
        private String teacher_name;
        private String time;
        private String pageview;
        private int dianzan_status;
        private String dianzannum;
        private String commentnum;
        private String fenxiangnum;
        private String fenxiangurl;
        private List<ContentBean> content;
        private List<ImgBean> img;

        public String getTianyuanid() {
            return tianyuanid;
        }

        public void setTianyuanid(String tianyuanid) {
            this.tianyuanid = tianyuanid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public String getFenxiangurl() {
            return fenxiangurl;
        }

        public void setFenxiangurl(String fenxiangurl) {
            this.fenxiangurl = fenxiangurl;
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
             * img : bd286adc65af02d6b474564831eaa09a.jpeg
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
    }
}
