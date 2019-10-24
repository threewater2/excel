package com.threewater.excel.core;

import com.threewater.excel.exception.ExcelException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;

public interface ExcelUtil {
    <T> List<T> excelParse(Class<T> entity, Workbook workbook) throws ExcelException;

    <T> InputStream toExcel(List<T> entityList) throws ExcelException;

    Workbook getExcelTemplate(Class entity) throws ExcelException;
}
