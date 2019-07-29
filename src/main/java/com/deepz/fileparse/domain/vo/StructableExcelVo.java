package com.deepz.fileparse.domain.vo;

import java.util.List;

/**
 * @author zhangdingping
 * @date 2019/7/25 15:52
 * @description
 */
public class StructableExcelVo {

    private List<StructableFileVO> values;

    public List<StructableFileVO> getValues() {
        return values;
    }

    public void setValues(List<StructableFileVO> values) {
        this.values = values;
    }
}
