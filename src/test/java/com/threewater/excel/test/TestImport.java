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
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

class TestImport {

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
                assertEquals(user.getEmail(),"waterthreewaterzxm@gmail.com");
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
        assertEquals(4,userList.size());
        assertEquals("water",userList.get(0).getName());
        assertEquals("water",userList.get(1).getName());
        assertEquals("water",userList.get(2).getName());
    }

    @Test
    void email() throws Exception{
        Workbook workbook=initWorkBook();
        DefaultExcelUtil excelUtil=new DefaultExcelUtil();
        List<User> userList = excelUtil.excelParse(User.class, workbook);
        assertEquals(4,userList.size());
        List<ExcelException> excelExceptions = excelUtil.showResult();
        assertEquals(0,excelExceptions.size());
//        assertEquals(ColumnIllegalException.class,excelExceptions.get(0).getClass());
    }

    @Test
    void date() throws Exception{
        Workbook workbook=initWorkBook();
        DefaultExcelUtil excelUtil=new DefaultExcelUtil();
        List<User> userList = excelUtil.excelParse(User.class, workbook);
        assertEquals(4,userList.size());
        Calendar birth=Calendar.getInstance();
        for(User user:userList){
            birth.setTime(user.getBirth());
            assertEquals(2019,birth.get(Calendar.YEAR));
            assertEquals(Calendar.SEPTEMBER,birth.get(Calendar.MONTH));
            assertEquals(12,birth.get(Calendar.DATE));
        }
    }

    @Test
    void customCellReader() throws Exception{
        Workbook workbook=initWorkBook();
        DefaultExcelUtil excelUtil=new DefaultExcelUtil();
        List<User> userList = excelUtil.excelParse(User.class, workbook);
        for(User user:userList){
            assertEquals("123我自定义",user.getContent());
        }
    }


}
