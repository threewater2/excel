package com.threewater.excel.validator;

import com.threewater.excel.exception.ColumnIllegalException;

public class EmailValidator implements Validator {

    @Override
    public boolean isValid(Object value) throws ColumnIllegalException, ClassCastException {
        String email=(String) value;
        boolean valid=org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
        if(!valid){
            throw new ColumnIllegalException(value+"不是邮箱");
        }
        return true;
    }
}
