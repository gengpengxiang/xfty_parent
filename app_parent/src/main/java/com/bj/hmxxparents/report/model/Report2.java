package com.bj.hmxxparents.report.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/18 0018.
 */

public class Report2 {

    /**
     * ret : 1
     * msg :
     * data : {"hznum":[{"hznum":"1","type":"3","name":"品德","code":"3"},{"hznum":"1","type":"7","name":"人文","code":"7"},{"hznum":"2","type":"4","name":"科学","code":"4"},{"hznum":"1","type":"5","name":"艺术","code":"5"},{"hznum":"1","type":"6","name":"健康","code":"6"},{"hznum":"1","type":"9","name":"实践","code":"9"}],"hzsum":[{"hzsum":"7"}]}
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
        private List<HznumBean> hznum;
        private List<HzsumBean> hzsum;

        public List<HznumBean> getHznum() {
            return hznum;
        }

        public void setHznum(List<HznumBean> hznum) {
            this.hznum = hznum;
        }

        public List<HzsumBean> getHzsum() {
            return hzsum;
        }

        public void setHzsum(List<HzsumBean> hzsum) {
            this.hzsum = hzsum;
        }

        public static class HznumBean {
            /**
             * hznum : 1
             * type : 3
             * name : 品德
             * code : 3
             */

            private String hznum;
            private String type;
            private String name;
            private String code;

            public String getHznum() {
                return hznum;
            }

            public void setHznum(String hznum) {
                this.hznum = hznum;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }

        public static class HzsumBean {
            /**
             * hzsum : 7
             */

            private String hzsum;

            public String getHzsum() {
                return hzsum;
            }

            public void setHzsum(String hzsum) {
                this.hzsum = hzsum;
            }
        }
    }
}
