package com.deepz.fileparse;

import com.deepz.fileparse.parse.Parser;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangdingping
 * @date 2019/7/25 18:29
 * @description
 */
public class ParserUtils {

    /**
     * 文件解析策略
     */
    private Parser parser;

    /**
     * bean类型容器  key：文件后缀 value 对应解析类实现类
     */
    private static Map<String, Object> beanDefinitions = new ConcurrentHashMap<>();

    static {
        Reflections reflections = new Reflections("com.deepz.fileparse.parse", new TypeAnnotationsScanner(), new SubTypesScanner());
        Set<Class<?>> typeClass = reflections.getTypesAnnotatedWith(com.deepz.fileparse.annotation.Parser.class, true);
        Iterator<Class<?>> iterator = typeClass.iterator();
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            Object parserImpl = null;
            try {
                parserImpl = clazz.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.deepz.fileparse.annotation.Parser annotation = clazz.getAnnotation(com.deepz.fileparse.annotation.Parser.class);
            String[] strings = annotation.fileType();
            for (int i = 0; i < strings.length; i++) {
                beanDefinitions.put(strings[i], parserImpl);
            }
        }
    }


    public <T> T parse(String path) {
        String suffix = path.substring(path.lastIndexOf('.') + 1, path.length());
        this.parser = (Parser) beanDefinitions.get(suffix);

        return (T) parser.parse(path);
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

}
