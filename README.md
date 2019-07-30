# 开源项目(小工具) fileparse 文件解析工具

### 我的联系方式(欢迎交流啊):
邮箱:deepz2019@163.com    
[博客园]: https://www.cnblogs.com/deepSleeping/

### 为什么编写与总结这个工具
因为需求，需要做到对各类文件的解析，并且考虑到未来新增文件类型的拓展问题。所以我编写了这个工具类，途中遇到了很多困难，
从最开始的博客寥寥无几导致无从下手，到后来会使用关键字去更好地搜索自己想要的东西，最后提升了自己阅读官方文档、阅读官方API、阅读官方example的能力，
最后东拼西凑可算是完成了1.0版本，写这篇文档也是希望有同样需求的人如果不知道如何使用，可以借鉴我的代码去使用或者给我一些建议或意见。因为当初迷茫寻找各种零散博客时候的我是非常痛苦的，当然还有很多不完善的，而且代码质量也不高，请多多担待。

### 提示(最新版本)
最新版本在**分支1.0**里，使用了策略设计模式+反射框架Reflections，更好地处理了if else、文件类型扩展的问题


### 功能一览：
> 对各种文件可以进行解析，提取出文本内容，针对word、pdf等文件可以提取出目录

### 目前所支持的文件类型：
* .doc .docx .eml .xls .xlsx .ppt .pptx .pdf .txt .json .csv 截止(2019/7/30)

### 所使用的的第三方依赖
 1. Apache Tika  [教程]: https://www.yiibai.com/tika
>  用于文件类型检测和从各种格式的文件内容提取到库。(本工具中用它来讲文件内容提取成文本,用于后续针对不同输出要求来解析)
 2. Apache POI  [官方文档]: https://poi.apache.org/components/index.html
>  提供API给Java程式对Microsoft Office格式档案读和写的功能 最新的稳定版是Version 3.15 (2016-09-19)
 3. commons-email  [官网]: http://commons.apache.org/proper/commons-email/
 > 处理邮件文件，可以读写，发送等(本工具用它来解析邮件文件，提取出主题、收件人等信息)
 4. Apache PDFBox [官网]: https://pdfbox.apache.org/
 > 处理pdf文件的依赖，因为当时在其他API中没有找到或者不会使用，所以就使用了这个，暂时解决功能空缺问题
 

### 增加新的文件解析类
1. 创建对应文件的vo类,根据情况可选择是否继承**StructableFileVo**
2. 创建对应的parse解析类，并且该解析类必须在**com.deepz.fileparse.parse**包下，否则无法发现。
3. 给新建的parse解析类添加注解**@Parse(com.deepz.fileparse.annotation.Parse)**
4. 给添加的注解设置fileType属性，代表文件类型(如fileType = "doc"、fileType={"xls","xlsx"})
5. 编写解析类中的代码即可

### 核心代码展示(与上文需求相关)2019/7/30
```java
public class FileParser {

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
            String[] fileTypes = annotation.fileType();
            for (int i = 0; i < fileTypes.length; i++) {
                beanDefinitions.put(fileTypes[i], parserImpl);
            }
        }
    }


    public <T> T parse(FileDto fileDto) {
        //String suffix = path.substring(path.lastIndexOf('.') + 1, path.length());
        this.parser = (Parser) beanDefinitions.get(fileDto.getSuffx());

        return (T) parser.parse(fileDto);
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

}
```

### 使用
> 创建FileParser(com.deepz.fileparse.main),将文件上传后的输入流、文件名后缀(suffix)封装成FileDto对象作为入参，调用parse方法即可
