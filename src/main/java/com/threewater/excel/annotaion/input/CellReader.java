package com.threewater.excel.annotaion.input;

import com.threewater.excel.exception.ColumnIllegalException;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;

public interface CellReader {
    Object readCell(Field field, Cell cell) throws ColumnIllegalException;
}
