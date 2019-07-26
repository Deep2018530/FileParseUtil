package com.deepz.fileparse.parse;

import com.deepz.fileparse.vo.StructablePptVo;

/**
 * @author 张定平
 * @date 2019/7/19 13:52
 * @description
 */
public class PptParser implements FileParse<StructablePptVo> {


    @Override
    public StructablePptVo parse(String path) {
        String text = parseToString(path);
        StructablePptVo pptVo = new StructablePptVo();
        pptVo.setContent(text);
        return pptVo;
    }
}
