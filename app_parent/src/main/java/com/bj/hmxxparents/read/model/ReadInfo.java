package com.bj.hmxxparents.read.model;

import java.util.List;

/**
 * Created by Administrator on 2019/1/7 0007.
 */

public class ReadInfo {

    /**
     * ret : 1
     * msg : 成功
     * data : {"student_code":"20001","student_name":"米多其","student_img":"01342eb49f20c6d87890e034e2132619.jpeg","fengmi":"0.4","huizhang":"0","qiandao":"5","qiandao_status":"0","city":[{"code":"11","name":"宜宾","img":"yuedu/c9.png","status":"1","suo":"0"},{"code":"10","name":"重庆","img":"yuedu/c10.png","status":"1","suo":"0"},{"code":"9","name":"宜昌","img":"yuedu/c9.png","status":"1","suo":"0"},{"code":"8","name":"荆州","img":"yuedu/c8.png","status":"1","suo":"0"},{"code":"7","name":"岳阳","img":"yuedu/c7.png","status":"1","suo":"0"},{"code":"6","name":"武汉","img":"yuedu/c6.png","status":"1","suo":"0"},{"code":"5","name":"九江","img":"yuedu/c5.png","status":"1","suo":"0"},{"code":"4","name":"安庆","img":"yuedu/c4.png","status":"1","suo":"0"},{"code":"3","name":"南京","img":"yuedu/c3_wuhan.png","status":"1","suo":"0"},{"code":"2","name":"镇江","img":"yuedu/c2_nanjing.png","status":"1","suo":"0"},{"code":"1","name":"上海","img":"yuedu/c1_shanghai.png","status":"1","suo":"0"}]}
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
         * student_img : 01342eb49f20c6d87890e034e2132619.jpeg
         * fengmi : 0.4
         * "fengmi_num": "100"
         * huizhang : 0
         * qiandao : 5
         * qiandao_status : 0
         * "lujin_code": "2",
         * guize:"""
         * city : [{"code":"11","name":"宜宾","img":"yuedu/c9.png","status":"1","suo":"0"},{"code":"10","name":"重庆","img":"yuedu/c10.png","status":"1","suo":"0"},{"code":"9","name":"宜昌","img":"yuedu/c9.png","status":"1","suo":"0"},{"code":"8","name":"荆州","img":"yuedu/c8.png","status":"1","suo":"0"},{"code":"7","name":"岳阳","img":"yuedu/c7.png","status":"1","suo":"0"},{"code":"6","name":"武汉","img":"yuedu/c6.png","status":"1","suo":"0"},{"code":"5","name":"九江","img":"yuedu/c5.png","status":"1","suo":"0"},{"code":"4","name":"安庆","img":"yuedu/c4.png","status":"1","suo":"0"},{"code":"3","name":"南京","img":"yuedu/c3_wuhan.png","status":"1","suo":"0"},{"code":"2","name":"镇江","img":"yuedu/c2_nanjing.png","status":"1","suo":"0"},{"code":"1","name":"上海","img":"yuedu/c1_shanghai.png","status":"1","suo":"0"}]
         */

        private String student_code;
        private String student_name;
        private String student_img;
        private String fengmi;
        private String huizhang;
        private String qiandao;
        private String qiandao_status;
        private List<CityBean> city;

        private String lujin_code;
        private String fengmi_num;
        private String guize;

        public String getGuize() {
            return guize;
        }

        public void setGuize(String guize) {
            this.guize = guize;
        }

        public String getFengmi_num() {
            return fengmi_num;
        }

        public void setFengmi_num(String fengmi_num) {
            this.fengmi_num = fengmi_num;
        }

        public String getLujin_code() {
            return lujin_code;
        }

        public void setLujin_code(String lujin_code) {
            this.lujin_code = lujin_code;
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

        public String getFengmi() {
            return fengmi;
        }

        public void setFengmi(String fengmi) {
            this.fengmi = fengmi;
        }

        public String getHuizhang() {
            return huizhang;
        }

        public void setHuizhang(String huizhang) {
            this.huizhang = huizhang;
        }

        public String getQiandao() {
            return qiandao;
        }

        public void setQiandao(String qiandao) {
            this.qiandao = qiandao;
        }

        public String getQiandao_status() {
            return qiandao_status;
        }

        public void setQiandao_status(String qiandao_status) {
            this.qiandao_status = qiandao_status;
        }

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }

        public static class CityBean {
            /**
             * code : 11
             * name : 宜宾
             * img : yuedu/c9.png
             * status : 1
             * suo : 0
             */

            private String code;
            private String name;
            private String img;
            private String status;
            private String suo;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSuo() {
                return suo;
            }

            public void setSuo(String suo) {
                this.suo = suo;
            }
        }
    }
}
