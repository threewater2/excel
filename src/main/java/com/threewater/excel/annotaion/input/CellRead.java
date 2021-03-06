package com.threewater.excel.annotaion.input;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CellRead {
    Class<? extends CellReader> value() default DefaultCellReader.class;
}
