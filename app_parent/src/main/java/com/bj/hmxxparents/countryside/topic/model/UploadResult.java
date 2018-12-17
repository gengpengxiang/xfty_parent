package com.bj.hmxxparents.countryside.topic.model;

/**
 * Created by Administrator on 2018/11/25 0025.
 */

public class UploadResult {

    /**
     * ret : 1
     * msg :
     * data : {"img":"468391f51b4fce5c4e331a72ab28cf0f.jpg"}
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
         * img : 468391f51b4fce5c4e331a72ab28cf0f.jpg
         */

        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
