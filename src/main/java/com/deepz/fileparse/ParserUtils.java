package com.deepz.fileparse;

import com.deepz.fileparse.constant.PropertiesLocation;
import com.deepz.fileparse.parse.FileParse;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhangdingping
 * @date 2019/7/25 18:29
 * @description
 */
public class ParserUtils {

    private FileParse fileParse;

    public <T> T parse(String path) {
        String suffix = "suffx." + path.substring(path.lastIndexOf('.') + 1, path.length());
        Properties prop = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PropertiesLocation.CLASS_NAME_PROPERTIES);

        try {
            prop.load(inputStream);
            String className = prop.getProperty(suffix);
            Class<?> aClass = Class.forName(className);
            Object o = aClass.getConstructor().newInstance();
            this.setFileParse((FileParse) o);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) fileParse.parse(path);
    }

    public FileParse getFileParse() {
        return fileParse;
    }

    public void setFileParse(FileParse fileParse) {
        this.fileParse = fileParse;
    }

}
