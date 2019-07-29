package com.deepz.fileparse.domain.dto;

import java.io.InputStream;

/**
 * @author DeepSleeping
 * @date 2019/7/29 11:34
 * @description
 */
public class FileDto {

    private InputStream inputStream;

    /**
     * 文件后缀(不包含 .)
     */
    private String suffx;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getSuffx() {
        return suffx;
    }

    public void setSuffx(String suffx) {
        this.suffx = suffx;
    }
}
