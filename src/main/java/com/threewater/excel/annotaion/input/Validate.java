package com.threewater.excel.annotaion.input;

import com.threewater.excel.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    Class[] value();
    boolean allowNull() default true;
}
