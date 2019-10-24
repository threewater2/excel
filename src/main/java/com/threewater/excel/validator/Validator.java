package com.threewater.excel.validator;

import com.threewater.excel.exception.ColumnIllegalException;


public interface Validator {
    boolean isValid(Object value) throws ColumnIllegalException,ClassCastException;
}
