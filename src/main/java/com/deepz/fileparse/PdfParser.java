package com.deepz.fileparse;

import com.deepz.fileparse.exception.NotPdfFileException;
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
public class PdfParser extends FileParser {

    /**
     * @author 张定平
     * @description 获取pdf文件中的文本内容
     * @date 2019/7/19 10:28
     */
    @Override
    public String getText(File file) {

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
     * @author 张定平
     * @description 获取pdf文件中的文本内容
     * @date 2019/7/19 10:42
     */
    @Override
    public String getText(String filePath) {

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
     * @author 张定平
     * @description 获取pdf中的目录和对应页码
     * @date 2019/7/19 10:57
     */
    public Map<Integer, String> getTitleAndPage(File file) {

        Map<Integer, String> map = new LinkedHashMap<>();

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }
        PDDocument pdDocument;
        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            PDFParser parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }

    /**
     * @author 张定平
     * @description
     * @date 2019/7/19 11:05
     */
    public Map<Integer, String> getTitleAndPage(String filePath) {
        File file = new File(filePath);
        Map<Integer, String> map = new LinkedHashMap<>();

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }
        PDDocument pdDocument;
        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            PDFParser parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }


    /**
     * @author 张定平
     * @description 获取PDF文件的所有标题(目录)
     * @date 2019/7/19 14:25
     */
    public List<String> getTitle(File file) {
        List<String> list = new ArrayList<>();

        if (!file.isFile() || !file.getAbsolutePath().toUpperCase().endsWith(".PDF")) {
            throw new NotPdfFileException("无法解析非PDF文件,请检查该文件是否为一个文件，其次查看是否为pdf文件");
        }
        PDDocument pdDocument;
        try (InputStream is = new RandomAccessBufferedFileInputStream(file)) {
            PDFParser parser = new PDFParser((RandomAccessRead) is);
            parser.parse();
            pdDocument = parser.getPDDocument();
            PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmarks(outline, "", list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void printBookmarks(PDOutlineNode bookmark, String indentation, Map<Integer, String> map) throws IOException {
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
            if (pages == 0) {
                map.put(null, current.getTitle());
                System.out.println(indentation + current.getTitle());
            } else {
                map.put(pages, current.getTitle());
                System.out.println(indentation + current.getTitle() + "  " + pages);
            }
            printBookmarks(current, indentation + "    ", map);
            current = current.getNextSibling();
        }
    }

    private void printBookmarks(PDOutlineNode bookmark, String indentation, List<String> list) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {

            list.add(current.getTitle());
            printBookmarks(current, indentation + "    ", list);
            current = current.getNextSibling();
        }
    }
}
