package com.example.demo.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.Product;
import com.example.demo.bean.Staff;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class StaffController {

    @Autowired
    StaffService staffService;

    @GetMapping("/activation")
    public Map<String,Object> activationAccount(String confirmCode){
        return staffService.activationAccount(confirmCode);
    }

    @GetMapping("/staffs")
    public String staffsList(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model){
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("valid", (byte) 1);
        List<Staff> list = staffService.list();
        //model.addAttribute("staffs", list);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        long current = page.getCurrent();
        long pages = page.getPages();
        long total = page.getTotal();
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }

    public void showPortrait(Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", LoginController.userAccount).eq("password", LoginController.userPassword);
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

    @GetMapping("/staff")
    public String toStaffAddPage(){
        return "Staff_add";
    }

    @PostMapping("/staff")
    public String addStaff(Staff staff){
        staff.setIsValid((byte) 1);
        staff.setPassword(SecureUtil.md5("123456"));
        staffService.save(staff);
        return "redirect:/staffs";
    }

    @GetMapping("/staff/delete/{id}")
    public String removeStaff(@PathVariable("id") Long id,
                              @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              RedirectAttributes ra){

        staffService.removeById(id);
        ra.addAttribute("pn", pn);
        return "redirect:/staffs";
    }

    @GetMapping("/staff/{id}")
    public String toStaffEditPage(@PathVariable("id") Integer id,
                                  @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                                  Model model){

        Staff staff = staffService.getById(id);
        model.addAttribute("staff", staff);
        //ra.addAttribute("pn", pn);
        return "staff_add";
    }

    @PutMapping("/staff")
    public String editStaff(Staff staff

                            //@PathVariable("id") Long id,
                            //@RequestParam(value = "pn", defaultValue = "1") Integer pn
                            ){

        staffService.updateById(staff);
        //ra.addAttribute("pn", pn);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs")
    public String searchStaffName(@RequestParam("keywords") String keywords,
                                  @RequestParam("position") String position,
                                  @RequestParam("account") String account,
                                  @RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                  Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        // wrapper = staffService.selectStaffsByName(keywords);
        if(!keywords.equals("")) { wrapper.like("name", keywords); }
        if(!position.equals("")) { wrapper.eq("position", position); }
        if(!account.equals("")) { wrapper.like("account", account); }
        wrapper.eq("valid", (byte) 1);
        // List<Staff> list = staffService.selectStaffsByName(keywords);
        // model.addAttribute("staffs", list);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }

    @GetMapping("/staffs/nameDown")
    public String sortNameDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                               Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("name");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }
    @GetMapping("/staffs/nameUp")
    public String sortNameUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                             Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }


    @GetMapping("/staffs/accountDown")
    public String sortAccountDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                  Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("account");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }

    @GetMapping("/staffs/accountUp")
    public String sortAccountUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("account");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }

    @GetMapping("/staffs/positionDown")
    public String sortOnSaleDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                 Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("position");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }

    @GetMapping("/staffs/positionUp")
    public String sortOnSaleUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                               Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("position");
        wrapper.eq("valid", (byte) 1);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "staff_list";
    }
}
