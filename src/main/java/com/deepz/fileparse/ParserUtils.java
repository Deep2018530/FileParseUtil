package com.deepz.fileparse;

/**
 * @author zhangdingping
 * @date 2019/7/25 18:29
 * @description
 */
public class ParserUtils {


    public <T> T parse(String path) {

        FileParse parse = null;

        if (path.toUpperCase().endsWith("XLSX") || path.toUpperCase().endsWith("XLS")) {
            parse = new ExcelParser();
        } else if (path.toUpperCase().endsWith("DOC") || path.toUpperCase().endsWith("DOCX")) {

        } else if (path.toUpperCase().endsWith("PPT") || path.toUpperCase().endsWith("PPTX")) {

        } else if (path.toUpperCase().endsWith("EML")) {
            parse = new EmailParser();
        } else if (path.toUpperCase().endsWith("PDF")) {
            parse = new PdfParser();
        } else if (path.toUpperCase().endsWith("TXT")) {

        } else if (path.toUpperCase().endsWith("JSON")) {
            parse = new JsonParser();
        } else if (path.toUpperCase().endsWith("CSV")) {

        } else {

        }
        return (T) parse.parse(path);
    }
}
