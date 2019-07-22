package com.deepz.fileparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
    public JSONObject getContent(String path) {

        String text = getText(path);
        return JSON.parseObject(text);
    }
}
