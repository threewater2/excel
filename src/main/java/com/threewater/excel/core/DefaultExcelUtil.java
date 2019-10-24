package com.threewater.excel.core;

import com.threewater.excel.annotaion.input.*;
import com.threewater.excel.annotaion.output.ColumnTemplate;
import com.threewater.excel.annotaion.output.ExcelTemplate;
import com.threewater.excel.exception.ColumnIllegalException;
import com.threewater.excel.exception.ExcelConfigException;
import com.threewater.excel.exception.ExcelException;
import com.threewater.excel.exception.IllegalValidatorException;
import com.threewater.excel.validator.Validator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DefaultExcelUtil implements ExcelUtil {

    private List<ExcelException> excelExceptions = new ArrayList<>();
    private int failNum = 0;
    private int successNum = 0;

    @Override
    public <T> List<T> excelParse(Class<T> entity, Workbook workbook) {
        List<T> resList = new ArrayList<>();
        if(entity.getDeclaredAnnotation(ExcelReadable.class)==null)
            throw new ExcelConfigException(entity.getName()+"没有标注"+ExcelReadable.class.getName()+"注解");
        Sheet sheet = workbook.getSheetAt(0);
        int length = sheet.getLastRowNum();
        for (int rowIndex = 0; rowIndex <= length; rowIndex++) {
            T rowObject = readRow(entity, sheet.getRow(rowIndex));
            if (rowObject == null){
                failNum++;
                continue;
            }
            successNum++;
            resList.add(rowObject);
        }
        return resList;
    }

    @Override
    public <T> InputStream toExcel(List<T> entityList) {
        return null;
    }

    @Override
    public Workbook getExcelTemplate(Class entity) {
        ExcelTemplate excelTemplate = (ExcelTemplate) entity.getDeclaredAnnotation(ExcelTemplate.class);
        if(excelTemplate==null) return null;
        Workbook workbook=new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Field[] fields = entity.getDeclaredFields();
        for(Field field:fields){
            ColumnTemplate columnTemplate = field.getAnnotation(ColumnTemplate.class);
            if(columnTemplate==null) continue;
            String value = columnTemplate.value();
            Cell cell = row.createCell(columnTemplate.columnIndex());
            cell.setCellValue(value);
        }
        return workbook;
    }

    private <T> T readRow(Class<T> entity, Row row) {
        Field[] fields = entity.getDeclaredFields();
        Object instance = null;
        try {
            instance = entity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            excelExceptions.add(new ExcelException(entity.getName() + "无法实例化"));
        }
        for (Field field : fields) {
            field.setAccessible(true);
            Integer columnNum = resolveField(field);
            if (columnNum == null) continue;
            Cell cell = row.getCell(columnNum);
            Object cellObj = null;
            try {
                cellObj = resolveCell(field,cell);
            } catch (ColumnIllegalException e) {
               excelExceptions.add(e);
               return null;
            }
            if (!isValid(field, cellObj)) {
                return null;
            }
            setField(field, instance, cellObj);
        }
        return (T) instance;
    }

    /**
     * 获取属性上的列序号
     */
    private Integer resolveField(Field field) {
        Column columnIndex = field.getAnnotation(Column.class);
        if (columnIndex == null) return null;
        return columnIndex.value();
    }

    private Object resolveCell(Field field,Cell cell) throws ColumnIllegalException{
        if(cell==null) return null;
        CellRead cellRead = field.getAnnotation(CellRead.class);
        try {
            CellReader cellReader=cellRead==null?new DefaultCellReader():cellRead.value().newInstance();
            return cellReader.readCell(field,cell);
        } catch (InstantiationException | IllegalAccessException e) {
            String expStr=cellRead.value().getName()+"无法实例化";
            throw new ColumnIllegalException(expStr);
        }
    }

    private void setField(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证当前值是否合法
     */
    private boolean isValid(Field field, Object value) {
        Validate validate = field.getAnnotation(Validate.class);
        //没有校验器，直接返回
        if (validate == null) return true;
        if(value==null) return validate.allowNull();
        Class[] validators = validate.value();
        for (Class validatorClass : validators) {
            Validator validator;
            try {
                validator = (Validator) validatorClass.newInstance();
                return validator.isValid(value);
            } catch (InstantiationException | IllegalAccessException e) {
                String className = e.getStackTrace()[0].getClassName();
                String message = className + "验证器无法实例化,或者不是" + Validator.class.getName() + "的子类";
                excelExceptions.add(new IllegalValidatorException(message));
            } catch (ColumnIllegalException e) {
                excelExceptions.add(e);
            } catch (ClassCastException e) {
                ExcelException excelException=new ExcelException(e.getMessage());
                excelExceptions.add(excelException);
            }
        }
        return false;
    }

    public List<ExcelException> showResult() {
        System.out.println("解析成功行数:" + successNum);
        System.out.println("解析失败行数:" + failNum);
        System.out.println("失败原因:");
        for (ExcelException excelException : excelExceptions) {
            System.out.println(excelException.getMessage());
        }
        return excelExceptions;
    }

    public static void main(String[] args) {
        Field[] declaredFields = User.class.getDeclaredFields();
        for(Field field:declaredFields){
            Class<?> type = field.getType();
            if(type==Date.class){
                System.out.println("gg");
            }
        }
    }

    private static class User{
        private String username;
        private Date date;
    }

}
