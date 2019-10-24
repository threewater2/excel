package com.threewater.excel;

import com.threewater.excel.core.DefaultExcelUtil;
import com.threewater.excel.core.ExcelUtil;
import com.threewater.excel.exception.ExcelException;
import com.threewater.excel.model.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ExcelApplicationTests {


    Workbook initWorkBook() throws IOException {
        String path="C:/Users/water/Desktop/excelTest.xlsx";
        InputStream inputStream=new FileInputStream(path);
        return new XSSFWorkbook(inputStream);
    }

    @Test
    void contextLoads() {
    }



    @Test
    void test2(){
        ExcelUtil excelUtil=new DefaultExcelUtil();
        try {
            InputStream excelTemplate = excelUtil.getExcelTemplate(User.class);
        } catch (ExcelException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test3(){
        List<User> userList=new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        ExcelUtil excelUtil=new DefaultExcelUtil();
        try {
            excelUtil.toExcel(userList);
        } catch (ExcelException e) {
            e.printStackTrace();
        }
    }



}
