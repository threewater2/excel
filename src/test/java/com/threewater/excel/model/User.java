package com.threewater.excel.model;

import com.threewater.excel.annotaion.input.Column;
import com.threewater.excel.annotaion.input.Validate;
import com.threewater.excel.annotaion.output.ColumnTemplate;
import com.threewater.excel.annotaion.output.ExcelTemplate;
import com.threewater.excel.core.ExcelReadable;
import com.threewater.excel.core.ExcelWriteable;
import com.threewater.excel.validator.EmailValidator;
import com.threewater.excel.validator.PhoneValidator;

@ExcelTemplate
public class User implements ExcelReadable , ExcelWriteable {
    @Column(0)
    @ColumnTemplate("名字")
    private String name;
    @Column(1)
    @ColumnTemplate("邮箱")
    @Validate(value = {EmailValidator.class},allowNull = false)
    private String email;
    @Column(2)
    @ColumnTemplate("电话")
    @Validate(PhoneValidator.class)
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
