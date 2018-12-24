package com.bj.hmxxparents.report.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/19 0019.
 */

public class Report3 {

    /**
     * ret : 1
     * msg :
     * data : {"dznum":[{"dznum":"55","liyou":"值日认真"},{"dznum":"6","liyou":"友爱同学"},{"dznum":"2","liyou":"团队合作"},{"dznum":"21","liyou":"坐姿端正"},{"dznum":"7","liyou":"尊敬老师"},{"dznum":"5","liyou":"待人礼貌"},{"dznum":"3","liyou":"热心助人"},{"dznum":"1","liyou":"维持卫生"},{"dznum":"1","liyou":"认真听讲"},{"dznum":"3","liyou":"讲究卫生"},{"dznum":"4","liyou":"课堂积极"}],"dzsum":[{"dzsum":"108"}]}
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
             * dznum : 55
             * liyou : 值日认真
             */

            private String dznum;
            private String liyou;

            private int color;

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

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
             * dzsum : 108
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
