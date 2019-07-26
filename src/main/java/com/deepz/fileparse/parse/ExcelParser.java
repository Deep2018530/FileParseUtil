package com.deepz.fileparse.parse;

import com.deepz.fileparse.vo.StructableExcelVo;
import com.deepz.fileparse.vo.StructableFileVO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
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
@com.deepz.fileparse.annotation.Parser(fileType = {"xls", "xlsx"})
public class ExcelParser implements Parser<StructableExcelVo> {

    /**
     * @author 张定平
     * @description 解析Excel文件
     * @date 2019/7/22 10:35
     */
    @Override
    public StructableExcelVo parse(String path) {
        boolean isXlsx = path.toUpperCase().endsWith(".XLSX");
        Workbook wb = null;
        try {
            //解析execl文件(2007+ 后缀.xlsx    HSSF对应2007-)
            wb = isXlsx == true ? new XSSFWorkbook(path) : new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractWorkBook(wb);
    }


    /**
     * @author 张定平
     * @description 解析excel的工作空间
     * @date 2019/7/22 14:21
     */
    private StructableExcelVo extractWorkBook(Workbook wb) {
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
        StructableExcelVo excelVo = new StructableExcelVo();
        excelVo.setValues(fileVOS);

        return excelVo;
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
                case BOOLEAN:
                    headers.add(String.valueOf(cell.getBooleanCellValue()));
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
