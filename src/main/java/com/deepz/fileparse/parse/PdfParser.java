package com.deepz.fileparse.parse;

import com.deepz.fileparse.common.util.StreamUtils;
import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructablePdfVo;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张定平
 * @date 2019/7/19 10:21
 * @description
 */
@com.deepz.fileparse.annotation.Parser(fileType = "pdf")
public class PdfParser implements Parser<StructablePdfVo> {

    /**
     * 输入流转换后的字节数组(用于流复用)
     */
    private byte[] bytes;

    /**
     * @description
     * @author DeepSleeping
     * @date 2019/7/29 13:13
     */
    @Override
    public StructablePdfVo parse(FileDto fileDto) {
        bytes = StreamUtils.inputToByteArray(fileDto.getInputStream());

        StructablePdfVo vo = new StructablePdfVo();
        vo.setContent(parseToString(new ByteArrayInputStream(bytes)));
        vo.setHeads(getTitleAndPage());

//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        Callable<String> taskForContent = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return getContent();
//            }
//        };
//        Callable<List<StructablePdfVo.Head>> taskForHead = new Callable<List<StructablePdfVo.Head>>() {
//            @Override
//            public List<StructablePdfVo.Head> call() throws Exception {
//                return getTitleAndPage();
//            }
//        };
//
//        FutureTask<String> futureTask1 = new FutureTask<String>(taskForContent);
//        FutureTask<List<StructablePdfVo.Head>> futureTask2 = new FutureTask<List<StructablePdfVo.Head>>(taskForHead);
//        executor.submit(futureTask1);
//        executor.submit(futureTask2);
//
//        try {
//            vo.setContent(futureTask1.get());
//            vo.setHeads(futureTask2.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        return vo;
    }


    /**
     * @author zhangdingping
     * @description 解析pdf文件
     * @date 2019/7/25 16:30
     */
    @Override
    public StructablePdfVo parse(String path) {
        StructablePdfVo pdfVo = new StructablePdfVo();
        pdfVo.setContent(getContent(path));
        pdfVo.setHeads(getTitleAndPage(path));
        return pdfVo;
    }

    public List<StructablePdfVo.Head> getTitleAndPage() {

        Map<Integer, String> map = new LinkedHashMap<>();


        List<StructablePdfVo.Head> heads = new ArrayList<>();
        PDDocument pdDocument = null;
        try (InputStream is = new ByteArrayInputStream(bytes);
             RandomAccessBufferedFileInputStream rabfis = new RandomAccessBufferedFileInputStream(is);
        ) {
            PDFParser parser = new PDFParser(rabfis);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", heads);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                pdDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return heads;

    }

    /**
     * @author zhangdingping
     * @description 获取pdf文件中的文本内容
     * @date 2019/7/25 16:31
     */
    public String getContent(File file) {

        PDDocument pdDocument;
        PDFParser parser;
        String text = "";

        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDFTextStripper textStripper = new PDFTextStripper();
            text = textStripper.getText(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * @author zhangdingping
     * @description 获取pdf文件中的文本内容
     * @date 2019/7/25 16:31
     */
    public String getContent(String filePath) {
        File file = new File(filePath);


        PDDocument pdDocument;
        PDFParser parser;
        String text = "";

        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDFTextStripper textStripper = new PDFTextStripper();
            text = textStripper.getText(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/25 16:29
     */
    public List<StructablePdfVo.Head> getTitleAndPage(File file) {

        Map<Integer, String> map = new LinkedHashMap<>();

        PDDocument pdDocument;
        List<StructablePdfVo.Head> heads = new ArrayList<>();
        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            PDFParser parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", heads);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return heads;

    }

    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/25 16:29
     */
    public List<StructablePdfVo.Head> getTitleAndPage(String filePath) {
        File file = new File(filePath);
        Map<Integer, String> map = new LinkedHashMap<>();

        PDDocument pdDocument;
        List<StructablePdfVo.Head> heads = new ArrayList<>();
        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            PDFParser parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", heads);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return heads;

    }


    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/25 16:29
     */
    private void printBookmarks(PDOutlineNode bookmark, String indentation, List<StructablePdfVo.Head> heads) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {
            int pages = 0;
            if (current.getDestination() instanceof PDPageDestination) {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                pages = pd.retrievePageNumber() + 1;
            }
            if (current.getAction() instanceof PDActionGoTo) {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination) {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    pages = pd.retrievePageNumber() + 1;
                }
            }
            StructablePdfVo.Head head = new StructablePdfVo.Head();
            head.setPage(pages == 0 ? null : String.valueOf(pages));
            head.setTitle(current.getTitle());
            heads.add(head);
            printBookmarks(current, indentation + "    ", heads);
            current = current.getNextSibling();
        }
    }

}
