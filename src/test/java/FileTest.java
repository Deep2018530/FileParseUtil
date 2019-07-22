import com.alibaba.fastjson.JSONObject;
import com.deepz.fileparse.JsonParser;
import com.deepz.fileparse.PptParser;
import org.junit.Test;

/**
 * @author 张定平
 * @date 2019/7/22 17:18
 * @description
 */
public class FileTest {

    PptParser parser = new PptParser();

    JsonParser jsonParser = new JsonParser();

    @Test
    public void getContent() {
        String path = "C:\\Users\\zhangdingping\\Desktop\\tika\\tika.json";

        JSONObject content = jsonParser.getContent(path);
        System.out.println(content);
    }
}
