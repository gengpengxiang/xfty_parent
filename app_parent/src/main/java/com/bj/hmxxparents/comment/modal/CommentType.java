package com.bj.hmxxparents.comment.modal;

import java.util.List;

/**
 * Created by gengpx on 2019/10/24.
 */

public class CommentType {

    /**
     * ret : 1
     * msg :
     * data : {"z1":[{"liyou_id":"0","liyou_name":"全部","liyou_num":"0"},{"liyou_id":"1","liyou_name":"值日认真","liyou_num":"0"},{"liyou_id":"2","liyou_name":"坐姿端正","liyou_num":"0"},{"liyou_id":"3","liyou_name":"待人礼貌","liyou_num":"0"},{"liyou_id":"4","liyou_name":"讲究卫生","liyou_num":"0"},{"liyou_id":"5","liyou_name":"友爱同学","liyou_num":"0"},{"liyou_id":"6","liyou_name":"积极回答","liyou_num":"0"},{"liyou_id":"7","liyou_name":"志愿服务","liyou_num":"0"},{"liyou_id":"8","liyou_name":"尊敬老师","liyou_num":"0"},{"liyou_id":"9","liyou_name":"助人为乐","liyou_num":"0"},{"liyou_id":"10","liyou_name":"注意力集中","liyou_num":"0"},{"liyou_id":"11","liyou_name":"认真听讲","liyou_num":"0"},{"liyou_id":"12","liyou_name":"认真思考","liyou_num":"0"},{"liyou_id":"13","liyou_name":"考试进步","liyou_num":"0"},{"liyou_id":"14","liyou_name":"积极讨论","liyou_num":"0"},{"liyou_id":"15","liyou_name":"书写工整","liyou_num":"0"},{"liyou_id":"16","liyou_name":"优秀作业","liyou_num":"0"},{"liyou_id":"17","liyou_name":"诚实守信","liyou_num":"0"},{"liyou_id":"18","liyou_name":"遵守纪律","liyou_num":"0"},{"liyou_id":"19","liyou_name":"爱护公物","liyou_num":"0"},{"liyou_id":"20","liyou_name":"言行文明","liyou_num":"0"},{"liyou_id":"21","liyou_name":"生活勤俭","liyou_num":"0"},{"liyou_id":"22","liyou_name":"活动积极","liyou_num":"0"},{"liyou_id":"23","liyou_name":"路队整齐","liyou_num":"0"},{"liyou_id":"24","liyou_name":"佩戴红领巾","liyou_num":"0"},{"liyou_id":"25","liyou_name":"佩戴小黄帽","liyou_num":"0"}]}
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
        private List<Z1Bean> z1;

        public List<Z1Bean> getZ1() {
            return z1;
        }

        public void setZ1(List<Z1Bean> z1) {
            this.z1 = z1;
        }

        public static class Z1Bean {
            /**
             * liyou_id : 0
             * liyou_name : 全部
             * liyou_num : 0
             */

            private String liyou_id;
            private String liyou_name;
            private String liyou_num;


            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getLiyou_id() {
                return liyou_id;
            }

            public void setLiyou_id(String liyou_id) {
                this.liyou_id = liyou_id;
            }

            public String getLiyou_name() {
                return liyou_name;
            }

            public void setLiyou_name(String liyou_name) {
                this.liyou_name = liyou_name;
            }

            public String getLiyou_num() {
                return liyou_num;
            }

            public void setLiyou_num(String liyou_num) {
                this.liyou_num = liyou_num;
            }
        }
    }
}
