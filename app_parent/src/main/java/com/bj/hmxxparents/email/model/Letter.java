package com.bj.hmxxparents.email.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26 0026.
 *
 *private boolean isSelected;
 private boolean isLongPress;
 */

public class Letter {


    /**
     * ret : 1
     * msg : 全部查询成功
     * data : {"list_data":[{"img":"f3d25d61873edf630296b7ba54a70367.JPEG","date":"2018-11-15 10:43:31","title":"5rWL6K+VMjIyMjIyYmln5Yia5Y+R55qEduWFiQ==","content":"5Yia5Yia57uZ5Yia5Yia57uZ5ZiO5ZiO5ZiO5ZiO5b6I5bC05bCs6ZW/5Z+OduWkn+WOmuWlveWTiOWuj+WImue/u+e/u3Z25YWJ546v5b6I5bC05bCsQ0PlmI7lmI7lmI7lmI526ZW/5Z+O6JePQ1bkuI3lpb3llp3lj43lj43lpI3lpI0=","huifu_status":"1","xinjianid":"64"},{"img":"f3d25d61873edf630296b7ba54a70367.JPEG","date":"2018-11-08 14:25:25","title":"5rWL6K+V","content":"5LuK5aSp5aSp5rCU5LiN6ZSZ5ZWK5ZWK5ZWK5ZWK5ZWK5ZWK5ZWK5ZWKYQ==","huifu_status":"1","xinjianid":"63"}],"quanbu_num":2,"huifu_num":2,"caogao_num":0}
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
         * list_data : [{"img":"f3d25d61873edf630296b7ba54a70367.JPEG","date":"2018-11-15 10:43:31","title":"5rWL6K+VMjIyMjIyYmln5Yia5Y+R55qEduWFiQ==","content":"5Yia5Yia57uZ5Yia5Yia57uZ5ZiO5ZiO5ZiO5ZiO5b6I5bC05bCs6ZW/5Z+OduWkn+WOmuWlveWTiOWuj+WImue/u+e/u3Z25YWJ546v5b6I5bC05bCsQ0PlmI7lmI7lmI7lmI526ZW/5Z+O6JePQ1bkuI3lpb3llp3lj43lj43lpI3lpI0=","huifu_status":"1","xinjianid":"64"},{"img":"f3d25d61873edf630296b7ba54a70367.JPEG","date":"2018-11-08 14:25:25","title":"5rWL6K+V","content":"5LuK5aSp5aSp5rCU5LiN6ZSZ5ZWK5ZWK5ZWK5ZWK5ZWK5ZWK5ZWK5ZWKYQ==","huifu_status":"1","xinjianid":"63"}]
         * quanbu_num : 2
         * huifu_num : 2
         * caogao_num : 0
         */

        private int quanbu_num;
        private int huifu_num;
        private int caogao_num;
        private List<ListDataBean> list_data;

        public int getQuanbu_num() {
            return quanbu_num;
        }

        public void setQuanbu_num(int quanbu_num) {
            this.quanbu_num = quanbu_num;
        }

        public int getHuifu_num() {
            return huifu_num;
        }

        public void setHuifu_num(int huifu_num) {
            this.huifu_num = huifu_num;
        }

        public int getCaogao_num() {
            return caogao_num;
        }

        public void setCaogao_num(int caogao_num) {
            this.caogao_num = caogao_num;
        }

        public List<ListDataBean> getList_data() {
            return list_data;
        }

        public void setList_data(List<ListDataBean> list_data) {
            this.list_data = list_data;
        }

        public static class ListDataBean {
            /**
             * "j_newhulfu_status": "1",
             * img : f3d25d61873edf630296b7ba54a70367.JPEG
             * date : 2018-11-15 10:43:31
             * title : 5rWL6K+VMjIyMjIyYmln5Yia5Y+R55qEduWFiQ==
             * content : 5Yia5Yia57uZ5Yia5Yia57uZ5ZiO5ZiO5ZiO5ZiO5b6I5bC05bCs6ZW/5Z+OduWkn+WOmuWlveWTiOWuj+WImue/u+e/u3Z25YWJ546v5b6I5bC05bCsQ0PlmI7lmI7lmI7lmI526ZW/5Z+O6JePQ1bkuI3lpb3llp3lj43lj43lpI3lpI0=
             * huifu_status : 1
             * xinjianid : 64
             */

            private String j_newhuifu_status;
            private String img;
            private String date;
            private String title;
            private String content;
            private String huifu_status;
            private String xinjianid;

            public String getJ_newhuifu_status() {
                return j_newhuifu_status;
            }

            public void setJ_newhuifu_status(String j_newhuifu_status) {
                this.j_newhuifu_status = j_newhuifu_status;
            }

            private boolean isLongPress;

            public boolean isLongPress() {
                return isLongPress;
            }

            public void setLongPress(boolean longPress) {
                isLongPress = longPress;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHuifu_status() {
                return huifu_status;
            }

            public void setHuifu_status(String huifu_status) {
                this.huifu_status = huifu_status;
            }

            public String getXinjianid() {
                return xinjianid;
            }

            public void setXinjianid(String xinjianid) {
                this.xinjianid = xinjianid;
            }
        }
    }
}
