package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.DemoApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.ui.Model;
import com.example.demo.bean.Staff;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Map;


@Controller
public class LoginController {

    String userAccount;
    String userPassword;

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
            userAccount = account;
            userPassword = password;
            return "dashboard";
        }
        else{
            map.put("msg", "invalid username or password");
            return "login";
        }
    }

    @GetMapping("/exit")
    public String exit(HttpSession session){
        session.removeAttribute("loginUser");
        return "login";
    }

    @RequestMapping(value = "/registry")
    public String jumpToRegister(){
        return "registry";
    }

    @RequestMapping(value = "/dashboard")
    public String jumpToDashboard(){
        return "dashboard";
    }

    @GetMapping("/profile")
    public String jumpToProfile(Model model){

        ApplicationHome home = new ApplicationHome(DemoApplication.class);
        home.getDir();
        home.getSource();
        System.out.println(home);
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", userAccount).eq("password", userPassword);
        Map<String, Object> loginUser = staffService.getMap(wrapper);
        String face = "face.jpg";
        if (loginUser.get("portrait") != null) {
            face = loginUser.get("portrait").toString();
        }
        model.addAttribute("face", face);
        return "staff_profile";
    }

    @PostMapping("/profile")
    public String editProfile(Staff staff,
                              @RequestPart("portraitFile") MultipartFile portraitFile) throws IOException {

        if (!portraitFile.isEmpty()) {
            String filename = portraitFile.getOriginalFilename();
            staff.setPortrait(filename);
            portraitFile.transferTo(new File("face/" + userAccount + portraitFile.getOriginalFilename()));
        }
        staffService.updateById(staff);
        return "redirect:/dashboard";
    }
}
