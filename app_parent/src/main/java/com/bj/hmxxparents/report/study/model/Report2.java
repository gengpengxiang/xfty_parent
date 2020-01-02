package com.bj.hmxxparents.report.study.model;

import java.util.List;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public class Report2 {

    /**
     * ret : 1
     * msg :
     * data : {"dengji":[{"code":"A","fanwei":{"max":"100","min":"99"}},{"code":"B","fanwei":{"max":"99","min":"85"}},{"code":"C","fanwei":{"max":"85","min":"60"}},{"code":"D","fanwei":{"max":"60","min":"0"}}],"fenxi":[{"exam_code":20180901,"exam_name":"第一次月考","avg_yuwen":"75.70","max_yuwen":"95","yuwen":"0"},{"exam_code":20181109,"exam_name":"第二次月考","avg_yuwen":"90.28","max_yuwen":"98","yuwen":"75"}]}
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
        private List<DengjiBean> dengji;
        private List<FenxiBean> fenxi;

        public List<DengjiBean> getDengji() {
            return dengji;
        }

        public void setDengji(List<DengjiBean> dengji) {
            this.dengji = dengji;
        }

        public List<FenxiBean> getFenxi() {
            return fenxi;
        }

        public void setFenxi(List<FenxiBean> fenxi) {
            this.fenxi = fenxi;
        }

        public static class DengjiBean {
            /**
             * code : A
             * fanwei : {"max":"100","min":"99"}
             */

            private String code;
            private FanweiBean fanwei;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public FanweiBean getFanwei() {
                return fanwei;
            }

            public void setFanwei(FanweiBean fanwei) {
                this.fanwei = fanwei;
            }

            public static class FanweiBean {
                /**
                 * max : 100
                 * min : 99
                 */

                private String max;
                private String min;

                public String getMax() {
                    return max;
                }

                public void setMax(String max) {
                    this.max = max;
                }

                public String getMin() {
                    return min;
                }

                public void setMin(String min) {
                    this.min = min;
                }
            }
        }

        public static class FenxiBean {
            /**
             * exam_code : 20180901
             * exam_name : 第一次月考
             * avg_yuwen : 75.70
             * max_yuwen : 95
             * yuwen : 0
             */

            private int exam_code;
            private String exam_name;
            private String avg_yuwen;
            private String max_yuwen;
            private String yuwen;

            public int getExam_code() {
                return exam_code;
            }

            public void setExam_code(int exam_code) {
                this.exam_code = exam_code;
            }

            public String getExam_name() {
                return exam_name;
            }

            public void setExam_name(String exam_name) {
                this.exam_name = exam_name;
            }

            public String getAvg_yuwen() {
                return avg_yuwen;
            }

            public void setAvg_yuwen(String avg_yuwen) {
                this.avg_yuwen = avg_yuwen;
            }

            public String getMax_yuwen() {
                return max_yuwen;
            }

            public void setMax_yuwen(String max_yuwen) {
                this.max_yuwen = max_yuwen;
            }

            public String getYuwen() {
                return yuwen;
            }

            public void setYuwen(String yuwen) {
                this.yuwen = yuwen;
            }
        }
    }
}
