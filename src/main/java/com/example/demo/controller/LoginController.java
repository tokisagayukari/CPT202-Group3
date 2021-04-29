package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.bean.Staff;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
public class LoginController {

    @Autowired
    StaffService staffService;

    @RequestMapping("/")
    public String hello(){
        return "login";
    }

    @PostMapping(value = "/")
    public String login(@RequestParam("username") String account,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account).eq("password", password);
        if(staffService.getMap(wrapper) != null){
            session.setAttribute("loginUser", account);
            return "dashboard";
        }
        else{
            map.put("msg", "invalid username or password");
            return "login";
        }
    }

    @RequestMapping(value = "/registry")
    public String jumpToRegister(){
        return "registry";
    }

    @RequestMapping(value = "/dashboard")
    public String jumpToDashboard(){
        return "dashboard";
    }
}
