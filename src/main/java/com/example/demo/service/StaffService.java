package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bean.Staff;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface StaffService extends IService<Staff> {

    @Update("ALTER TABLE `staff` AUTO_INCREMENT=1;")
    public void resetAutoIncrement();

    public boolean checkAccountAndPassword(String account, String password);

    public Map<String,Object> createAccount (Staff staff);

    Map<String, Object> activationAccount(String confirmCode);
}
