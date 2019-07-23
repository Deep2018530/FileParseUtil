package com.deepz.fileparse;

import com.alibaba.fastjson.JSONArray;

/**
 * @author 张定平
 * @date 2019/7/22 17:32
 * @description
 */
public class JsonParser extends FileParser {

    /**
     * @author 张定平
     * @description 解析json文件并结构化返回json数据
     * @date 2019/7/22 17:33
     */
    public JSONArray getContent(String path) {

        String text = getText(path);
        Object parse = JSONArray.parse(text);
        return (JSONArray) parse;
    }
}
