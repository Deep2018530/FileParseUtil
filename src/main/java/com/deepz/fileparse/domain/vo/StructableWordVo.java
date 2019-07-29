package com.deepz.fileparse.domain.vo;

import com.deepz.fileparse.domain.enums.TitleEnum;

import java.util.List;

/**
 * @author zhangdingping
 * @date 2019/7/26 10:06
 * @description
 */
public class StructableWordVo {

    /**
     * 标题对象
     */
    private List<StructableWordVo.Head> heads;

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
        /**
         * 风格(标题风格，一般表示几级标题)
         */
        //防止空指针 Json转换报错，所以给默认值
        private TitleEnum style = TitleEnum.None;

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

        public String getStyle() {

            return style.getTitle();
        }

        public void setStyle(TitleEnum style) {
            this.style = style;
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
