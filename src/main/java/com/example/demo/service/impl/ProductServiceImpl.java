package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bean.Product;
import com.example.demo.bean.Staff;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.ProductService;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Update("""
            ALTER TABLE product
            \t${AUTO_INCREMENT=1};
            """)
    public void resetAutoIncrement(){}

    public List<Product> selectProductsByName(String keywords){

        QueryWrapper<Product> QueryWrapper = new QueryWrapper<>();
        // QueryWrapper<User> userQueryWrapper = Wrappers.query(); 和上面一样的效果
        QueryWrapper.like("name", keywords);
        List<Product> productList = productMapper.selectList(QueryWrapper);
        return productList;
    }

}
