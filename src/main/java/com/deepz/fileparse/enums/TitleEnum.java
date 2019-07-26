package com.deepz.fileparse.enums;

/**
 * @author zhangdingping
 * @description 标题枚举
 * @date 2019/7/26 11:32
 */
public enum TitleEnum {

    First(2, "一级标题"), Second(3, "二级标题"), Third(4, "三级标题"), Fourth(5, "四级标题"),
    Fifth(6, "五级标题"), Sixth(7, "六级标题"), Seventh(8, "七级标题"), Eighth(9, "八级标题"),
    Ninth(10, "九级标题"),None(10, "null");

    private int style;
    private String title;


    TitleEnum(int style, String title) {
        this.style = style;
        this.title = title;
    }

    TitleEnum(int style) {
        this.style = style;
        this.title = this.getTitle();
    }

    /**
     * @author zhangdingping
     * @description 根据style获取相应枚举
     * @date 2019/7/26 11:20
     */
    public static TitleEnum findTitle(int style) {

        TitleEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getStyle() == style) {
                return values[i];
            }
        }

        return null;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
