package com.bj.hmxxparents.countryside.topic.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/23 0023.
 */

public class TopicDetail {


    /**
     * ret : 1
     * msg : 查询成功
     * data : {"tianyuanid":"115","teacher_img":"01342eb49f20c6d87890e034e2132619.jpeg","teacher_name":"米多其","dongtai_del":"0","date":"2019-10-30 18:06","pageview":"22","dianzan_status":0,"dianzannum":"0","commentnum":"1","fenxiangnum":"0","fenxiangurl":"http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=115","content":[{"content":"5rWL6K+V","ordernumber":"1"}],"img":[{"img":"29a27cebe3654de2d09f21dba391089a.jpg","ordernumber":"2"}],"huifu_list":[{"name":"米多其爸爸","img":"01342eb49f20c6d87890e034e2132619.jpeg","time":"2019-10-31 16:40","huifu_content":"5Yqg5rK5"}],"huodong_suyang":[{"tianyuan_id":"115","code":"1","name":"运动健将","huodong_code":"1"}]}
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
         * tianyuanid : 115
         * teacher_img : 01342eb49f20c6d87890e034e2132619.jpeg
         * teacher_name : 米多其
         * dongtai_del : 0
         * date : 2019-10-30 18:06
         * pageview : 22
         * dianzan_status : 0
         * dianzannum : 0
         * commentnum : 1
         * fenxiangnum : 0
         * fenxiangurl : http://testxixin.gamepku.com/index.php/tianyuan_ex/tianyuanfenxiang?tianyuanid=115
         * content : [{"content":"5rWL6K+V","ordernumber":"1"}]
         * img : [{"img":"29a27cebe3654de2d09f21dba391089a.jpg","ordernumber":"2"}]
         * huifu_list : [{"name":"米多其爸爸","img":"01342eb49f20c6d87890e034e2132619.jpeg","time":"2019-10-31 16:40","huifu_content":"5Yqg5rK5"}]
         * huodong_suyang : [{"tianyuan_id":"115","code":"1","name":"运动健将","huodong_code":"1"}]
         */

        private String tianyuanid;
        private String teacher_img;
        private String teacher_name;
        private String dongtai_del;
        private String date;
        private String pageview;
        private int dianzan_status;
        private String dianzannum;
        private String commentnum;
        private String fenxiangnum;
        private String fenxiangurl;
        private List<ContentBean> content;
        private List<ImgBean> img;
        private List<HuifuListBean> huifu_list;
        private List<HuodongSuyangBean> huodong_suyang;

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

        public String getDongtai_del() {
            return dongtai_del;
        }

        public void setDongtai_del(String dongtai_del) {
            this.dongtai_del = dongtai_del;
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

        public List<HuifuListBean> getHuifu_list() {
            return huifu_list;
        }

        public void setHuifu_list(List<HuifuListBean> huifu_list) {
            this.huifu_list = huifu_list;
        }

        public List<HuodongSuyangBean> getHuodong_suyang() {
            return huodong_suyang;
        }

        public void setHuodong_suyang(List<HuodongSuyangBean> huodong_suyang) {
            this.huodong_suyang = huodong_suyang;
        }

        public static class ContentBean {
            /**
             * content : 5rWL6K+V
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
             * img : 29a27cebe3654de2d09f21dba391089a.jpg
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
             * name : 米多其爸爸
             * img : 01342eb49f20c6d87890e034e2132619.jpeg
             * time : 2019-10-31 16:40
             * huifu_content : 5Yqg5rK5
             */

            private String name;
            private String img;
            private String time;
            private String huifu_content;

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

        public static class HuodongSuyangBean {
            /**
             * tianyuan_id : 115
             * code : 1
             * name : 运动健将
             * huodong_code : 1
             */

            private String tianyuan_id;
            private String code;
            private String name;
            private String huodong_code;

            public String getTianyuan_id() {
                return tianyuan_id;
            }

            public void setTianyuan_id(String tianyuan_id) {
                this.tianyuan_id = tianyuan_id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHuodong_code() {
                return huodong_code;
            }

            public void setHuodong_code(String huodong_code) {
                this.huodong_code = huodong_code;
            }
        }
    }
}
