package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {
    @RequestMapping({"/", "/login"})
    public String hello(){
        return "login";
    }
    @PostMapping(value = "/")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session){
        if((!username.isEmpty()) && password.equals("123")){
            session.setAttribute("loginUser", username);
            return "products_list";
        }
        else{
            map.put("msg", "invalid username or password");
            return "login";
        }
    }
    @RequestMapping(value="/registry")
    public String jumpToRegister(){
        return "registry";
    }
}
