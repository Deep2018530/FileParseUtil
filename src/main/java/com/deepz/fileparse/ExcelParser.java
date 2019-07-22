package com.deepz.fileparse;

import com.deepz.fileparse.vo.StructableFileVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 张定平
 * @date 2019/7/19 13:51
 * @description
 */
public class ExcelParser extends FileParser {

    /**
     * @author 张定平
     * @description 解析execl文件(2007 +, 后缀.xlsx)
     * @date 2019/7/22 14:20
     */
    private List<StructableFileVO> extractXssfContents(File file) {
        String path = file.getAbsolutePath();
        try (FileInputStream fileIn = new FileInputStream(path)) {

            //获取execl文件工作空间
            Workbook wb = new XSSFWorkbook(fileIn);

            return extractWorkBook(wb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @author 张定平
     * @description 解析文件（97-2007后缀为.xls）
     * @date 2019/7/22 10:35
     */
    public List<StructableFileVO> extractHssfContents(File file) {


        String path = file.getAbsolutePath();
        try (FileInputStream fileIn = new FileInputStream(path)) {
            POIFSFileSystem fs = new POIFSFileSystem(fileIn);
            //获取execl文件工作空间
            HSSFWorkbook wb = new HSSFWorkbook(fs);

            return extractWorkBook(wb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * @author 张定平
     * @description 解析excel的工作空间
     * @date 2019/7/22 14:21
     */
    private List<StructableFileVO> extractWorkBook(Workbook wb) {
        List<StructableFileVO> fileVOS = new ArrayList<>();

        List<List<Object>> datas = new ArrayList<>();

        for (int i = 0, size = wb.getNumberOfSheets(); i < size; i++) {
            //获取每个工作表
            Sheet sheet = wb.getSheetAt(i);

            StructableFileVO fileVO = new StructableFileVO();
            List<String> headers = new ArrayList<>();
            for (Row row : sheet) {

                if (headers.size() == 0 || headers == null) {
                    setHeaders(headers, row);
                    fileVO.setHeaders(headers);
                    continue;
                }
                datas.add(getCellDatas(row));


            }
            fileVO.setDataRows(dataList2DataRows(datas));
            fileVOS.add(fileVO);
        }
        return fileVOS;
    }

    /**
     * @author 张定平
     * @description
     * @date 2019/7/22 13:46
     */
    private Object[][] dataList2DataRows(List<List<Object>> datas) {

        int size = datas.size();
        Object[][] result = new Object[size][];

        for (int i = 0; i < size; i++) {
            List<Object> objects = datas.get(i);
            result[i] = objects.toArray();
        }
        return result;
    }

    /**
     * @author 张定平
     * @description 获取行的所有值
     * @date 2019/7/22 13:43
     */
    private List<Object> getCellDatas(Row row) {
        List<Object> cellDatas = new ArrayList<>();
        for (Cell cell : row) {
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case NUMERIC: //判断是否为日期时间格式
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        cellDatas.add(cell.getDateCellValue().getTime());
                    } else {
                        String strValue = String.valueOf(cell.getNumericCellValue());
                        if (strValue.contains(".") && !strValue.endsWith(".0") && !strValue.toUpperCase().contains("E")) {
                            //如果是小数
                            cellDatas.add(Double.parseDouble(strValue));
                        } else if (strValue.toUpperCase().contains("E")) {
                            //消除科学计数法
                            DecimalFormat df = new DecimalFormat("0");
                            cellDatas.add(df.format(cell.getNumericCellValue()));
                        } else {
                            //整数,排除了科学计数法那么长的整数，剩下的整数long应该够用了,并且把类似1.0截取成1
                            cellDatas.add(Long.parseLong(strValue.substring(0, strValue.lastIndexOf('.'))));
                        }

                    }
                    break;
                case BOOLEAN:
                    cellDatas.add(cell.getBooleanCellValue());
                    break;
                default:
                    cellDatas.add(cell.getStringCellValue());
            }
        }

        return cellDatas;
    }

    /**
     * @author 张定平
     * @description 获取文件内容
     * @date 2019/7/22 10:35
     */
    public List<StructableFileVO> getContent(String path) {
        File file = new File(path);
        //Excel2007- 与 Excel2007+
        boolean isXlsx = path.toUpperCase().endsWith(".XLSX");

        if (isXlsx) {
            return extractXssfContents(file);
        }

        return extractHssfContents(file);
    }


    /**
     * @author 张定平
     * @description
     * @date 2019/7/22 14:04
     */
    public void setHeaders(List<String> headers, Row row) {

        boolean flag = false;

        //获取每一行中的每一个单元格
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            //获取单元格类型
            CellType cellType = cell.getCellType();

            switch (cellType) {
                case NUMERIC:
                    headers.add(String.valueOf(cell.getNumericCellValue()));
                    break;
                case BLANK:
                    //如果剩下的都说空的，那么就不添加
                    break;
                default:
                    headers.add(cell.getStringCellValue());
                    break;
            }
        }
        if (headers.size() == 1) {
            //如果只有一个，代表是表标题，那么将headers置为null，就能继续读取行内容进来了
            headers.clear();
        }

    }
}
