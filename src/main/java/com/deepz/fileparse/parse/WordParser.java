package com.deepz.fileparse.parse;

import com.deepz.fileparse.StreamUtils;
import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.enums.TitleEnum;
import com.deepz.fileparse.domain.vo.StructableWordVo;
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
@com.deepz.fileparse.annotation.Parser(fileType = {"doc", "docx"})
public class WordParser implements Parser<StructableWordVo> {

    /**
     * 输入流转换后的字节数组(用于流复用)
     */
    private byte[] bytes;

    /**
     * @description
     * @author DeepSleeping
     * @date 2019/7/29 13:26
     */
    @Override
    public StructableWordVo parse(FileDto fileDto) {
        bytes = StreamUtils.inputToByteArray(fileDto.getInputStream());

        StructableWordVo vo = new StructableWordVo();
        List<StructableWordVo.Head> heads = fileDto.getSuffx().equals("doc") ? getHwpfHead() : getXwpfHead();
        vo.setContent(parseToString(new ByteArrayInputStream(bytes)));
        return vo;
    }


    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/26 10:07
     */
    @Override
    public StructableWordVo parse(String path) {
        StructableWordVo wordVo = new StructableWordVo();
        wordVo.setContent(parseToString(new File(path)));
        wordVo.setHeads(getHead(path));
        return wordVo;
    }


    /**
     * @author 张定平
     * @description 获取文档中的标题
     * @date 2019/7/19 14:00
     */
    public List<StructableWordVo.Head> getHead(String path) {
        File file = new File(path);
        if (file.getAbsolutePath().toUpperCase().endsWith(".DOC")) {

            return getHwpfHead(path);
        }
        if (file.getAbsolutePath().toUpperCase().endsWith(".DOCX")) {

            return getXwpfHead(path);
        }

        return null;
    }

    private List<StructableWordVo.Head> getXwpfHead() {
        List<StructableWordVo.Head> heads = new ArrayList<>();
        XWPFDocument document = null;
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            document = new XWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            String style = paragraphs.get(i).getStyle();
            if (style != null) {
                //(n-1)一般代表几级标题
                if (Integer.parseInt(style) < 7) {
                    StructableWordVo.Head head = new StructableWordVo.Head();
                    head.setTitle(paragraphs.get(i).getText());
                    head.setStyle(TitleEnum.findTitle(Integer.parseInt(style)));
                    heads.add(head);
                }
            }

        }
        return heads;
    }


    /**
     * @author zhangdingping
     * @description .docx 获取标题页码相关信息
     * @date 2019/7/26 10:22
     */
    private List<StructableWordVo.Head> getXwpfHead(String path) {
        List<StructableWordVo.Head> heads = new ArrayList<>();
        File file = new File(path);
        XWPFDocument document = null;

        try {
            document = new XWPFDocument(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            String style = paragraphs.get(i).getStyle();
            if (style != null) {
                //(n-1)一般代表几级标题
                if (Integer.parseInt(style) < 7) {
                    StructableWordVo.Head head = new StructableWordVo.Head();
                    head.setTitle(paragraphs.get(i).getText());
                    head.setStyle(TitleEnum.findTitle(Integer.parseInt(style)));
                    heads.add(head);
                }
            }

        }
        return heads;
    }

    private List<StructableWordVo.Head> getHwpfHead() {
        List<StructableWordVo.Head> heads = new ArrayList<>();

        HWPFDocument doc = null;
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            doc = new HWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Range r = doc.getRange();
        for (int i = 0; i < r.numParagraphs(); i++) {
            Paragraph p = r.getParagraph(i);
            int numStyles = doc.getStyleSheet().numStyles();
            int styleIndex = p.getStyleIndex();
            if (numStyles > styleIndex) {
                StyleSheet style_sheet = doc.getStyleSheet();
                StyleDescription style = style_sheet.getStyleDescription(styleIndex);
                String styleName = style.getName();
                if (styleName != null && styleName.contains("标题")) {
                    StructableWordVo.Head head = new StructableWordVo.Head();
                    head.setTitle(p.text());
                    heads.add(head);
                }
            }
        }
        return heads;
    }


    /**
     * @author zhangdingping
     * @description .doc
     * @date 2019/7/26 10:22
     */
    private List<StructableWordVo.Head> getHwpfHead(String path) {
        List<StructableWordVo.Head> heads = new ArrayList<>();

        HWPFDocument doc = null;

        try {
            doc = new HWPFDocument(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Range r = doc.getRange();
        for (int i = 0; i < r.numParagraphs(); i++) {
            Paragraph p = r.getParagraph(i);
            int numStyles = doc.getStyleSheet().numStyles();
            int styleIndex = p.getStyleIndex();
            if (numStyles > styleIndex) {
                StyleSheet style_sheet = doc.getStyleSheet();
                StyleDescription style = style_sheet.getStyleDescription(styleIndex);
                String styleName = style.getName();
                if (styleName != null && styleName.contains("标题")) {
                    StructableWordVo.Head head = new StructableWordVo.Head();
                    head.setTitle(p.text());
                    heads.add(head);
                }
            }
        }
        return heads;
    }


    @NotNull
    private List<String> doGetTitle2007(File file) {
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

    private List<String> doGetTitle2007(InputStream inputStream) {
        List<String> list = new ArrayList<>();
        XWPFDocument document = null;

        try (InputStream is = inputStream) {
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
