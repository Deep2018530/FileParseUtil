package com.deepz.fileparse.parse;

import com.deepz.fileparse.exception.NotPdfFileException;
import com.deepz.fileparse.vo.StructablePdfVo;
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

    /**
     * @author zhangdingping
     * @description 获取pdf文件中的文本内容
     * @date 2019/7/25 16:31
     */
    public String getContent(File file) {

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }

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

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }

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

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }
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

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }
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
