package com.example.demo.controller;


import com.example.demo.bean.Staff;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ForgetPasswordController {

    @Autowired
    StaffService staffService;

    @GetMapping("/forgetPassword")
    public String jumpToForgetPassword() {
        return "forget_password";
    }

    @GetMapping("/resetPassword")
    public String jumpToResetPassword() {
        return "reset_password";
    }

    @PostMapping(value = "/forgetPassword")
    public Map<String, Object> forgetPassword(@RequestParam("email") String email) {
        return staffService.forgetPassword(email);
    }

    @PostMapping(value = "/resetPassword")
    public Map<String, Object> resetPassword(@RequestParam("account") String account,
                                             @RequestParam("password") String password) {
        return staffService.resetPassword(account,password);
    }
}
