package com.threewater.excel.annotaion.output;


import com.threewater.excel.output.ExcelBeanExporter;

public @interface ExcelTemplate {
    Class<ExcelBeanExporter> value() default ExcelBeanExporter.class;
}
