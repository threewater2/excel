package com.threewater.excel.test;

import com.threewater.excel.core.DefaultExcelUtil;
import com.threewater.excel.model.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class TestTemplate {
    @Test
    void template() throws Exception{
        DefaultExcelUtil excelUtil=new DefaultExcelUtil();
        Workbook workbook = excelUtil.getExcelTemplate(User.class);
        Sheet sheetAt = workbook.getSheetAt(0);
        Row row = sheetAt.getRow(0);
        assertEquals("名字",row.getCell(0).getStringCellValue());
        assertEquals("邮箱",row.getCell(1).getStringCellValue());
        assertEquals("电话",row.getCell(2).getStringCellValue());
    }
}
