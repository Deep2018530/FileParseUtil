package com.deepz.fileparse.vo;

import java.util.List;

/**
 * @author zhangdingping
 * @date 2019/7/25 16:01
 * @description
 */
public class StructablePdfVo {


    /**
     * 标题对象
     */
    private List<Head> heads;

    /**
     * 正文内容
     */
    private String content;


    public static class Head {

        /**
         * 页码
         */
        private String page;
        /**
         * 标题
         */
        private String title;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public List<Head> getHeads() {
        return heads;
    }

    public void setHeads(List<Head> heads) {
        this.heads = heads;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

