package com.deepz.fileparse.parse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileParse<T> {
    /**
     * @author zhangdingping
     * @description 解析文件
     * @date 2019/7/25 18:09
     */
    T parse(String path);

    default T parse(File file) {
        return null;
    }

    default T parse(InputStream inputStream) {
        return null;
    }

    default String parseToString(String path) {
        Tika tika = new Tika();
        try {
            return tika.parseToString(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return null;
    }

    default String parseToString(File file) {
        Tika tika = new Tika();
        try {
            return tika.parseToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return null;
    }

}
