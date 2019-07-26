package com.deepz.fileparse.parse;

import com.deepz.fileparse.vo.StructableTxtVo;

/**
 * @author 张定平
 * @date 2019/7/19 13:54
 * @description
 */
public class TxtParser implements FileParse<StructableTxtVo> {

    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/26 10:35
     */
    @Override
    public StructableTxtVo parse(String path) {
        StructableTxtVo txtVo = new StructableTxtVo();
        txtVo.setContent(parseToString(path));
        return txtVo;
    }
}
