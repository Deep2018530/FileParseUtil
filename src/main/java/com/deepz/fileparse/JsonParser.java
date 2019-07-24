package com.deepz.fileparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deepz.fileparse.vo.StructableFileVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 张定平
 * @date 2019/7/22 17:32
 * @description
 */
public class JsonParser extends FileParser {

    public void process(String path) {
        StructableFileVO fileVO = new StructableFileVO();
        String text = super.getText(new File(path));
        fileVO.setHeaders(getHeaders(text));
        //List<List<Object>> data = getData(text);
        //System.out.println(data.toString());
        System.out.println(fileVO.toString());
    }

    /**
     * @author zhangdingping
     * @description 获取json对象的所有key值(去重)
     * @date 2019/7/23 14:08
     */
    private List<String> getHeaders(String jsonStr) {
        int checkJson = checkJson(jsonStr);
        if (checkJson == 1) {
            return getDistinctKeys(JSON.parseArray(jsonStr));
        } else if (checkJson == 0) {
            return getKeys(JSON.parseObject(jsonStr));
        }
        return null;
    }

    /**
     * @author zhangdingping
     * @description 拿到JSONObject中的所有key值
     * @date 2019/7/24 10:19
     */
    public List<String> getKeys(JSONObject jsonObject) {
        List<String> headers = new ArrayList<>();
        Set<String> strings = jsonObject.keySet();
        headers.addAll(strings);
        return headers;
    }

    /**
     * @author zhangdingping
     * @description 从Json数组中获取JsonObject所有的一级Key值(去重)
     * @date 2019/7/24 10:18
     */
    public List<String> getDistinctKeys(JSONArray jsonArray) {
        //如果有多个对象，则可以转换成JSONObject，否则会报错
        int size = jsonArray.size();
        if (size > 1) {
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                headers.addAll(getKeys(jsonArray.getJSONObject(i)));
            }
            //将key值去重
            return headers.stream().distinct().collect(Collectors.toList());
        } else if (size == 1) {
            //只有一个对象，那么直接把这个对象的key放入heads中就行了
            return getKeys(jsonArray.getJSONObject(0));
        } else {
            return null;

        }
    }

    private int checkJson(String jsonStr) {
        if (jsonStr.startsWith("[")) {
            //如果是JSONArray
            return 1;
        } else if (jsonStr.startsWith("{")) {
            //如果是JSONObject
            return 0;
        } else {
            return -1;
        }
    }
}
