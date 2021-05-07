package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Staff {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String position;
    private String account;
    private String password;
    private String email;
    @TableField(value = "confirm_code")
    private String confirmCode;
    @TableField(value = "valid")
    private Byte isValid;
    private String portrait;
}
