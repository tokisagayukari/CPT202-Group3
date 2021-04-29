package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bean.Staff;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.StaffService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Autowired
    StaffMapper staffMapper;

    @Update("ALTER TABLE `staff` AUTO_INCREMENT=1;")
    public void resetAutoIncrement() {}

    public boolean checkAccountAndPassword(String account, String password){

        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).eq("password", password);
        List<Staff> loginUser = staffMapper.selectList(queryWrapper);
        if(loginUser != null){
            return true;
        }
        return false;
    }
}
