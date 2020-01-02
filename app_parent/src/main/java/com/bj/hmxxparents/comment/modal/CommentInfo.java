package com.bj.hmxxparents.comment.modal;

import java.util.List;

/**
 * Created by gengpx on 2019/10/24.
 */

public class CommentInfo {

    /**
     * ret : 1
     * msg :
     * data : [{"dongtai_id":"51510","dongtai_pic":"z1.png","dongtai_type":"z1","dongtai_title":"值日认真","dongtai_time":"2019-08-30","dongtai_ganxiestatus":"3","teacher_pic":{"id":"19","phone":"18988888888","name":"幸福博士","phoneyzm":"123456","updatetime":"2019-08-30 13:57:39","img":"10e9cb69c7fee488bb3610c9aed4eed2.JPEG","xueke":"02","schoolcode":"1","youke":"2","replyblack":"","nicheng":"","type":"2","xinxiang":"1","pingbi":"2","xueqi_code":"0"},"student_pic":"aa3709bc8344ecf43ca542fd7f8309f9.jpeg","student_name":"米多其","student_pid":"20001","teacher_name":"幸福博士","teacher_phone":"18988888888","jiazhang_phone":"18977777777"}]
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
         * dongtai_id : 51510
         * dongtai_pic : z1.png
         * dongtai_type : z1
         * dongtai_title : 值日认真
         * dongtai_time : 2019-08-30
         * dongtai_ganxiestatus : 3
         * teacher_pic : {"id":"19","phone":"18988888888","name":"幸福博士","phoneyzm":"123456","updatetime":"2019-08-30 13:57:39","img":"10e9cb69c7fee488bb3610c9aed4eed2.JPEG","xueke":"02","schoolcode":"1","youke":"2","replyblack":"","nicheng":"","type":"2","xinxiang":"1","pingbi":"2","xueqi_code":"0"}
         * student_pic : aa3709bc8344ecf43ca542fd7f8309f9.jpeg
         * student_name : 米多其
         * student_pid : 20001
         * teacher_name : 幸福博士
         * teacher_phone : 18988888888
         * jiazhang_phone : 18977777777
         */

        private String dongtai_id;
        private String dongtai_pic;
        private String dongtai_type;
        private String dongtai_title;
        private String dongtai_time;
        private String dongtai_ganxiestatus;
        private TeacherPicBean teacher_pic;
        private String student_pic;
        private String student_name;
        private String student_pid;
        private String teacher_name;
        private String teacher_phone;
        private String jiazhang_phone;

        public String getDongtai_id() {
            return dongtai_id;
        }

        public void setDongtai_id(String dongtai_id) {
            this.dongtai_id = dongtai_id;
        }

        public String getDongtai_pic() {
            return dongtai_pic;
        }

        public void setDongtai_pic(String dongtai_pic) {
            this.dongtai_pic = dongtai_pic;
        }

        public String getDongtai_type() {
            return dongtai_type;
        }

        public void setDongtai_type(String dongtai_type) {
            this.dongtai_type = dongtai_type;
        }

        public String getDongtai_title() {
            return dongtai_title;
        }

        public void setDongtai_title(String dongtai_title) {
            this.dongtai_title = dongtai_title;
        }

        public String getDongtai_time() {
            return dongtai_time;
        }

        public void setDongtai_time(String dongtai_time) {
            this.dongtai_time = dongtai_time;
        }

        public String getDongtai_ganxiestatus() {
            return dongtai_ganxiestatus;
        }

        public void setDongtai_ganxiestatus(String dongtai_ganxiestatus) {
            this.dongtai_ganxiestatus = dongtai_ganxiestatus;
        }

        public TeacherPicBean getTeacher_pic() {
            return teacher_pic;
        }

        public void setTeacher_pic(TeacherPicBean teacher_pic) {
            this.teacher_pic = teacher_pic;
        }

        public String getStudent_pic() {
            return student_pic;
        }

        public void setStudent_pic(String student_pic) {
            this.student_pic = student_pic;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStudent_pid() {
            return student_pid;
        }

        public void setStudent_pid(String student_pid) {
            this.student_pid = student_pid;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_phone() {
            return teacher_phone;
        }

        public void setTeacher_phone(String teacher_phone) {
            this.teacher_phone = teacher_phone;
        }

        public String getJiazhang_phone() {
            return jiazhang_phone;
        }

        public void setJiazhang_phone(String jiazhang_phone) {
            this.jiazhang_phone = jiazhang_phone;
        }

        public static class TeacherPicBean {
            /**
             * id : 19
             * phone : 18988888888
             * name : 幸福博士
             * phoneyzm : 123456
             * updatetime : 2019-08-30 13:57:39
             * img : 10e9cb69c7fee488bb3610c9aed4eed2.JPEG
             * xueke : 02
             * schoolcode : 1
             * youke : 2
             * replyblack :
             * nicheng :
             * type : 2
             * xinxiang : 1
             * pingbi : 2
             * xueqi_code : 0
             */

            private String id;
            private String phone;
            private String name;
            private String phoneyzm;
            private String updatetime;
            private String img;
            private String xueke;
            private String schoolcode;
            private String youke;
            private String replyblack;
            private String nicheng;
            private String type;
            private String xinxiang;
            private String pingbi;
            private String xueqi_code;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoneyzm() {
                return phoneyzm;
            }

            public void setPhoneyzm(String phoneyzm) {
                this.phoneyzm = phoneyzm;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getXueke() {
                return xueke;
            }

            public void setXueke(String xueke) {
                this.xueke = xueke;
            }

            public String getSchoolcode() {
                return schoolcode;
            }

            public void setSchoolcode(String schoolcode) {
                this.schoolcode = schoolcode;
            }

            public String getYouke() {
                return youke;
            }

            public void setYouke(String youke) {
                this.youke = youke;
            }

            public String getReplyblack() {
                return replyblack;
            }

            public void setReplyblack(String replyblack) {
                this.replyblack = replyblack;
            }

            public String getNicheng() {
                return nicheng;
            }

            public void setNicheng(String nicheng) {
                this.nicheng = nicheng;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getXinxiang() {
                return xinxiang;
            }

            public void setXinxiang(String xinxiang) {
                this.xinxiang = xinxiang;
            }

            public String getPingbi() {
                return pingbi;
            }

            public void setPingbi(String pingbi) {
                this.pingbi = pingbi;
            }

            public String getXueqi_code() {
                return xueqi_code;
            }

            public void setXueqi_code(String xueqi_code) {
                this.xueqi_code = xueqi_code;
            }
        }
    }
}
