package com.example.demo.controller;

import cn.hutool.crypto.SecureUtil;
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

    public static String userAccount;
    public static String userPassword;

    @Autowired
    StaffService staffService;

    @RequestMapping("/")
    public String hello(){
        return "login";
    }

    @PostMapping(value = "/")
    public String login(@RequestParam("username") String account,
                        @RequestParam("password") String password,
                        Map<String, Object> map,
                        HttpSession session,
                        Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", account).eq("password", SecureUtil.md5(password)).eq("valid", (byte) 1);
        System.out.println(password);
        System.out.println(SecureUtil.md5(password));
        if(staffService.getMap(wrapper) != null){
            session.setAttribute("loginUser", account);
            userAccount = account;
            userPassword = SecureUtil.md5(password);
            showPortrait(model);
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
    public String jumpToDashboard(Model model){

        showPortrait(model);
        return "dashboard";
    }

    public void showPortrait(Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", userAccount).eq("password", userPassword);
        Map<String, Object> loginUser = staffService.getMap(wrapper);
        String dir;
        if (loginUser.get("portrait") != null) {
            dir = "/face/" + loginUser.get("portrait").toString();
        }
        else {
            dir = "/face/face.jpg";
        }
        model.addAttribute("face", dir);
    }

    @GetMapping("/profile")
    public String jumpToProfile(Model model){

        showPortrait(model);
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", userAccount).eq("password", userPassword);
        Map<String, Object> loginUser = staffService.getMap(wrapper);
        Long loginUserId = (Long) loginUser.get("id");
        Staff loginStaff = staffService.getById(loginUserId);
        model.addAttribute("staff", loginStaff);
        return "staff_profile";
    }

    @PostMapping("/profile")
    public String editProfile(Staff staff,
                              @RequestPart("portraitFile") MultipartFile portraitFile) throws IOException {

        if (!portraitFile.isEmpty()) {
            String filename = portraitFile.getOriginalFilename();
            staff.setPortrait(userAccount+filename);
            portraitFile.transferTo(new File(System.getProperty("user.dir"+"/face/"+filename)));
            // portraitFile.transferTo(new File("E:\\Documents\\cpt204\\demo\\face\\" + userAccount + filename));
        }
        if (staff.getPassword() != null && !staff.getPassword().equals("")) {
            staff.setPassword(SecureUtil.md5(staff.getPassword()));
        }
        staffService.updateById(staff);
        return "redirect:/dashboard";
    }

    @GetMapping("/aboutUs")
    public String jumpToAboutUs(Model model) {
        showPortrait(model);
        return "about_us";
    }
}
