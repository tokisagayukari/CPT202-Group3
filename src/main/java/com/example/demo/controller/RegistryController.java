package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.bean.Staff;
import com.example.demo.service.StaffService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegistryController {
    @Autowired
    StaffService staffService;

    @PostMapping(value = "/registry")
    public Map<String, Object> createAccount(Staff staff,
                                             Map<String, Object> map) throws UnknownHostException {
        return staffService.createAccount(staff);
    }
}
    /*
    public String register(@RequestParam("username") String username, @RequestParam("password")
            String password, @RequestParam("email") String email, Map<String, Object> map, HttpSession session) {


    System.out.println("运行了Controller！！！！！！！！！！！！！！！！！！！！！！！！！");
        //用户或密码为空的条件判断
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {

            map.put("tip1", "用户或密码或邮箱不能为空");
            return "registry";
        }

        //判断是否取到用户，如果没有就保存在数据库中
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", username).eq("password", password);
        if (staffService.getMap(wrapper) == null) {


        } else {

        }
        return "test";

    }
}
*/
