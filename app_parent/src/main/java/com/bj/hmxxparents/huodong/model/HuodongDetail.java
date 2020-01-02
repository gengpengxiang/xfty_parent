package com.bj.hmxxparents.huodong.model;

import java.util.List;

/**
 * Created by gengpx on 2019/10/28.
 */

public class HuodongDetail {

    /**
     * ret : 1
     * msg :
     * data : {"huodong_info":{"Id":"1","code":"1","title":"跳绳运动","img":null,"taolun_num":"100","user_num":"10","status":"1","createtime":"2019-10-28 14:39:38","updatetime":"2019-10-28 14:47:50"},"huodong_suyang":[{"Id":"1","code":"1","name":"运动健将","huodong_code":"1","createtime":"2019-10-28 15:11:12","updatetime":"2019-10-28 15:11:12"}]}
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
         * huodong_info : {"Id":"1","code":"1","title":"跳绳运动","img":null,"taolun_num":"100","user_num":"10","status":"1","createtime":"2019-10-28 14:39:38","updatetime":"2019-10-28 14:47:50"}
         * huodong_suyang : [{"Id":"1","code":"1","name":"运动健将","huodong_code":"1","createtime":"2019-10-28 15:11:12","updatetime":"2019-10-28 15:11:12"}]
         */

        private HuodongInfoBean huodong_info;
        private List<HuodongSuyangBean> huodong_suyang;

        public HuodongInfoBean getHuodong_info() {
            return huodong_info;
        }

        public void setHuodong_info(HuodongInfoBean huodong_info) {
            this.huodong_info = huodong_info;
        }

        public List<HuodongSuyangBean> getHuodong_suyang() {
            return huodong_suyang;
        }

        public void setHuodong_suyang(List<HuodongSuyangBean> huodong_suyang) {
            this.huodong_suyang = huodong_suyang;
        }

        public static class HuodongInfoBean {
            /**
             * Id : 1
             * code : 1
             * title : 跳绳运动
             * img : null
             * taolun_num : 100
             * user_num : 10
             * status : 1
             * createtime : 2019-10-28 14:39:38
             * updatetime : 2019-10-28 14:47:50
             */

            private String Id;
            private String code;
            private String title;
            private Object img;
            private String taolun_num;
            private String user_num;
            private String status;
            private String createtime;
            private String updatetime;

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public String getTaolun_num() {
                return taolun_num;
            }

            public void setTaolun_num(String taolun_num) {
                this.taolun_num = taolun_num;
            }

            public String getUser_num() {
                return user_num;
            }

            public void setUser_num(String user_num) {
                this.user_num = user_num;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }

        public static class HuodongSuyangBean {
            /**
             * Id : 1
             * code : 1
             * name : 运动健将
             * huodong_code : 1
             * createtime : 2019-10-28 15:11:12
             * updatetime : 2019-10-28 15:11:12
             */

            private String Id;
            private String code;
            private String name;
            private String huodong_code;
            private String createtime;
            private String updatetime;

            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
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

            public String getCreatetime() {
                return createtime;
            }

            public void setCreatetime(String createtime) {
                this.createtime = createtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }
}
