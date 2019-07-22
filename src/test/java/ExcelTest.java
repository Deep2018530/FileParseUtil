import com.deepz.fileparse.ExcelParser;
import com.deepz.fileparse.vo.StructableFileVO;
import org.junit.Test;

import java.util.List;

/**
 * @author 张定平
 * @date 2019/7/22 10:07
 * @description
 */
public class ExcelTest {
    ExcelParser parser = new ExcelParser();

    @Test
    public void getContent() {
        String path = "C:\\Users\\zhangdingping\\Desktop\\tika\\tika.xlsx";

        List<StructableFileVO> content = parser.getContent(path);
        for (StructableFileVO fileVO : content) {
            List<String> headers = fileVO.getHeaders();
            if (headers != null && headers.size() != 0 ){
                for (String header : headers) {
                    System.out.print(header + "\t");
                }
            }

            System.out.println();
            System.out.println();
            Object[][] dataRows = fileVO.getDataRows();
            for (int i = 0; i < dataRows.length; i++) {
                for (int i1 = 0; i1 < dataRows[i].length; i1++) {
                    System.out.print(dataRows[i][i1] + "\t");
                }
                System.out.println();
            }
        }

    }
}
