package com.threewater.excel.core;

import com.threewater.excel.annotaion.input.Column;
import com.threewater.excel.annotaion.input.Validate;
import com.threewater.excel.exception.ColumnIllegalException;
import com.threewater.excel.exception.ExcelException;
import com.threewater.excel.exception.IllegalValidatorException;
import com.threewater.excel.validator.Validator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultExcelUtil implements ExcelUtil {

    private List<ExcelException> excelExceptions = new ArrayList<>();
    private int failNum = 0;
    private int successNum = 0;

    @Override
    public <T extends ExcelReadable> List<T> excelParse(Class<T> entity, Workbook workbook) {
        List<T> resList = new ArrayList<>();
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
    public <T extends ExcelWriteable> InputStream toExcel(List<T> entityList) {
        return null;
    }

    @Override
    public <T> InputStream getExcelTemplate(T entity) {
        return null;
    }

    private <T extends ExcelReadable> T readRow(Class<T> entity, Row row) {
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
            String cellStr = resolveCell(cell);
            if (!isValid(field, cellStr)) {
                return null;
            }
            setField(field, instance, cellStr);
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

    private String resolveCell(Cell cell) {
        if (cell == null) return null;
        return cell.getStringCellValue();
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
        Object u=new HashMap<>();
        try{
            Integer in=(Integer)u;
        } catch (ClassCastException e){
            e.printStackTrace();
            String message = e.getMessage();
            System.out.println(message);
        }
    }

}
