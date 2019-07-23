package com.deepz.fileparse.vo;

        import java.util.List;

/**
 * @author 张定平
 * @date 2019/7/22 11:30
 * @description 文件解析内容Vo
 */
public class StructableFileVO {
    //列名
    private List<String> headers;
    //数据
    private Object[][] dataRows;


    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public Object[][] getDataRows() {
        return dataRows;
    }

    public void setDataRows(Object[][] dataRows) {
        this.dataRows = dataRows;
    }

}
