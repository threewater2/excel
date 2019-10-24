package com.threewater.excel.annotaion.input;

import com.threewater.excel.exception.ColumnIllegalException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultCellReader implements CellReader {

    @Override
    public Object readCell(Field field, Cell cell) throws ColumnIllegalException{
        Class fieldType=field.getType();
        if (String.class.equals(fieldType)) {
            return getStringCellValue(cell);
        } else if (Date.class.equals(fieldType)) {
            return getDateCellValue(cell);
        } else if(Double.class.equals(fieldType)){
            return getDoubleValue(cell);
        } else if(Float.class.equals(fieldType)){
            return getFloatDateValue(cell);
        } else if(Integer.class.equals(fieldType)){
            return getIntegerValue(cell);
        } else{
            throw new ColumnIllegalException("暂不支持的类型"+fieldType.getName());
        }
    }


    private static String getStringCellValue(Cell cell) throws ColumnIllegalException {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else {
            String expStr = cell.getRowIndex() + "行" + cell.getColumnIndex() + "列的单元格不是字符串类型";
            throw new ColumnIllegalException(expStr);
        }
    }

    private static Date getDateCellValue(Cell cell) throws ColumnIllegalException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            switch (cell.getCellType()){
                case STRING:
                    return sdf.parse(cell.getStringCellValue());
                case NUMERIC:
                    return cell.getDateCellValue();
                default:
                    throw new ColumnIllegalException(cell.getRowIndex()+"行"+cell.getColumnIndex()+"无法识别的单元格");
            }
        } catch (Exception e){
            String expStr=cell.getRowIndex()+"行"+cell.getColumnIndex()+"列无法解析成时间类型";
            throw new ColumnIllegalException(expStr);
        }
    }

    private static Float getFloatDateValue(org.apache.poi.ss.usermodel.Cell cell) throws ColumnIllegalException {
        try{
            switch (cell.getCellType()){
                case STRING:
                    return Float.parseFloat(cell.getStringCellValue());
                case NUMERIC:
                    return (float)cell.getNumericCellValue();
                default:
                    throw new ColumnIllegalException(cell.getRowIndex()+"行"+cell.getColumnIndex()+"无法识别的单元格");
            }

        }catch (Exception e){
            String expStr=cell.getRowIndex()+"行"+cell.getColumnIndex()+"列无法解析成Float类型";
            throw new ColumnIllegalException(expStr);
        }
    }

    private static Integer getIntegerValue(org.apache.poi.ss.usermodel.Cell cell) throws ColumnIllegalException {
        try{
            switch (cell.getCellType()){
                case STRING:
                    return Integer.parseInt(cell.getStringCellValue());
                case NUMERIC:
                    return (int)cell.getNumericCellValue();
                default:
                    throw new ColumnIllegalException(cell.getRowIndex()+"行"+cell.getColumnIndex()+"无法识别的单元格");
            }

        }catch (Exception e){
            String expStr=cell.getRowIndex()+"行"+cell.getColumnIndex()+"列无法解析成Integer类型";
            throw new ColumnIllegalException(expStr);
        }
    }

    private static Double getDoubleValue(org.apache.poi.ss.usermodel.Cell cell) throws ColumnIllegalException {
        try{
            switch (cell.getCellType()){
                case STRING:
                    return Double.parseDouble(cell.getStringCellValue());
                case NUMERIC:
                    return cell.getNumericCellValue();
                default:
                    throw new ColumnIllegalException(cell.getRowIndex()+"行"+cell.getColumnIndex()+"无法识别的单元格");
            }

        }catch (Exception e){
            String expStr=cell.getRowIndex()+"行"+cell.getColumnIndex()+"列无法解析成Double类型";
            throw new ColumnIllegalException(expStr);
        }
    }

}
