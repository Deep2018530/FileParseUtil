package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件解析策略
 *
 * @param <T>
 */
public interface Parser<T> {


    /**
     * @author zhangdingping
     * @description 解析文件
     * @date 2019/7/25 18:09
     */
    default T parse(String path) {
        return null;
    }

    default T parse(File file) {
        return null;
    }

    T parse(FileDto fileDto);

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

    default String parseToString(InputStream inputStream) {
        Tika tika = new Tika();
        try {
            return tika.parseToString(inputStream);
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
