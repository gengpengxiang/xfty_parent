package com.bj.hmxxparents.report.study.model;

import java.util.List;

public class Report1 {

    /**
     * ret : 1
     * msg :
     * data : {"dengji":[{"code":"A","fanwei":{"max":"200","min":"199"}},{"code":"B","fanwei":{"max":"199","min":"170"}},{"code":"C","fanwei":{"max":"170","min":"120"}},{"code":"D","fanwei":{"max":"120","min":"0"}}],"fenxi":[{"exam_code":20180901,"exam_name":"第一次月考","avg_zongfen":"161.08","max_zongfen":"194","zongfen":"0"},{"exam_code":20181109,"exam_name":"第二次月考","avg_zongfen":"183.23","max_zongfen":"196","zongfen":"149"},{"exam_code":1,"exam_name":"第三次月考","avg_zongfen":"0","max_zongfen":"0","zongfen":"0"},{"exam_code":2,"exam_name":"第四次月考","avg_zongfen":"0","max_zongfen":"0","zongfen":"0"}]}
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
             * fanwei : {"max":"200","min":"199"}
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
                 * max : 200
                 * min : 199
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
             * avg_zongfen : 161.08
             * max_zongfen : 194
             * zongfen : 0
             */

            private int exam_code;
            private String exam_name;
            private String avg_zongfen;
            private String max_zongfen;
            private String zongfen;

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

            public String getAvg_zongfen() {
                return avg_zongfen;
            }

            public void setAvg_zongfen(String avg_zongfen) {
                this.avg_zongfen = avg_zongfen;
            }

            public String getMax_zongfen() {
                return max_zongfen;
            }

            public void setMax_zongfen(String max_zongfen) {
                this.max_zongfen = max_zongfen;
            }

            public String getZongfen() {
                return zongfen;
            }

            public void setZongfen(String zongfen) {
                this.zongfen = zongfen;
            }
        }
    }
}
