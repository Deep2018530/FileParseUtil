package com.deepz.fileparse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

/**
 * @author 张定平
 * @date 2019/7/19 13:28
 * @description
 */
public class FileParser{


    public String getText(File file) {

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

    public String getText(String filePath) {

        File file = new File(filePath);

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
