package com.deepz.fileparse;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张定平
 * @date 2019/7/19 11:27
 * @description
 */
public class WordParser extends FileParser {

    /**
     * @author 张定平
     * @description 获取文档中的标题
     * @date 2019/7/19 14:00
     */
    public List<String> getTitle(File file) {
        if (file.getAbsolutePath().toUpperCase().endsWith(".DOC")) {

            return getTitle2003(file);
        }
        if (file.getAbsolutePath().toUpperCase().endsWith(".DOCX")) {

            return getTitle2007(file);
        }

        return null;
    }

    /**
     * @author 张定平
     * @description 获取doc后缀的文档目录
     * @date 2019/7/19 15:35
     */
    @NotNull
    private List<String> getTitle2003(File file) {
        String path = file.getAbsolutePath();
        InputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HWPFDocument doc = null;
        try {
            doc = new HWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Range r = doc.getRange();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < r.numParagraphs(); i++) {
            Paragraph p = r.getParagraph(i);
            int numStyles = doc.getStyleSheet().numStyles();
            int styleIndex = p.getStyleIndex();
            if (numStyles > styleIndex) {
                StyleSheet style_sheet = doc.getStyleSheet();
                StyleDescription style = style_sheet.getStyleDescription(styleIndex);
                String styleName = style.getName();
                if (styleName != null && styleName.contains("标题")) {
                    String text = p.text();
                    list.add(text);
                }
            }
        }
        return list;
    }

    @NotNull
    private List<String> getTitle2007(File file) {
        List<String> list = new ArrayList<>();
        InputStream is = null;
        XWPFDocument document = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            document = new XWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            String style = paragraphs.get(i).getStyle();
            if (style != null) {
                //n代表几级标题
                if (Integer.parseInt(style) < 9) {
                    list.add(paragraphs.get(i).getText());
                }
            }

        }
        return list;
    }
}
