package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.bean.Staff;
import org.apache.ibatis.annotations.Update;

import java.net.UnknownHostException;
import java.util.Map;

public interface StaffService extends IService<Staff> {

    @Update("ALTER TABLE `staff` AUTO_INCREMENT=1;")
    public void resetAutoIncrement();

    public boolean checkAccountAndPassword(String account, String password);

    public Map<String,Object> createAccount (Staff staff) throws UnknownHostException;

    Map<String, Object> activationAccount(String confirmCode);

    public String forgetPassword (String email, String account, String newPassword, Map<String, Object> map) throws UnknownHostException;

    public String resetPassword (String account, String password, String newPassword, Map<String, Object> map);
}
