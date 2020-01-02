package com.bj.hmxxparents.report.term.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19 0019.
 */

public class Report4 {

    /**
     * ret : 1
     * msg :
     * data : {"dznum":[{"dznum":"1","liyou":"铺张浪费"}],"dzsum":[{"dzsum":"1"}]}
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
        private List<DznumBean> dznum;
        private List<DzsumBean> dzsum;

        public List<DznumBean> getDznum() {
            return dznum;
        }

        public void setDznum(List<DznumBean> dznum) {
            this.dznum = dznum;
        }

        public List<DzsumBean> getDzsum() {
            return dzsum;
        }

        public void setDzsum(List<DzsumBean> dzsum) {
            this.dzsum = dzsum;
        }

        public static class DznumBean {
            /**
             * dznum : 1
             * liyou : 铺张浪费
             */

            private String dznum;
            private String liyou;

            public String getDznum() {
                return dznum;
            }

            public void setDznum(String dznum) {
                this.dznum = dznum;
            }

            public String getLiyou() {
                return liyou;
            }

            public void setLiyou(String liyou) {
                this.liyou = liyou;
            }
        }

        public static class DzsumBean {
            /**
             * dzsum : 1
             */

            private String dzsum;

            public String getDzsum() {
                return dzsum;
            }

            public void setDzsum(String dzsum) {
                this.dzsum = dzsum;
            }
        }
    }
}
