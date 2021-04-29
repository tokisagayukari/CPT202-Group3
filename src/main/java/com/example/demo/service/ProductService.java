package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bean.Product;
import com.example.demo.mapper.ProductMapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ProductService extends IService<Product> {

    @Update("ALTER TABLE `product` AUTO_INCREMENT=1;")
    public void resetAutoIncrement();

    public List<Product> selectProductsByName(String keywords);
}
