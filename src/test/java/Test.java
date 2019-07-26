import com.deepz.fileparse.ParserUtils;

/**
 * @author zhangdingping
 * @date 2019/7/26 15:33
 * @description
 */
public class Test {

    public static void main(String[] args) {
        ParserUtils parserUtils = new ParserUtils();
        Object parse = parserUtils.parse("C:\\Users\\zhangdingping\\Desktop\\tika\\tika.eml");
        System.out.println(parse);
    }
}
