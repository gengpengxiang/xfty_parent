package com.bj.hmxxparents.huodong.model;

import java.util.List;

/**
 * Created by gengpx on 2019/10/25.
 */

public class HuodongInfo {

    /**
     * ret : 1
     * msg :
     * data : [{"Id":"1","code":"1","title":"跳绳运动","img":null,"taolun_num":"100","user_num":"10","status":"1","createtime":"2019-10-28 14:39:38","updatetime":"2019-10-28 14:47:50"}]
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
}
