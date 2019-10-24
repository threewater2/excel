package com.threewater.excel.validator;

import com.threewater.excel.exception.ColumnIllegalException;

import java.lang.reflect.Field;

public interface Validator {
    boolean isValid(Object value) throws ColumnIllegalException,ClassCastException;
}
