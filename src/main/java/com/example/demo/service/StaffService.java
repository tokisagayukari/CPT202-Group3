package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bean.Staff;
import org.apache.ibatis.annotations.Update;

public interface StaffService extends IService<Staff> {

    @Update("ALTER TABLE `staff` AUTO_INCREMENT=1;")
    public void resetAutoIncrement();

    public boolean checkAccountAndPassword(String account, String password);
}
