package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type;
    private Double price;
    private Double discount;
    @TableField(value = "on_sale")
    private Integer onSale;
    @TableField(value = "in_store")
    private Integer inStore;
    @TableField(value = "sale_per_week")
    private Integer salePerWeek;

}
