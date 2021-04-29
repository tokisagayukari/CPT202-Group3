package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.IdType;
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

}
