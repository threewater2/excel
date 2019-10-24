package com.threewater.excel.validator;

import com.threewater.excel.exception.ColumnIllegalException;


public class PhoneValidator implements Validator {
    @Override
    public boolean isValid(Object value) throws ColumnIllegalException, ClassCastException {
        String regex="\\d{11}";
        String res=(String)value;
        if(!res.matches(regex)){
            throw new ColumnIllegalException(res+"不是一个电话号码");
        }
        return true;
    }

    public static void main(String[] args) throws ColumnIllegalException {
        PhoneValidator validator=new PhoneValidator();
        validator.isValid("1111");
    }
}
