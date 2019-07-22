import com.deepz.fileparse.WordParser;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author 张定平
 * @date 2019/7/22 16:43
 * @description
 */
public class WordTest {

    WordParser parser = new WordParser();

    @Test
    public void getContent() {
        String path = "C:\\Users\\zhangdingping\\Desktop\\tika\\Title.docx";

        List<String> title = parser.getTitle(new File(path));
        System.out.println(title);

    }
}
