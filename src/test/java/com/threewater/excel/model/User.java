package com.threewater.excel.model;

import com.threewater.excel.annotaion.input.*;
import com.threewater.excel.annotaion.output.ColumnTemplate;
import com.threewater.excel.annotaion.output.ExcelTemplate;
import com.threewater.excel.validator.EmailValidator;
import com.threewater.excel.validator.PhoneValidator;

import java.util.Date;

@ExcelTemplate
@ExcelReadable
@ExcelWriteable
public class User {
    @Column(0)
    @ColumnTemplate(value = "名字",columnIndex = 0)
    private String name;
    @Column(1)
    @ColumnTemplate(value = "邮箱",columnIndex = 1)
    @Validate(EmailValidator.class)
    private String email;
    @Column(2)
    @ColumnTemplate(value = "电话",columnIndex = 2)
    @Validate(PhoneValidator.class)
    private String phone;

    @Column(3)
    private Date birth;

    @Column(4)
    @CellRead(MySimpleCellReader.class)
    private String content;

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

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
