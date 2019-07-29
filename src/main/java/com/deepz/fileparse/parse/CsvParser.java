package com.deepz.fileparse.parse;

import com.csvreader.CsvReader;
import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructableCsvVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author zhangdingping
 * @date 2019/7/29 16:47
 * @description
 */
@com.deepz.fileparse.annotation.Parser(fileType = "csv")
public class CsvParser implements Parser<StructableCsvVo> {


    /**
     * @author zhangdingping
     * @description
     * @date 2019/7/29 16:48
     */
    @Override
    public StructableCsvVo parse(FileDto fileDto) {

        StructableCsvVo vo = new StructableCsvVo();

        CsvReader reader = new CsvReader(fileDto.getInputStream(), UTF_8);
        try {
            if (reader.readHeaders()) {
                String[] headers = reader.getHeaders();
                vo.setHeaders(Arrays.asList(headers));
            }

            List<List<Object>> result = new ArrayList<>();
            while (reader.readRecord()) {
                String[] rawRecord = reader.getValues();

                result.add(Arrays.asList(rawRecord));
            }

            int size = result.size();
            Object[][] data = new Object[size][];
            for (int i = 0; i < size; i++) {
                data[i] = result.get(i).toArray();
            }
            vo.setDataRows(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return vo;
    }
}
