package com.bj.hmxxparents.pet.model;

import java.util.List;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class PetInfo {
    /**
     * ret : 1
     * msg :
     * data : [{"id":"1","name":"海狸","img":"chongwu/haili-01.png","img_s":"","shuoming":"","status":"","jiesuo_status":1}]
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
         * id : 1
         * name : 海狸
         * img : chongwu/haili-01.png
         * img_s :
         * shuoming :
         * status :
         * jiesuo_status : 1
         */

        private String id;
        private String name;
        private String img;
        private String img_s;
        private String shuoming;
        private int status;
        private int jiesuo_status;

        public boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getImg_s() {
            return img_s;
        }

        public void setImg_s(String img_s) {
            this.img_s = img_s;
        }

        public String getShuoming() {
            return shuoming;
        }

        public void setShuoming(String shuoming) {
            this.shuoming = shuoming;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getJiesuo_status() {
            return jiesuo_status;
        }

        public void setJiesuo_status(int jiesuo_status) {
            this.jiesuo_status = jiesuo_status;
        }
    }

}
