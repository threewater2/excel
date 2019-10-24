package com.threewater.excel.annotaion.output;


import com.threewater.excel.output.ExcelBeanExporter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelTemplate {
    Class<ExcelBeanExporter> value() default ExcelBeanExporter.class;
}
