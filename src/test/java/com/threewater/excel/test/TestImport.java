package com.threewater.excel.test;

import com.threewater.excel.core.DefaultExcelUtil;
import com.threewater.excel.core.ExcelUtil;
import com.threewater.excel.exception.ColumnIllegalException;
import com.threewater.excel.exception.ExcelException;
import com.threewater.excel.model.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

//@SpringBootTest
public class TestImport {

    Workbook initWorkBook() throws IOException {
        String path="C:/Users/water/Desktop/excelTest.xlsx";
        InputStream inputStream=new FileInputStream(path);
        return new XSSFWorkbook(inputStream);
    }

    @Test
    void multiLine() throws IOException {
        Workbook workbook=initWorkBook();
        ExcelUtil excelUtil=new DefaultExcelUtil();
        try {
            List<User> userList = excelUtil.excelParse(User.class, workbook);
            assertNotNull(userList);
            for (User user:userList){
                assertEquals(user.getName(),"water");
                assertEquals(user.getEmail(),"water");
            }
        } catch (ExcelException e) {
            e.printStackTrace();
        }
    }

    @Test
    void nullable() throws Exception{
        Workbook workbook=initWorkBook();
        ExcelUtil excelUtil=new DefaultExcelUtil();
        List<User> userList = excelUtil.excelParse(User.class, workbook);
        assertNull(userList.get(0).getPhone());
    }

    @Test()
    void notNull() throws Exception{
        Workbook workbook=initWorkBook();
        DefaultExcelUtil excelUtil=new DefaultExcelUtil();
        List<User> userList=excelUtil.excelParse(User.class,workbook);
        List<ExcelException> excelExceptions = excelUtil.showResult();
        boolean exist=false;
        for(ExcelException excelException:excelExceptions){
            if (excelException instanceof ColumnIllegalException) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);
        assertEquals(3,userList.size());
        assertEquals("water1",userList.get(0).getName());
        assertEquals("water2",userList.get(1).getName());
        assertEquals("water3",userList.get(2).getName());
    }
}
